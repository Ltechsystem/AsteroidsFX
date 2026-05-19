/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.health.IDamageableService;
import dk.sdu.mmmi.cbse.common.services.IWaveSpawnerService;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author jcs
 */
class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServiceList;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;
    private final List<IWaveSpawnerService> waveSpawnerServices;
    private int currentWave = 0;
    private Text waveText;
    private Text asteroidsText;
    private Text enemiesText;
    private Text healthText;
    private Rectangle healthBarFill;
    private IDamageableService damageableService;

    Game(List<IGamePluginService> gamePluginServices, List<IEntityProcessingService> entityProcessingServiceList, List<IPostEntityProcessingService> postEntityProcessingServices, List<IWaveSpawnerService> waveSpawnerServices) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServiceList = entityProcessingServiceList;
        this.postEntityProcessingServices = postEntityProcessingServices;
        this.waveSpawnerServices = waveSpawnerServices;
    }

    public void start(Stage window) throws Exception {
        asteroidsText = new Text(10, 20, "Asteroids left: 0");
        asteroidsText.setFill(Color.WHITE);
        enemiesText = new Text(10, 40, "Enemies left: 0");
        enemiesText.setFill(Color.WHITE);
        waveText = new Text("Wave: 1");
        waveText.setX(gameData.getDisplayWidth() / 2.0 - 30);
        waveText.setY(20);
        waveText.setFill(Color.WHITE);
        int barWidth = 150;
        int barHeight = 15;
        int barX = gameData.getDisplayWidth() - barWidth - 10;
        int barY = 18;
        healthText = new Text(barX - 30, barY + 12, "HP:");
        healthText.setFill(Color.WHITE);
        Rectangle healthBarBg = new Rectangle(barX, barY, barWidth, barHeight);
        healthBarBg.setFill(Color.TRANSPARENT);
        healthBarBg.setStroke(Color.WHITE);
        healthBarBg.setStrokeWidth(1);
        healthBarFill = new Rectangle(barX, barY, barWidth, barHeight);
        healthBarFill.setFill(Color.LIMEGREEN);
        Line hudLine = new Line(0, 50, gameData.getDisplayWidth(), 50);
        hudLine.setStroke(Color.WHITE);
        hudLine.setStrokeWidth(1);
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(asteroidsText);
        gameWindow.getChildren().add(enemiesText);
        gameWindow.getChildren().add(waveText);
        gameWindow.getChildren().add(healthText);
        gameWindow.getChildren().add(healthBarBg);
        gameWindow.getChildren().add(healthBarFill);
        gameWindow.getChildren().add(hudLine);

        Scene scene = new Scene(gameWindow);
        scene.setFill(Color.BLACK);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT) || event.getCode().equals(KeyCode.A)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT) || event.getCode().equals(KeyCode.D)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP) || event.getCode().equals(KeyCode.W)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT) || event.getCode().equals(KeyCode.A)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT) || event.getCode().equals(KeyCode.D)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP) || event.getCode().equals(KeyCode.W)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });

        for (IGamePluginService iGamePlugin : getGamePluginServices()) {
            iGamePlugin.start(gameData, world);
        }
        damageableService = ServiceLoader.load(IDamageableService.class).stream()
                .map(ServiceLoader.Provider::get).findFirst().orElse(null);
        startNextWave();
        asteroidsText.setVisible(world.getEntities().stream().anyMatch(e -> "asteroid".equals(e.getCollisionGroup())));
        enemiesText.setVisible(world.getEntities().stream().anyMatch(e -> "enemy".equals(e.getCollisionGroup())));
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            stylePolygon(entity, polygon);
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }
        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.setResizable(false);
        window.show();
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                checkWaveCompletion();
                updateAsteroidCounter();
                draw();
                gameData.getKeys().update();
            }

        }.start();
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity polygonEntity : polygons.keySet()) {
            if (!world.getEntities().contains(polygonEntity)) {
                Polygon removedPolygon = polygons.get(polygonEntity);
                polygons.remove(polygonEntity);
                gameWindow.getChildren().remove(removedPolygon);
            }
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                stylePolygon(entity, polygon);
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }

    }

    private void updateAsteroidCounter() {
        long asteroids = world.getEntities().stream()
                .filter(e -> "asteroid".equals(e.getCollisionGroup()))
                .count();
        long enemies = world.getEntities().stream()
                .filter(e -> "enemy".equals(e.getCollisionGroup()))
                .count();
        asteroidsText.setText("Asteroids left: " + asteroids);
        enemiesText.setText("Enemies left: " + enemies);

        if (damageableService != null) {
            world.getEntities().stream()
                    .filter(e -> "player".equals(e.getCollisionGroup()))
                    .findFirst()
                    .ifPresentOrElse(player -> {
                        double pct = (double) damageableService.getHealth(player) / damageableService.getMaxHealth(player);
                        healthBarFill.setWidth(150 * pct);
                        healthBarFill.setFill(pct > 0.5 ? Color.LIMEGREEN : pct > 0.25 ? Color.ORANGE : Color.RED);
                    }, () -> healthBarFill.setWidth(0));
        } else {
            healthBarFill.setWidth(0);
        }
    }

    private void startNextWave() {
        currentWave++;
        waveText.setText("Wave: " + currentWave);
        for (IWaveSpawnerService spawner : waveSpawnerServices) {
            spawner.spawnWave(currentWave, gameData, world);
        }
    }

    private void checkWaveCompletion() {
        boolean waveEntitiesRemain = world.getEntities().stream()
                .anyMatch(e -> "enemy".equals(e.getCollisionGroup()) || "asteroid".equals(e.getCollisionGroup()));
        if (!waveEntitiesRemain) {
            startNextWave();
        }
    }

    private void stylePolygon(Entity entity, Polygon polygon) {
        String fillColor = entity.getFillColor();
        polygon.setFill(fillColor != null ? Color.web(fillColor) : Color.WHITE);
        polygon.setStroke(Color.WHITE);
        polygon.setStrokeWidth(1);
    }

    public List<IGamePluginService> getGamePluginServices() {
        return gamePluginServices;
    }

    public List<IEntityProcessingService> getEntityProcessingServices() {
        return entityProcessingServiceList;
    }

    public List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        return postEntityProcessingServices;
    }

}

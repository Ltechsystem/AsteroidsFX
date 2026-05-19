package dk.sdu.mmmi.cbse.common.data;

public class GameData {

    private int displayWidth  = 800;
    private int displayHeight = 800;
    private int displayTopBound = 60;
    private final GameKeys keys = new GameKeys();


    public GameKeys getKeys() {
        return keys;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public int getDisplayTopBound() {
        return displayTopBound;
    }

}

package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public interface IWaveSpawnerService {
    void spawnWave(int waveNumber, GameData gameData, World world);
}

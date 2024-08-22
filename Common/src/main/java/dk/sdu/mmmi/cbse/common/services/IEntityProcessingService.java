package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * This interface is for precessing game entities.
 */

public interface IEntityProcessingService {

    /**
     * This method processes game entities
     * Pre-condition: GameData and World instances are being properly initialized.
     * Post-Condition: The entities in the World are being updated based on the game logic
     * @param gameData object contains game state information.
     * @param world object contains all game entities.d
     * @throws
     */
    void process(GameData gameData, World world);
}
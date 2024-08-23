package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public class CollisionDetector implements IPostEntityProcessingService {

    public CollisionDetector() {
        // Tom konstruktør - nødvendig for IPostEntityProcessingService-implementering
    }

    @Override
    public void process(GameData gameData, World world) {
        // Løkke igennem alle entiteter i verdenen for at tjekke kollisioner
        for (Entity entity1 : world.getEntities()) {
            checkCollisionsForEntity(entity1, world);
        }
    }

    private void checkCollisionsForEntity(Entity entity1, World world) {
        for (Entity entity2 : world.getEntities()) {
            if (shouldSkipCollisionCheck(entity1, entity2)) {
                continue;
            }

            if (detectCollision(entity1, entity2)) {
                handleCollision(entity1, entity2);
            }
        }
    }

    private boolean shouldSkipCollisionCheck(Entity entity1, Entity entity2) {
        // Skip hvis entiteterne er identiske
        return entity1.getID().equals(entity2.getID());
    }

    boolean detectCollision(Entity entity1, Entity entity2) {
        // Metode til at detektere kollision mellem to entiteter
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private void handleCollision(Entity entity1, Entity entity2) {
        // Metode til at håndtere kollision mellem to entiteter
        entity1.setCollided(true);
        entity2.setCollided(true);
    }
}


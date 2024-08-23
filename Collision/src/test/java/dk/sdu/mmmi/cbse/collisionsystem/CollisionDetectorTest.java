package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
import dk.sdu.mmmi.cbse.common.data.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CollisionDetectorTest {

    private CollisionDetector collisionDetector;

    @BeforeEach
    public void setUp() {
        collisionDetector = new CollisionDetector();
    }

    @Test
    public void testEntitiesShouldCollide() {
        Entity entityA = createEntity(5, 5, 2);
        Entity entityB = createEntity(5, 5, 2);

        boolean result = collisionDetector.detectCollision(entityA, entityB);

        Assertions.assertTrue(result, "Entities should collide but did not.");
    }

    @Test
    public void testEntitiesShouldNotCollide() {
        Entity entityA = createEntity(0, 0, 2);
        Entity entityB = createEntity(10, 10, 2);

        boolean result = collisionDetector.detectCollision(entityA, entityB);

        Assertions.assertFalse(result, "Entities should not collide but did.");
    }

    private Entity createEntity(float x, float y, float radius) {
        Entity entity = new Entity();
        entity.setX(x);
        entity.setY(y);
        entity.setRadius(radius);
        return entity;
    }
}

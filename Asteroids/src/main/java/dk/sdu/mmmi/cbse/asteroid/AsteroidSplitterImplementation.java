package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Random;

public class AsteroidSplitterImplementation implements IAsteroidSplitter {

    @Override
    public void createSplitAsteroid(Entity asteroidEntity, World world) {
        if (!(asteroidEntity instanceof Asteroid)) {
            return;
        }
        Asteroid parentAsteroid = (Asteroid) asteroidEntity;
        Random random = new Random();

        int splitCount = 2;
        double shrinkFactor = 0.5;
        double rotationVariance = 15;

        for (int i = 0; i < splitCount; i++) {
            Asteroid fragment = new Asteroid();
            int newSize = (int) (parentAsteroid.getRadius() * shrinkFactor);
            fragment.setRadius(newSize);

            double rotationOffset = i == 0 ? rotationVariance : -rotationVariance;
            double newRotation = parentAsteroid.getRotation() + random.nextDouble() * rotationVariance - rotationVariance / 2;
            fragment.setRotation(newRotation);

            double offsetX = Math.cos(Math.toRadians(newRotation)) * parentAsteroid.getRadius();
            double offsetY = Math.sin(Math.toRadians(newRotation)) * parentAsteroid.getRadius();
            fragment.setX(parentAsteroid.getX() + offsetX);
            fragment.setY(parentAsteroid.getY() + offsetY);

            world.addEntity(fragment);
        }

        world.removeEntity(parentAsteroid);
    }
}

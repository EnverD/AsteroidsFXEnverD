package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Random;

public class AsteroidSplitterImpl implements IAsteroidSplitter {

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        if (!(e instanceof Asteroid)) {
            return;
        }
        Asteroid original = (Asteroid) e;
        Random rnd = new Random();

        int numSplits = 2; // Number of asteroids created from the original
        double sizeReductionFactor = 0.5; // Smaller size for the new asteroids
        double angleVariation = 15; // Degrees to vary the new asteroid's path

        for (int i = 0; i < numSplits; i++) {
            Asteroid split = new Asteroid();
            int newSize = (int)(original.getRadius() * sizeReductionFactor);
            split.setRadius(newSize);

            double offsetAngle = i == 0 ? angleVariation : -angleVariation;
            double newRotation = original.getRotation() + rnd.nextDouble() * angleVariation - angleVariation / 2;
            split.setRotation(newRotation);

            // Calculating offset to avoid immediate collision
            double offsetX = Math.cos(Math.toRadians(newRotation)) * original.getRadius();
            double offsetY = Math.sin(Math.toRadians(newRotation)) * original.getRadius();
            split.setX(original.getX() + offsetX);
            split.setY(original.getY() + offsetY);

            world.addEntity(split);
        }

        world.removeEntity(original);
    }
}
package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class AsteroidProcessorService implements IEntityProcessingService {

    private final AsteroidPluginService asteroidPlugin = new AsteroidPluginService();
    private IAsteroidSplitter asteroidSplitter = new AsteroidSplitterImplementation();

    @Override
    public void process(GameData gameData, World world) {
        if (world.getEntities(Asteroid.class).size() < 5) {
            if (Math.random() < 0.10) {  // Increased spawn probability
                Entity newAsteroid = asteroidPlugin.generateAsteroid(20, gameData.getDisplayWidth(), gameData.getDisplayHeight() * Math.random());
                if (isSafeSpawn(newAsteroid, world)) {
                    world.addEntity(newAsteroid);
                }
            }
        }
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            moveAsteroid(gameData, asteroid);
            if (asteroid.isCollided()) {
                world.removeEntity(asteroid);
                splitAsteroidIfLarge(asteroid, world);
            }
        }
    }

    private void moveAsteroid(GameData gameData, Entity asteroid) {
        double deltaX = Math.cos(Math.toRadians(asteroid.getRotation()));
        double deltaY = Math.sin(Math.toRadians(asteroid.getRotation()));
        asteroid.setX(asteroid.getX() + deltaX);
        asteroid.setY(asteroid.getY() + deltaY);

        if (asteroid.getX() < 0 || asteroid.getX() > gameData.getDisplayWidth()) {
            asteroid.setRotation(180 - asteroid.getRotation());
        }

        if (asteroid.getY() < 0 || asteroid.getY() > gameData.getDisplayHeight()) {
            asteroid.setRotation(360 - asteroid.getRotation());
        }
    }

    private void splitAsteroidIfLarge(Entity asteroid, World world) {
        if (asteroid.getRadius() > 10) {
            Entity smallAsteroid1 = asteroidPlugin.generateAsteroid((int) (asteroid.getRadius() - 5), asteroid.getX(), asteroid.getY() - 50);
            smallAsteroid1.setRotation(asteroid.getRotation() + 35);

            Entity smallAsteroid2 = asteroidPlugin.generateAsteroid((int) (asteroid.getRadius() - 5), asteroid.getX(), asteroid.getY() + 50);
            smallAsteroid2.setRotation(asteroid.getRotation() - 35);

            world.addEntity(smallAsteroid1);
            world.addEntity(smallAsteroid2);
        }
    }

    private boolean isSafeSpawn(Entity newAsteroid, World world) {
        for (Entity existingAsteroid : world.getEntities(Asteroid.class)) {
            double distance = calculateDistance(existingAsteroid, newAsteroid);
            double safeDistance = (existingAsteroid.getRadius() + newAsteroid.getRadius()) * 1.5;
            if (distance < safeDistance) {
                return false;
            }
        }
        return true;
    }

    private double calculateDistance(Entity entity1, Entity entity2) {
        double dx = entity1.getX() - entity2.getX();
        double dy = entity1.getY() - entity2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = null;
    }
}

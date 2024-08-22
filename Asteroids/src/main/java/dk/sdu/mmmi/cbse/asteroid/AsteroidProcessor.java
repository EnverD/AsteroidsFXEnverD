package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class AsteroidProcessor implements IEntityProcessingService {

    AsteroidPlugin asteroidPlugin = new AsteroidPlugin();
    private IAsteroidSplitter asteroidSplitter = new AsteroidSplitterImpl();

    @Override
    public void process(GameData gameData, World world) {
        // Check if there are fewer than 5 asteroids in the game
        if (world.getEntities(Asteroid.class).size() < 5) {
            // Increased chance to add a new asteroid
            if (Math.random() < 0.10) {  // Changed for more frequent checks
                Entity newAsteroid = asteroidPlugin.createAsteroid(20, gameData.getDisplayWidth(), gameData.getDisplayHeight() * Math.random());
                if (isPositionSafeForNewAsteroid(newAsteroid, world)) {
                    world.addEntity(newAsteroid);
                }
            }
        }
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));
            asteroid.setX(asteroid.getX() + changeX);
            asteroid.setY(asteroid.getY() + changeY);

            if (asteroid.getX() < 0 || asteroid.getX() > gameData.getDisplayWidth()) {
                asteroid.setRotation(180 - asteroid.getRotation());
            }

            if (asteroid.getY() < 0 || asteroid.getY() > gameData.getDisplayHeight()) {
                asteroid.setRotation(360 - asteroid.getRotation());
            }

            if (asteroid.isCollided()) {
                world.removeEntity(asteroid);
                if (asteroid.getRadius() > 10) {
                    Entity asteroid1 = asteroidPlugin.createAsteroid((int) (asteroid.getRadius() - 5), asteroid.getX(), asteroid.getY() - 50);
                    asteroid1.setRotation(asteroid.getRotation() + 35);

                    Entity asteroid2 = asteroidPlugin.createAsteroid((int) (asteroid.getRadius() - 5), asteroid.getX(), asteroid.getY() + 50);
                    asteroid2.setRotation(asteroid.getRotation() - 35);

                    world.addEntity(asteroid1);
                    world.addEntity(asteroid2);
                }
            }
        }
    }

    private boolean isPositionSafeForNewAsteroid(Entity newAsteroid, World world) {
        for (Entity existingAsteroid : world.getEntities(Asteroid.class)) {
            double dx = existingAsteroid.getX() - newAsteroid.getX();
            double dy = existingAsteroid.getY() - newAsteroid.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            double safeDistance = (existingAsteroid.getRadius() + newAsteroid.getRadius()) * 1.5;
            if (distance < safeDistance) {
                return false; // Not safe to place new asteroid here
            }
        }
        return true;
    }

    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = null;
    }
}
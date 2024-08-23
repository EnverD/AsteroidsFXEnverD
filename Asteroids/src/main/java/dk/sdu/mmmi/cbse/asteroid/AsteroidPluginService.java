package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class AsteroidPluginService implements IGamePluginService {

    public AsteroidPluginService() {}

    public Entity generateAsteroid(int dimension, double posX, double posY) {
        Entity asteroidEntity = new Asteroid();
        Random random = new Random();
        asteroidEntity.setPolygonCoordinates(dimension, -dimension, -dimension, -dimension, -dimension, dimension, dimension, dimension);
        asteroidEntity.setX(posX);
        asteroidEntity.setY(posY);
        asteroidEntity.setRadius(dimension);
        asteroidEntity.setRotation(random.nextInt(180));
        return asteroidEntity;
    }

    @Override
    public void start(GameData gameData, World world) {
//        Entity asteroidEntity = generateAsteroid(gameData);
//        world.addEntity(asteroidEntity);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove asteroids from the world
        world.getEntities().removeIf(entity -> entity instanceof Asteroid);
    }
}
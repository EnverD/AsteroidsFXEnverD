package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

public class PlayerControlSystem implements IEntityProcessingService {

    private long lastShotTime = 0;  // Track the last time a shot was fired
    private static final long SHOOT_COOLDOWN_MS = 1000;  // Cooldown time in milliseconds (1 second)

    @Override
    public void process(GameData gameData, World world) {
        long currentTime = System.currentTimeMillis();  // Get the current time in milliseconds

        for (Entity player : world.getEntities(Player.class)) {
            handleMovement(gameData, player);
            handleShooting(gameData, world, player, currentTime);

            constrainToScreenBounds(gameData, player);
            handleCollisions(world, player);
        }
    }

    private void handleMovement(GameData gameData, Entity player) {
        if (gameData.getKeys().isDown(GameKeys.LEFT)) {
            player.setRotation(player.getRotation() - 5);
        }
        if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
            player.setRotation(player.getRotation() + 5);
        }
        if (gameData.getKeys().isDown(GameKeys.UP)) {
            double changeX = Math.cos(Math.toRadians(player.getRotation()));
            double changeY = Math.sin(Math.toRadians(player.getRotation()));
            player.setX(player.getX() + changeX);
            player.setY(player.getY() + changeY);
        }
    }

    private void handleShooting(GameData gameData, World world, Entity player, long currentTime) {
        if (gameData.getKeys().isDown(GameKeys.SPACE)) {
            // Check if enough time has passed since the last shot
            if (currentTime - lastShotTime >= SHOOT_COOLDOWN_MS) {
                // Find the BulletSPI service and create a bullet
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> world.addEntity(spi.createBullet(player, gameData))
                );
                lastShotTime = currentTime;  // Update the last shot time to the current time
            }
        }
    }

    private void constrainToScreenBounds(GameData gameData, Entity player) {
        if (player.getX() < 0) {
            player.setX(1);
        }
        if (player.getX() > gameData.getDisplayWidth()) {
            player.setX(gameData.getDisplayWidth() - 1);
        }
        if (player.getY() < 0) {
            player.setY(1);
        }
        if (player.getY() > gameData.getDisplayHeight()) {
            player.setY(gameData.getDisplayHeight() - 1);
        }
    }

    private void handleCollisions(World world, Entity player) {
        if (player.isCollided()) {
            world.removeEntity(player);
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }
}

import dk.sdu.mmmi.cbse.asteroid.AsteroidPluginService;
import dk.sdu.mmmi.cbse.asteroid.AsteroidProcessorService;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module Asteroid {
    exports dk.sdu.mmmi.cbse.asteroid;
    requires Common;
    requires CommonAsteroids;
    provides IGamePluginService with AsteroidPluginService;
    provides IEntityProcessingService with AsteroidProcessorService;
}
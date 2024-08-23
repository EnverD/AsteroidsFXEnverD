import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module Collision {
    exports dk.sdu.mmmi.cbse.collisionsystem;
    requires Common;
    requires org.testng;
    requires org.junit.jupiter.api;
    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
}
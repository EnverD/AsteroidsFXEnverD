module Core {
    requires Common;
    requires CommonBullet;
    requires javafx.graphics;
    requires spring.context; // Ensure Spring Context is required
    requires spring.beans;   // Ensure Spring Beans is required
    opens dk.sdu.mmmi.cbse.main to javafx.graphics, spring.core; // Adjust as necessary
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}

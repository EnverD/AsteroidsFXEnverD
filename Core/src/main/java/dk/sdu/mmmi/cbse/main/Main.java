package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toList;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> entityPolygons = new ConcurrentHashMap<>();
    private final Pane gamePane = new Pane();

    private final IEntityProcessingService entityProcessingService;
    private final IGamePluginService gamePluginService;

    public Main(IEntityProcessingService entityProcessingService, IGamePluginService gamePluginService) {
        this.entityProcessingService = entityProcessingService;
        this.gamePluginService = gamePluginService;
    }

    public Main() {
        this.entityProcessingService = (gameData, world) -> {};
        this.gamePluginService = new IGamePluginService() {
            @Override
            public void start(GameData gameData, World world) {}

            @Override
            public void stop(GameData gameData, World world) {}
        };
    }

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage stage) {
        Text scoreText = new Text(10, 20, "Destroyed asteroids: 0");
        gamePane.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gamePane.getChildren().add(scoreText);

        Scene scene = new Scene(gamePane);
        configureKeyBindings(scene);

        initializeGamePlugins();
        initializeEntities();

        startGameLoop();

        stage.setScene(scene);
        stage.setTitle("ASTEROIDS");
        stage.show();
    }

    private void configureKeyBindings(Scene scene) {
        scene.setOnKeyPressed(event -> {
            GameKeys keys = gameData.getKeys();
            if (event.getCode().equals(KeyCode.LEFT)) keys.setKey(GameKeys.LEFT, true);
            if (event.getCode().equals(KeyCode.RIGHT)) keys.setKey(GameKeys.RIGHT, true);
            if (event.getCode().equals(KeyCode.UP)) keys.setKey(GameKeys.UP, true);
            if (event.getCode().equals(KeyCode.SPACE)) keys.setKey(GameKeys.SPACE, true);
        });
        scene.setOnKeyReleased(event -> {
            GameKeys keys = gameData.getKeys();
            if (event.getCode().equals(KeyCode.LEFT)) keys.setKey(GameKeys.LEFT, false);
            if (event.getCode().equals(KeyCode.RIGHT)) keys.setKey(GameKeys.RIGHT, false);
            if (event.getCode().equals(KeyCode.UP)) keys.setKey(GameKeys.UP, false);
            if (event.getCode().equals(KeyCode.SPACE)) keys.setKey(GameKeys.SPACE, false);
        });
    }

    private void initializeGamePlugins() {
        getPluginServices().forEach(plugin -> plugin.start(gameData, world));
    }

    private void initializeEntities() {
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            entityPolygons.put(entity, polygon);
            gamePane.getChildren().add(polygon);
        }
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame();
                renderGame();
                gameData.getKeys().update();
            }
        }.start();
    }

    private void updateGame() {
        getEntityProcessingServices().forEach(service -> service.process(gameData, world));
        getPostEntityProcessingServices().forEach(service -> service.process(gameData, world));
    }

    private void renderGame() {
        entityPolygons.keySet().removeIf(entity -> {
            if (!world.getEntities().contains(entity)) {
                Polygon polygon = entityPolygons.remove(entity);
                gamePane.getChildren().remove(polygon);
                return true;
            }
            return false;
        });

        for (Entity entity : world.getEntities()) {
            Polygon polygon = entityPolygons.computeIfAbsent(entity, e -> {
                Polygon p = new Polygon(e.getPolygonCoordinates());
                gamePane.getChildren().add(p);
                return p;
            });
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}

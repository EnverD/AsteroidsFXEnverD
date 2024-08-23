package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "dk.sdu.mmmi.cbse")
public class SpringConfig {

    @Bean
    public Main game(IEntityProcessingService entityProcessingService, IGamePluginService gamePluginService) {
        return new Main(entityProcessingService, gamePluginService);
    }

    @Bean
    public IEntityProcessingService entityProcessingService() {
        // Replace with your actual implementation
        return new IEntityProcessingService() {
            @Override
            public void process(GameData gameData, World world) {

            }
        };
    }

    @Bean
    public IGamePluginService gamePluginService() {
        // Replace with your actual implementation
        return new IGamePluginService() {
            @Override
            public void start(GameData gameData, World world) {

            }

            @Override
            public void stop(GameData gameData, World world) {

            }
        };
    }
}

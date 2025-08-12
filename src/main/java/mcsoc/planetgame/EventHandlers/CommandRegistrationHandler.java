package mcsoc.planetgame.EventHandlers;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.StateManagement.GameEffects;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class CommandRegistrationHandler {
    public static void Register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("flip")
                .executes(cxt -> {

                    PlanetGame.LOGGER.info("flip command received");
                    MinecraftServer server = cxt.getSource().getServer();
                    ServerPlayerEntity player = cxt.getSource().getPlayer();
                    PlanetGame.LOGGER.info("flip command enacting");
                    GameEffects.toggleIsPlayerFlipped(player, server);

                    return 1;
                })
            );
        });
    }
}

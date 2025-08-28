package mcsoc.planetgame.EventHandlers;

import mcsoc.planetgame.StateManagement.GameEffects;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

public abstract class CommandRegistrationHandler {

    private CommandRegistrationHandler() { /* delete */ }

    public static void Register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("flip")
                .executes(GameEffects.CommandActions::flipCallingPlayerCommand)
            );
        });
    }
}

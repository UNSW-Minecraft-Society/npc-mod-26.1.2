package mcsoc.planetgame.EventHandlers;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import mcsoc.planetgame.EventHandlers.CommandRegistrationHandler.CommandSuggestionProviders.OnlinePlayerSuggestionProvider;
import mcsoc.planetgame.StateManagement.GameEffects;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public abstract class CommandRegistrationHandler {

    private CommandRegistrationHandler() { /* delete */ }

    public abstract static class CommandSuggestionProviders {
        private CommandSuggestionProviders() { /* delete */ }

        public static class OnlinePlayerSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
            
            public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> ctx, SuggestionsBuilder builder) {

                MinecraftServer server = ctx.getSource().getServer();
                return CommandSource.suggestMatching(server.getPlayerNames(), builder);
            }
        }
    }

    public static void Register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("flip")
                
                // no player given, flip self
                .executes(GameEffects.CommandActions::flipCallingPlayerCommand)
                
                // flip specified player
                .then(CommandManager.argument("player name", StringArgumentType.string())
                    .suggests(new OnlinePlayerSuggestionProvider())
                    .executes(GameEffects.CommandActions::flipTargetPlayerCommand)
            ));
        });
    }
}

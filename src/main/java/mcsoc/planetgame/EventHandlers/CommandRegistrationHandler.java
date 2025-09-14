package mcsoc.planetgame.eventhandlers;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import mcsoc.planetgame.statemanagement.GameEffects;
import mcsoc.planetgame.eventhandlers.CommandRegistrationHandler.CommandSuggestionProviders.OnlinePlayerSuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public abstract class CommandRegistrationHandler {
    private CommandRegistrationHandler() { /* delete */ }

    public static final String PLAYER_NAME_ARGUMENT = "player name";
    public static final String GRAVITY_STRENGTH_ARGUMENT = "strength";
    
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
            dispatcher.register(CommandManager.literal("gravity_control")
                .then(CommandManager.literal("flip")
                // no player given, flip self
                .executes(GameEffects.CommandActions::flipCallingPlayerCommand)
                    // flip specified player
                    .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                    .suggests(new OnlinePlayerSuggestionProvider())
                    .executes(GameEffects.CommandActions::flipTargetPlayerCommand)
                    )
                )
                .then(CommandManager.literal("strength")
                    .then(CommandManager.argument(GRAVITY_STRENGTH_ARGUMENT, DoubleArgumentType.doubleArg())
                    // no player given, set self strength
                    .executes(GameEffects.CommandActions::setCallingPlayerGravStrengthCommand)
                        // set specified player strength
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(GameEffects.CommandActions::setTargetPlayerGravStrengthCommand)
                        )
                    )
                )
                .then(CommandManager.literal("set_ability")
                    .then(CommandManager.literal("first")
                        .then(CommandManager.literal("none")
                        .executes(GameEffects.CommandActions::setCallingPlayerFirstAbilityNone)
                            .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                            .suggests(new OnlinePlayerSuggestionProvider())
                            .executes(GameEffects.CommandActions::setTargetPlayerFirstAbilityNone)
                            )
                        )
                        .then(CommandManager.literal("flip")
                        .executes(GameEffects.CommandActions::setCallingPlayerFirstAbilityFlip)
                            .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                            .suggests(new OnlinePlayerSuggestionProvider())
                            .executes(GameEffects.CommandActions::setTargetPlayerFirstAbilityFlip)
                            )
                        )
                        .then(CommandManager.literal("control")
                        .executes(GameEffects.CommandActions::setCallingPlayerFirstAbilityControl)
                            .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                            .suggests(new OnlinePlayerSuggestionProvider())
                            .executes(GameEffects.CommandActions::setTargetPlayerFirstAbilityControl)
                            )
                        )
                    )
                )
            );
        });
    }
}
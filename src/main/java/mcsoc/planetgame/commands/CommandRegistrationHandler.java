package mcsoc.planetgame.commands;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import mcsoc.planetgame.commands.CommandRegistrationHandler.CommandSuggestionProviders.OnlinePlayerSuggestionProvider;
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
    
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("gravity_control")
                .then(CommandManager.literal("flip")
                // no player given, flip self
                .executes(CommandActions::flipCallingPlayerCommand)
                    // flip specified player
                    .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                    .suggests(new OnlinePlayerSuggestionProvider())
                    .executes(CommandActions::flipTargetPlayerCommand)
                    )
                )
                .then(CommandManager.literal("strength")
                    .then(CommandManager.argument(GRAVITY_STRENGTH_ARGUMENT, DoubleArgumentType.doubleArg())
                    // no player given, set self strength
                    .executes(CommandActions::setCallingPlayerGravityStrengthCommand)
                        // set specified player strength
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerGravityStrengthCommand)
                        )
                    )
                )
            );

            dispatcher.register(CommandManager.literal("set_ability")
                .then(CommandManager.literal("first")
                    .then(CommandManager.literal("none")
                    .executes(CommandActions::setCallingPlayerFirstAbilityNone)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerFirstAbilityNone)
                        )
                    )
                    .then(CommandManager.literal("flip")
                    .executes(CommandActions::setCallingPlayerFirstAbilityFlip)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerFirstAbilityFlip)
                        )
                    )
                    .then(CommandManager.literal("control")
                    .executes(CommandActions::setCallingPlayerFirstAbilityControl)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerFirstAbilityControl)
                        )
                    )
                )
                .then(CommandManager.literal("second")
                    .then(CommandManager.literal("none")
                    .executes(CommandActions::setCallingPlayerSecondAbilityNone)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerSecondAbilityNone)
                        )
                    )
                    .then(CommandManager.literal("xray")
                    .executes(CommandActions::setCallingPlayerSecondAbilityXray)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerSecondAbilityXray)
                        )
                    )
                    .then(CommandManager.literal("drill")
                    .executes(CommandActions::setCallingPlayerSecondAbilityDrill)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerSecondAbilityDrill)
                        )
                    )
                )
                .then(CommandManager.literal("third")
                    .then(CommandManager.literal("none")
                    .executes(CommandActions::setCallingPlayerThirdAbilityNone)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerThirdAbilityNone)
                        )
                    )
                    .then(CommandManager.literal("dash_add")
                    .executes(CommandActions::setCallingPlayerThirdAbilityDash)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerThirdAbilityDash)
                        )
                    )
                    .then(CommandManager.literal("throw")
                    .executes(CommandActions::setCallingPlayerThirdAbilityThrow)
                        .then(CommandManager.argument(PLAYER_NAME_ARGUMENT, StringArgumentType.string())
                        .suggests(new OnlinePlayerSuggestionProvider())
                        .executes(CommandActions::setTargetPlayerThirdAbilityThrow)
                        )
                    )
                )
            );
        });
    }
}
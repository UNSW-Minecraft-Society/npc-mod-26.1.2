package mcsoc.npcmod.commands;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

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
    
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("reload_npc_data")
                    .requires(source -> source.hasPermissionLevel(2))
                    .executes(CommandActions::reloadNPCData)
            );

            dispatcher.register(
                CommandManager.literal("cutscene")
                //.requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("trigger")
                    .then(CommandManager.argument("cutscene_id", StringArgumentType.string())
                        .executes(CommandActions::triggerCutscene)
                    )
                )
                .then(CommandManager.literal("resume")
                    .executes(CommandActions::resumeCutscene)
                )
                .then(CommandManager.literal("stop")
                    .executes(CommandActions::stopCutscene)
                )
            );
        });
    }
}
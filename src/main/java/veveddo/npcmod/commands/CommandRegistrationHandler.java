package veveddo.npcmod.commands;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.server.MinecraftServer;


import veveddo.npcmod.dataloader.datastorage.NpcModServerDataStorage;


public abstract class CommandRegistrationHandler {
    private CommandRegistrationHandler() { /* delete */ }
    
    
    public abstract static class CommandSuggestionProviders {
        private CommandSuggestionProviders() { /* delete */ }
    
        public static class LoadedCutsceneSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
            
            public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
                return SharedSuggestionProvider.suggest(NpcModServerDataStorage.getInstance().getCutsceneMap().keySet(), builder);
            }
        }
        public static class OnlinePlayerSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
            
            public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
                MinecraftServer server = ctx.getSource().getServer();
                return SharedSuggestionProvider.suggest(server.getPlayerNames(), builder);
            }
        }
    }
    
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("reload_npc_data")
                    .requires(source -> source.hasPermission(2))
                    .executes(CommandActions::reloadNPCData)
            );

            dispatcher.register(
                Commands.literal("cutscene")
                //.requires(source -> source.hasPermissionLevel(2))
                .then(Commands.literal("trigger")
                    .then(Commands.argument("cutscene_id", StringArgumentType.string())
                        .suggests(new CommandSuggestionProviders.LoadedCutsceneSuggestionProvider())
                        .executes(CommandActions::triggerCutscene)
                    )
                )
                .then(Commands.literal("resume")
                    .executes(CommandActions::resumeCutscene)
                )
                .then(Commands.literal("stop")
                    .executes(CommandActions::stopCutscene)
                )
            );
        });
    }
}
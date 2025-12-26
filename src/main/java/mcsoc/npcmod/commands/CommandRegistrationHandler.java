package mcsoc.npcmod.commands;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
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
            
        });
    }
}
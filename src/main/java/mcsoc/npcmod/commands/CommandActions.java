package mcsoc.npcmod.commands;

import com.mojang.brigadier.context.CommandContext;

import mcsoc.npcmod.dataloader.NPCServerDataLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

public abstract class CommandActions {
    private CommandActions() { /* delete */ }

    protected static int reloadNPCData(CommandContext<ServerCommandSource> ctx) {
        NPCServerDataLoader loader = NPCServerDataLoader.getInstance();
        loader.loadData();

        MinecraftServer server = ctx.getSource().getServer();
        server.getPlayerManager().getPlayerList().forEach(player -> {
            loader.syncClientData(server, player);
        });
        return 1;
    }
}
package mcsoc.npcmod.commands;

import com.mojang.brigadier.context.CommandContext;

import mcsoc.npcmod.dataloader.datastorage.NPCServerDataLoader;
import net.minecraft.server.command.ServerCommandSource;

public abstract class CommandActions {
    private CommandActions() { /* delete */ }

    protected static int reloadNPCData(CommandContext<ServerCommandSource> ctx) {
        NPCServerDataLoader loader = NPCServerDataLoader.getInstance();
        loader.loadData();

        ctx.getSource().getServer().getPlayerManager().getPlayerList().forEach(loader::syncClientData);
        return 1;
    }
}
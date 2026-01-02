package mcsoc.npcmod.commands;

import com.mojang.brigadier.context.CommandContext;

import mcsoc.npcmod.dataloader.NPCServerDataLoader;
import net.minecraft.server.command.ServerCommandSource;

public abstract class CommandActions {
    private CommandActions() { /* delete */ }

    protected static int reloadNPCData(CommandContext<ServerCommandSource> ctx) {
        NPCServerDataLoader.getInstance().loadData();
        return 1;
    }
}
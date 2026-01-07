package mcsoc.npcmod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import mcsoc.npcmod.cutscenes.CutsceneHandler;
import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import net.minecraft.server.command.ServerCommandSource;

public abstract class CommandActions {
    private CommandActions() { /* delete */ }

    protected static int reloadNPCData(CommandContext<ServerCommandSource> ctx) {
        NpcModServerDataStorage loader = NpcModServerDataStorage.getInstance();
        loader.loadData();

        ctx.getSource().getServer().getPlayerManager().getPlayerList().forEach(loader::syncClientData);
        return 1;
    }

    protected static int triggerCutscene(CommandContext<ServerCommandSource> ctx) {
        String cutscene_id = StringArgumentType.getString(ctx, "cutscene_id");
        CutsceneHandler.getInstance().loadCutscene(cutscene_id);
        CutsceneHandler.getInstance().start();
        return 1;
    }

    protected static int resumeCutscene(CommandContext<ServerCommandSource> ctx) {
        CutsceneHandler.getInstance().start();
        return 1;
    }

    protected static int stopCutscene(CommandContext<ServerCommandSource> ctx) {
        CutsceneHandler.getInstance().stop();
        return 1;
    }
}
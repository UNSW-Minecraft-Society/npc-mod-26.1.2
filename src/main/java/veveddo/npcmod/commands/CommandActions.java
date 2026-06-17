package veveddo.npcmod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import veveddo.npcmod.NpcMod;
import veveddo.npcmod.cutscenes.CutsceneHandler;
import veveddo.npcmod.dataloader.datastorage.NpcModServerDataStorage;

public abstract class CommandActions {
    private CommandActions() { /* delete */ }

    protected static int reloadNPCData(CommandContext<ServerCommandSource> ctx) {
        NpcModServerDataStorage loader = NpcModServerDataStorage.getInstance();
        try {
            loader.loadData();
            ctx.getSource().getServer().getPlayerManager().getPlayerList().forEach(loader::syncClientData);
            ctx.getSource().sendFeedback(() -> Text.literal("cutscene data reload successful"), true);
            return 1;
        } catch (Exception e) {
            ctx.getSource().sendFeedback(() -> Text.literal("cutscene data reload failed"), false);
            return 0;
        }
    }

    protected static int triggerCutscene(CommandContext<ServerCommandSource> ctx) {
        String cutscene_id = StringArgumentType.getString(ctx, "cutscene_id");
        NpcMod.LOGGER.info("command rotation: ({}, {})", ctx.getSource().getRotation().x, ctx.getSource().getRotation().y);
        CutsceneHandler.getInstance().loadCutscene(cutscene_id, ctx.getSource().getPosition(), ctx.getSource().getRotation());
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
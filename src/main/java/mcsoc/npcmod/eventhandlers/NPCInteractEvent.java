package mcsoc.npcmod.eventhandlers;

import mcsoc.npcmod.dataloader.NPCDataStorage.DialogueData;
import mcsoc.npcmod.dataloader.NPCServerDataLoader;
import mcsoc.npcmod.entities.npc.BasicNPC;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;

public class NPCInteractEvent {
    private NPCInteractEvent() { /* delete */ }

    public static void registerHandler() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof BaseNPC npc) {
                DialogueData npc_dialogue = NPCServerDataLoader.getInstance().getDialogue(npc);
                player.sendMessage(npc_dialogue.getFormattedMessage());
                world.playSoundFromEntity(player, npc, SoundEvent.of(npc_dialogue.voice()), SoundCategory.PLAYERS, 1, 1);
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
}

package mcsoc.npcmod.eventhandlers;

import mcsoc.npcmod.entities.npc.BasicNPC;
import mcsoc.npcmod.entities.npc.NPCServerDataLoader;
import mcsoc.npcmod.entities.npc.NPCServerDataLoader.DialogueData;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;

public class NPCInteractEvent {

    public static void registerEvent() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof BasicNPC npc) {
                DialogueData npc_dialogue = NPCServerDataLoader.getInstance().getDialogue(npc);
                player.sendMessage(npc_dialogue.getFormattedMessage());
                world.playSoundFromEntity(player, npc, SoundEvent.of(npc_dialogue.voice()), SoundCategory.PLAYERS, 1, 1);
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
}

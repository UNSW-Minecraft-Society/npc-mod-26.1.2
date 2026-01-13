package mcsoc.npcmod.eventhandlers;

import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import mcsoc.npcmod.dataloader.datastorage.datatypes.DialogueData;
import mcsoc.npcmod.entities.npc.BaseNPC;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class NPCInteractEvent {
    private NPCInteractEvent() { /* delete */ }

    public static void registerEvent() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (hand != Hand.MAIN_HAND) {
                return ActionResult.PASS;
            }

            if (entity instanceof BaseNPC npc) {
                if (world.isClient()) {
                    return ActionResult.SUCCESS;
                }
                DialogueData npc_dialogue = NpcModServerDataStorage.getInstance().getDialogue(npc);
                player.sendMessage(npc_dialogue.getFormattedMessage());
                world.playSoundFromEntity(player, npc, SoundEvent.of(npc_dialogue.voice()), SoundCategory.PLAYERS, 1, 1);
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
        
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient()) {
                return ActionResult.PASS;
            }
            if (entity instanceof BaseNPC) return ActionResult.FAIL;
            return ActionResult.PASS;
        });
    }
}

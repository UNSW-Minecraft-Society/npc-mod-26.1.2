package mcsoc.npcmod.eventhandlers;

import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import mcsoc.npcmod.datatypes.npcs.DialogueData;
import mcsoc.npcmod.entities.npc.BaseNPC;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class NPCInteractEvent {
    private NPCInteractEvent() { /* delete */ }

    public static void registerEvent() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (hand != InteractionHand.MAIN_HAND) {
                return InteractionResult.PASS;
            }

            if (entity instanceof BaseNPC npc) {
                if (world.isClientSide()) {
                    return InteractionResult.SUCCESS;
                }
                DialogueData npc_dialogue = NpcModServerDataStorage.getInstance().getDialogue(npc);
                player.sendSystemMessage(npc_dialogue.getFormattedMessage());
                world.playSound(player, npc, SoundEvent.createVariableRangeEvent(npc_dialogue.voice()), SoundSource.PLAYERS, 1, 1);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
        
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClientSide()) {
                return InteractionResult.PASS;
            }
            if (entity instanceof BaseNPC) return InteractionResult.FAIL;
            return InteractionResult.PASS;
        });
    }
}

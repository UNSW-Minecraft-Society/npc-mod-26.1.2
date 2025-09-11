package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.network.ServerPlayerEntity;


@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends LivingEntityMixin {

}
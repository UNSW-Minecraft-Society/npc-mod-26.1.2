package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import mcsoc.planetgame.statemanagement.gamestate.GameStateManager;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler implements ServerPlayPacketListener, PlayerAssociatedNetworkHandler, TickablePacketListener {
    protected ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection,
            ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Shadow
    public ServerPlayerEntity player;

    @ModifyVariable(method = "onPlayerMove", at = @At("STORE"), ordinal = 1)
    private boolean modifyGroundCondition(boolean val) {
        if (GameStateManager.getPlayerGravityDirection(this.player).equals(Direction.UP)) {
            return !val;
        }
        return val;
    } 

}

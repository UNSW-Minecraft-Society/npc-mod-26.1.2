package mcsoc.planetgame.blocks.gravityfieldblock;

import java.util.ArrayList;
import java.util.List;

import mcsoc.planetgame.blocks.BlockEntityRegistration;
import mcsoc.planetgame.gameeffects.FirstAbilityGameEffects;
import mcsoc.planetgame.statemanagement.gamestate.GameStateManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class GravityFieldBlockEntity extends BlockEntity {

    protected static final int DEFAULT_SIZE = 10;
    
    private final Box gravity_field_box;
    private List<ServerPlayerEntity> tracked_players = new ArrayList<>();


    public GravityFieldBlockEntity(BlockPos pos, BlockState state, float size) {
        super(BlockEntityRegistration.GRAVITY_FIELD_BLOCK_ENTITY, pos, state);
        gravity_field_box = new Box(pos).expand(size);
    }

    public GravityFieldBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, DEFAULT_SIZE);
    }

    private void resetTrackedPlayers() {
        tracked_players.clear();
    }

    public void addTrackedPlayer(ServerPlayerEntity player) {
        tracked_players.add(player);
    }

    protected void setAllTrackedPlayersOutOfField() {
        tracked_players.forEach(player -> FirstAbilityGameEffects.setPlayerInGravityField(player, false));
    }


    protected static void tick(World world, BlockPos pos, BlockState state, GravityFieldBlockEntity entity) {
        if (world.isClient) return;

        MinecraftServer server = world.getServer();
        GameStateManager.registerGravityGeneratorPosition(server, entity);
        if (!GameStateManager.shouldUpdateGravityFields(server)) return;

        entity.tracked_players.forEach(player -> 
            FirstAbilityGameEffects.setPlayerInGravityField(player, entity.gravity_field_box.contains(player.getPos()))
        );
        entity.resetTrackedPlayers();
    }
}

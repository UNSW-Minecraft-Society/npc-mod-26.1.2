package mcsoc.planetgame.registration.blocks.rockpile;

import mcsoc.planetgame.GameEffects;
import mcsoc.planetgame.registration.entities.EntityRegistration;
import mcsoc.planetgame.registration.entities.ThrowableRockEntity;
import mcsoc.planetgame.statemanagement.GameStateManager;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RockPileBlock extends Block {

    public RockPileBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(player instanceof ServerPlayerEntity server_player) || GameEffects.getIsPlayerCarryingSomething(server_player)) return ActionResult.PASS;
        
        LivingEntity rock_projectile = new ThrowableRockEntity(EntityRegistration.ROCK, world);
        rock_projectile.setPosition(player.getPos());
        world.spawnEntity(rock_projectile);
        GameEffects.pickUpEntity(server_player, rock_projectile);
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(-0.0625, 0, -0.125, 1.1875, 1.25, 1.125);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(
            VoxelShapes.cuboid(-0.2098214285714285, -0.0017857142857142794, -0.2366071428571428, 1.2901785714285716, -0.0017857142857142794, 1.2633928571428572),
            VoxelShapes.cuboid(0.41517857142857145, -0.0267857142857143, 0.5133928571428571, 0.9151785714285714, 0.4732142857142857, 1.0133928571428572),
            VoxelShapes.cuboid(0.6651785714285714, -0.0267857142857143, 0.01339285714285715, 1.1651785714285714, 0.4732142857142857, 0.5133928571428572),
            VoxelShapes.cuboid(0.1651785714285714, -0.0267857142857143, -0.1116071428571428, 0.6651785714285714, 0.4732142857142857, 0.3883928571428572),
            VoxelShapes.cuboid(-0.08482142857142849, 0.4732142857142857, 0.2633928571428571, 0.4151785714285715, 0.9732142857142857, 0.7633928571428571),
            VoxelShapes.cuboid(-0.08482142857142849, -0.0267857142857143, 0.38839285714285715, 0.4151785714285715, 0.4732142857142857, 0.8883928571428572),
            VoxelShapes.cuboid(0.41517857142857145, 0.4732142857142857, 0.2633928571428571, 0.9151785714285714, 0.9732142857142857, 0.7633928571428571),
            VoxelShapes.cuboid(0.1651785714285714, 0.9732142857142857, 0.38839285714285715, 0.6651785714285714, 1.4732142857142856, 0.8883928571428572),
            VoxelShapes.cuboid(0.1651785714285714, -0.0267857142857143, 0.8883928571428572, 0.4151785714285714, 0.2232142857142857, 1.1383928571428572),
            VoxelShapes.cuboid(0.41517857142857145, 1.4732142857142858, 0.38839285714285715, 0.6651785714285714, 1.7232142857142858, 0.6383928571428572),
            VoxelShapes.cuboid(0.9151785714285714, -0.0267857142857143, 0.5133928571428571, 1.1651785714285714, 0.2232142857142857, 0.7633928571428572),
            VoxelShapes.cuboid(-0.08482142857142849, -0.0267857142857143, 0.13839285714285715, 0.1651785714285715, 0.2232142857142857, 0.38839285714285715),
            VoxelShapes.cuboid(-0.08482142857142849, -0.0267857142857143, 0.8883928571428572, 0.1651785714285715, 0.2232142857142857, 1.1383928571428572),
            VoxelShapes.cuboid(0.41517857142857145, 0.4732142857142857, -0.1116071428571428, 0.6651785714285714, 0.7232142857142857, 0.1383928571428572),
            VoxelShapes.cuboid(0.9151785714285714, -0.0267857142857143, 0.8883928571428572, 1.1651785714285714, 0.2232142857142857, 1.1383928571428572)
        );
    }

}

package mcsoc.planetgame.registration.blocks.rockpile;

import mcsoc.planetgame.GameEffects;
import mcsoc.planetgame.registration.entities.EntityRegistration;
import mcsoc.planetgame.registration.entities.ThrowableRockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
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
        
        ProjectileEntity rock_projectile = new ThrowableRockEntity(EntityRegistration.ROCK, world);
        rock_projectile.setPosition(player.getPos());
        world.spawnEntity(rock_projectile);
        GameEffects.pickUpEntity(server_player, rock_projectile);
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.3125, 0, 0.3125, 0.6875, 0.453125, 0.6875);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(
            VoxelShapes.cuboid(0.3125, 0, 0.3125, 0.6875, 0.012499999999999997, 0.6875),
            VoxelShapes.cuboid(0.46875, 0.015625, 0.5, 0.59375, 0.140625, 0.625),
            VoxelShapes.cuboid(0.53125, 0.015625, 0.375, 0.65625, 0.140625, 0.5),
            VoxelShapes.cuboid(0.40625, 0.015625, 0.34375, 0.53125, 0.140625, 0.46875),
            VoxelShapes.cuboid(0.34375, 0.140625, 0.4375, 0.46875, 0.265625, 0.5625),
            VoxelShapes.cuboid(0.34375, 0.015625, 0.46875, 0.46875, 0.140625, 0.59375),
            VoxelShapes.cuboid(0.46875, 0.140625, 0.4375, 0.59375, 0.265625, 0.5625),
            VoxelShapes.cuboid(0.40625, 0.265625, 0.46875, 0.53125, 0.390625, 0.59375),
            VoxelShapes.cuboid(0.40625, 0.015625, 0.59375, 0.46875, 0.078125, 0.65625),
            VoxelShapes.cuboid(0.46875, 0.390625, 0.46875, 0.53125, 0.453125, 0.53125),
            VoxelShapes.cuboid(0.59375, 0.015625, 0.5, 0.65625, 0.078125, 0.5625),
            VoxelShapes.cuboid(0.34375, 0.015625, 0.40625, 0.40625, 0.078125, 0.46875),
            VoxelShapes.cuboid(0.34375, 0.015625, 0.59375, 0.40625, 0.078125, 0.65625),
            VoxelShapes.cuboid(0.46875, 0.140625, 0.34375, 0.53125, 0.203125, 0.40625),
            VoxelShapes.cuboid(0.59375, 0.015625, 0.59375, 0.65625, 0.078125, 0.65625)
        );
    }

}

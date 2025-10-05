package mcsoc.planetgame.registration.blocks.xrayblock;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;

public interface XrayableBlock extends BlockEntityProvider {

    public ItemConvertible getItem();

    @Override
    public default BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new XrayableBlockEntity(pos, state, getItem());
    }
}
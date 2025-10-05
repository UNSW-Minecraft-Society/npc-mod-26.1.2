package mcsoc.planetgame.registration.blocks.xrayblock;

import mcsoc.planetgame.registration.blocks.BlockEntityRegistration;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class XrayableBlockEntity extends BlockEntity {

    private final ItemStack stack;

    public XrayableBlockEntity(BlockPos pos, BlockState state, ItemConvertible item) {
        super(BlockEntityRegistration.XRAYABLE_BLOCK_ENTITY, pos, state);
        this.stack = new ItemStack(item);
    }

    public XrayableBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, Items.AIR);
    }

    public ItemStack getItemStack() {
        return stack;
    }
}

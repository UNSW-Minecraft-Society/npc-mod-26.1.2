package mcsoc.planetgame.registration.blocks.xrayblock;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ColoredFallingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ColorCode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class XrayableSandBlock extends ColoredFallingBlock implements XrayableBlock {

    public XrayableSandBlock(Settings settings) {
        super(
            new ColorCode(14406560), 
            settings
                .mapColor(MapColor.PALE_YELLOW)
                .instrument(NoteBlockInstrument.SNARE)
                .strength(0.5F).sounds(BlockSoundGroup.SAND)
                .nonOpaque()
        );
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public ItemConvertible getItemToDisplay() {
        return Items.SAND;
    }
}

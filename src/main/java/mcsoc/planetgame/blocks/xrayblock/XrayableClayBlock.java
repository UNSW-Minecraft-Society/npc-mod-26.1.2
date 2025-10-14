package mcsoc.planetgame.blocks.xrayblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class XrayableClayBlock extends Block implements XrayableBlock {

    public XrayableClayBlock(Settings settings) {
        super(settings
                .mapColor(MapColor.LIGHT_BLUE_GRAY)
                .instrument(NoteBlockInstrument.FLUTE)
                .strength(0.6F)
                .sounds(BlockSoundGroup.GRAVEL)
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
        return Items.CLAY;
    }
}

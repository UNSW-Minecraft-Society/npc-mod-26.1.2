package mcsoc.planetgame.blocks;

import mcsoc.planetgame.blocks.xrayblock.XrayBlockDynamicRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BlockEntityRendererRegistration {
    private BlockEntityRendererRegistration() { /* delete */ }
    
    public static void register() {
        BlockEntityRendererFactories.register(BlockEntityRegistration.XRAYABLE_BLOCK_ENTITY, XrayBlockDynamicRenderer::new);
    }
}

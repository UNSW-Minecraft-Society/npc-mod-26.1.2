package mcsoc.planetgame.blocks.xrayblock;

import mcsoc.planetgame.PlanetGameClient;
import mcsoc.planetgame.registration.blocks.xrayblock.XrayableBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;


public class XrayBlockDynamicRenderer implements BlockEntityRenderer<XrayableBlockEntity> {
    ItemRenderer item_renderer;

	public XrayBlockDynamicRenderer(Context context) {
        this.item_renderer = context.getItemRenderer();
	}

	@Override
	public void render(XrayableBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        
        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(1, 1, 1);

        if (PlanetGameClient.getPlayerState().getXrayState() && entity.getPos().getSquaredDistance(MinecraftClient.getInstance().player.getPos()) <= 25) {
            // render nothing for now
        } else {
            item_renderer.renderItem(entity.getItemStack(), ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), overlay);
        }

        matrices.pop();
    }
}

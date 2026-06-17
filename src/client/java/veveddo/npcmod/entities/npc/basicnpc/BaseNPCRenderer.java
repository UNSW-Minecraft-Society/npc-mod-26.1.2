package veveddo.npcmod.entities.npc.basicnpc;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerClothingFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.util.Identifier;
import veveddo.npcmod.entities.npc.BaseNPC;


public class BaseNPCRenderer extends MobEntityRenderer<BaseNPC, VillagerResemblingModel<BaseNPC>> {
   private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/villager/villager.png");
   
    public BaseNPCRenderer(EntityRendererFactory.Context context) {
        super(context, new VillagerResemblingModel<>(context.getPart(EntityModelLayers.VILLAGER)), 0.5F);
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
        this.addFeature(new VillagerClothingFeatureRenderer<>(this, context.getResourceManager(), "villager"));
        this.addFeature(new VillagerHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    public Identifier getTexture(BaseNPC villagerEntity) {
        return TEXTURE;
    }
}

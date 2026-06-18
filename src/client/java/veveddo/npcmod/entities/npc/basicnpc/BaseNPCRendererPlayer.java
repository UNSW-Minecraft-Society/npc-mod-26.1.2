package veveddo.npcmod.entities.npc.basicnpc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import veveddo.npcmod.datatypes.npcs.ModelData;
import veveddo.npcmod.entities.npc.BaseNPC;
import veveddo.npcmod.entities.npc.NPCClientDataLoader;


public class BaseNPCRendererPlayer extends LivingEntityRenderer<BaseNPC, HumanoidModel<BaseNPC>> {

    public BaseNPCRendererPlayer(EntityRendererProvider.Context ctx, boolean slim) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER)), 0.5F);   
        this.addLayer(new HumanoidArmorLayer<>(this,
            new HumanoidArmorModel<>(
                ctx.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)
            ), 
            new HumanoidArmorModel<>(
                ctx.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), ctx.getModelManager()
            )
        );
        this.addLayer(new ItemInHandLayer<>(this, ctx.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, ctx.getModelSet(), ctx.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, ctx.getModelSet()));
    }

    public BaseNPCRendererPlayer(EntityRendererProvider.Context ctx) {
        this(ctx, false);
    }


    @Override
    public ResourceLocation getTextureLocation(BaseNPC npc_entity) {   
        NPCClientDataLoader npc_data_loader = NPCClientDataLoader.getInstance();
        ModelData npc_model = npc_data_loader.getModel(npc_entity);
        PlayerSkin skin_to_render = npc_data_loader.getSkin(npc_model);
        return skin_to_render.texture();
    }
}




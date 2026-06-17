package veveddo.npcmod.entities.npc.basicnpc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import veveddo.npcmod.datatypes.npcs.ModelData;
import veveddo.npcmod.entities.npc.BaseNPC;
import veveddo.npcmod.entities.npc.NPCClientDataLoader;


public class BaseNPCRendererPlayer extends LivingEntityRenderer<BaseNPC, BaseNPCRenderState, HumanoidModel<BaseNPCRenderState>> {

    public BaseNPCRendererPlayer(EntityRendererProvider.Context ctx, boolean slim) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER)), 0.5F);   
        
        this.addLayer(new HumanoidArmorLayer<BaseNPCRenderState, HumanoidModel<BaseNPCRenderState>, HumanoidModel<BaseNPCRenderState>>(
            this, 
            ArmorModelSet.bake(
                slim ? ModelLayers.PLAYER_SLIM_ARMOR : ModelLayers.PLAYER_ARMOR,
                ctx.getModelSet(),
                HumanoidModel::new
            ), 
            ctx.getEquipmentRenderer()
        ));
        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new CustomHeadLayer<>(this, ctx.getModelSet(), Minecraft.getInstance().playerSkinRenderCache()));
        this.addLayer(new WingsLayer<>(this, ctx.getModelSet(), ctx.getEquipmentRenderer()));

    }

    public BaseNPCRendererPlayer(EntityRendererProvider.Context ctx) {
        this(ctx, false);
    }

    @Override
    public BaseNPCRenderState createRenderState() {
        return new BaseNPCRenderState();
    }

    @Override
    public void extractRenderState(BaseNPC npc_entity, BaseNPCRenderState state, float delta_tick) {
        super.extractRenderState(npc_entity, state, delta_tick);
        
        NPCClientDataLoader npc_data_loader = NPCClientDataLoader.getInstance();
        ModelData npc_model = npc_data_loader.getModel(npc_entity);
        PlayerSkin skin_to_render = npc_data_loader.getSkin(npc_model);
        state.textureLocation = skin_to_render.body().texturePath();
    }

    @Override
    public Identifier getTextureLocation(BaseNPCRenderState state) {
        return state.textureLocation;
    }
}




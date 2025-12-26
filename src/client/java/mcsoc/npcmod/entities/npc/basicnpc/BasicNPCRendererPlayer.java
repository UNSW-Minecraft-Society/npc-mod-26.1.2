package mcsoc.npcmod.entities.npc.basicnpc;

import java.util.UUID;

import mcsoc.npcmod.entities.npc.BasicNPC;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.entities.npc.NPCServerDataLoader;
import mcsoc.npcmod.entities.npc.NPCServerDataLoader.ModelData;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.feature.StuckStingersFeatureRenderer;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;

public class BasicNPCRendererPlayer extends LivingEntityRenderer<BasicNPC, PlayerEntityModel<BasicNPC>> {

    public BasicNPCRendererPlayer(EntityRendererFactory.Context ctx, boolean slim) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim), 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this, new ArmorEntityModel<>(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_INNER_ARMOR : EntityModelLayers.PLAYER_INNER_ARMOR)), new ArmorEntityModel<>(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR : EntityModelLayers.PLAYER_OUTER_ARMOR)), ctx.getModelManager()));
        this.addFeature(new HeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
        this.addFeature(new StuckArrowsFeatureRenderer<>(ctx, this));
        this.addFeature(new HeadFeatureRenderer<>(this, ctx.getModelLoader(), ctx.getHeldItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer<>(this, ctx.getModelLoader()));
        this.addFeature(new TridentRiptideFeatureRenderer<>(this, ctx.getModelLoader()));
        this.addFeature(new StuckStingersFeatureRenderer<>(this));
    }

    public BasicNPCRendererPlayer(EntityRendererFactory.Context ctx) {
        this(ctx, false);
    }

    public Identifier getTexture(BasicNPC npc_entity) {
        NPCClientDataLoader skin_data_loader = NPCClientDataLoader.getInstance();
        ModelData npc_model = NPCServerDataLoader.getInstance().getModel(npc_entity);
        UUID to_display = npc_model.uuid();
        SkinTextures skin_to_render = skin_data_loader.getSkin(to_display);
        return skin_to_render.texture();
    }
}
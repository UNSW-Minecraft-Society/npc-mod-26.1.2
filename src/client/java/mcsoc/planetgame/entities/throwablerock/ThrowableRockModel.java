package mcsoc.planetgame.entities.throwablerock;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.registration.entities.ThrowableRockEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;

public class ThrowableRockModel<T extends ThrowableRockEntity> extends SinglePartEntityModel<T> {
        public static final EntityModelLayer ROCK = new EntityModelLayer(Identifier.of(PlanetGame.MOD_ID, "throwable_rock"), "main");
    private final ModelPart body;

    ThrowableRockModel(ModelPart root) {
        this.body = root.getChild(EntityModelPartNames.CUBE);
    }

    @Override
    public ModelPart getPart() {
        return body;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F), ModelTransform.pivot(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
            float headPitch) {
        return;
    }
    

}

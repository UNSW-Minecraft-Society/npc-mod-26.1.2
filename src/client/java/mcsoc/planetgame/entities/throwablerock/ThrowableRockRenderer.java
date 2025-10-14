package mcsoc.planetgame.entities.throwablerock;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.entities.throwables.ThrowableRockEntity;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;

public class ThrowableRockRenderer extends LivingEntityRenderer<ThrowableRockEntity, ThrowableRockModel<ThrowableRockEntity>> {

    public ThrowableRockRenderer(Context ctx) {
        super(ctx, new ThrowableRockModel<>(ctx.getPart(ThrowableRockModel.ROCK)), 0.75f);
    }

    @Override
    public Identifier getTexture(ThrowableRockEntity entity) {
        return Identifier.of(PlanetGame.MOD_ID, "textures/entity/rock/rock.png");
    }

}

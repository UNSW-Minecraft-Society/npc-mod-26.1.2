package mcsoc.planetgame.entities.throwablerock;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.registration.entities.ThrowableRockEntity;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class ThrowableRockRenderer extends ProjectileEntityRenderer<ThrowableRockEntity> {

    // ThrowableRockModel<ThrowableRockEntity>
    // new ThrowableRockModel<>(ctx.getPart(ThrowableRockModel.ROCK)), 0.75f
    
    public ThrowableRockRenderer(Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(ThrowableRockEntity entity) {
        return Identifier.of(PlanetGame.MOD_ID, "textures/entity/rock/rock.png");
    }

}

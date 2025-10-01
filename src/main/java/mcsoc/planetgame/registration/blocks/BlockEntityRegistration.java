package mcsoc.planetgame.registration.blocks;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.registration.blocks.gravityfieldblock.GravityFieldBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class BlockEntityRegistration {
    private BlockEntityRegistration() { /* delete */ }

    public static final BlockEntityType<GravityFieldBlockEntity> GRAVITY_FIELD_BLOCK_ENTITY =
            register("gravity_field_generator", GravityFieldBlockEntity::new, 
        BlockRegistration.GRAVITY_FIELD_BLOCK, 
        BlockRegistration.CAVE_GRAVITY_FIELD_BLOCK
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(String name,
                                                                    BlockEntityType.BlockEntityFactory<? extends T> entityFactory,
                                                                    Block... blocks) {
        Identifier id = Identifier.of(PlanetGame.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, BlockEntityType.Builder.<T>create(entityFactory, blocks).build());
    }

    public static void registerBlockEntities() {
        // init statics
    }
}

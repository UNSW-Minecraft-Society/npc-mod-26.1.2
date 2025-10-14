package mcsoc.planetgame.entities.damagesources;

import mcsoc.planetgame.PlanetGame;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class DamageSourceRegistration {
    private DamageSourceRegistration() { /* delete */ }
    
    public static final RegistryKey<DamageType> SPIKE_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(PlanetGame.MOD_ID, "spike_damage"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

    public static void registerDamageSources() {
        // init statics
    }
}

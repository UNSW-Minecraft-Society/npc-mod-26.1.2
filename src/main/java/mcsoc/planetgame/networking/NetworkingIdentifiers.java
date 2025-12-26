package mcsoc.planetgame.networking;

import mcsoc.planetgame.PlanetGame;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public abstract class NetworkingIdentifiers {

    private NetworkingIdentifiers() { /* delete */ }

    public static final Identifier PLAYER_SYNC_GRAVITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "sync_player_gravity_data_s2c_packet");
    public static final Identifier PLAYER_SYNC_DRILLERS_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "sync_player_mining_data_s2c_packet");

    public static final Identifier SET_FIRST_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "set_first_ability_c2s_packet");
    public static final Identifier SET_SECOND_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "set_second_ability_c2s_packet");
    public static final Identifier SET_THIRD_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "set_third_ability_c2s_packet");

    public static final Identifier TRIGGER_FIRST_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "trigger_first_ability_c2s_packet");
    public static final Identifier TRIGGER_SECOND_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "trigger_second_ability_c2s_packet");
    public static final Identifier TRIGGER_THIRD_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "trigger_third_ability_c2s_packet");

    
    public static void registerPackets() {
        PayloadTypeRegistry.playS2C().register(SyncPlayerGravityDataS2CPayload.ID, SyncPlayerGravityDataS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncPlayerDrillingDataS2CPayload.ID, SyncPlayerDrillingDataS2CPayload.CODEC);

        

        PayloadTypeRegistry.playC2S().register(TriggerFirstAbilityC2SPayload.ID, TriggerFirstAbilityC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TriggerSecondAbilityC2SPayload.ID, TriggerSecondAbilityC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TriggerThirdAbilityC2SPayload.ID, TriggerThirdAbilityC2SPayload.CODEC);
    }
}

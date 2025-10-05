package mcsoc.planetgame.networking;

import mcsoc.planetgame.PlanetGame;
import net.minecraft.util.Identifier;

public abstract class NetworkingIdentifiers {

    private NetworkingIdentifiers() { /* delete */ }

    public static final Identifier PLAYER_SYNC_GRAVITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "sync_player_gravity_data_s2c_packet");
    public static final Identifier FIRST_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "trigger_first_ability_c2s_packet");
    public static final Identifier SECOND_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "trigger_second_ability_c2s_packet");
    public static final Identifier THIRD_ABILITY_PACKET_ID = Identifier.of(PlanetGame.MOD_ID, "trigger_third_ability_c2s_packet");
}

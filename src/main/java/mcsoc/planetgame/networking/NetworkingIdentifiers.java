package mcsoc.planetgame.networking;

import net.minecraft.util.Identifier;

public abstract class NetworkingIdentifiers {

    private NetworkingIdentifiers() { /* delete */ }

    public static final Identifier PLAYER_SYNC_PACKET_ID = Identifier.of("planet_game", "sync_player_data_s2c_packet");
    public static final Identifier ABILITY_1_PACKET_ID = Identifier.of("planet_game", "trigger_ability1_c2s_packet");
    public static final Identifier ABILITY_2_PACKET_ID = Identifier.of("planet_game", "trigger_ability2_c2s_packet");
    public static final Identifier ABILITY_3_PACKET_ID = Identifier.of("planet_game", "trigger_ability3_c2s_packet");
}

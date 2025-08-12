package mcsoc.planetgame.Networking;

import net.minecraft.util.Identifier;

public abstract class NetworkingIdentifiers {
    public static final Identifier PLAYER_SYNC_PACKET_ID = Identifier.of("planet_game", "sync_player_data_s2c_packet");
}

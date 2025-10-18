package mcsoc.planetgame.statemanagement.gamestate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mcsoc.planetgame.statemanagement.ManagedPlayerState;
import net.minecraft.util.Uuids;

public record PersistentGameData(Map<UUID, ManagedPlayerState> player_state_map) {

    public static PersistentGameData getEmpty() {
        return new PersistentGameData(new HashMap<>());
    }
    
    public static final Codec<PersistentGameData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Codec.unboundedMap(Uuids.CODEC, ManagedPlayerState.CODEC).fieldOf("player_states_map").forGetter(PersistentGameData::player_state_map)
    ).apply(inst, PersistentGameData::new));
}

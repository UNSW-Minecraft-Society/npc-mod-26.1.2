package mcsoc.planetgame.statemanagement;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.statemanagement.PlayerState.GravityStrength;
import mcsoc.planetgame.statemanagement.PlayerState.PlayerFirstAbilities;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.Direction;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
 
public class GameState extends PersistentState {
    
    private Map<UUID, PlayerState> player_state_map = new HashMap<>();



    private GameState() {/* delete */}

    private GameState(Map<UUID, PlayerState> data) {
        player_state_map.putAll(data);
    }

    private Map<UUID, PlayerState> getStates() {
        return player_state_map;
    }
    
    public static final Codec<GameState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Codec.unboundedMap(Uuids.CODEC, PlayerState.CODEC).fieldOf("player_states_map").forGetter(GameState::getStates)
    ).apply(inst, GameState::new));


    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {

        DataResult<NbtElement> encodeResult = GameState.CODEC.encodeStart(NbtOps.INSTANCE, this);
        NbtElement element;
        
        try {
            element = encodeResult.result().orElseThrow();
        } catch (Exception e) {
            return nbt;
        }
        
        NbtCompound compound;
        if (element instanceof NbtCompound element_compound) {
            compound = element_compound;
        } else {
            compound = ((NbtList) element).getCompound(0);
        }

        nbt.copyFrom(compound);
        return nbt;
    }
    
    protected static GameState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        DataResult<Pair<GameState, NbtElement>> state_packed = GameState.CODEC.decode(NbtOps.INSTANCE, tag);
        return state_packed.getOrThrow().getFirst();
    }


    private static GameState createNewGameState() {
        GameState state = new GameState();

        // assign other stuff here

        return state;
    }


    private static Type<GameState> type = new Type<>(
            GameState::createNewGameState, // If there's no 'StateSaverAndLoader' yet create one and refresh variables
            GameState::createFromNbt, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
            DataFixTypes.LEVEL // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );
 
    protected static GameState getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
 
        // The first time the following 'getOrCreate' function is called, it creates a brand new 'StateSaverAndLoader' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'StateSaverAndLoader' NBT on disk to our function 'StateSaverAndLoader::createFromNbt'.
        GameState state = persistentStateManager.getOrCreate(type, PlanetGame.MOD_ID);
 
        // If state is not marked dirty, when Minecraft closes, 'writeNbt' won't be called and therefore nothing will be saved.
        // Technically it's 'cleaner' if you only mark state as dirty when there was actually a change, but the vast majority
        // of mod writers are just going to be confused when their data isn't being saved, and so it's best just to 'markDirty' for them.
        // Besides, it's literally just setting a bool to true, and the only time there's a 'cost' is when the file is written to disk when
        // there were no actual change to any of the mods state (INCREDIBLY RARE).
        state.markDirty();
 
        return state;
    }


    private PlayerState getPlayerState(UUID uuid) throws NoSuchElementException {
        PlayerState player_state = this.player_state_map.get(uuid);
        if (Objects.isNull(player_state)) {
            throw new NoSuchElementException("No player state entry associated with uuid!");
        }
        return player_state;
    }

    private PlayerState getOrCreatePlayerState(UUID uuid) throws NoSuchElementException {
        PlayerState player_state;
        try {
            player_state = getPlayerState(uuid);
        } catch (NoSuchElementException e) {
            player_state = PlayerState.getDefaultPlayerState();
        }
        return player_state;
    }

    private void setPlayerState(UUID uuid, PlayerState player_state) {
        this.player_state_map.put(uuid, player_state);
    }

    protected static PlayerState getPlayerState(UUID uuid, MinecraftServer server) {
        GameState state = getServerState(server);
        return state.getOrCreatePlayerState(uuid);
    }

    protected static PlayerState getPlayerState(ServerPlayerEntity player) {
        return getPlayerState(player.getUuid(), player.getServer());
    }

    protected static void setPlayerState(UUID uuid, MinecraftServer server, PlayerState player_state) {
        GameState state = getServerState(server);
        state.setPlayerState(uuid, player_state);
    }

    protected static void setPlayerState(ServerPlayerEntity player, PlayerState player_state) {
        setPlayerState(player.getUuid(), player.getServer(), player_state);
    }


    protected static Direction getPlayerGravityDirection(UUID uuid, MinecraftServer server) {
        PlayerState player_state = getPlayerState(uuid, server);
        return player_state.getCurrentPlayerGravityDirection();
    }

    protected static Direction getPlayerGravityDirection(ServerPlayerEntity player) {
        return getPlayerGravityDirection(player.getUuid(), player.getServer());
    }

    protected static void setPlayerGravityDirection(UUID uuid, MinecraftServer server, Direction grav_dir) {
        PlayerState player_state = getPlayerState(uuid, server);
        player_state = player_state.setPlayerGravityDirection(grav_dir);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerGravityDirection(ServerPlayerEntity player, Direction grav_dir) {
        setPlayerGravityDirection(player.getUuid(), player.getServer(), grav_dir);
    }


    protected static GravityStrength getPlayerGravStrengthModifier(UUID uuid, MinecraftServer server) {
        PlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerGravStrengthModifier();
    }

    protected static GravityStrength getPlayerGravStrengthModifier(ServerPlayerEntity player) {
        return getPlayerGravStrengthModifier(player.getUuid(), player.getServer());
    }

    protected static void setPlayerGravStrengthModifier(UUID uuid, MinecraftServer server, GravityStrength grav_strength_mod) {
        PlayerState player_state = getPlayerState(uuid, server);
        player_state = player_state.setPlayerGravStrengthModifier(grav_strength_mod);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerGravStrengthModifier(ServerPlayerEntity player, GravityStrength grav_strength_mod) {
        setPlayerGravStrengthModifier(player.getUuid(), player.getServer(), grav_strength_mod);
    }


    protected static PlayerFirstAbilities getPlayerFirstAbility(UUID uuid, MinecraftServer server) {
        PlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerFirstAbility();
    }

    protected static PlayerFirstAbilities getPlayerFirstAbility(ServerPlayerEntity player) {
        return getPlayerFirstAbility(player.getUuid(), player.getServer());
    }

    protected static void setPlayerFirstAbility(UUID uuid, MinecraftServer server, PlayerFirstAbilities first_ability) {
        PlayerState player_state = getPlayerState(uuid, server);
        PlanetGame.LOGGER.info("setting ability to {}", first_ability);
        player_state = player_state.setPlayerFirstAbility(first_ability);
        PlanetGame.LOGGER.info("set ability to {}", player_state.getPlayerFirstAbility());
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerFirstAbility(ServerPlayerEntity player, PlayerFirstAbilities first_ability) {
        setPlayerFirstAbility(player.getUuid(), player.getServer(), first_ability);
    }

}
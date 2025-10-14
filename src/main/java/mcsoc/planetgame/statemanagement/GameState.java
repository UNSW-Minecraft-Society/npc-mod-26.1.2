package mcsoc.planetgame.statemanagement;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.blocks.gravityfieldblock.GravityFieldBlockEntity;
import mcsoc.planetgame.eventhandlers.PerTickServerEvent;
import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.*;
import mcsoc.planetgame.statemanagement.playerstate.ManagedPlayerState;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class GameState extends PersistentState {
    
    // data I care about
    private PersistentGameData persistent_state_data = PersistentGameData.getEmpty();

    // data I don't care about
    private Instant prev_tick_time = Instant.now();
    private long pending_ticks = 0;
    private long pending_ticks_partial = 0;
    private long grav_field_update_tick_counter = 0;
    private Set<BlockPos> gravity_generator_locations = new HashSet<>();
    private Map<UUID, PlayerInventory> player_inventory_heap = new HashMap<>();


    private GameState(PersistentGameData data) {
        persistent_state_data.player_state_map().putAll(data.player_state_map());
    }

    private PersistentGameData getPersistentData() {
        return persistent_state_data;
    }

    private Map<UUID, ManagedPlayerState> getStates() {
        return getPersistentData().player_state_map();
    }
    
    public static final Codec<GameState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        PersistentGameData.CODEC.fieldOf("game_data").forGetter(GameState::getPersistentData)
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
        GameState state = new GameState(PersistentGameData.getEmpty());

        // assign other stuff here
        state.prev_tick_time = Instant.now();

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


    protected Stream<ManagedPlayerState> getPlayerStateStream() {
        return this.getStates().values().stream();
    }

    protected Stream<Entry<UUID, ManagedPlayerState>> getPlayerEntryStream() {
        return this.getStates().entrySet().stream();
    }

    private Optional<ManagedPlayerState> getPlayerState(UUID uuid) {
        return Optional.ofNullable(this.getStates().get(uuid));
    }

    private ManagedPlayerState getOrCreatePlayerState(UUID uuid) {
        return getPlayerState(uuid).orElse(ManagedPlayerState.getDefaultPlayerState());
    }

    private void setPlayerState(UUID uuid, ManagedPlayerState player_state) {
        this.getStates().put(uuid, player_state);
    }

    protected static ManagedPlayerState getPlayerState(UUID uuid, MinecraftServer server) {
        GameState state = getServerState(server);
        return state.getOrCreatePlayerState(uuid);
    }

    protected static ManagedPlayerState getPlayerState(ServerPlayerEntity player) {
        return getPlayerState(player.getUuid(), player.getServer());
    }

    protected static void setPlayerState(UUID uuid, MinecraftServer server, ManagedPlayerState player_state) {
        GameState state = getServerState(server);
        state.setPlayerState(uuid, player_state);
    }

    protected static void setPlayerState(ServerPlayerEntity player, ManagedPlayerState player_state) {
        setPlayerState(player.getUuid(), player.getServer(), player_state);
    }


    protected static PlayerFirstAbilities getPlayerFirstAbility(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerFirstAbility();
    }

    protected static PlayerFirstAbilities getPlayerFirstAbility(ServerPlayerEntity player) {
        return getPlayerFirstAbility(player.getUuid(), player.getServer());
    }

    protected static void setPlayerFirstAbility(UUID uuid, MinecraftServer server, PlayerFirstAbilities first_ability) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerFirstAbility(first_ability);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerFirstAbility(ServerPlayerEntity player, PlayerFirstAbilities first_ability) {
        setPlayerFirstAbility(player.getUuid(), player.getServer(), first_ability);
    }

    protected static Direction getPlayerGravityDirection(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getCurrentPlayerGravityDirection();
    }

    protected static Direction getPlayerGravityDirection(ServerPlayerEntity player) {
        return getPlayerGravityDirection(player.getUuid(), player.getServer());
    }

    protected static void setPlayerGravityDirection(UUID uuid, MinecraftServer server, Direction grav_dir) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerGravityDirection(grav_dir);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerGravityDirection(ServerPlayerEntity player, Direction grav_dir) {
        setPlayerGravityDirection(player.getUuid(), player.getServer(), grav_dir);
    }

    protected static GravityStrength getPlayerGravityStrengthModifier(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerGravityStrengthModifier();
    }

    protected static GravityStrength getPlayerGravityStrengthModifier(ServerPlayerEntity player) {
        return getPlayerGravityStrengthModifier(player.getUuid(), player.getServer());
    }

    protected static void setPlayerGravityStrengthModifier(UUID uuid, MinecraftServer server, GravityStrength grav_strength_mod) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerGravityStrengthModifier(grav_strength_mod);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerGravityStrengthModifier(ServerPlayerEntity player, GravityStrength grav_strength_mod) {
        setPlayerGravityStrengthModifier(player.getUuid(), player.getServer(), grav_strength_mod);
    }

    protected static boolean getPlayerInGravityField(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getIfPlayerInGravityField();
    }

    protected static boolean getPlayerInGravityField(ServerPlayerEntity player) {
        return getPlayerInGravityField(player.getUuid(), player.getServer());
    }

    protected static void setPlayerInGravityField(UUID uuid, MinecraftServer server, boolean in_field) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerInGravityField(in_field);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerInGravityField(ServerPlayerEntity player, boolean in_field) {
        setPlayerInGravityField(player.getUuid(), player.getServer(), in_field);
    }

    protected static boolean getPlayerGravityModified(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerGravityModified();
    }

    protected static boolean getPlayerGravityModified(ServerPlayerEntity player) {
        return getPlayerGravityModified(player.getUuid(), player.getServer());
    }

    protected static void setPlayerGravityModified(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerGravityModified();
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerGravityModified(ServerPlayerEntity player) {
        setPlayerGravityModified(player.getUuid(), player.getServer());
    }


    protected static PlayerSecondAbilities getPlayerSecondAbility(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerSecondAbility();
    }

    protected static PlayerSecondAbilities getPlayerSecondAbility(ServerPlayerEntity player) {
        return getPlayerSecondAbility(player.getUuid(), player.getServer());
    }

    protected static void setPlayerSecondAbility(UUID uuid, MinecraftServer server, PlayerSecondAbilities second_ability) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerSecondAbility(second_ability);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerSecondAbility(ServerPlayerEntity player, PlayerSecondAbilities second_ability) {
        setPlayerSecondAbility(player.getUuid(), player.getServer(), second_ability);
    }

    protected static boolean getPlayerSecondAbilityState(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerSecondAbilityState();
    }

    protected static boolean getPlayerSecondAbilityState(ServerPlayerEntity player) {
        return getPlayerSecondAbilityState(player.getUuid(), player.getServer());
    }

    protected static void setPlayerSecondAbilityState(UUID uuid, MinecraftServer server, boolean xray_on) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerSecondAbilityState(xray_on);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerSecondAbilityState(ServerPlayerEntity player, boolean xray_on) {
        setPlayerSecondAbilityState(player.getUuid(), player.getServer(), xray_on);
    }

    protected static double getPlayerDrillCharge(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerDrillCharge();
    }

    protected static double getPlayerDrillCharge(ServerPlayerEntity player) {
        return getPlayerDrillCharge(player.getUuid(), player.getServer());
    }

    protected static void setPlayerDrillCharge(UUID uuid, MinecraftServer server, double new_drill_charge) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerDrillCharge(new_drill_charge);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerDrillCharge(ServerPlayerEntity player, double new_drill_charge) {
        setPlayerDrillCharge(player.getUuid(), player.getServer(), new_drill_charge);
    }

    protected static double getPlayerDrillHeat(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerDrillHeat();
    }

    protected static double getPlayerDrillHeat(ServerPlayerEntity player) {
        return getPlayerDrillHeat(player.getUuid(), player.getServer());
    }

    public static void setPlayerDrillHeat(UUID uuid, MinecraftServer server, double new_drill_heat) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerDrillHeat(new_drill_heat);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerDrillHeat(ServerPlayerEntity player, double new_drill_heat) {
        setPlayerDrillHeat(player.getUuid(), player.getServer(), new_drill_heat);
    }

    public static void incrementPlayerDrillHeat(UUID uuid, MinecraftServer server, double added_drill_heat) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.incrementPlayerDrillHeat(added_drill_heat);
        setPlayerState(uuid, server, player_state);
    }

    protected static void incrementPlayerDrillHeat(ServerPlayerEntity player, double added_drill_heat) {
        incrementPlayerDrillHeat(player.getUuid(), player.getServer(), added_drill_heat);
    }

    public static void decrementPlayerDrillHeat(UUID uuid, MinecraftServer server, double removed_drill_heat) {
        GameState.decrementPlayerDrillHeat(uuid, server, removed_drill_heat);
    }

    protected static void decrementPlayerDrillHeat(ServerPlayerEntity player, double removed_drill_heat) {
        decrementPlayerDrillHeat(player.getUuid(), player.getServer(), removed_drill_heat);
    }


    protected static PlayerThirdAbilities getPlayerThirdAbility(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerThirdAbility();
    }

    protected static PlayerThirdAbilities getPlayerThirdAbility(ServerPlayerEntity player) {
        return getPlayerThirdAbility(player.getUuid(), player.getServer());
    }

    protected static void setPlayerThirdAbility(UUID uuid, MinecraftServer server, PlayerThirdAbilities third_ability) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerThirdAbility(third_ability);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerThirdAbility(ServerPlayerEntity player, PlayerThirdAbilities third_ability) {
        setPlayerThirdAbility(player.getUuid(), player.getServer(), third_ability);
    }

    protected static int getPlayerThirdAbilityCooldownTicks(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getPlayerThirdAbilityCooldownTicks();
    }

    protected static int getPlayerThirdAbilityCooldownTicks(ServerPlayerEntity player) {
        return getPlayerThirdAbilityCooldownTicks(player.getUuid(), player.getServer());
    }

    protected static void setPlayerThirdAbilityCooldownTicks(UUID uuid, MinecraftServer server, int cooldown_ticks) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setPlayerThirdAbilityCooldownTicks(cooldown_ticks);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setPlayerThirdAbilityCooldownTicks(ServerPlayerEntity player, int cooldown_ticks) {
        setPlayerThirdAbilityCooldownTicks(player.getUuid(), player.getServer(), cooldown_ticks);
    }

    protected static boolean getIfPlayerIsCarrying(UUID uuid, MinecraftServer server) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        return player_state.getIfPlayerIsCarrying();
    }

    protected static boolean getIfPlayerIsCarrying(ServerPlayerEntity player) {
        return getIfPlayerIsCarrying(player.getUuid(), player.getServer());
    }

    protected static void setIfPlayerIsCarrying(UUID uuid, MinecraftServer server, boolean is_carrying) {
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        player_state.setIfPlayerIsCarrying(is_carrying);
        setPlayerState(uuid, server, player_state);
    }

    protected static void setIfPlayerIsCarrying(ServerPlayerEntity player, boolean is_carrying) {
        setIfPlayerIsCarrying(player.getUuid(), player.getServer(), is_carrying);
    }


    protected static void updateTickTimings(MinecraftServer server) {
        GameState state = getServerState(server);
        
        long tick_delta_millis = Duration.between(state.prev_tick_time, Instant.now()).toMillis();
        long ticks_to_millis = 50;

        tick_delta_millis += state.pending_ticks_partial;
        state.pending_ticks_partial = tick_delta_millis % ticks_to_millis;
        state.pending_ticks = tick_delta_millis / ticks_to_millis;
        state.prev_tick_time = Instant.now();
    }

    protected static void tickPlayerState(UUID uuid, MinecraftServer server) {
        GameState state = getServerState(server);
        ManagedPlayerState player_state = getPlayerState(uuid, server);
        for (int i = 0; i < state.pending_ticks; i++) {
            player_state.tick();
        }
        setPlayerState(uuid, server, player_state);
    }

    protected static void tickPlayerState(ServerPlayerEntity player) {
        tickPlayerState(player.getUuid(), player.getServer());
    }

    protected static void tickGravityFieldTimer(MinecraftServer server) {
        GameState state = getServerState(server);
        state.grav_field_update_tick_counter += state.pending_ticks;
    }

    protected static void resetGravityFieldTimer(MinecraftServer server) {
        GameState state = getServerState(server);
        state.grav_field_update_tick_counter = 0;
    }

    protected static boolean shouldUpdateGravityFields(MinecraftServer server) {
        GameState state = getServerState(server);
        return state.grav_field_update_tick_counter >= PerTickServerEvent.GRAV_FIELD_UPDATE_TIME_TICKS;
    }


    protected static Instant getTimer(MinecraftServer server) {
        GameState state = getServerState(server);
        return state.prev_tick_time;
    }


    protected static void registerGravityGeneratorPosition(MinecraftServer server, GravityFieldBlockEntity entity) {
        GameState state = getServerState(server);
        state.gravity_generator_locations.add(entity.getPos());
    }

    protected static void forEachGravityGenerator(MinecraftServer server, Consumer<BlockPos> todo_for_each) {
        GameState state = getServerState(server);
        state.gravity_generator_locations.forEach(todo_for_each);
    }


    protected static void addInventoryToHeap(UUID uuid, MinecraftServer server, PlayerInventory inventory) {
        GameState state = getServerState(server);
        state.player_inventory_heap.put(uuid, inventory);
    }

    protected static void addInventoryToHeap(ServerPlayerEntity player, PlayerInventory inventory) {
        
        addInventoryToHeap(player.getUuid(), player.getServer(), inventory);
    }

    protected static Optional<Inventory> retrieveInventoryFromHeap(UUID uuid, MinecraftServer server) {
        GameState state = getServerState(server);
        return Optional.ofNullable(state.player_inventory_heap.get(uuid));
    }

    protected static Optional<Inventory> retrieveInventoryFromHeap(ServerPlayerEntity player) {
        return retrieveInventoryFromHeap(player.getUuid(), player.getServer());
    }
}
package mcsoc.planetgame.blocks;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.blocks.crackedblocks.CrackedBricksBlock;
import mcsoc.planetgame.blocks.gravityfieldblock.GravityFieldBlock;
import mcsoc.planetgame.blocks.rockpile.RockPileBlock;
import mcsoc.planetgame.blocks.spikes.SpikeBlock;
import mcsoc.planetgame.blocks.throwswitch.RockSwitch;
import mcsoc.planetgame.blocks.weightedpressureplate.WeightedGravityPlate;
import mcsoc.planetgame.blocks.xrayblock.XrayableSandBlock;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class BlockRegistration {
    private BlockRegistration() { /* delete */ }

    public static final Block ANDESITE_CASING = register(
        new Block(AbstractBlock.Settings.create()),
        "andesite_casing",
        true
    );

    public static final Block ANDESITE_ALLOY_BLOCK = register(
        new Block(AbstractBlock.Settings.create()),
        "andesite_alloy_block",
        true
    );

    public static final Block TRAIN_CASING = register(
        new Block(AbstractBlock.Settings.create()),
        "train_casing",
        true
    );

    public static final Block TRAIN_TRAPDOOR = register(
        new TrapdoorBlock(BlockSetType.DARK_OAK, AbstractBlock.Settings.create()),
        "train_trapdoor",
        true
    );

    public static final Block TRAIN_DOOR = register(
        new DoorBlock(BlockSetType.DARK_OAK, AbstractBlock.Settings.create()),
        "train_door",
        true
    );

    public static final Block ANDESITE_LADDER = register(
        new LadderBlock(AbstractBlock.Settings.create()
            .strength(0.4F)
            .sounds(BlockSoundGroup.LADDER)
            .nonOpaque()
            .pistonBehavior(PistonBehavior.DESTROY)
        ),
        "andesite_ladder",
        true
    );


    public static final Block WEIGHTED_PRESSURE_PLATE = register(
            new WeightedGravityPlate(AbstractBlock.Settings.create()),
            "weighted_pressure_plate",
            true
    );

    public static final Block GRAVITY_FIELD_BLOCK = register(
            new GravityFieldBlock(AbstractBlock.Settings.create()),
            "gravity_field_generator",
            true
    );

    public static final Block P1_SPIKE_BLOCK = register(
        new SpikeBlock(AbstractBlock.Settings.create()), 
        "spike",
        true
    );


    public static final Block XRAYABLE_SAND_BLOCK = register(
        new XrayableSandBlock(Settings.create()), 
        "xrayable_sand", 
        true
    );

    


    public static final Block ROCK_PILE_BLOCK = register(
        new RockPileBlock(AbstractBlock.Settings.create()),
        "rock_pile",
        true
    );

    public static final Block CRACKED_BRICKS_BLOCK = register(
        new CrackedBricksBlock(AbstractBlock.Settings.create()),
        "cracked_bricks",
        true
    );

    public static final Block ROCK_SWITCH_BLOCK = register(
        new RockSwitch(AbstractBlock.Settings.create()),
        "rock_switch",
        true
    );
    
    public static final Block CAVE_GRAVITY_FIELD_BLOCK = register(
        new GravityFieldBlock(AbstractBlock.Settings.create(), 200),
        "cave_gravity_field_generator",
        true
    );


    public static final RegistryKey<ItemGroup> PLANET_GAME_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(PlanetGame.MOD_ID, "planet_game_group"));
    public static final ItemGroup PLANET_GAME_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(GRAVITY_FIELD_BLOCK))
        .displayName(Text.translatable("itemGroup.planet_game"))
    .build();

    
    // from fabric wiki: https://docs.fabricmc.net/1.21/develop/blocks/block-entities
    public static Block register(Block block, String name, boolean should_register_item) {
		// Register the block and its item.
		Identifier id = Identifier.of(PlanetGame.MOD_ID, name);

		// Sometimes, you may not want to register an item for the block.
		// Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
		if (should_register_item) {
			BlockItem blockItem = new BlockItem(block, new Item.Settings());
			Registry.register(Registries.ITEM, id, blockItem);
		}

		return Registry.register(Registries.BLOCK, id, block);
	}


    public static void registerBlocks() {
        Registry.register(Registries.ITEM_GROUP, PLANET_GAME_GROUP_KEY, PLANET_GAME_GROUP);

        ItemGroupEvents.modifyEntriesEvent(PLANET_GAME_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(GRAVITY_FIELD_BLOCK.asItem());
            itemGroup.add(WEIGHTED_PRESSURE_PLATE.asItem());
            itemGroup.add(P1_SPIKE_BLOCK.asItem());
            itemGroup.add(XRAYABLE_SAND_BLOCK.asItem());
            itemGroup.add(ROCK_PILE_BLOCK.asItem());
            itemGroup.add(CRACKED_BRICKS_BLOCK.asItem());
            itemGroup.add(ROCK_SWITCH_BLOCK.asItem());
            itemGroup.add(CAVE_GRAVITY_FIELD_BLOCK.asItem());
        });
    }
}
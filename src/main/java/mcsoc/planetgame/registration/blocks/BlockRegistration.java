package mcsoc.planetgame.registration.blocks;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.registration.blocks.gravityfieldblock.GravityFieldBlock;
import mcsoc.planetgame.registration.blocks.rockpile.RockPileBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class BlockRegistration {
    private BlockRegistration() { /* delete */ }

    public static final Block WEIGHTED_PRESSURE_PLATE = register(
            new SuperHeavyPressurePlateBlock(1.5D, BlockSetType.POLISHED_BLACKSTONE, AbstractBlock.Settings.create()),
            "weighted_pressure_plate",
            true
    );
    public static final Block GRAVITY_FIELD_BLOCK = register(
            new GravityFieldBlock(AbstractBlock.Settings.create()),
            "gravity_field_generator",
            true
    );
    public static final Block ROCK_PILE_BLOCK = register(
            new RockPileBlock(AbstractBlock.Settings.create()),
            "rock_pile",
            true
    );
    
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
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(itemGroup -> {
            itemGroup.add(GRAVITY_FIELD_BLOCK.asItem());
            itemGroup.add(WEIGHTED_PRESSURE_PLATE.asItem());
        });
    }
}
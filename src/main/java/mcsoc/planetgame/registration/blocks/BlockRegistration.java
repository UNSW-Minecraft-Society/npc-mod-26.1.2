package mcsoc.planetgame.registration.blocks;

import mcsoc.planetgame.PlanetGame;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class BlockRegistration {
    private BlockRegistration() { /* delete */ }

    public static final Block WEIGHTED_PRESSURE_PLATE = new SuperHeavyPressurePlateBlock(1.5D, BlockSetType.POLISHED_BLACKSTONE, AbstractBlock.Settings.create().strength(4.0f));

    public static void registerBlockItemPair(Block block, String block_name) {
        Registry.register(Registries.BLOCK, Identifier.of(PlanetGame.MOD_ID, block_name), block);
        Registry.register(Registries.ITEM, Identifier.of(PlanetGame.MOD_ID, block_name), new BlockItem(block, new Item.Settings()));
    }

    public static void RegisterBlocks() {
        registerBlockItemPair(WEIGHTED_PRESSURE_PLATE, "weighted_pressure_plate");
    }
}
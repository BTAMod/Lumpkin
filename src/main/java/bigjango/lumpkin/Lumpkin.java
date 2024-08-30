package bigjango.lumpkin;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import net.minecraft.client.render.block.model.BlockModelHorizontalRotation;
import net.minecraft.core.block.tag.BlockTags;

import net.fabricmc.api.ModInitializer;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import turniplabs.halplibe.helper.RecipeBuilder;

public class Lumpkin implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "lumpkin";

    static public int blockId = 4800;
    static public Block lumpkin;

    @Override
    public void onInitialize() {
    }

	@Override
	public void beforeGameStart() {
	    lumpkin = new BlockBuilder(MOD_ID)
	        .setHardness(1.0F)
	        .setVisualUpdateOnMetadata()
	        .setTags(BlockTags.MINEABLE_BY_AXE)
	        .setBlockModel(block -> new BlockModelHorizontalRotation<>(block)
	            .withTextures(
	                "minecraft:block/pumpkin_top",
	                "minecraft:block/pumpkin_bottom",
	                "minecraft:block/pumpkin_side"
                )
            ).build(new BlockLumpkin("block.lumpkin", blockId++));
        EntityHelper.createSpecialTileEntity(TileEntityLumpkin.class, "Lumpkin", () -> new TileEntityRendererLumpkin());
	}

	@Override
	public void afterGameStart() {
	}

	@Override
	public void onRecipesReady() {
	    RecipeBuilder.Shapeless(MOD_ID)
            .addInput(Block.pumpkin)
            .create("relumpkin", new ItemStack(lumpkin));
	    RecipeBuilder.Shapeless(MOD_ID)
            .addInput(lumpkin)
            .create("delumpkin", new ItemStack(Block.pumpkin));
	}

	@Override
	public void initNamespaces() {
	    RecipeBuilder.initNameSpace(MOD_ID);
	}
}

package bigjango.lumpkin;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;

public class BlockLumpkin extends BlockTileEntity {
    public BlockLumpkin(String key, int id) {
        super(key, id, Material.vegetable);
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, Side side, double xHit, double yHit) {
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.getItem() instanceof net.minecraft.core.item.tool.ItemToolSword) {
            if (side == Side.TOP || side == Side.BOTTOM) return false;
            TileEntityLumpkin lumpkin = (TileEntityLumpkin) world.getBlockTileEntity(x, y, z);
            lumpkin.carve(side, xHit * 16, yHit * 16);
            return true;
        }
        return false;
    }

    public TileEntity getNewBlockEntity() {
        return new TileEntityLumpkin();
    }

    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        ItemStack stack = new ItemStack(Lumpkin.lumpkin);
        if (tileEntity != null) {
            CompoundTag compound = new CompoundTag();
            ((TileEntityLumpkin) tileEntity).writeNewNBT(compound);
            stack.getData().putCompound("LumpkinData", compound);
        }
        return new ItemStack[] { stack };
    }

    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        EntityPlayer player = (EntityPlayer) entity;
        ItemStack held = player.getHeldItem();
        if (held == null) return;
        CompoundTag lumpkinData = held.getData().getCompoundOrDefault("LumpkinData", null);
        if (lumpkinData == null) return;

        TileEntityLumpkin lumpkin = (TileEntityLumpkin) world.getBlockTileEntity(x, y, z);
        lumpkin.readNewNBT(lumpkinData);
    }
}

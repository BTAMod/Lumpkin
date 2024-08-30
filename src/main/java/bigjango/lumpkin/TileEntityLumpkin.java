package bigjango.lumpkin;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;

public class TileEntityLumpkin extends TileEntity {
    public short[][] carves = new short[4][64];

    public void carve(Side side, double xPlaced, double yPlaced) {
        int id = (int) xPlaced * 16 + (int) yPlaced;
        int bit = id & 15;
        id >>= 4;
        carves[side.getId() - 2][id] ^= 1 << bit;
    }

    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        writeNewNBT(tag);
    }

    public void writeNewNBT(CompoundTag tag) {
        for (int i = 0; i < 4; i++) {
            tag.putShortArray("S" + i, carves[i]);
        }
    }

    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        readNewNBT(tag);
    }

    public void readNewNBT(CompoundTag tag) {
        for (int i = 0; i < 4; i++) {
            carves[i] = tag.getShortArrayOrDefault("S" + i, null);
            if (carves[i] == null) {
                carves[i] = new short[64];
            }
        }
    }
}

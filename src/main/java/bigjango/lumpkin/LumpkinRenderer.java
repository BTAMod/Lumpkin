package bigjango.lumpkin;

import net.minecraft.client.GLAllocation;
import net.minecraft.client.util.helper.Colors;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.client.render.RenderEngine;

public class LumpkinRenderer {
    private final RenderEngine renderEngine;
    public int[] texture = new int[4];

    public LumpkinRenderer(RenderEngine renderEngine) {
        this.renderEngine = renderEngine;

        this.texture[0] = renderEngine.createTexture(16, 16);
        this.texture[1] = renderEngine.createTexture(16, 16);
        this.texture[2] = renderEngine.createTexture(16, 16);
        this.texture[3] = renderEngine.createTexture(16, 16);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        GLAllocation.deleteTexture(this.texture[0]);
        GLAllocation.deleteTexture(this.texture[1]);
        GLAllocation.deleteTexture(this.texture[2]);
        GLAllocation.deleteTexture(this.texture[3]);
    }

    public void updateTexture(TileEntityLumpkin tileEntity) {
        int[] data = new int[16 * 16];
        int color_carved = 0xff441300;
        int color_shadow = 0xff2d0003;
        for (int i = 0; i < 4; i++) {
            boolean first_bit = true;
            for (int id = 255; id >= 0; id--) {
                int bit = id >> 4;
                int index = id & 15;
                // Reset
                if (index == 15) first_bit = true;
                // Get carved
                boolean carved = ((tileEntity.carves[i][index] >> bit) & 1) == 1;
                // Draw
                if (carved && first_bit) data[id] = color_shadow;
                else if (carved) data[id] = color_carved;
                else data[id] = 0;
                // Update first data
                first_bit = !carved;
            }
            this.renderEngine.updateTextureData(data, 16, 16, this.texture[i]);
        }
    }
}

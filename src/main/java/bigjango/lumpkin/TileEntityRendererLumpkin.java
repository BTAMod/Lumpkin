package bigjango.lumpkin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererLumpkin extends TileEntityRenderer<TileEntityLumpkin> {
    public final Minecraft mc = Minecraft.getMinecraft(this);
    private LumpkinRenderer lumpkinRenderer = null;
    private Cube[] qbs = new Cube[4];

    public TileEntityRendererLumpkin() {
        qbs[0] = new Cube(0, 0, 16, 16);
        qbs[0].addBox(0, 0, 0, 16, 16, 0);
        qbs[1] = new Cube(0, 0, 16, 16);
        qbs[1].addBox(0, 0, 0, -16, 16, 0);
        qbs[2] = new Cube(0, 0, 16, 16);
        qbs[2].addBox(0, 0, 0, 0, 16, -16);
        qbs[3] = new Cube(0, 0, 16, 16);
        qbs[3].addBox(0, 0, 0, 0, 16, 16);
    }

    @Override
    public void doRender(Tessellator tessellator, TileEntityLumpkin tileEntity, double x, double y, double z, float partialTick) {
        if (this.lumpkinRenderer == null)
            this.lumpkinRenderer = new LumpkinRenderer(this.renderDispatcher.renderEngine);
        this.lumpkinRenderer.updateTexture(tileEntity);
        GL11.glPushMatrix();
        float scale = 1.0F;
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glPushMatrix();

        // South
        this.renderDispatcher.renderEngine.bindTexture(this.lumpkinRenderer.texture[0]);
        qbs[0].render(1.0f / 16.0f);
        // West
        this.renderDispatcher.renderEngine.bindTexture(this.lumpkinRenderer.texture[3]);
        GL11.glTranslatef(1.001F, 0.0F, 0.0F);
        qbs[3].render(1.0f / 16.0f);
        // North
        this.renderDispatcher.renderEngine.bindTexture(this.lumpkinRenderer.texture[1]);
        GL11.glTranslatef(-0.001F, 0.0F, 1.001F);
        qbs[1].render(1.0f / 16.0f);
        // East
        this.renderDispatcher.renderEngine.bindTexture(this.lumpkinRenderer.texture[2]);
        GL11.glTranslatef(-1.0F, 0.0F, -0.001F);
        qbs[2].render(1.0f / 16.0f);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}

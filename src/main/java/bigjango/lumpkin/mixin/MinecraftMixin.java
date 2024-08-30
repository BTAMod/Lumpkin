package bigjango.lumpkin.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.HitResult;
import net.minecraft.core.item.ItemStack;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.core.world.World;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.client.entity.player.EntityPlayerSP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
    @Shadow
    public HitResult objectMouseOver;

    @Shadow
    public PlayerController playerController;

    @Shadow private int mouseTicksRan;
    @Shadow private int ticksRan;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public World theWorld;
    @Shadow
    public WorldRenderer worldRenderer;

    /**
    * No JavaDoc for you! :)
    * @author BTA
    * @reason it was broken
    */
    @Overwrite
    private void mineBlocks(int i, boolean flag) {
        if (flag && objectMouseOver != null && objectMouseOver.hitType == HitResult.HitType.TILE && i == 0) {
            double xPlaced;
            int blockX = objectMouseOver.x;
            int blockY = objectMouseOver.y;
            int blockZ = objectMouseOver.z;
            Side side = objectMouseOver.side;
            double yPlaced = objectMouseOver.location.yCoord - objectMouseOver.y;
            if (side.getAxis() == Axis.X) {
                xPlaced = objectMouseOver.location.zCoord - objectMouseOver.z;
            } else if (side.getAxis() == Axis.Z) {
                xPlaced = objectMouseOver.location.xCoord - objectMouseOver.x;
            } else {
                xPlaced = objectMouseOver.location.zCoord - objectMouseOver.z;
                yPlaced = objectMouseOver.location.xCoord - objectMouseOver.x;
            }
            playerController.continueDestroyBlock(blockX, blockY, blockZ, objectMouseOver.side, xPlaced, yPlaced);
        } else {
           playerController.stopDestroyBlock(flag);
        }
    }

    /**
    * No JavaDoc for you! :)
    * @author BTA
    * @reason it was broken
    */
    @Overwrite
    private void clickMouse(int clickType, boolean attack, boolean repeat) {
        mouseTicksRan = ticksRan;
        boolean flag = true;
        if (objectMouseOver == null) {
            if (clickType == 0 && attack)
                playerController.swingItem(true);
        } else if (objectMouseOver.hitType == HitResult.HitType.ENTITY) {
            if (clickType == 0 && attack) {
                playerController.swingItem(true);
                playerController.attack(thePlayer, objectMouseOver.entity);
            }
            if (clickType == 1)
                if (playerController.interact(thePlayer, objectMouseOver.entity))
                    flag = false;
        } else if (objectMouseOver.hitType == HitResult.HitType.TILE) {
            double xPlaced;
            int blockX = objectMouseOver.x;
            int blockY = objectMouseOver.y;
            int blockZ = objectMouseOver.z;
            Side side = objectMouseOver.side;
            double yPlaced = objectMouseOver.location.yCoord - objectMouseOver.y;
            if (side.getAxis() == Axis.X) {
                xPlaced = objectMouseOver.location.zCoord - objectMouseOver.z;
            } else if (side.getAxis() == Axis.Z) {
                xPlaced = objectMouseOver.location.xCoord - objectMouseOver.x;
            } else {
                xPlaced = objectMouseOver.location.zCoord - objectMouseOver.z;
                yPlaced = objectMouseOver.location.xCoord - objectMouseOver.x;
            }
            if (clickType == 0) {
                playerController.startDestroyBlock(blockX, blockY, blockZ, objectMouseOver.side, xPlaced, yPlaced, repeat);
                playerController.swingItem(true);
            } else {
                ItemStack stack = thePlayer.inventory.getCurrentItem();
                int numItemsInStack = (stack == null) ? 0 : stack.stackSize;
                if (playerController.useItemOn(thePlayer, theWorld, stack, blockX, blockY, blockZ, side, xPlaced, yPlaced)) {
                    flag = false;
                    playerController.swingItem(true);
                }
                if (stack == null)
                    return;
                if (stack.stackSize <= 0) {
                    thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
                } else if (stack.stackSize != numItemsInStack) {
                    worldRenderer.itemRenderer.resetEquippedProgress();
                }
            }
        }
        if (flag && clickType == 1) {
            ItemStack itemstack = thePlayer.inventory.getCurrentItem();
            if (itemstack != null && playerController.useItem(thePlayer, theWorld, itemstack))
                worldRenderer.itemRenderer.resetEquippedProgress();
        }
    }
}

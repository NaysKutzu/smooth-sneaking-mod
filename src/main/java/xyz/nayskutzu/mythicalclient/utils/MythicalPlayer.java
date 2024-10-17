package xyz.nayskutzu.mythicalclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import xyz.nayskutzu.mythicalclient.MythicalClientMod;

public class MythicalPlayer {
    public static MythicalPlayer instance = new MythicalPlayer();
    private MythicalClientMod mod = MythicalClientMod.instance;
    private Config config = Config.instance;
    private Minecraft mc = Minecraft.getMinecraft();
    private BlockPos currentBlock;
    private EnumFacing currentFacing;
    private float yawBefore;
    private float pitchBefore;
    private boolean sneaking = false;
    private boolean safewalkSneaking = false;
    private boolean userSneaking = false;
    private long lastRightClick = System.currentTimeMillis();
    private int rightClickDelay = 290;

    public void onJump() {
    if (this.config.jump && this.mod.isToggled()) {
        this.mod.toggle(false);
        this.mod.sendToggle(this.config.getMode(this.config.mode), "on", "off", false);
    }
}

public void onTick() {
    // Check if player is falling and safewalk is toggled
    if (this.mc.thePlayer.prevPosY - this.mc.thePlayer.posY > 0.4 && this.config.fall && this.mod.isToggled()) {
        this.mod.toggle(false);
        this.mod.sendToggle(this.config.getMode(this.config.mode), "on", "off", false);
    }

    double playerX = this.mc.thePlayer.posX;
    double playerY = this.mc.thePlayer.posY - 1.0;
    double playerZ = this.mc.thePlayer.posZ;
    BlockPos currentBlock = new BlockPos((int) Math.floor(playerX), (int) Math.floor(playerY), (int) Math.floor(playerZ));

    if (this.currentBlock == null || !this.isSameBlock(currentBlock, this.currentBlock)) {
        this.currentBlock = currentBlock;
    }

    // Check if the current block is air (not solid) and set safewalk sneaking
    if (this.mc.theWorld.isAirBlock(this.currentBlock)) {
        this.setSafewalkSneaking(true);
    } else {
        this.setSafewalkSneaking(false);
    }

    this.checkSneak();
}

private void checkSneak() {
    if (!this.userSneaking) {
        if (this.mc.thePlayer.onGround) {
            this.sneaking = this.safewalkSneaking;
            this.doSafewalk();
        } else {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    } else {
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
    }
}

private void doSafewalk() {
    if (this.config.mode == 1) {
        if (this.isSneaking() != this.mc.thePlayer.isSneaking()) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), this.isSneaking());
        }
        if (this.config.click) {
            this.tryRightClick();
        }
    }

    if (this.config.mode == 2) {
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        if (this.isSneaking()) {
            this.mc.thePlayer.motionX *= 0.5;
            this.mc.thePlayer.motionZ *= 0.5;

            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.getBlockPos() != null) {
                if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                    if (this.isSameBlock(this.mc.objectMouseOver.getBlockPos(), this.getBlock(this.currentBlock, this.mc.objectMouseOver.sideHit))) {
                        if (this.isFacingSide(this.mc.objectMouseOver.sideHit)) {
                            if (System.currentTimeMillis() - this.lastRightClick > this.rightClickDelay) {
                                this.lastRightClick = System.currentTimeMillis();
                                this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), this.mc.objectMouseOver.getBlockPos(), this.mc.objectMouseOver.sideHit, this.mc.objectMouseOver.hitVec);
                                this.mc.thePlayer.swingItem();
                            }
                        } else {
                            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                        }
                    }
                } else {
                    KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
            } else {
                KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
        }
    }

    if (this.config.mode == 3) {
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        if (this.isSneaking()) {
            if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                try {
                    this.yawBefore = this.mc.thePlayer.rotationYaw;
                    this.pitchBefore = this.mc.thePlayer.rotationPitch;
                    this.setBlockAndFacing(this.currentBlock);
                    float[] facing = BlockUtil.getDirectionToBlock(this.currentBlock.getX(), this.currentBlock.getY(), this.currentBlock.getZ(), this.currentFacing);
                    float yawToBlock = facing[0];
                    float pitchToBlock = Math.min(90.0f, facing[1] + 9.0f);

                    this.mc.thePlayer.rotationYaw = yawToBlock;
                    this.mc.thePlayer.rotationPitch = pitchToBlock;

                    this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), this.currentBlock, this.currentFacing, new Vec3(this.currentBlock.getX(), this.currentBlock.getY(), this.currentBlock.getZ()));
                    this.mc.thePlayer.swingItem();

                    new Delay(0) {
                        @Override
                        public void onTick() {
                            mc.thePlayer.rotationYaw = yawBefore;
                            mc.thePlayer.rotationPitch = pitchBefore;
                        }
                    };
                } catch (NullPointerException e) {
                    KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                }
            } else {
                KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
        }
    }
}

/**private void tryRightClick() {
    if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.getBlockPos() != null && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock && this.isFacingSide(this.mc.objectMouseOver.sideHit) && this.isSameBlock(this.mc.objectMouseOver.getBlockPos(), this.getBlock(this.currentBlock, this.mc.objectMouseOver.sideHit))) {
        if (System.currentTimeMillis() - this.lastRightClick > this.rightClickDelay) {
            this.lastRightClick = System.currentTimeMillis();
            try {
                java.awt.Robot robot = new java.awt.Robot();
                robot.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
            } catch (java.awt.AWTException e) {
                e.printStackTrace();
            }
            this.mc.thePlayer.swingItem();
        } else {
            this.mc.thePlayer.swingItem();
        }
    }
}**/

private void tryRightClick() {
    if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.getBlockPos() != null && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock && this.isFacingSide(this.mc.objectMouseOver.sideHit) && this.isSameBlock(this.mc.objectMouseOver.getBlockPos(), this.getBlock(this.currentBlock, this.mc.objectMouseOver.sideHit))) {
        if (System.currentTimeMillis() - this.lastRightClick > this.rightClickDelay) {
            this.lastRightClick = System.currentTimeMillis();
            // Player needs to right-click manually
            //this.mc.thePlayer.swingItem();
        } else {
            //this.mc.thePlayer.swingItem();
        }
    }
}

public void setSafewalkSneaking(boolean safewalkSneaking) {
    this.safewalkSneaking = safewalkSneaking;
}

public void setUserSneaking(boolean userSneaking) {
    this.userSneaking = userSneaking;
}

private boolean isSneaking() {
    return this.sneaking;
}

private boolean isSameBlock(BlockPos pos1, BlockPos pos2) {
    return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
}

private boolean isFacingSide(EnumFacing facing) {
    return facing != EnumFacing.UP && facing != EnumFacing.DOWN;
}

private BlockPos getBlock(BlockPos block, EnumFacing facing) {
    switch (facing) {
        case NORTH:
            return new BlockPos(block.getX(), block.getY(), block.getZ() + 1);
        case SOUTH:
            return new BlockPos(block.getX(), block.getY(), block.getZ() - 1);
        case EAST:
            return new BlockPos(block.getX() - 1, block.getY(), block.getZ());
        case WEST:
            return new BlockPos(block.getX() + 1, block.getY(), block.getZ());
        case UP:
            return new BlockPos(block.getX(), block.getY() + 1, block.getZ());
        default:
            return block;
    }
}

private void setBlockAndFacing(BlockPos block) {
    if (this.mc.theWorld.getBlockState(block.down()).getBlock() != Blocks.air) {
        this.currentBlock = block.down();
        this.currentFacing = EnumFacing.UP;
    } else if (this.mc.theWorld.getBlockState(block.west()).getBlock() != Blocks.air) {
        this.currentBlock = block.west();
        this.currentFacing = EnumFacing.EAST;
    } else if (this.mc.theWorld.getBlockState(block.east()).getBlock() != Blocks.air) {
        this.currentBlock = block.east();
        this.currentFacing = EnumFacing.WEST;
    } else if (this.mc.theWorld.getBlockState(block.north()).getBlock() != Blocks.air) {
        this.currentBlock = block.north();
        this.currentFacing = EnumFacing.SOUTH;
    } else if (this.mc.theWorld.getBlockState(block.south()).getBlock() != Blocks.air) {
        this.currentBlock = block.south();
        this.currentFacing = EnumFacing.NORTH;
    } else {
        this.currentBlock = null;
        this.currentFacing = null;
    }
}

}

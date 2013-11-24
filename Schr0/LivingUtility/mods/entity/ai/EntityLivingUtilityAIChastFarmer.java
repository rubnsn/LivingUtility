package Schr0.LivingUtility.mods.entity.ai;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class EntityLivingUtilityAIChastFarmer extends AIBaseEntityLivingUtility {
    //再実行間隔
    private final static int COOL_TIME = 40;
    private int timer;

    public EntityLivingUtilityAIChastFarmer(EntityLivingUtility LivingUtility) {
        super(LivingUtility);
    }

    @Override
    public boolean shouldExecute() {
        if (timer-- < 0) {
            timer = COOL_TIME;
            for (int i = -3; i < 3; i++) {
                for (int k = -3; k < 3; k++) {
                    int targetPosX = (int) this.theUtility.posX + i;
                    int targetPosY = (int) this.theUtility.posY;
                    int targetPosZ = (int) this.theUtility.posZ + k;
                    Block block = this.getBlockTargetPosition(targetPosX, targetPosY, targetPosZ);
                    List<ItemStack> list = this.getItemTargetPosition(this.theWorld, block, targetPosX, targetPosY, targetPosZ);
                    if (list != null) {
                        for (ItemStack itemStack : list) {
                            this.theUtility.addItemStackToInventory(itemStack);
                            this.theUtility.getNavigator().tryMoveToXYZ(targetPosX, targetPosY, targetPosZ, 1.0F);
                        }
                        if (0 < list.size()) {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Block getBlockTargetPosition(int targetPosX, int targetPosY, int targetPosZ) {
        return Block.blocksList[this.theWorld.getBlockId(targetPosX, targetPosY, targetPosZ)];
    }

    //アイテム化させる
    private List<ItemStack> getItemTargetPosition(World world, Block plant, int targetPosX, int targetPosY, int targetPosZ) {
        int blockID = world.getBlockId(targetPosX, targetPosY, targetPosZ);
        if (blockID == Block.crops.blockID) {
            return getCrop(world, targetPosX, targetPosY, targetPosZ);
        }
        if (blockID == Block.reed.blockID || blockID == Block.cactus.blockID) {
            return getReed(world, plant, targetPosX, targetPosY, targetPosZ);
        }
        if (blockID == Block.melon.blockID || blockID == Block.pumpkin.blockID) {
            List list = this.getBlockDropped(world, this.getBlockTargetPosition(targetPosX, targetPosY, targetPosZ), targetPosX, targetPosY, targetPosZ);
            this.theWorld.setBlockToAir(targetPosX, targetPosY, targetPosZ);
            return list;
        }
        return null;
    }

    //さとーきびとサボテン
    private List<ItemStack> getReed(World world, Block block, int targetPosX, int targetPosY, int targetPosZ) {
        ArrayList<ItemStack> res = new ArrayList<ItemStack>();
        int count = 0;
        while (this.getBlockTargetPosition(targetPosX, ++targetPosY, targetPosZ) == block) {
            count++;
        }
        while (0 < count--) {
            res.addAll(this.getBlockDropped(world, block, targetPosX, targetPosY--, targetPosZ));
            this.theWorld.setBlockToAir(targetPosX, targetPosY, targetPosZ);
        }
        return res;
    }

    //むぎ
    private List<ItemStack> getCrop(World world, int targetPosX, int targetPosY, int targetPosZ) {
        if (world.getBlockMetadata(targetPosX, targetPosY, targetPosZ) == 7) {
            Block block = this.getBlockTargetPosition(targetPosX, targetPosY, targetPosZ);
            return pullPlantIfPossible(world, block, targetPosX, targetPosY, targetPosZ, Item.seeds.itemID, 0);
        }
        return null;
    }

    //ひっこぬいて植えられるなら植える系
    private List<ItemStack> pullPlantIfPossible(World world, Block block, int targetPosX, int targetPosY, int targetPosZ, int seedId, int meta) {
        List<ItemStack> res = getBlockDropped(world, block, targetPosX, targetPosY, targetPosZ);
        this.theWorld.setBlockToAir(targetPosX, targetPosY, targetPosZ);
        for (ItemStack is : res) {
            if (is.itemID == seedId && is.getItemDamage() == meta && 0 < is.stackSize) {
                is.stackSize--;
                world.setBlock(targetPosX, targetPosY, targetPosZ, block.blockID);
                break;
            }
        }
        return res;

    }

    private List<ItemStack> getBlockDropped(World world, Block block, int targetPosX, int targetPosY, int targetPosZ) {
        return block.getBlockDropped(world, targetPosX, targetPosY, targetPosZ, world.getBlockMetadata(targetPosX, targetPosY, targetPosZ), 0);
    }

}

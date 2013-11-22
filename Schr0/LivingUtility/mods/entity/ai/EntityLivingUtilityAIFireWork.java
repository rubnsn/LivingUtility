package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class EntityLivingUtilityAIFireWork extends AIBaseEntityLivingUtility {
    //探す周期
    private static final int COOL_TIME_SEARCH = 20;
    //使う周期
    private static final int COOL_TIME_USE = 20;
    private int timer;
    private ItemStack containerStack;
    //ぶっちゃけなんでもいい、ちょっと変えると右クリック代行チャスト君！
    private static final ItemStack GUN_POWDER = new ItemStack(Item.gunpowder);

    public EntityLivingUtilityAIFireWork(EntityLivingUtility LivingUtility) {
        super(LivingUtility);
    }

    @Override
    public boolean shouldExecute() {
        if (this.timer-- < 0) {
            this.timer = this.COOL_TIME_SEARCH;
            this.containerStack = this.getContainerItemEqual(GUN_POWDER);
            if (this.containerStack != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void startExecuting() {
    }

    @Override
    public boolean continueExecuting() {
        return 0 < this.containerStack.stackSize && this.theUtility.getHeldItem() != null && this.theUtility.getHeldItem().getItem() == Item.firework;
    }

    @Override
    public void resetTask() {
        this.theUtility.setOpen(false);
    }

    @Override
    public void updateTask() {
        if (timer-- < 0) {
            this.theUtility.setOpen(true);
            timer = COOL_TIME_USE;
            EntityLivingBase entity = this.theUtility.getOwner();
            if (entity instanceof EntityPlayer) {
                //このへんで減らすと火薬が減る
                if (this.theUtility.getHeldItem() != null && 0 < this.containerStack.stackSize) {
                    this.theUtility.getHeldItem().stackSize++;
                    this.theUtility.getHeldItem().tryPlaceItemIntoWorld((EntityPlayer) entity, this.theWorld, (int) this.theUtility.posX, (int) this.theUtility.posY, (int) this.theUtility.posZ, 0, 0F, 1F, 0F);
                }
            }
        }
    }

    private ItemStack getContainerItemEqual(ItemStack is) {
        boolean isWird = is.getItemDamage() == Short.MAX_VALUE;
        for (int i = 0; i < this.theUtility.getSizeInventory(); i++) {
            if (this.theUtility.getStackInSlot(i) != null && 0 < this.theUtility.getStackInSlot(i).stackSize) {
                if (isWird) {
                    if (this.theUtility.getStackInSlot(i).getItem() == is.getItem()) {
                        return this.theUtility.getStackInSlot(i);
                    }
                } else {
                    if (this.theUtility.getStackInSlot(i).isItemEqual(is)) {
                        return this.theUtility.getStackInSlot(i);
                    }
                }
            }
        }
        return null;
    }
}

package Schr0.LivingUtility.mods.entity.ai;

import Schr0.LivingUtility.mods.entity.EntityLivingUtility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityLivingUtilityAIFreedom implements ILivingUtilityAI {

    @Override
    public boolean hasExecution(ItemStack handItm) {
        return handItm != null ? handItm.isItemEqual(new ItemStack(Item.feather)) : false;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String getMessage() {
        return "Freedom";
    }

    @Override
    public void addTasks(EntityLivingUtility entity, EntityAITasks tasks) {
        tasks.addTask(4, new EntityLivingUtilityAICollectItem(entity, 1.25F));
        tasks.addTask(5, new EntityAIWander(entity, 1.25F));
    }

}

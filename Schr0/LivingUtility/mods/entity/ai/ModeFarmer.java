package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class ModeFarmer implements ILivingUtilityAI {
    @Override
    public boolean hasExecution(ItemStack handItm) {
        return handItm != null ? handItm.getItem() instanceof ItemHoe : false;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String getMessage() {
        return "Farmer";
    }

    @Override
    public void addTasks(EntityLivingUtility entity) {
        entity.tasks.addTask(this.getPriority(), new EntityLivingUtilityAIChastFarmer(entity));
        entity.tasks.addTask(5, new EntityAIWander(entity, 1.25F));
    }
}

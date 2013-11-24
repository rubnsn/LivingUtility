package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.item.ItemStack;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class ModeFollowOwner implements ILivingUtilityAI {

    @Override
    public boolean hasExecution(ItemStack handItm) {
        return handItm == null;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String getMessage() {
        return "Follow";
    }

    @Override
    public void addTasks(EntityLivingUtility entity) {
        entity.tasks.addTask(this.getPriority(), new EntityLivingUtilityAIFollowOwner(entity, 1.25F, 2.0F, 2.0F));
    }
}

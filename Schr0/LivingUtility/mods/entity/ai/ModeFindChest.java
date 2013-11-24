package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.item.ItemStack;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class ModeFindChest implements ILivingUtilityAI {
    @Override
    public boolean hasExecution(ItemStack handItm) {
        return handItm != null ? handItm.isItemEqual(new ItemStack(Block.chest)) : false;
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public String getMessage() {
        return "FindChest";
    }

    @Override
    public void addTasks(EntityLivingUtility entity) {
        entity.tasks.addTask(this.getPriority(), new EntityLivingUtilityAIFindChest(entity, 1.25F));
        entity.tasks.addTask(5, new EntityLivingUtilityAICollectItem(entity, 1.25F));
        entity.tasks.addTask(6, new EntityAIWander(entity, 1.25F));
    }
}

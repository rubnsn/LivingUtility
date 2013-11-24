package Schr0.LivingUtility.mods.entity.ai;

import java.util.List;

import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.item.ItemStack;

public interface ILivingUtilityAI {
    public boolean hasExecution(ItemStack handItm);
    public int getPriority();
    public String getMessage();
    public void addTasks(EntityLivingUtility entity,EntityAITasks tasks);
}

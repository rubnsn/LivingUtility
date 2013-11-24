package Schr0.LivingUtility.mods.entity.ai;

import java.util.PriorityQueue;

import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

import net.minecraft.item.ItemStack;

public class ChastModeManager {
    private PriorityQueue<ILivingUtilityAI> aiQueue = new PriorityQueue<ILivingUtilityAI>(6, new AIComparator());

    public ChastModeManager() {
        aiQueue.add(new ModeFollowOwner());
        aiQueue.add(new ModeFindChest());
        aiQueue.add(new ModeVillagersLove());
        aiQueue.add(new ModeFarmer());
        aiQueue.add(new ModeFreedom());
    }

    public void setTasks(EntityLivingUtility entity,ItemStack is) {
        if(!entity.worldObj.isRemote){
            for (ILivingUtilityAI ilu : aiQueue) {
                if (ilu.hasExecution(is)) {
                    if (ilu.getMessage() != null) {
                        entity.Information(entity.getInvName() + " : " + ilu.getMessage());
                    }
                    ilu.addTasks(entity);
                    return;
                }
            }
        }
    }
}

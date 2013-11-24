package Schr0.LivingUtility.mods.entity.ai;

import java.util.Comparator;

public class AIComparator implements Comparator {

    @Override
    public int compare(Object arg0, Object arg1) {
        if (arg0 instanceof ILivingUtilityAI && arg1 instanceof ILivingUtilityAI) {
            return ((ILivingUtilityAI) arg0).getPriority() - ((ILivingUtilityAI) arg1).getPriority();
        }
        return 0;
    }

}

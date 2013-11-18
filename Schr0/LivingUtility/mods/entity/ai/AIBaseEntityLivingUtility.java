package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class AIBaseEntityLivingUtility extends EntityAIBase
{
	protected EntityLivingUtility	theUtility;
	protected World					theWorld;
	
	public AIBaseEntityLivingUtility(EntityLivingUtility LivingUtility)
	{
		theUtility = LivingUtility;
		theWorld = LivingUtility.worldObj;
	}
	
	@Override
	public boolean shouldExecute()
	{
		return false;
	}
}

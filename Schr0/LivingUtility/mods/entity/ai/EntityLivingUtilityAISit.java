package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class EntityLivingUtilityAISit extends AIBaseEntityLivingUtility
{
	/** If the EntityTameable is sitting. */
	private boolean	isSitting	= false;
	
	public EntityLivingUtilityAISit(EntityLivingUtility LivingUtility)
	{
		super(LivingUtility);
		this.setMutexBits(5);
	}
	
	public boolean shouldExecute()
	{
		if (!this.theUtility.isTamed())
		{
			return false;
		}
		else if (this.theUtility.isInWater())
		{
			return false;
		}
		else if (!this.theUtility.onGround)
		{
			return false;
		}
		else
		{
			EntityLivingBase entityLivingbase = this.theUtility.getOwner();
			return entityLivingbase == null ? true : (this.theUtility.getDistanceSqToEntity(entityLivingbase) < 144.0D && entityLivingbase.getAITarget() != null ? false : this.isSitting);
		}
	}
	
	public void startExecuting()
	{
		this.theUtility.getNavigator().clearPathEntity();
		this.theUtility.setSitting(true);
	}
	
	public void resetTask()
	{
		this.theUtility.setSitting(false);
	}
	
	public void setSitting(boolean par1)
	{
		this.isSitting = par1;
	}
}

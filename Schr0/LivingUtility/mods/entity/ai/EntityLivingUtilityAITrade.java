package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class EntityLivingUtilityAITrade extends AIBaseEntityLivingUtility
{
	public EntityLivingUtilityAITrade(EntityLivingUtility Utility)
	{
		super(Utility);
		this.setMutexBits(5);
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (!this.theUtility.isEntityAlive())
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
		else if (this.theUtility.velocityChanged)
		{
			return false;
		}
		else
		{
			EntityPlayer entityplayer = this.theUtility.getCustomer();
			return entityplayer == null ? false : (this.theUtility.getDistanceSqToEntity(entityplayer) > 16.0D ? false : entityplayer.openContainer instanceof Container);
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.theUtility.getNavigator().clearPathEntity();
	}
	
	@Override
	public void resetTask()
	{
		this.theUtility.setCustomer((EntityPlayer) null);
	}
}

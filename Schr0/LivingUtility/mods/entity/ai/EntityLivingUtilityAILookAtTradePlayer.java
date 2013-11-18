package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class EntityLivingUtilityAILookAtTradePlayer extends EntityAIWatchClosest
{
	private final EntityLivingUtility	theUtility;
	
	public EntityLivingUtilityAILookAtTradePlayer(EntityLivingUtility Utility)
	{
		super(Utility, EntityPlayer.class, 8.0F);
		this.theUtility = Utility;
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (this.theUtility.isTrading())
		{
			this.closestEntity = this.theUtility.getCustomer();
			return true;
		}
		else
		{
			return false;
		}
	}
}

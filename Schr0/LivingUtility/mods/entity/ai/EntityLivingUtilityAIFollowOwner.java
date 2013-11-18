package Schr0.LivingUtility.mods.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class EntityLivingUtilityAIFollowOwner extends AIBaseEntityLivingUtility
{
	private EntityLivingBase	theOwner;
	private final float			moveSpeed;
	private final PathNavigate	pathfinder;
	private int					catchCounter;
	private boolean				avoidsWater;
	
	float						maxDist;
	float						minDist;
	
	public EntityLivingUtilityAIFollowOwner(EntityLivingUtility LivingUtility, float speed, float min, float max)
	{
		super(LivingUtility);
		this.moveSpeed = speed;
		this.pathfinder = LivingUtility.getNavigator();
		this.minDist = min;
		this.maxDist = max;
		this.setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute()
	{
		EntityLivingBase LivingBase = this.theUtility.getOwner();
		
		if (LivingBase == null)
		{
			return false;
		}
		else if (this.theUtility.isSitting())
		{
			return false;
		}
		else if (this.theUtility.getDistanceSqToEntity(LivingBase) < (double) (this.minDist * this.minDist))
		{
			return false;
		}
		else
		{
			this.theOwner = LivingBase;
			return true;
		}
	}
	
	@Override
	public boolean continueExecuting()
	{
		return !this.pathfinder.noPath() && this.theUtility.getDistanceSqToEntity(this.theOwner) > (double) (this.maxDist * this.maxDist) && !this.theUtility.isSitting();
	}
	
	@Override
	public void startExecuting()
	{
		this.catchCounter = 0;
		this.avoidsWater = this.theUtility.getNavigator().getAvoidsWater();
		this.theUtility.getNavigator().setAvoidsWater(false);
	}
	
	@Override
	public void resetTask()
	{
		this.theOwner = null;
		this.pathfinder.clearPathEntity();
		this.theUtility.getNavigator().setAvoidsWater(this.avoidsWater);
	}
	
	@Override
	public void updateTask()
	{
		this.theUtility.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float) this.theUtility.getVerticalFaceSpeed());
		
		if (!this.theUtility.isSitting())
		{
			if (--this.catchCounter <= 0)
			{
				this.catchCounter = 10;
				
				if (!this.pathfinder.tryMoveToEntityLiving(this.theOwner, this.moveSpeed))
				{
					if (this.theUtility.getDistanceSqToEntity(this.theOwner) >= 144.0D)
					{
						int x = MathHelper.floor_double(this.theOwner.posX) - 2;
						int z = MathHelper.floor_double(this.theOwner.posZ) - 2;
						int y = MathHelper.floor_double(this.theOwner.boundingBox.minY);
						
						for (int l = 0; l <= 4; ++l)
						{
							for (int i1 = 0; i1 <= 4; ++i1)
							{
								if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(x + l, y - 1, z + i1) && !this.theWorld.isBlockNormalCube(x + l, y, z + i1) && !this.theWorld.isBlockNormalCube(x + l, y + 1, z + i1))
								{
									this.theUtility.setLocationAndAngles((double) ((float) (x + l) + 0.5F), (double) y, (double) ((float) (z + i1) + 0.5F), this.theUtility.rotationYaw, this.theUtility.rotationPitch);
									this.pathfinder.clearPathEntity();
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}

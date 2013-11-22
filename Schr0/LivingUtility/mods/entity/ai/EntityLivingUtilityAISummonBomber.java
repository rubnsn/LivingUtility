package Schr0.LivingUtility.mods.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;

import Schr0.LivingUtility.mods.entity.EntityLivingChest;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

public class EntityLivingUtilityAISummonBomber extends AIBaseEntityLivingUtility {
    //のこりの問題・再起動時にオプションのタスクがもげる。
    //NBTのりーどらいとをAI側にも回せばいける
    //改変部位が結構本体の仕様に食い込むので待機、すちゅおあああああ氏がんばって(他人事)
    private LinkedList<EntityLivingChest> chiledList = new LinkedList<EntityLivingChest>();
    //最大同時召喚数
    private static final int CHAIN_MAX = 3;
    private int timer;
    //5秒に1回
    private static final int COOL_TIME_EXECUTE = 100;
    //ぱこぱこモーション用待機
    private static final int COOL_TIME_CREATE = 20;
    //探索距離
    private static final int SERCH_RANGE = 10;
    private Entity target;

    public EntityLivingUtilityAISummonBomber(EntityLivingUtility LivingUtility) {
        super(LivingUtility);
    }

    @Override
    public boolean shouldExecute() {
        if (this.timer-- < 0) {
            //存在しないエントリの削除
            Iterator<EntityLivingChest> ite = this.chiledList.iterator();
            while (ite.hasNext()) {
                Entity entity = ite.next();
                if (entity == null || entity.isDead) {
                    ite.remove();
                }
            }
            //敵の探索
            if (this.chiledList.size() < this.CHAIN_MAX) {
                float minDistance = 100;
                for (Entity e : this.getInRangeEntitys(this.SERCH_RANGE, 2, this.SERCH_RANGE)) {
                    if (e instanceof EntityMob) {
                        if (this.theUtility.getDistanceToEntity(e) < minDistance) {
                            minDistance = this.theUtility.getDistanceToEntity(e);
                            this.target = (EntityLiving) e;
                        }
                    }
                }
                return true;
            }
            this.timer = this.COOL_TIME_EXECUTE;
        }
        return false;
    }

    private List<Entity> getInRangeEntitys(int rangeX, int rangeY, int rangeZ) {
        return theWorld.getEntitiesWithinAABBExcludingEntity(this.theUtility, this.theUtility.boundingBox.expand(rangeX, rangeY, rangeZ));
    }

    @Override
    public void startExecuting() {
        this.theUtility.setOpen(true);
        this.timer = this.COOL_TIME_CREATE;
    }

    @Override
    public boolean continueExecuting() {
        return 0 < this.timer--;
    }

    @Override
    public void resetTask() {
        this.theUtility.setOpen(false);
        this.timer = this.COOL_TIME_EXECUTE;
    }

    @Override
    public void updateTask() {
        if (this.timer == this.COOL_TIME_CREATE) {
            EntityLivingChest entity = new EntityLivingChest(this.theWorld);
            entity.setPosition(this.theUtility.posX, this.theUtility.posY, this.theUtility.posZ);
            entity.setOwner("");
            entity.setTamed(true);
            entity.tasks.taskEntries.clear();
            entity.tasks.addTask(1, new AIBombing(entity, this.target));
            this.chiledList.addLast(entity);
            this.theWorld.spawnEntityInWorld(entity);
        }
    }

    //オプション専用AI
    private class AIBombing extends AIBaseEntityLivingUtility {
        private int timer;
        private static final int COOL_TIME = 100;
        private Entity target;

        public AIBombing(EntityLivingUtility LivingUtility, Entity target) {
            super(LivingUtility);
            this.target = target;
        }

        @Override
        public boolean shouldExecute() {
            if (this.target != null && !target.isDead) {
                return true;
            }else{
                this.theUtility.setDead();
            }
            return false;
        }

        @Override
        public void startExecuting() {
            this.timer = this.COOL_TIME;
        }

        @Override
        public boolean continueExecuting() {
            return 0 < this.timer-- && this.target != null && !target.isDead;
        }

        @Override
        public void resetTask() {
            this.theUtility.setDead();
        }

        @Override
        public void updateTask() {
            if (this.target != null && !target.isDead) {
                this.theUtility.getNavigator().tryMoveToEntityLiving(this.target, 2.0F);
                if (this.theUtility.getDistanceToEntity(this.target) < 2) {
                    this.destruct();
                }
            }
        }

        private void destruct() {
            this.theWorld.createExplosion(this.theUtility, this.theUtility.posX, this.theUtility.posY, this.theUtility.posZ, 1.0F, true);
            this.theUtility.setDead();
        }
    }
}

package Schr0.LivingUtility.mods.entity.ai;

import java.util.List;

import Schr0.LivingUtility.mods.entity.EntityLivingChest;
import Schr0.LivingUtility.mods.entity.EntityLivingUtility;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class EntityLivingUtilityAIEatVillager extends AIBaseEntityLivingUtility {
    //捕食開始距離
    private static final float EAT_RANGE = 1F;
    //おっかけ時間制限
    private static final int TIME_LIMIT = 600;
    private static final int COOL_TIME=20;
    //ドロップ開始率
    private static final float DROP_RATE = 0.1F;
    //ドロップ連鎖率
    private static final float CHAIN_DROP_RATE = 0.25F;
    //ドロップアイテムマップ
    private static final Multimap<Integer, ItemStack> DROP_MAP = ArrayListMultimap.create();
    static {
        //同レアリティからはランダムで1種のみ
        DROP_MAP.put(0, new ItemStack(Item.rottenFlesh));
        DROP_MAP.put(0, new ItemStack(Item.silk));
        DROP_MAP.put(1, new ItemStack(Item.leather));
        DROP_MAP.put(1, new ItemStack(Item.rottenFlesh, 2));
        DROP_MAP.put(2, new ItemStack(Item.leather, 2));
        DROP_MAP.put(2, new ItemStack(Item.beefRaw));
        DROP_MAP.put(2, new ItemStack(Item.porkRaw));
        DROP_MAP.put(2, new ItemStack(Item.fishRaw));
        DROP_MAP.put(2, new ItemStack(Item.chickenRaw));
        DROP_MAP.put(3, new ItemStack(Item.goldNugget));
        DROP_MAP.put(3, new ItemStack(Item.redstone, 2));
        DROP_MAP.put(3, new ItemStack(Item.dyePowder, 2, 4));//らぴす
        DROP_MAP.put(4, new ItemStack(Item.ingotIron));
        DROP_MAP.put(4, new ItemStack(Item.goldNugget, 2));
        DROP_MAP.put(4, new ItemStack(Item.redstone, 4));
        DROP_MAP.put(4, new ItemStack(Item.dyePowder, 4, 4));//らぴす
        DROP_MAP.put(4, new ItemStack(Item.glowstone, 2));
        DROP_MAP.put(5, new ItemStack(Item.ingotGold));
        DROP_MAP.put(5, new ItemStack(Item.ingotIron, 2));
        DROP_MAP.put(5, new ItemStack(Item.glowstone, 4));
        DROP_MAP.put(6, new ItemStack(Item.emerald));
        DROP_MAP.put(6, new ItemStack(Item.ingotGold, 2));
        DROP_MAP.put(7, new ItemStack(Item.diamond));
        DROP_MAP.put(7, new ItemStack(Item.emerald, 2));
    }
    //獲物
    private EntityLiving entity;
    //つかまえた
    private boolean capture;
    //おっかけ時間
    private int timer;
    //蓋の進行方向
    private boolean directionCover;

    public EntityLivingUtilityAIEatVillager(EntityLivingUtility LivingUtility) {
        super(LivingUtility);
        this.capture = false;
    }

    @Override
    public boolean shouldExecute() {
        this.entity = null;
        if(this.timer--<0){
            this.timer=COOL_TIME;
            float minDistance = 100;
            //最短距離の獲物を探そう！
            for (Entity e : this.getInRangeEntitys(5, 2, 5)) {
                if (e instanceof EntityVillager) {
                    if (this.theUtility.getDistanceToEntity(e) < minDistance) {
                        minDistance = this.theUtility.getDistanceToEntity(e);
                        this.entity = (EntityLiving) e;
                    }
                }
            }
        }
        return this.entity != null;
    }

    private List<Entity> getInRangeEntitys(int rangeX, int rangeY, int rangeZ) {
        return theWorld.getEntitiesWithinAABBExcludingEntity(this.theUtility, this.theUtility.boundingBox.expand(rangeX, rangeY, rangeZ));
    }

    @Override
    public void startExecuting() {
        this.timer = 0;
    }

    @Override
    public boolean continueExecuting() {
        return entity != null&& !this.entity.isDead && timer++ < TIME_LIMIT || capture;
    }

    @Override
    public void resetTask() {
        this.entity = null;
        ((EntityLivingChest) this.theUtility).setOpen(false);
        this.capture = false;
        //パーティクルを止める
        this.theUtility.getDataWatcher().updateObject(30, "");
        this.theUtility.getNavigator().clearPathEntity();
        this.timer = 0;
    }

    @Override
    public void updateTask() {
        if (entity != null) {
            this.theUtility.getNavigator().tryMoveToXYZ(this.entity.posX, this.entity.posY, this.entity.posZ, 2);
            if (!capture) {
                if (this.theUtility.getDistanceToEntity(entity) < EntityLivingUtilityAIEatVillager.EAT_RANGE) {
                    capture = true;
                    //頑張って逃げよう！
                    entity.tasks.addTask(0, new EntityAIAvoidEntity(this.theUtility, this.theUtility.getClass(), 8.0F, 1.2D, 1.2D));
                    timer = TIME_LIMIT;
                }
            } else {
                entity.attackEntityFrom(DamageSource.magic, 1);
                //もぐもぐ
                this.eatAction();
                //剥ぎ取りタイム
                if (this.theWorld.rand.nextFloat() < DROP_RATE) {
                    this.dropChance(0);
                }
                //お食事終了！
                if (this.entity.isDead) {
                    this.theWorld.playSoundAtEntity(this.theUtility, "random.burp", 1.5F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F);
                    capture = false;
                }
            }
        }
    }

    private void eatAction() {
        //ぱこぱこ
        float angle = ((EntityLivingChest) this.theUtility).getCoverAngle(1);
        if (directionCover) {
            directionCover = angle < Math.PI / 2;
        } else {
            directionCover = angle < 0F;
        }
        ((EntityLivingChest) this.theUtility).setOpen(directionCover);
        ((EntityLivingChest) this.theUtility).setLidAngle(angle += directionCover ? 0.8F : -0.8F);
        //食べ散らかしはiconcrack_ItemID_Damegeで指定
        this.theUtility.getDataWatcher().updateObject(30, "iconcrack_363_0");
        this.theUtility.playSound("random.eat", 0.5F + 0.5F * this.theWorld.rand.nextInt(2), (this.theWorld.rand.nextFloat() - this.theWorld.rand.nextFloat()) * 0.2F + 1.0F);
    }

    private void dropChance(int reality) {
        ItemStack is = null;
        if (DROP_MAP.containsKey(reality)) {
            is = this.getRandomDrop(reality).copy();
        }
        //たまーにポロリもアリかなって
        if (is != null) {
            this.theUtility.entityDropItem(is, 0.5F);
            //確率で村人のサイフ(?)からさらなるレアをゲット！
            if (this.theWorld.rand.nextFloat() < CHAIN_DROP_RATE) {
                this.dropChance(reality + 1);
            }
        }
    }

    private ItemStack getRandomDrop(int reality) {
        return (ItemStack) DROP_MAP.get(reality).toArray()[theWorld.rand.nextInt(DROP_MAP.get(reality).size())];
    }

}

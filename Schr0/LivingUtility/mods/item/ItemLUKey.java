package Schr0.LivingUtility.mods.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import Schr0.LivingUtility.mods.LivingUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLUKey extends Item
{
	//Iconの配列
	private final String[]	textures	= new String[]
										{
										LivingUtility.TextureDomain + "UtilityKey"
										};
	
	@SideOnly(Side.CLIENT)
	private Icon[]			Icons;
	
	public ItemLUKey(int id)
	{
		super(id);
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	//内部名の設定
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		int Meta = (textures.length - 1);
		int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, Meta);
		return super.getUnlocalizedName() + "." + "LU_Materials_" + i;
	}
	
	//metaIconsの登録
	@Override
	public Icon getIconFromDamage(int par1)
	{
		int Meta = (textures.length - 1);
		int i = MathHelper.clamp_int(par1, 0, Meta);
		return this.Icons[i];
	}
	
	//使用するテクスチャファイル
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.Icons = new Icon[textures.length];
		
		for (int i = 0; i < textures.length; ++i)
		{
			this.Icons[i] = par1IconRegister.registerIcon(textures[i]);
		}
	}
	
	//クリエイティブに表示するアイテムの設定
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < textures.length; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	//アイテム情報の表示
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		String name;
		
		//NBT値を取得
		NBTTagCompound nbt = par1ItemStack.getTagCompound();
		if (nbt == null)
		{
			name = "Unknown";
		}
		else
		{
			name = nbt.getString("OwnerName");
		}
		
		//オーナー名
		par3List.add("Owner Name : " + name);
	}
	
	//右クリックの処理
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		double posX = par3EntityPlayer.posX;
		double posY = par3EntityPlayer.posY;
		double posZ = par3EntityPlayer.posZ;
		
		//通常キーの場合
		if (par1ItemStack.getItemDamage() == 0)
		{
			//NBTタグを取得
			NBTTagCompound nbt = par1ItemStack.getTagCompound();
			if (nbt == null)
			{
				nbt = new NBTTagCompound();
				par1ItemStack.setTagCompound(nbt);
			}
			
			String OwnerName = nbt.getString("OwnerName");
			
			if (OwnerName.length() > 0)
			{
				return par1ItemStack;
			}
			//登録されていない ＆ スニーキング状態の場合
			else if (par3EntityPlayer.isSneaking())
			{
				//クライントだけの処理
				if (!par2World.isRemote)
				{
					//NBTの登録
					nbt.setString("OwnerName", par3EntityPlayer.username);
				}
				
				//メッセージの表示（独自）
				this.Message(par3EntityPlayer, "Set OwnerName : " + par3EntityPlayer.username);
				
				//SEの出力（独自
				this.playSE(par2World, posX, posY, posZ, "random.orb", 1.0F, 1.0F);
				
				//Itemを振る動作
				par3EntityPlayer.swingItem();
			}
		}
		return par1ItemStack;
	}
	
	//SEの出力（独自）
	private void playSE(World world, double PosX, double PosY, double PosZ, String type, float vol, float pitch)
	{
		world.playSoundEffect(PosX, PosY, PosZ, type, vol, pitch);
	}
	
	//メッセージの表示（独自）
	private void Message(EntityPlayer player, String messe)
	{
		//クライアントだけの処理
		if (!player.worldObj.isRemote)
		{
			player.addChatMessage(messe);
		}
	}
	
}

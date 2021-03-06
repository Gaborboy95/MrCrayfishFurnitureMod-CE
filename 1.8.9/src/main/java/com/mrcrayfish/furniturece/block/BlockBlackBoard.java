package com.mrcrayfish.furniturece.block;

import java.util.List;

import com.mrcrayfish.furniturece.MrCrayfishFurnitureModCE;
import com.mrcrayfish.furniturece.Reference;
import com.mrcrayfish.furniturece.gui.GuiBlackBoard;
import com.mrcrayfish.furniturece.tileentity.TileEntityBlackBoard;
import com.mrcrayfish.furniturece.util.CollisionHelper;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBlackBoard extends BlockFurniture implements ITileEntityProvider
{
	public static final PropertyBool LEFT = PropertyBool.create("left");
	public static final PropertyBool RIGHT = PropertyBool.create("right");
	
	public static boolean placed = false;
	
	public BlockBlackBoard(Material materialIn) 
	{
		super(materialIn);
		this.setHardness(0.5F);
		this.setStepSound(soundTypeWood);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LEFT, Boolean.valueOf(false)).withProperty(RIGHT, Boolean.valueOf(false)));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(placer instanceof EntityPlayer)
		{
			if(worldIn.isRemote && placed)
			{
				placer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "TIP: " + EnumChatFormatting.YELLOW + "Place blackboards to the left and right to extend it."));
				placed = true;
			}
			((EntityPlayer) placer).openGui(MrCrayfishFurnitureModCE.instance, GuiBlackBoard.ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) 
	{
		int meta = getMetaFromState(worldIn.getBlockState(pos));
		CollisionHelper.setBlockBounds(this, meta, 8 * 0.0625F, 0F, 0F, 1F, 1F, 1F);
		super.setBlockBoundsBasedOnState(worldIn, pos);
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) 
	{
		int meta = getMetaFromState(state);
		CollisionHelper.setBlockBounds(this, meta, 14 * 0.0625F, 0F, 0F, 1F, 1F, 1F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		CollisionHelper.setBlockBounds(this, meta, 8 * 0.0625F, 0F, 0F, 14 * 0.0625F, 0.0625F, 1F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) 
	{
		EnumFacing facing = (EnumFacing) state.getValue(FACING);
		IBlockState leftState = worldIn.getBlockState(pos.offset(facing.rotateYCCW()));
		IBlockState rightState = worldIn.getBlockState(pos.offset(facing.rotateY()));
		boolean left = leftState.getBlock() == this && leftState.getValue(FACING).equals(facing);
		boolean right = rightState.getBlock() == this && rightState.getValue(FACING).equals(facing);;
		return state.withProperty(LEFT, Boolean.valueOf(left)).withProperty(RIGHT, Boolean.valueOf(right));
	}
	

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityBlackBoard();
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, FACING, LEFT, RIGHT);
	}

	@Override
	public String getAuthorName() 
	{
		return "Pre55ure";
	}

	@Override
	public int getAuthorID() 
	{
		return 310;
	}

	@Override
	public String getTheme()
	{
		return Reference.THEME_BASIC;
	}
}

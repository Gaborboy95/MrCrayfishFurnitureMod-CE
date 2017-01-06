package com.mrcrayfish.furniturece.tileentity.render;

import java.awt.Color;
import java.util.List;

import com.mrcrayfish.furniturece.block.BlockBlackBoard;
import com.mrcrayfish.furniturece.tileentity.TileEntityBlackBoard;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class BlackBoardRenderer extends TileEntitySpecialRenderer<TileEntityBlackBoard>
{
	@Override
	public void renderTileEntityAt(TileEntityBlackBoard doorMat, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if (doorMat.getMessage() != null)
		{
			IBlockState state = getWorld().getBlockState(doorMat.getPos());
			if (state.getBlock() instanceof BlockBlackBoard)
			{
				int rotation = state.getValue(BlockBlackBoard.FACING).getHorizontalIndex();
				GlStateManager.pushMatrix();
				{
					GlStateManager.translate(x, y, z);
					GlStateManager.translate(0.5, 0.5625F, 0.5);
					GlStateManager.rotate(-90F * rotation, 0, 1, 0);
					GlStateManager.rotate(180F, 0, 1, 0);
					GlStateManager.translate(-0.375, 0, 0);
					GlStateManager.translate(0, 0, -0.4);
					GlStateManager.scale(1, -1, -1);
					GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
					int lines = getFontRenderer().listFormattedStringToWidth(doorMat.getMessage(), 50).size();
					GlStateManager.translate(0, -(lines * getFontRenderer().FONT_HEIGHT - 1) / 2, 0);
					GlStateManager.enableAlpha();
					GlStateManager.disableLighting();
					drawSplitString(getFontRenderer(), doorMat.getMessage(), 0, 0, 50, Color.white.getRGB());
					GlStateManager.enableLighting();
					GlStateManager.disableAlpha();
				}
				GlStateManager.popMatrix();
			}
		}
	}

	public void drawSplitString(FontRenderer renderer, String str, int x, int y, int wrapWidth, int textColor)
	{
		str = this.trimStringNewline(str);
		this.renderSplitStringCentered(renderer, str, x, y, wrapWidth, textColor);
	}

	private void renderSplitStringCentered(FontRenderer renderer, String str, int x, int y, int wrapWidth, int textColor)
	{
		List<String> lines = renderer.listFormattedStringToWidth(str, wrapWidth);
		for (int i = 0; i < lines.size() && i < 5; i++)
		{
			String line = lines.get(i);
			x = (wrapWidth + -renderer.getStringWidth(line)) / 2;
			renderer.drawString(line, x, y, textColor);
			y += renderer.FONT_HEIGHT;
		}
	}

	private String trimStringNewline(String text)
	{
		while (text != null && text.endsWith("\n"))
		{
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}
}

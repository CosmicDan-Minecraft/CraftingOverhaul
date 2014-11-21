package com.cosmicdan.craftingoverhaul.client.gui;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.cosmicdan.cosmiclib.gui.CosmicScrollView;
import com.cosmicdan.craftingoverhaul.Data.CraftingType;
import com.cosmicdan.craftingoverhaul.Recipe;
import com.cosmicdan.craftingoverhaul.RecipeHandler;

import cpw.mods.fml.client.GuiModOptionList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;

public class CraftingGui extends GuiScreen {
    static final ResourceLocation craftingBackground = new ResourceLocation("craftingoverhaul:textures/gui/craftingGui.png");
    
    private final EntityPlayer player;
    private final CraftingType craftingType;
    private final CosmicScrollView scrollView;
    
    private static final float uvFactor = 0.00390625F; // used by drawTexturedQuadWithUv
    private static final int TEXTURE_SCALE = 4; // texture is 8x native, i.e. 2048x2048
    private static final int BG_WIDTH = 768 / TEXTURE_SCALE;
    private static final int BG_HEIGHT = 768 / TEXTURE_SCALE;

    public CraftingGui(EntityPlayer player, CraftingType craftingType) {
        this.player = player;
        this.craftingType = craftingType;
        scrollView = new CosmicScrollView(this);
    }
    
    @Override
    public void initGui() {
        //craftingList.registerScrollButtons(this.buttonList, 7, 8);

    }
    
    @Override
    public void onGuiClosed() {
        //GL11.glDisable(GL11.GL_BLEND);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartials) {
        final int drawX = (width - BG_WIDTH) / 2;
        final int drawY = (height - BG_HEIGHT) / 2;
        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //GL11.glEnable(GL11.GL_BLEND);
        mc.renderEngine.bindTexture(craftingBackground);
        drawTexturedQuadWithUv(drawX, drawY, 0, 0, BG_WIDTH, BG_HEIGHT);
        scrollView.init(drawX + 9, drawY + 18, 146, 154, 16, fontRendererObj);
        for (Recipe recipe : RecipeHandler.recipes) {
            scrollView.addTextOnlyRow(recipe.recipeLabel, 0xFFFFFF);
        }
        scrollView.done();
        int hoverIndex = scrollView.getHoveredRow(mouseX, mouseY);
        if (hoverIndex >= 0) {
            //System.out.println(RecipeHandler.recipes.get(hoverIndex).recipeLabel);
            fontRendererObj.drawString(RecipeHandler.recipes.get(hoverIndex).recipeLabel, 0, 0, 0xFFFFFF);
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
        
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void keyTyped(char c, int key) {
        switch (key) {
            case Keyboard.KEY_ESCAPE:
            case Keyboard.KEY_E:
                close();
                break;
        }
    }
    
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        scrollView.doScrollEvent(Mouse.getEventDWheel());
    }
    
    public final void onMouseWheel(int dWheel) {
        if (dWheel != 0) {
            System.out.println(dWheel);
        }
    }
    
    private void close() {
        //GL11.glDisable(GL11.GL_BLEND);
        mc.displayGuiScreen(null);
    }
    
    private void drawTexturedQuadWithUv(int x, int y, int u, int v, int width, int height){
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0, (double)((float)(u) * uvFactor), (double)((float)(v + height) * uvFactor));
        tessellator.addVertexWithUV(x + width, y + height, 0, (double)((float)(u + width) * uvFactor), (double)((float)(v + height) * uvFactor));
        tessellator.addVertexWithUV(x + width, y, 0, (double)((float)(u + width) * uvFactor), (double)((float)(v) * uvFactor));
        tessellator.addVertexWithUV(x, y, 0, (double)((float)(u) * uvFactor), (double)((float)(v) * uvFactor));
        tessellator.draw();
    }
    
    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }
}

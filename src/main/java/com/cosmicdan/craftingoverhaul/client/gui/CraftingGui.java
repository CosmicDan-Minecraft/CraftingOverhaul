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
    static final ResourceLocation loadingSprite = new ResourceLocation("craftingoverhaul:textures/gui/loadingSprite.png");
    
    private final EntityPlayer player;
    private final CraftingType craftingType;
    private final CosmicScrollView scrollView;
    
    private static final float uvFactor = 0.00390625F; // used by drawTexturedQuadWithUv 
    private static final int TEXTURE_SCALE = 4; // bg texture is 4x native, i.e. 1024x1024
    // total size of the texture
    private static final int BG_WIDTH = 768 / TEXTURE_SCALE;
    private static final int BG_HEIGHT = 768 / TEXTURE_SCALE;
    // position of the scrolling content area
    private static final int CONTENT_X = 36 / TEXTURE_SCALE;
    private static final int CONTENT_Y = 72 / TEXTURE_SCALE;
    // size of the scrolling content area
    private static final int CONTENT_WIDTH = 584 / TEXTURE_SCALE;
    private static final int CONTENT_HEIGHT = 616 / TEXTURE_SCALE;
    // frame width / height for loader sprite (remember to manually scale this according to native 256x256)
    private static final int LOADER_WIDTH = 128 / TEXTURE_SCALE;
    private static final int LOADER_HEIGHT = 120 / TEXTURE_SCALE;
    
    // total frames for loader sprite (0-based)
    private static final int LOADER_FRAMES = 5;
    // frame delay for loader sprite (0-based)
    private static final int LOADER_FRAMEDELAY = 2;
    
    // initial frame for loader sprite
    private int loadingSpriteFrame = 0;
    // initial frame delay count for loader sprite
    private int loadingSpriteDelay = 0;
    

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
        if (!RecipeHandler.recipesLoaded) {
            //fontRendererObj.drawString("Loading", drawX + 9, drawY + 18, 0xFFFFFF);
            doLoading(drawX, drawY);
            return;
        }
        scrollView.init(drawX + CONTENT_X, drawY + CONTENT_Y, CONTENT_WIDTH, CONTENT_HEIGHT, 16, fontRendererObj);
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
    
    private void doLoading(int drawX, int drawY) {
        mc.renderEngine.bindTexture(loadingSprite);
        drawTexturedQuadWithUv(drawX + CONTENT_X + (CONTENT_WIDTH / 2) - (LOADER_WIDTH / 2), drawY + CONTENT_Y + (CONTENT_HEIGHT / 2) - (LOADER_HEIGHT / 2), 0 + (loadingSpriteFrame * LOADER_WIDTH), 0, LOADER_WIDTH, LOADER_HEIGHT);
        loadingSpriteDelay++;
        if (loadingSpriteDelay == LOADER_FRAMEDELAY) {
            loadingSpriteDelay = 0;
            loadingSpriteFrame++;
            if (loadingSpriteFrame > LOADER_FRAMES) {
                loadingSpriteFrame = 0;
            }
        }
    }
}

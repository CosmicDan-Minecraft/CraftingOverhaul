package com.cosmicdan.craftingoverhaul.client.gui;

import org.lwjgl.input.Keyboard;

import com.cosmicdan.cosmiclib.gui.GuiScreenScrolling;
import com.cosmicdan.craftingoverhaul.Data.CraftingType;
import com.cosmicdan.craftingoverhaul.Recipe;
import com.cosmicdan.craftingoverhaul.RecipeHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class CraftingGui extends GuiScreenScrolling {
    static final ResourceLocation craftingBackground = new ResourceLocation("craftingoverhaul:textures/gui/craftingGui.png");
    static final ResourceLocation loadingSprite = new ResourceLocation("craftingoverhaul:textures/gui/loadingSprite.png");
    
    private final boolean debugmode;

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
    
    @Override
    protected int[] scrollviewPadding() {
        int[] padding = new int[2];
        padding[0] = 4;
        padding[1] = 3;
        return padding;
    }


    @Override
    protected int[] scrollviewAttr() {
        int[] attr = new int[5];
        attr[0] = (width - BG_WIDTH) / 2 + CONTENT_X;
        attr[1] = (height - BG_HEIGHT) / 2 + CONTENT_Y;
        attr[2] = CONTENT_WIDTH;
        attr[3] = CONTENT_HEIGHT;
        attr[4] = 18;
        return attr;
    }
    

    public CraftingGui(EntityPlayer player, CraftingType craftingType) {
        this.debugmode = true; // TODO: Base this on a config option
    }
    
    
    @Override
    public void onGuiClosed() {
    }
    
    @Override
    protected void onCreate() {
        // nothing to do here
    }
    
    @Override
    public void onDraw(int mouseX, int mouseY, float renderPartials, int[] attr) {
        
        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //GL11.glEnable(GL11.GL_BLEND);
        
        mc.renderEngine.bindTexture(craftingBackground);
        drawTexturedQuadWithUv(attr[0] - CONTENT_X, attr[1] - CONTENT_Y, 0, 0, BG_WIDTH, BG_HEIGHT);
        if (!RecipeHandler.recipesLoaded) {
            doLoading(attr[0] - CONTENT_X, attr[1] - CONTENT_Y); //TODO: Move this to a new class perhaps?
            return;
        }
    }
    
    @Override
    protected void onAddRows() {
        for (Recipe recipe : RecipeHandler.recipes) {
            addItemIconAndTextRow(recipe.recipeOutput, recipe.recipeLabel, 0xFFFFFF);
        }
    }
    
    @Override
    protected void onRowHover(int index) {
        if (debugmode) {
            fontRendererObj.drawString(RecipeHandler.recipes.get(index).recipeLabel, 0, 0, 0xFFFFFF);
            fontRendererObj.drawString(RecipeHandler.recipes.get(index).recipeClass.getSimpleName(), 0, 10, 0xFFFFFF);
            Class<?> recipeClass = RecipeHandler.recipes.get(index).recipeClass.getSuperclass();
            int offset = 20;
            while (recipeClass != null) {
                fontRendererObj.drawString(recipeClass.getSimpleName(), 0, offset, 0xFFFFFF);
                offset += 10;
                recipeClass = recipeClass.getSuperclass();
            }
            String recipeCategory = "NO CATEGORY";
            //if (RecipeHandler.recipes.get(hoverIndex).recipeCategory != null)
            //    recipeCategory = RecipeHandler.recipes.get(hoverIndex).recipeCategory.toString();
            fontRendererObj.drawString(recipeCategory, 0, offset + 10, 0xFFFFFF);
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
    
    private void close() {
        //GL11.glDisable(GL11.GL_BLEND);
        mc.displayGuiScreen(null);
    }
    
    private void drawTexturedQuadWithUv(int x, int y, int u, int v, int width, int height){
        Tessellator t = Tessellator.getInstance();
        WorldRenderer renderer = t.getWorldRenderer();
        renderer.startDrawingQuads();
        renderer.addVertexWithUV(x, y + height, 0, (double)((float)(u) * uvFactor), (double)((float)(v + height) * uvFactor));
        renderer.addVertexWithUV(x + width, y + height, 0, (double)((float)(u + width) * uvFactor), (double)((float)(v + height) * uvFactor));
        renderer.addVertexWithUV(x + width, y, 0, (double)((float)(u + width) * uvFactor), (double)((float)(v) * uvFactor));
        renderer.addVertexWithUV(x, y, 0, (double)((float)(u) * uvFactor), (double)((float)(v) * uvFactor));
        t.draw();
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

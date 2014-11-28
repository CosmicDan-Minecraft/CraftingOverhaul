package com.cosmicdan.cosmiclib.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

public class CosmicScrollView {
    private final static int ROW_X_PADDING = 4; // left padding for whole row
    private final static int ROW_Y_PADDING_TEXT = 3; // top and bottom padding for text per row
    private final List<RowEntry> rowEntries;

    // instanced (set once on creation)
    private final GuiScreen parent;
    private final TextureManager renderEngine;
    private final RenderItem itemRender;
    
    // initialized (set once per draw)
    private int x;
    private int y;
    private int width;
    private int height;
    private int rowHeight;
    private FontRenderer fontRendererObj;
    //private Tessellator tessellator;
    private int startX; // each row padding start
    private Minecraft mc;
    
    // dynamic (set many per draw)
    //private int startY;
    private int rowY = 0; // current Y-offset of row to draw on
    private int scrollY = 0;
    private int contentYsize = 0;
    
    /**
     * Create a new ScrollView. Should be called from your GuiScreen constructor and assigned to a final class field.
     */
    public CosmicScrollView(GuiScreen parent) {
        this.parent = parent;
        this.renderEngine = Minecraft.getMinecraft().renderEngine;
        this.itemRender = new RenderItem();
        rowEntries = new ArrayList<RowEntry>();
    }
    
    /**
     * Initialize the ScrollView data for drawing. Should be called as the very last GUI element in onDraw. 
     * 
     * Don't forget to call done() after all rows are added!
     */
    public void init(int contentX, int contentY, int contentWidth, int contentHeight, int rowHeight, FontRenderer fontRendererObj) {
        this.x = contentX;
        this.y = contentY;
        this.width = contentWidth;
        this.height = contentHeight;
        this.rowHeight = rowHeight;
        this.fontRendererObj = fontRendererObj;
        //this.tessellator = Tessellator.instance;
        this.startX = x * 2 + ROW_X_PADDING;
        this.mc = parent.mc;
        this.contentYsize = 0;
        // scissor only what we want to draw
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        final int scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor(); // big thanks to diesieben07
        GL11.glScissor(0, mc.displayHeight - (y + height) * scale, (width + x) * scale, height * scale);
        // scale the row content to half-size
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 1.0D);
        rowEntries.clear();
    }

    public void done() {
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        // also reset the runtime vars
        rowY = 0;
    }
    
    public void addTextOnlyRow(String text, int color) {
        fontRendererObj.drawString(text, startX, y * 2 + rowY + scrollY + ROW_Y_PADDING_TEXT, color); // multiply by 2 because we're scaled by half
        incRow();
    }
    
    /**
     * Draw an item + text row
     */
    public void addItemIconAndTextRow(ItemStack item, String text, int color) {
        itemRender.renderItemIntoGUI(null, renderEngine, item, startX, y * 2 + rowY + scrollY);
        GL11.glDisable(GL11.GL_LIGHTING);
        fontRendererObj.drawString(text, startX + 20, y * 2 + rowY + scrollY + ROW_Y_PADDING_TEXT, color); // multiply by 2 because we're scaled by half
        incRow();
    }
    
    private void incRow() {
        rowY += rowHeight;
        contentYsize += rowHeight;
        rowEntries.add(new RowEntry(x, y, x + width, y + (contentYsize + scrollY) / 2));
    }
    
    public void doScrollEvent(int delta) {
        if (delta > 0) { // scroll up, increase vertical padding
            if (scrollY < 0)
                scrollY += 20;
        } else if (delta < 0) { // scroll down, decrease vertical padding
            if (scrollY > (height*2)-contentYsize)
                scrollY -= 20;
        }
    }
    
    public int getHoveredRow(int mouseX, int mouseY) {
        //fontRendererObj.drawString("mouseX ~~~ " + mouseX + " : " + rowEntries.get(1).minX + "-" + rowEntries.get(1).maxX, 0, 10, 0xFFFFFF);
        //fontRendererObj.drawString("mouseY ~~~ " + mouseY + " : " + rowEntries.get(1).minY + "-" + rowEntries.get(1).maxY, 0, 20, 0xFFFFFF);
        if ((mouseX < x) || (mouseX > x + width))
            return -1;
        if ((mouseY < y) || (mouseY > y + height))
            return -1;
        // mouse is on the scroll content area, search for hover row
        RowEntry rowEntry;
        for (int i = 0; i < rowEntries.size(); i++) {
            rowEntry = rowEntries.get(i);
            if ((mouseX > rowEntry.minX) && (mouseX < rowEntry.maxX)) {
                if ((mouseY > rowEntry.minY) && (mouseY < rowEntry.maxY)) {
                    return i;
                }
            }
        }
        //System.err.println("getHoveredRow out of bounds");
        return -1;
    }
    
    public class RowEntry {
        public int minX;
        public int minY;
        public int maxX;
        public int maxY;
        
        public RowEntry(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }
    }
}

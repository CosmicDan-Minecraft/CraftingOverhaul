package com.cosmicdan.craftingoverhaul.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.cosmicdan.craftingoverhaul.CommonProxy;
import com.cosmicdan.craftingoverhaul.Data.CraftingType;
import com.cosmicdan.craftingoverhaul.RecipeHandler;
import com.cosmicdan.craftingoverhaul.client.gui.CraftingGui;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    
    public void buildRecipeData(EntityPlayer player) {
        RecipeHandler.init(player);
    }
    
    public void openCraftingGui(EntityPlayer player, CraftingType craftingType) {
        // client-only hook command for when user presses right-click on their Journal (called from the Journal item class)
        // remember - getMinecraft() only exists in the client!
        Minecraft.getMinecraft().displayGuiScreen(new CraftingGui(player, craftingType));
    }
    
    @Override
    public EntityPlayer getPlayerFromMessageContext(MessageContext ctx) {
        switch(ctx.side) {
            case CLIENT:
                EntityPlayer entityClientPlayerMP = Minecraft.getMinecraft().thePlayer;
                return entityClientPlayerMP;
            case SERVER:
                EntityPlayer entityPlayerMP = ctx.getServerHandler().playerEntity;
                return entityPlayerMP;
            default:
                assert false : "Invalid side in TestMsgHandler: " + ctx.side;
        }
        return null;
    }
}
package com.cosmicdan.craftingoverhaul.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.cosmicdan.craftingoverhaul.CommonProxy;

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
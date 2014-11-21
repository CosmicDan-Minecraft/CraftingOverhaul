package com.cosmicdan.craftingoverhaul.eventhandlers;

import com.cosmicdan.craftingoverhaul.Main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityEvents {
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if(event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
            String msg = StatCollector.translateToLocal("text.loadingrecipes");
            ((EntityPlayer)event.entity).addChatMessage(new ChatComponentText(msg));
            Main.proxy.buildRecipeData((EntityPlayer)event.entity);
        }
    }
}

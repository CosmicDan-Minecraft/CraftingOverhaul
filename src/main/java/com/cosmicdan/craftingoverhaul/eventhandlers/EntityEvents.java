package com.cosmicdan.craftingoverhaul.eventhandlers;

import com.cosmicdan.craftingoverhaul.Main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityEvents {
    @SubscribeEvent
    public void onEntityJoinWorld(final EntityConstructing event) {
        if(event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
            new Thread(new Runnable() {
                public void run() {
                    Main.proxy.buildRecipeData((EntityPlayer)event.entity);
                }
            }).start();
        }
    }
}

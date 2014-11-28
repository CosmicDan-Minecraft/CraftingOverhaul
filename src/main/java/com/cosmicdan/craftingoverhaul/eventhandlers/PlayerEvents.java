package com.cosmicdan.craftingoverhaul.eventhandlers;

import com.cosmicdan.craftingoverhaul.Data.CraftingType;
import com.cosmicdan.craftingoverhaul.Main;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

// Player events
public class PlayerEvents {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            Block blockUsed = event.world.getBlock(event.x, event.y, event.z);
            // TODO: check if "override crafting table" option is enabled
            if (blockUsed instanceof net.minecraft.block.BlockWorkbench) {
                Main.proxy.openCraftingGui(event.entityPlayer, CraftingType.VANILLA_WORKBENCH);
                event.setCanceled(true);
            }
        }
    }
}

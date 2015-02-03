package com.cosmicdan.craftingoverhaul.eventhandlers;

import com.cosmicdan.craftingoverhaul.Data.CraftingType;
import com.cosmicdan.craftingoverhaul.Main;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// Player events
public class PlayerEvents {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            Block blockUsed = event.world.getBlockState(new BlockPos(event.pos.getX(), event.pos.getY(), event.pos.getZ())).getBlock();
            // TODO: check if "override crafting table" option is enabled
            if (blockUsed instanceof net.minecraft.block.BlockWorkbench) {
                Main.proxy.openCraftingGui(event.entityPlayer, CraftingType.VANILLA_WORKBENCH);
                event.setCanceled(true);
            }
        }
    }
}

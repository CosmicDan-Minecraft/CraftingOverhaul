package com.cosmicdan.craftingoverhaul;

import java.util.ArrayList;
import java.util.List;

import com.cosmicdan.craftingoverhaul.RecipeCategories.Category;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class Recipe {
    public String recipeName;
    public String recipeLabel;
    public List<Object[]> recipeInput = new ArrayList<Object[]>(); // recipes can have multiple inputs
    public ItemStack recipeOutput;
    public int recipeOutputId;
    public List<RecipeHandler.RecipeSize> recipeSize = new ArrayList<RecipeHandler.RecipeSize>(); // recipes can have multiple inputs
    public List<RecipeHandler.RecipeStyles> recipeStyle = new ArrayList<RecipeHandler.RecipeStyles>(); // recipes can have multiple inputs
    public Class<?> recipeClass; // this may only be useful for debugging, not certain at this point
    
    public List<Category> recipeCategories = new ArrayList<Category>();
    
    public Recipe(String recipeName, 
                  String recipeLabel, 
                  Object[] recipeInput, 
                  ItemStack recipeOutput,
                  int recipeOutputId,
                  RecipeHandler.RecipeSize recipeSize, 
                  RecipeHandler.RecipeStyles recipeStyle,
                  Class<?> recipeClass) {
        this.recipeName = recipeName;
        this.recipeLabel = recipeLabel;
        this.recipeInput.add(recipeInput); // remember to check if it's an instance of ItemStack before reading this (some Recipes use null instead of "")
        this.recipeOutput = recipeOutput;
        this.recipeOutputId = recipeOutputId;
        this.recipeSize.add(recipeSize);
        this.recipeStyle.add(recipeStyle);
        this.recipeClass = recipeClass;
        setRecipeCategory(recipeOutput.getItem());
    }
    


    private void setRecipeCategory(Object recipeOutputItem) {
        RecipeCategories category = new RecipeCategories();
        CreativeTabs creativeTab = ((Item) recipeOutputItem).getCreativeTab();
        if (creativeTab.equals(CreativeTabs.tabBlock))
            category.add(Category.BUILDING_BLOCKS);
        if (creativeTab.equals(CreativeTabs.tabDecorations))
            category.add(Category.DECORATIONS);
        if (creativeTab.equals(CreativeTabs.tabRedstone))
            category.add(Category.REDSTONE);
        if (creativeTab.equals(CreativeTabs.tabTransport))
            category.add(Category.TRANSPORTATION);
        if (creativeTab.equals(CreativeTabs.tabMisc))
            category.add(Category.MISC);
        if (creativeTab.equals(CreativeTabs.tabFood))
            category.add(Category.FOOD);
        if (creativeTab.equals(CreativeTabs.tabTools))
            category.add(Category.TOOLS);
        if (creativeTab.equals(CreativeTabs.tabCombat))
            category.add(Category.COMBAT);
        if (creativeTab.equals(CreativeTabs.tabBrewing))
            category.add(Category.BREWING);
        if (creativeTab.equals(CreativeTabs.tabMaterials))
            category.add(Category.MATERIALS);
        
        /*
        if (isInstanceOf(recipeOutputItem, "net.minecraft.item.ItemArmor"))
            return RecipeHandler.RecipeCategories.ARMOR;
        if (isInstanceOf(recipeOutputItem, "net.minecraftforge.common.IPlantable"))
            return RecipeHandler.RecipeCategories.CROPS;
        if (isInstanceOf(recipeOutputItem, "net.minecraft.item.ItemFood"))
            return RecipeHandler.RecipeCategories.FOOD;
        if (isInstanceOf(recipeOutputItem, "net.minecraft.item.ItemMapBase"))
            return RecipeHandler.RecipeCategories.MAPPING;
        if (isInstanceOf(recipeOutputItem, "net.minecraft.item.ItemTool", 
                                           "net.minecraft.item.ItemShears", 
                                           "net.minecraft.item.ItemHoe", 
                                           "net.minecraft.item.ItemFlintAndSteel", 
                                           "net.minecraft.item.ItemFishingRod"
                                           ))
            return RecipeHandler.RecipeCategories.TOOLS;
        if (isInstanceOf(recipeOutputItem, "net.minecraft.item.ItemSword", 
                                           "net.minecraft.item.ItemBow"
                                           ))
            return RecipeHandler.RecipeCategories.WEAPONS;
        if (isInstanceOf(recipeOutputItem, "net.minecraft.item.ItemBlock")) // we check for blocks last because other types may extend it
            return RecipeHandler.RecipeCategories.BLOCKS;
        return RecipeHandler.RecipeCategories.OTHER; // default
        */
    }
    
    private static boolean isInstanceOf(Object item, String... classPaths) {
        Class<?> matchedClass;
        for (String classPath : classPaths) {
            try {
                matchedClass = Class.forName(getClassName(classPath));
                if (matchedClass.isInstance(item)) return true;
            } catch (Exception e) {
                // no item class, use generic category
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    
    private static String getClassName(String cls) {
        if (FMLDeobfuscatingRemapper.INSTANCE.isRemappedClass(cls))
            return FMLDeobfuscatingRemapper.INSTANCE.unmap(cls.replace('.', '/'));
        return cls; // default
    }
}

package com.cosmicdan.craftingoverhaul;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class Recipe {
    public String recipeName;
    public String recipeLabel;
    public List<Object> recipeInput = new ArrayList<Object>();
    public ItemStack recipeOutput;
    public RecipeHandler.RecipeSize recipeType;
    public RecipeHandler.RecipeStyles recipeStyle;
    public Class<?> recipeClass; // this may only be useful for debugging, not certain at this point
    public RecipeHandler.RecipeCategories recipeCategory;
    
    
    public Recipe(String recipeName, 
                  String recipeLabel, 
                  List<Object> recipeInput, 
                  ItemStack recipeOutput, 
                  RecipeHandler.RecipeSize recipeType, 
                  RecipeHandler.RecipeStyles recipeStyle,
                  Class<?> recipeClass) {
        this.recipeName = recipeName;
        this.recipeLabel = recipeLabel;
        this.recipeInput = recipeInput; // remember to check if it's an instance of ItemStack before reading this (some Recipes use null instead of "")
        this.recipeOutput = recipeOutput; 
        this.recipeType = recipeType;
        this.recipeStyle = recipeStyle;
        this.recipeClass = recipeClass;
        this.recipeCategory = setRecipeCategory(recipeOutput.getItem());
    }
    


    private RecipeHandler.RecipeCategories setRecipeCategory(Object recipeOutputItem) {
        // TODO: Export the class <> category mappings to a plain-text file
        // TODO: Convert the RecipeCategories enum to a new object, allowing the categories 
        //       to be exported too. See GotoLink's suggestion here-
        //       http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2289243-dynamically-refer-to-a-class-reflection-when?comment=3
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
        if (isInstanceOf(recipeOutputItem, "net.minecraft.item.ItemBlock")) 
            // we check for blocks last because other types may extend it
            return RecipeHandler.RecipeCategories.BLOCKS;
        return RecipeHandler.RecipeCategories.OTHER; // default
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

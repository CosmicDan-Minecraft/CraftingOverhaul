package com.cosmicdan.craftingoverhaul;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.*;

public class Recipe {
    public String recipeName;
    public String recipeLabel;
    public List<Object> recipeInput = new ArrayList<Object>();
    public ItemStack recipeOutput;
    public RecipeHandler.RecipeSize recipeType;
    public RecipeHandler.RecipeStyles recipeStyle;
    public Class recipeClass;
    public RecipeHandler.RecipeCategories recipeCategory;
    
    
    public Recipe(String recipeName, 
                  String recipeLabel, 
                  List<Object> recipeInput, 
                  ItemStack recipeOutput, 
                  RecipeHandler.RecipeSize recipeType, 
                  RecipeHandler.RecipeStyles recipeStyle,
                  Class recipeClass) {
        this.recipeName = recipeName;
        this.recipeLabel = recipeLabel;
        this.recipeInput = recipeInput; // remember to check if it's an instance of ItemStack before reading this
        this.recipeOutput = recipeOutput; 
        this.recipeType = recipeType;
        this.recipeStyle = recipeStyle;
        this.recipeClass = recipeClass; // this may only be useful for debugging, not certain at this point
        this.recipeCategory = setRecipeCategory(recipeOutput.getItem());
    }
    
    private RecipeHandler.RecipeCategories setRecipeCategory(Object recipeOutputItem) {
        // I don't like this code *at all*. Surely there's a more readable way to do this.
        // TODO: Implement some "intelligent" category detection based on heuristic reflection.
        if (recipeOutputItem instanceof ItemArmor)
            return RecipeHandler.RecipeCategories.ARMOR;
        if (recipeOutputItem instanceof ItemBlock)
            return RecipeHandler.RecipeCategories.BLOCKS;
        if (recipeOutputItem instanceof net.minecraftforge.common.IPlantable)
            return RecipeHandler.RecipeCategories.CROPS;
        if (recipeOutputItem instanceof ItemFood)
            return RecipeHandler.RecipeCategories.FOOD;
        if (recipeOutputItem instanceof ItemMapBase)
            return RecipeHandler.RecipeCategories.MAPPING;
        if ((recipeOutputItem instanceof ItemTool) ||
                (recipeOutputItem instanceof ItemShears) ||
                (recipeOutputItem instanceof ItemHoe) ||
                (recipeOutputItem instanceof ItemFlintAndSteel) ||
                (recipeOutputItem instanceof ItemFishingRod)
                )
            return RecipeHandler.RecipeCategories.TOOLS;
        if ((recipeOutputItem instanceof ItemSword) ||
                (recipeOutputItem instanceof ItemBow)
                )
            return RecipeHandler.RecipeCategories.WEAPONS;
        return RecipeHandler.RecipeCategories.OTHER; // default
    }
}

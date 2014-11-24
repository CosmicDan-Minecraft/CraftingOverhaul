package com.cosmicdan.craftingoverhaul;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class Recipe {
    public String recipeName;
    public String recipeLabel;
    public List<Object> recipeInput = new ArrayList<Object>();
    public ItemStack recipeOutput;
    public RecipeHandler.RecipeSize recipeType;
    public RecipeHandler.RecipeStyles recipeStyle;
    public Class recipeClass;
    
    
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
        this.recipeClass = recipeClass;
    }

}

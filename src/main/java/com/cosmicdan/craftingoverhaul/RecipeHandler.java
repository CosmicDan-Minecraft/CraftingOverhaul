package com.cosmicdan.craftingoverhaul;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import scala.actors.threadpool.Arrays;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCloning;
import net.minecraft.item.crafting.RecipeFireworks;
import net.minecraft.item.crafting.RecipesArmorDyes;
import net.minecraft.item.crafting.RecipesMapCloning;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class RecipeHandler {
    public static enum RecipeSize {
        SMALL, LARGE // basic == 2x2 crafting
    }
    
    public static enum RecipeStyles {
        LAZY, STRICT // lazy == shapeless
    }
    
    public static enum SortOrder {
        ALPHABYLABEL,
    }
    
    public static boolean recipesLoaded = false;
    
    public static List<Recipe> recipes = new ArrayList<Recipe>();
    
    public static void init() {
        List recipeList = CraftingManager.getInstance().getRecipeList();
        for (Object recipe : recipeList) {
            RecipeSize recipeType;
            if (recipe instanceof ShapelessRecipes) { 
                final ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
                recipeType = RecipeSize.SMALL;
                if (newRecipe.getRecipeSize() > 4)
                    recipeType = RecipeSize.LARGE;
                recipes.add(new Recipe(
                        newRecipe.getRecipeOutput().getUnlocalizedName(),
                        newRecipe.getRecipeOutput().getDisplayName(),
                        newRecipe.recipeItems,
                        newRecipe.getRecipeOutput(),
                        recipeType,
                        RecipeStyles.LAZY
                    )
                );
            } else 
            if (recipe instanceof ShapedRecipes) {
                final ShapedRecipes newRecipe = (ShapedRecipes) recipe;
                recipeType = RecipeSize.SMALL;
                if ((newRecipe.recipeWidth > 2) || (newRecipe.recipeHeight > 2))
                    recipeType = RecipeSize.LARGE;
                recipes.add(
                    new Recipe(
                            newRecipe.getRecipeOutput().getUnlocalizedName(),
                            newRecipe.getRecipeOutput().getDisplayName(),
                        Arrays.asList(newRecipe.recipeItems),
                        newRecipe.getRecipeOutput(),
                        recipeType,
                        RecipeStyles.STRICT
                    )
                );
            } else
            if (recipe instanceof ShapelessOreRecipe) {
                final ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
                recipeType = RecipeSize.SMALL;
                if (newRecipe.getRecipeSize() > 4)
                    recipeType = RecipeSize.LARGE;
                recipes.add(new Recipe(
                        newRecipe.getRecipeOutput().getUnlocalizedName(),
                        newRecipe.getRecipeOutput().getDisplayName(),
                        newRecipe.getInput(),
                        newRecipe.getRecipeOutput(),
                        recipeType,
                        RecipeStyles.LAZY
                    )
                );
                
            } else
            if (recipe instanceof ShapedOreRecipe) {
                final ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
                recipeType = RecipeSize.SMALL;
                if (newRecipe.getRecipeSize() > 4) // not sure if this is accurate
                    recipeType = RecipeSize.LARGE;
                recipes.add(new Recipe(
                        newRecipe.getRecipeOutput().getUnlocalizedName(),
                        newRecipe.getRecipeOutput().getDisplayName(),
                        Arrays.asList(newRecipe.getInput()),
                        newRecipe.getRecipeOutput(),
                        recipeType,
                        RecipeStyles.STRICT
                    )
                );
            } else
            if (recipe instanceof RecipesArmorDyes) {
                // I don't know how to handle this one
            } else
            if (recipe instanceof RecipeFireworks) {
                // I don't know how to handle this one
            } else
            if (recipe instanceof RecipeBookCloning) {
                // I don't know how to handle this one
            } else
            if (recipe instanceof RecipesMapCloning) {
                // I don't know how to handle this one
            } else {
                // check if debug enabled
                //System.out.println("Unhandled recipe from class " + recipe.getClass().toString());
            }
        }
    }
    
    public void sortRecipes(SortOrder order) {
        if (order == SortOrder.ALPHABYLABEL) {
            Collections.sort(recipes, new Comparator<Object>() {

                @Override
                public int compare(Object recipe1, Object recipe2) {
                    // TODO Auto-generated method stub
                    return 0;
                }
            });
        }
    }
}

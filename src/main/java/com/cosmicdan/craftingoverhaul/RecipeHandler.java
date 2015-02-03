package com.cosmicdan.craftingoverhaul;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.cosmicdan.craftingoverhaul.RecipeSortComparator.RecipeOrder;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCloning;
import net.minecraft.item.crafting.RecipeFireworks;
import net.minecraft.item.crafting.RecipesArmorDyes;
import net.minecraft.item.crafting.RecipesMapCloning;
import net.minecraft.item.crafting.RecipesMapExtending;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class RecipeHandler {
    public static enum RecipeSize {
        SMALL, LARGE // small == 2x2 grid; large == 3x3 grid
    }
    
    public static enum RecipeStyles {
        LAZY, STRICT // lazy == shapeless
    }
    
    public static boolean recipesLoaded = false;
    
    public static List<Recipe> recipes = new ArrayList<Recipe>();
    
    private static List<String> debugClassHandled = new ArrayList<String>();
    
    private static ItemStack getRecipeOutput(Object recipe) {
        try {
            return (ItemStack) recipe.getClass().getMethod("getRecipeOutput").invoke(recipe);
        } catch (Exception e) {
            return null;
        }
    }
    
    private static Object[] getRecipeInput(Object recipe) {
        if (recipe instanceof ShapedRecipes)
            return ((ShapedRecipes) recipe).recipeItems;
        else if (recipe instanceof ShapelessRecipes)
            return ((ShapelessRecipes) recipe).recipeItems.toArray();
        else if (recipe instanceof ShapedOreRecipe)
            return ((ShapedOreRecipe) recipe).getInput();
        else if (recipe instanceof ShapelessOreRecipe)
            return ((ShapelessOreRecipe) recipe).getInput().toArray();
        else
            return null;
    }
    
    /**
     * Get the RecipeSize for a recipe
     * @param recipe may be any object of these classes: ShapedRecipes;
     * @return Either RecipeSize.LARGE or RecipeSize.SMALL, or null if recipe is an unrecognized type
     */
    private static RecipeSize getRecipeSize(Object recipe) {
        int size;
        try {
            size = (int) recipe.getClass().getDeclaredMethod("getRecipeSize").invoke(recipe);
        } catch (Exception e) {
            return null;
        }
        if (size > 4)
            return RecipeSize.LARGE;
        else
            return RecipeSize.SMALL;
    }
    
    private static RecipeStyles getRecipeStyle(Object recipe) {
        if (recipe instanceof ShapedRecipes)
            return RecipeStyles.STRICT;
        if (recipe instanceof ShapedOreRecipe)
            return RecipeStyles.STRICT;
        if (recipe instanceof ShapelessRecipes)
            return RecipeStyles.LAZY;
        if (recipe instanceof ShapelessOreRecipe)
            return RecipeStyles.LAZY;
        return null;
    }
    
    private static void addUnhandledClassIfNew(Object recipe, String reason) {
        if (!debugClassHandled.contains(recipe.getClass().getSimpleName())) {
            debugClassHandled.add(recipe.getClass().getSimpleName() + " (" + reason + ")");
        }
    }

    public static void init() {
        if (recipesLoaded)
            return;
        List<?> recipeList = CraftingManager.getInstance().getRecipeList();
        
        for (Object recipe : recipeList) {
            String recipeName;
            String recipeLabel;
            //List<Object[]> recipeInput = new ArrayList<Object[]>();
            Object[] recipeInput;
            ItemStack recipeOutput;
            int recipeOutputId;
            RecipeSize recipeSize;
            RecipeStyles recipeStyle;
            Class<?> recipeClass; // this may only be useful for debugging, not certain at this point
            
            if (recipe instanceof IRecipe) {
                if (((IRecipe) recipe).getRecipeOutput() == null) {
                    // All dynamic-output recipes are not yet implemented. This includes:
                    //      RecipesArmorDyes
                    //      RecipeFireworks
                    //      RecipeAddPattern
                    //      RecipeBookCloning
                    //      RecipesMapCloning
                    //      RecipeRepairItem
                    // ...will try to handle these another day (or not at all).
                    continue;
                } else if (recipe instanceof RecipesMapExtending) {
                    // also unhandled
                    continue;
                } else {
                    recipeOutput = getRecipeOutput(recipe);
                    if (recipeOutput == null) {
                        addUnhandledClassIfNew(recipe, "unhandled getRecipeOutput implementation");
                        continue;
                    }
                    recipeOutputId = Item.getIdFromItem(recipeOutput.getItem());
                    // TODO: Search recipes list for duplicate recipeOutputId and add to recipeInput list 
                    recipeName = recipeOutput.getUnlocalizedName();
                    recipeLabel = recipeOutput.getDisplayName();
                    recipeInput = getRecipeInput(recipe);
                    if (recipeInput == null) {
                        addUnhandledClassIfNew(recipe, "unhandled getRecipeInput implementation");
                        continue;
                    }
                    recipeSize = getRecipeSize(recipe);
                    if (recipeSize == null) {
                        addUnhandledClassIfNew(recipe, "unhandled getRecipeSize implementation");
                        continue;
                    }
                    recipeStyle = getRecipeStyle(recipe);
                    if (recipeStyle == null) {
                        addUnhandledClassIfNew(recipe, "unhandled getRecipeStyle implementation");
                        continue;
                    }
                    recipeClass = recipe.getClass();
                    recipes.add(new Recipe(
                            recipeName,
                            recipeLabel,
                            recipeInput,
                            recipeOutput,
                            recipeOutputId,
                            recipeSize,
                            recipeStyle,
                            recipeClass)
                    );
                }
            } else {
                addUnhandledClassIfNew(recipe, "not an instance of IRecipe");
            }
        }
        
        if (debugClassHandled.size() > 0)
            System.out.println("Unhandled Recipe classes found: " + debugClassHandled.toString());
        
        // obviously this is just a test!
        /*
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        
        sortRecipes(RecipeOrder.LABEL);
        recipesLoaded = true;
    }
    
    public static void sortRecipes(RecipeOrder order) {
        RecipeSortComparator recipeComparator = new RecipeSortComparator();
        recipeComparator.setSortingBy(order);
        Collections.sort(recipes, recipeComparator);
    }
}

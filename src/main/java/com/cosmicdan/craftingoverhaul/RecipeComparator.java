package com.cosmicdan.craftingoverhaul;

import java.util.Comparator;

public class RecipeComparator implements Comparator<Recipe> {
    public static enum RecipeOrder {
        LABEL, 
    }
    
    private RecipeOrder sortBy = RecipeOrder.LABEL; 
    
    @Override
    public int compare(Recipe recipe1, Recipe recipe2) {
        switch (sortBy) {
            case LABEL:
                return recipe1.recipeLabel.compareTo(recipe2.recipeLabel);
        }
        throw new RuntimeException("RecipeComparator: Unhandled sortBy!");
    }
    
    public void setSortingBy(RecipeOrder sortBy) {
        this.sortBy = sortBy;
    }
}

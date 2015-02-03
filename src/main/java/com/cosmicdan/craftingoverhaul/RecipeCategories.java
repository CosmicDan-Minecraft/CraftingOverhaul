package com.cosmicdan.craftingoverhaul;

import java.util.ArrayList;
import java.util.List;

public class RecipeCategories {
    
    private List<Category> categories = new ArrayList<Category>();
    
    public static enum Category {
        // The first 10 correspond to CreativeTabs (Search and Survival Intentory are obviously skipped)
        BUILDING_BLOCKS,
        DECORATIONS,
        REDSTONE,
        TRANSPORTATION,
        MISC,
        FOOD,
        TOOLS,
        COMBAT,
        BREWING,
        MATERIALS,
        // the rest are additionals/overrides
    }
    
    public RecipeCategories() {}
    
    public void add(Category category) {
        categories.add(category);
    }
    
    public Category get(int index) {
        return categories.get(index);
    }
    
    public List<Category> getAll() {
        return categories;
    }
    
    public int size() {
        return categories.size();
    }
}

package edu.rosehulman.turnerrs.gourmate;

/**
 * Created by turnerrs on 4/2/2015.
 */
public class IngredientClass implements RecipeType{

    public String quantity;
    public String unit;
    public String ingredient;

    @Override
    public int getType() {
        return INGREDIENT_TYPE;
    }
}

package edu.rosehulman.turnerrs.gourmate;

/**
 * Created by turnerrs on 4/20/2015.
 */
public interface RecipeType {
    int HEADER_TYPE = 0;
    int INGREDIENT_TYPE = 1;
    int SEPARATOR_TYPE = 2;
    int STEP_TYPE = 3;

    int getType();
}

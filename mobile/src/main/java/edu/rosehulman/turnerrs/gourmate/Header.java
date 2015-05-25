package edu.rosehulman.turnerrs.gourmate;

/**
 * Created by turnerrs on 4/20/2015.
 */
public class Header implements RecipeType {

    public String title;
    public String cookTime;
    public String prepTime;

    @Override
    public int getType() {
        return HEADER_TYPE;
    }
}

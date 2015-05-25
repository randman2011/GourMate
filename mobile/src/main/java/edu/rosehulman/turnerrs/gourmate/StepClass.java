package edu.rosehulman.turnerrs.gourmate;

/**
 * Created by turnerrs on 4/18/2015.
 */
public class StepClass implements RecipeType{
    public int stepNumber;
    public String stepText;

    @Override
    public int getType() {
        return STEP_TYPE;
    }
}

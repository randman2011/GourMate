package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import com.appspot.turnerrs_gourmade.gourmade.model.Recipe;
import com.appspot.turnerrs_gourmade.gourmade.model.Ingredient;
import com.appspot.turnerrs_gourmade.gourmade.model.Step;

/**
 * Created by turnerrs on 4/2/2015.
 */
public class ViewRecipeRowAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<RecipeType> mArrayList;

    public ViewRecipeRowAdapter(Context context, Recipe recipe) {
        this.mContext = context;

        mArrayList = Recipe.parseRecipeIntoArrayList(recipe);
    }

    public ViewRecipeRowAdapter(Context context, ArrayList<RecipeType> list) {
        this.mContext = context;
        this.mArrayList = list;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int type = mArrayList.get(position).getType();
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == RecipeType.INGREDIENT_TYPE) {
            IngredientClass item = (IngredientClass) this.getItem(position);
            IngredientRow view = null;
            if (convertView == null) {
                view = new IngredientRow(mContext);
            } else {
                view = (IngredientRow) convertView;
            }
            view.setQuantity(item.quantity);
            view.setIngredient(item.ingredient);
            view.setUnit(item.unit);
            view.setBackgroundColor(Constants.mMainActivity.getResources().getColor(R.color.background_material_light));
            return view;
        }
        else if (type == RecipeType.STEP_TYPE) {
            StepClass item = (StepClass) this.getItem(position);
            StepRow view = null;
            if (convertView == null) {
                view = new StepRow(mContext);
            } else {
                view = (StepRow) convertView;
            }

            view.setStepNumber("" + item.stepNumber);
            view.setStepText(item.stepText);
            view.setBackgroundColor(Constants.mMainActivity.getResources().getColor(R.color.background_material_light));
            return view;
        }
        else if (type == RecipeType.HEADER_TYPE){
            Header item = (Header) this.getItem(position);
            HeaderRow view = null;
            if (convertView == null) {
                view = new HeaderRow(mContext);
            } else {
                view = (HeaderRow) convertView;
            }
            view.setTitle(item.title);
            view.setCookTime(item.cookTime);
            view.setPrepTime(item.prepTime);

            view.setElevation(Constants.mMainActivity.getResources().getDimension(R.dimen.header_elevation));
            return view;
        }
        else {
            SeparatorRow view = null;
            if (convertView == null) {
                view = new SeparatorRow(mContext);
            } else {
                view = (SeparatorRow) convertView;
            }
            view.setBackgroundColor(Constants.mMainActivity.getResources().getColor(R.color.background_material_light));
            return view;
        }
    }


}

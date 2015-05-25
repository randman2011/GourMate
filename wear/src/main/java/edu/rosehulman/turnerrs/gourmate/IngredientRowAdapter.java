package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.appspot.turnerrs_gourmade.gourmade.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by turnerrs on 4/2/2015.
 */
public class IngredientRowAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Ingredient> ingredients;

    public IngredientRowAdapter(Context context, ArrayList<Ingredient> ingredients) {
        this.mContext = context;
        this.ingredients = ingredients;
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Ingredient getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ingredient item = this.getItem(position);
        IngredientRow view = null;
        if (convertView == null) {
            view = new IngredientRow(mContext);
        } else {
            view = (IngredientRow) convertView;
        }

        view.setQuantity(item.getQuantity());
        view.setUnit(item.getUnits());
        view.setIngredient(item.getIngredient());

        return view;
    }
}

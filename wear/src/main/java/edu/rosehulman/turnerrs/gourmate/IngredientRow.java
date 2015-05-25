package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appspot.turnerrs_gourmade.gourmade.model.Ingredient;

/**
 * Created by turnerrs on 4/2/2015.
 */
public class IngredientRow extends LinearLayout {

    private TextView mQuantity;
    private TextView mUnit;
    private TextView mIngredient;

    public IngredientRow(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.ingredient_row, this);

        mQuantity = (TextView)findViewById(R.id.quantity_text);
        mUnit = (TextView)findViewById(R.id.units_text);
        mIngredient = (TextView)findViewById(R.id.ingredient_text);
    }

    public void setQuantity(String quantity) { mQuantity.setText(quantity); }
    public void setUnit(String unit) { mUnit.setText(unit); }
    public void setIngredient(String ingredient) { mIngredient.setText(ingredient); }

    public void setIngredientRow(Ingredient ingredient) {
        this.mQuantity.setText(ingredient.getQuantity());
        this.mUnit.setText(ingredient.getUnits());
        this.mIngredient.setText(ingredient.getIngredient());
    }
}

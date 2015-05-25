package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by turnerrs on 4/2/2015.
 */
public class RecipeRow extends FrameLayout {

    private ImageView mImage;
    private TextView mRecipeTitle;
    private TextView mPrepText;
    private TextView mCookText;

    public RecipeRow(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.recipe_row, this);
        mImage = (ImageView) findViewById(R.id.recipe_image);
        mRecipeTitle = (TextView) findViewById(R.id.recipe_title);
        mPrepText = (TextView) findViewById(R.id.prep_time_text);
        mCookText = (TextView) findViewById(R.id.cook_time_text);
    }

    public void setImage(Bitmap image) { mImage.setImageBitmap(image); }
    public void setRecipeTitle(String title) { mRecipeTitle.setText(title); }
    public void setPrepText(String prepTime) { mPrepText.setText("Prep: " + prepTime); }
    public void setCookText(String cookTime) { mCookText.setText("Cook: "+ cookTime); }
}

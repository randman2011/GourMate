package edu.rosehulman.turnerrs.gourmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appspot.turnerrs_gourmade.gourmade.Gourmade;
import com.appspot.turnerrs_gourmade.gourmade.model.Ingredient;
import com.appspot.turnerrs_gourmade.gourmade.model.Recipe;
import com.appspot.turnerrs_gourmade.gourmade.model.Step;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NewRecipeFragment extends Fragment implements View.OnClickListener {

    private LinearLayout mIngredientLayout;
    private LinearLayout mStepLayout;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    private ImageView imageView;
    private EditText mPrepTime;
    private EditText mCookTime;
    private EditText mTitle;
    private Bitmap selectedImageBitmap;
    private Gourmade mService;
    public Recipe mRecipe;

    private int currentStepNumber = 1;
    private static int RESULT_LOAD_IMG = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);

        view.findViewById(R.id.recipe_image_button).setOnClickListener(this);
        view.findViewById(R.id.add_ingredient_button).setOnClickListener(this);
        view.findViewById(R.id.add_step_button).setOnClickListener(this);
        view.findViewById(R.id.save_recipe_button).setOnClickListener(this);
        view.findViewById(R.id.cancel_button).setOnClickListener(this);

        mIngredientLayout = (LinearLayout)view.findViewById(R.id.ingredients_list);
        mStepLayout = (LinearLayout)view.findViewById(R.id.steps_list);
        mPrepTime = (EditText)view.findViewById(R.id.prep_time_edit_text);
        mCookTime = (EditText)view.findViewById(R.id.cook_time_edit_text);
        mTitle = (EditText)view.findViewById(R.id.title_edit_text);

        mRecipe = Constants.selectedRecipe;

        if (mRecipe != null) {
            mIngredients = new ArrayList<>(mRecipe.getIngredients());
            for (Ingredient i : mIngredients) {
                IngredientRow r = new IngredientRow(getActivity());
                r.setIngredientRow(i);
                mIngredientLayout.addView(r);
            }
            mSteps = new ArrayList<>(mRecipe.getSteps());
            for (Step s : mSteps) {
                StepRow r = new StepRow(getActivity());
                r.setStepRow(s);
                mStepLayout.addView(r);
                currentStepNumber = Integer.parseInt(s.getStepNumber()) + 1;
            }
            mTitle.setText(mRecipe.getRecipeTitle());
            mPrepTime.setText(mRecipe.getPrepTime());
            mCookTime.setText(mRecipe.getCookTime());
            try {
                byte[] b = Base64.decode(mRecipe.getImage(), Base64.DEFAULT);
                selectedImageBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                imageView.setImageBitmap(selectedImageBitmap);
            } catch (Exception e) {
                Log.e(Constants.LOG_NAME, "Failed decoding: " + e);
            }

        } else {
            mRecipe = new Recipe();
            mIngredients = new ArrayList<>();
            mSteps = new ArrayList<>();
        }

        Gourmade.Builder builder = new Gourmade.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
        mService = builder.build();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recipe_image_button:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

                return;
            case R.id.add_ingredient_button:
                // TODO: Open custom dialog to input ingredient and save to an IngredientRow type

                DialogFragment iFrag = new DialogFragment(){

                    View v = Constants.mMainActivity.getLayoutInflater().inflate(R.layout.dialog_new_ingredient, null);
                    @NonNull
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.add_ingredient);
                        builder.setPositiveButton(R.string.add_ingredient_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Ingredient ingredient = new Ingredient();
                                ingredient.setQuantity(((EditText)v.findViewById(R.id.new_ingredient_quantity)).getText().toString());
                                ingredient.setUnits(((EditText) v.findViewById(R.id.new_ingredient_unit)).getText().toString());
                                ingredient.setIngredient(((EditText) v.findViewById(R.id.new_ingredient_ingredient)).getText().toString());
                                mIngredients.add(ingredient);
                                IngredientRow row = new IngredientRow(getActivity());
                                row.setIngredientRow(ingredient);
                                mIngredientLayout.addView(row);
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, null);
                        builder.setView(v);
                        return builder.create();
                    }
                };
                iFrag.show(getFragmentManager(), "");

                return;
            case R.id.add_step_button:
                // TODO: Open custom dialog to input step and save to an StepRow type

                DialogFragment sFrag = new DialogFragment(){

                    View v = Constants.mMainActivity.getLayoutInflater().inflate(R.layout.dialog_new_step, null);
                    @NonNull
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.add_step);
                        builder.setPositiveButton(R.string.add_step_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Step step = new Step();
                                String number = Integer.toString(currentStepNumber++);
                                step.setStepNumber(number);
                                step.setStepText(((EditText) v.findViewById(R.id.new_step_step)).getText().toString());
                                mSteps.add(step);
                                StepRow row = new StepRow(getActivity());
                                row.setStepRow(step);
                                mStepLayout.addView(row);
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, null);
                        builder.setView(v);
                        return builder.create();
                    }
                };
                sFrag.show(getFragmentManager(), "");

                return;
            case R.id.save_recipe_button:
                Recipe recipe = new Recipe();
                recipe.setRecipeTitle(mTitle.getText().toString());
                recipe.setPrepTime(mPrepTime.getText().toString());
                recipe.setCookTime(mCookTime.getText().toString());
                if (selectedImageBitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
                    byte[] b = stream.toByteArray();
                    String temp = Base64.encodeToString(b, Base64.DEFAULT);
                    recipe.setImage(temp);
                }
                recipe.setIngredients(mIngredients);
                recipe.setSteps(mSteps);

                if (Constants.selectedRecipe != null) {
                    recipe.setEntityKey(Constants.selectedRecipe.getEntityKey());
                }

                new InsertRecipeTask().execute(recipe);

                Constants.mMainActivity.replaceFragment(getString(R.string.menu_recipe_list));
                return;
            case R.id.cancel_button:
                Constants.mMainActivity.replaceFragment(getString(R.string.menu_recipe_list));

                return;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                imageView = (ImageView)getActivity().findViewById(R.id.recipe_image_button);
                selectedImageBitmap = BitmapFactory.decodeFile(imgDecodableString);
                imageView.setImageBitmap(selectedImageBitmap);
                imageView.setColorFilter(getResources().getColor(R.color.transparent));
                imageView.setBackgroundResource(R.color.transparent);
                imageView.requestLayout();
                getActivity().findViewById(R.id.add_image_label).setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_NAME, "Error: " + e);
        }
    }

    public class InsertRecipeTask extends AsyncTask<Recipe, Void, Recipe> {

        @Override
        protected Recipe doInBackground(Recipe... params) {
            Recipe returnedRecipe = null;
            try {
                returnedRecipe = mService.recipe().insert(params[0]).execute();
            } catch (IOException e) {
                Log.e(Constants.LOG_NAME, "Failed inserting " + e);
            }
            return returnedRecipe;
        }

        @Override
        protected void onPostExecute(Recipe recipe) {
            super.onPostExecute(recipe);
            if (recipe == null) {
                Log.d(Constants.LOG_NAME, "Failed inserting. Result is null");
                return;
            }
        }
    }
}

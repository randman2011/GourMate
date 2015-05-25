package edu.rosehulman.turnerrs.gourmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import com.appspot.turnerrs_gourmade.gourmade.Gourmade;
import com.appspot.turnerrs_gourmade.gourmade.model.Ingredient;
import com.appspot.turnerrs_gourmade.gourmade.model.Recipe;
import com.appspot.turnerrs_gourmade.gourmade.model.RecipeCollection;

import com.appspot.turnerrs_gourmade.gourmade.model.Step;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

/**
 * Created by turnerrs on 4/2/2015.
 */
public class RecipeListFragment extends Fragment implements View.OnClickListener {

    private ArrayList<RecipeClass> recipes;
    private RecipeListRowAdapter mAdapter;
    private Gourmade mService;
    private ListView mListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list_fragment, container, false);

        Gourmade.Builder builder = new Gourmade.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
        mService = builder.build();

        updateQuotes();

        (view.findViewById(R.id.fab_new_recipe)).setOnClickListener(this);
        mListView = (ListView)view.findViewById(R.id.recipe_list);

        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(Constants.LOG_NAME, "Recipe selected: " + mAdapter.getItem(position).getRecipeTitle());
                Intent recipeIntent = new Intent(view.getContext(), ViewRecipeActivity.class);
                Recipe recipe = (Recipe)mListView.getAdapter().getItem(position);
                //Bundle bundle = new Bundle();
                //bundle.putParcelable(Constants.RECIPE_KEY, recipe);
                //recipeIntent.putExtra(Constants.RECIPE_KEY, recipe);
                recipeIntent.putExtra(Constants.TITLE_KEY, recipe.getRecipeTitle());
                recipeIntent.putExtra(Constants.PREP_KEY, recipe.getPrepTime());
                recipeIntent.putExtra(Constants.COOK_KEY, recipe.getCookTime());
                //recipeIntent.putExtra(Constants.IMAGE_KEY, recipe.getImage());
                ArrayList<String[]> arrayList = new ArrayList<String[]>();
                for (Ingredient i : recipe.getIngredients()) {
                    arrayList.add(i.toStringArray());
                }
                recipeIntent.putExtra(Constants.INGREDIENTS_KEY, arrayList);
                arrayList = new ArrayList<String[]>();
                for (Step s : recipe.getSteps()) {
                    arrayList.add(s.toStringArray());
                }
                recipeIntent.putExtra(Constants.STEPS_KEY, arrayList);
                Constants.selectedRecipe = recipe;
                Constants.mMainActivity.startActivity(recipeIntent);
            }
        });
        mListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(Constants.LOG_NAME, "Long press recipe: " + mAdapter.getItem(position).getRecipeTitle());
                final Recipe r = (Recipe)mListView.getAdapter().getItem(position);

                DialogFragment df = new DialogFragment() {
                    @NonNull
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        //builder.setTitle();
                        builder.setItems(R.array.recipe_list_options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] options = getResources().getStringArray(R.array.recipe_list_options);
                                if (options[which].equals(getString(R.string.delete))) {
                                    new DeleteRecipeTask().execute(r.getEntityKey());
                                } else if (options[which].equals(getString(R.string.edit))) {
                                    Constants.selectedRecipe = r;
                                    Constants.mMainActivity.replaceFragment(getString(R.string.menu_add_recipe));
                                }
                            }
                        });

                        return builder.create();
                    }
                };
                df.show(getFragmentManager(), "");

                return true;
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_new_recipe) {
            Constants.selectedRecipe = null;
            Constants.mMainActivity.replaceFragment(getString(R.string.menu_add_recipe));
        }
    }

    class QueryForQuotesTask extends AsyncTask<Void, Void, RecipeCollection> {

        @Override
        protected RecipeCollection doInBackground(Void... params) {
            RecipeCollection recipes = null;
            try {
                Gourmade.Recipe.List query = mService.recipe().list();
                query.setOrder("-last_touch_date_time");
                query.setLimit(50L);
                recipes = query.execute();
            } catch (IOException e) {
                Log.e(Constants.LOG_NAME, "Failed loading " + e);
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(RecipeCollection recipeCollection) {
            super.onPostExecute(recipeCollection);
            if (recipeCollection == null) {
                Log.d(Constants.LOG_NAME, "Failed loading. Result is null.");
                return;
            }
            mAdapter = new RecipeListRowAdapter(getActivity(), recipeCollection.getItems());
            mListView.setAdapter(mAdapter);
        }
    }

    class InsertRecipeTask extends AsyncTask<Recipe, Void, Recipe> {

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
            updateQuotes();
        }
    }

    class DeleteRecipeTask extends AsyncTask<String, Void, Recipe> {

        @Override
        protected Recipe doInBackground(String... params) {
            Recipe recipe = null;
            try {
                recipe = mService.recipe().delete(params[0]).execute();
            } catch (IOException e) {
                Log.e(Constants.LOG_NAME, "Failed deleting " + e);
            }
            return recipe;
        }

        @Override
        protected void onPostExecute(Recipe recipe) {
            super.onPostExecute(recipe);
            if (recipe == null) {
                Log.e(Constants.LOG_NAME, "Failed deleting. Result is null.");
                return;
            }
            updateQuotes();
        }
    }



    private void updateQuotes() {
        new QueryForQuotesTask().execute();
    }
}

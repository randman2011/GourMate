package edu.rosehulman.turnerrs.gourmate;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.turnerrs_gourmade.gourmade.model.*;
import com.appspot.turnerrs_gourmade.gourmade.model.Ingredient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientListActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView mTextView;
    private ListView ingredientListView;
    private IngredientRowAdapter mIngredientAdapter;
    private StepRowAdapter mStepAdapter;
    private GoogleApiClient mGoogleApiClient;
    private String recipeTitle;
    private String prepTime;
    private String cookTime;
    private String image;
    private String[] ingredients;
    private String[] steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                ingredientListView = (ListView)findViewById(R.id.ingredient_listview);
                mTextView = (TextView)findViewById(R.id.ingredient_title);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(Constants.WEAR_MAP_PATH) == 0) {
                    DataMap dm = DataMapItem.fromDataItem(item).getDataMap();
                    recipeTitle = dm.getString(Constants.TITLE_KEY);
                    cookTime = dm.getString(Constants.COOK_KEY);
                    prepTime = dm.getString(Constants.PREP_KEY);
                    image = dm.getString(Constants.IMAGE_KEY);
                    ingredients = dm.getStringArray(Constants.INGREDIENTS_KEY);
                    steps = dm.getStringArray(Constants.STEPS_KEY);
                    parseStringsIntoArrayLists();
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {

            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void parseStringsIntoArrayLists() {
        if (recipeTitle == null) {
            Toast.makeText(IngredientListActivity.this, "No data", Toast.LENGTH_LONG).show();
            return;
        }
        Recipe r = new Recipe();

        if (ingredients != null) {
            ArrayList<Ingredient> ing = new ArrayList<>();
            for (int index = 0; index < ingredients.length / 3; index += 3) {
                ing.add(new Ingredient(new String[]{ingredients[index], ingredients[index + 1], ingredients[index + 2]}));
            }
            r.setIngredients(ing);
            mIngredientAdapter = new IngredientRowAdapter(IngredientListActivity.this, ing);
            ingredientListView.setAdapter(mIngredientAdapter);
        }

        if (steps != null) {
            ArrayList<Step> s = new ArrayList<>();
            for (int index = 0; index < ingredients.length / 2; index += 2) {
                s.add(new Step(new String[]{steps[index], steps[index + 1]}));
            }
            r.setSteps(s);
        }

        r.setCookTime(cookTime);
        r.setPrepTime(prepTime);
        r.setRecipeTitle(recipeTitle);
        r.setImage(image);

        Constants.selectedRecipe = r;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(recipeTitle);
            }
        });
    }
}

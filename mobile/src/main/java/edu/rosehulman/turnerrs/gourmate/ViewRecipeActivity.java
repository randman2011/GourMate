package edu.rosehulman.turnerrs.gourmate;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.appspot.turnerrs_gourmade.gourmade.model.Recipe;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

/**
 * Created by turnerrs on 4/2/2015.
 */
public class ViewRecipeActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private StretchyImageView recipeImage;
    private ViewRecipeRowAdapter mAdapter;
    private ListView mListView;
    private Recipe mRecipe;
    private GoogleApiClient apiClient;
    private WearableListener wearableListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe_activity);

        mRecipe = Constants.selectedRecipe;

        mListView = (ListView) findViewById(R.id.recipe_list_view);
        recipeImage = (StretchyImageView) findViewById(R.id.recipe_image);

        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        recipeImage.setMinimumWidth(p.x);
        try {
            byte[] b = Base64.decode(mRecipe.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            recipeImage.setImageBitmap(bitmap);
            recipeImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (recipeImage.getMeasuredHeight() != 0) recipeImage.getViewTreeObserver().removeOnPreDrawListener(this);
                    LinearLayout v = new LinearLayout(ViewRecipeActivity.this);
                    v.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, recipeImage.getMeasuredHeight()));
                    mListView.addHeaderView(v, null, false);
                    return true;
                }
            });
        } catch (Exception e) {
            Log.e(Constants.LOG_NAME, "Failed decoding: " + e);
        }

        ArrayList<RecipeType> r = Recipe.parseRecipeIntoArrayList(mRecipe);

        mAdapter = new ViewRecipeRowAdapter(this, r);
        mListView.setAdapter(mAdapter);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        builder.addApi(Wearable.API);
        apiClient = builder.build();
        apiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        new SendDataTask(this, mRecipe, wearableListener).execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    class SendDataTask extends AsyncTask<Node, Void, Void> {

        private final Recipe recipe;
        private WearableListener wearableListener;
        private Context context;

        public SendDataTask(Context context, Recipe recipe, WearableListener listener) {
            this.context = context;
            this.recipe = recipe;
            this.wearableListener = listener;
        }

        @Override
        protected Void doInBackground(Node... params) {
            PutDataMapRequest dataMap = PutDataMapRequest.create(Constants.WEAR_MAP_PATH);
            dataMap.getDataMap().putString(Constants.TITLE_KEY, recipe.getRecipeTitle());
            dataMap.getDataMap().putString(Constants.PREP_KEY, recipe.getPrepTime());
            dataMap.getDataMap().putString(Constants.COOK_KEY, recipe.getCookTime());
            dataMap.getDataMap().putString(Constants.IMAGE_KEY, recipe.getImage());
            dataMap.getDataMap().putStringArray(Constants.INGREDIENTS_KEY, recipe.getIngredientStringArray());
            dataMap.getDataMap().putStringArray(Constants.STEPS_KEY, recipe.getStepStringArray());

            PutDataRequest request = dataMap.asPutDataRequest();

            PendingResult<DataApi.DataItemResult> dataItemResult = Wearable.DataApi.putDataItem(apiClient, request);

            return null;
        }
    }

    class WearableListener extends WearableListenerService {
        // TODO: Implement
    }
}
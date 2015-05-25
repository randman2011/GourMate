package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.appspot.turnerrs_gourmade.gourmade.model.Recipe;

import java.util.List;

/**
 * Created by turnerrs on 4/2/2015.
 */
public class RecipeListRowAdapter extends BaseAdapter {

    private Context mContext;
    private List<Recipe> arrayList;

    public RecipeListRowAdapter(Context context, List<Recipe> list) {
        this.mContext = context;
        this.arrayList = list;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Recipe getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe item = this.getItem(position);
        RecipeRow view = null;
        if (convertView == null) {
            view = new RecipeRow(this.mContext);
        } else {
            view = (RecipeRow) convertView;
        }



        view.setRecipeTitle(item.getRecipeTitle());
        view.setPrepText(item.getPrepTime());
        view.setCookText(item.getCookTime());
        Bitmap bitmap = null;
        try {
            byte[] b = Base64.decode(item.getImage(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            view.setImage(bitmap);
        } catch (Exception e) {
            Log.e(Constants.LOG_NAME, "Failed decoding: " + e);
        }

        return view;

    }
}

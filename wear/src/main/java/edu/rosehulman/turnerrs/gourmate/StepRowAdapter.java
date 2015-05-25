package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.appspot.turnerrs_gourmade.gourmade.model.Ingredient;
import com.appspot.turnerrs_gourmade.gourmade.model.Step;

import java.util.ArrayList;

/**
 * Created by turnerrs on 5/24/2015.
 */
public class StepRowAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Step> steps;

    public StepRowAdapter(Context context, ArrayList<Step> steps) {
        this.mContext = context;
        this.steps = steps;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public Step getItem(int position) {
        return steps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Step item = this.getItem(position);
        StepRow view = null;
        if (convertView == null) {
            view = new StepRow(mContext);
        } else {
            view = (StepRow) convertView;
        }

        view.setStepNumber(item.getStepNumber());
        view.setStepText(item.getStepText());
        view.setStep(item);

        return view;
    }
}

package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by turnerrs on 4/18/2015.
 */
public class SeparatorRow extends LinearLayout {

    public SeparatorRow(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.separator_row, this);
    }
}

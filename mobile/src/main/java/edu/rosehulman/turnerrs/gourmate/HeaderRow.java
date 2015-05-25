package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by turnerrs on 4/20/2015.
 */
public class HeaderRow extends RelativeLayout {

    private TextView cookTimeView;
    private TextView prepTimeView;
    private TextView titleView;

    public HeaderRow(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.header_row, this);

        cookTimeView = (TextView)findViewById(R.id.cook_time_text);
        prepTimeView = (TextView)findViewById(R.id.prep_time_text);
        titleView = (TextView)findViewById(R.id.recipe_title_text);
    }

    public HeaderRow(Context context, Header header) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.header_row, this);

        cookTimeView = (TextView)findViewById(R.id.cook_time_text);
        prepTimeView = (TextView)findViewById(R.id.prep_time_text);
        titleView = (TextView)findViewById(R.id.recipe_title_text);

        setCookTime(header.cookTime);
        setPrepTime(header.prepTime);
        setTitle(header.title);
    }

    public void setCookTime(String time) { cookTimeView.setText(time);}
    public void setPrepTime(String time) { prepTimeView.setText(time);}
    public void setTitle(String title) { titleView.setText(title);}
}

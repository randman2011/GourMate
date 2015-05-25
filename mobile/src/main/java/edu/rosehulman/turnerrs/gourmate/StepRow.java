package edu.rosehulman.turnerrs.gourmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appspot.turnerrs_gourmade.gourmade.model.Step;

/**
 * Created by turnerrs on 4/18/2015.
 */
public class StepRow extends RelativeLayout {
    public TextView stepNumber;
    public TextView stepText;

    public StepRow(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.step_row, this);
        stepText = (TextView)findViewById(R.id.step_text);
        stepNumber = (TextView)findViewById(R.id.step_number);
    }

    public void setStepNumber(String number) { stepNumber.setText(number); }
    public void setStepText(String text) { stepText.setText(text); }

    public void setStepRow(Step step) {
        this.stepNumber.setText(step.getStepNumber());
        this.stepText.setText(step.getStepText());
    }
}

package com.ianfield.bodyscoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ianfield.bodyscoring.models.Record;
import com.ianfield.bodyscoring.utils.DateUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String PLANNED_CALVING_DATE_PICKER = "planned_calving_date_picker";
    public static final String SCORING_DATE_PICKER = "scoring_date_picker";

    @Bind(R.id.txtScoringDate)
    TextView mDateText;

    @Bind(R.id.txtExpectedCalvingDate)
    TextView mExpectedCalvingDate;

    @Bind(R.id.etFarmName)
    EditText mFarmName;

    Record mRecord = new Record();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_record);
        ButterKnife.bind(this);
        Date today = new Date();
        mRecord.setPlannedCalvingDate(today);
        mRecord.setScoringDate(today);
        mDateText.setText(DateUtils.dateToString(new Date()));
        mExpectedCalvingDate.setText(DateUtils.dateToString(new Date()));
    }

    Calendar todaysDatePickerCaldendar = Calendar.getInstance();
    DatePickerDialog todaysDatePickerDialog = DatePickerDialog.newInstance(
            this,
            todaysDatePickerCaldendar.get(Calendar.YEAR),
            todaysDatePickerCaldendar.get(Calendar.MONTH),
            todaysDatePickerCaldendar.get(Calendar.DAY_OF_MONTH)
    );

    Calendar expectedDatePickerCaldendar = Calendar.getInstance();
    DatePickerDialog expectedDatePickerDialog = DatePickerDialog.newInstance(
            this,
            expectedDatePickerCaldendar.get(Calendar.YEAR),
            expectedDatePickerCaldendar.get(Calendar.MONTH),
            expectedDatePickerCaldendar.get(Calendar.DAY_OF_MONTH)
    );

    @OnClick(R.id.txtScoringDate)
    public void clickSetDate() {
        todaysDatePickerDialog.show(getFragmentManager(), SCORING_DATE_PICKER);
    }

    @OnClick(R.id.txtExpectedCalvingDate)
    public void clickSetExpectedDate() {
        expectedDatePickerDialog.show(getFragmentManager(), PLANNED_CALVING_DATE_PICKER);
    }

    @OnClick(R.id.btnNext)
    public void clickNext() {
        mRecord.setName(mFarmName.getText().toString());
        if (mRecord.isValid()) {
            startActivity(new Intent(this, ScoringActivity.class));
            finish();
        } else {
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), R.string.snackbar_missing_name, Snackbar.LENGTH_LONG);

            snackBar.setAction(R.string.snackbar_dismiss, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)  {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (view == todaysDatePickerDialog) {
            mRecord.setScoringDate(date.getTime());
            mDateText.setText(DateUtils.dateToString(date.getTime()));
        } else {
            mRecord.setPlannedCalvingDate(date.getTime());
            mExpectedCalvingDate.setText(DateUtils.dateToString(date.getTime()));
        }
    }
}

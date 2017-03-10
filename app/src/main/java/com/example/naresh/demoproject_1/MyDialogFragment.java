package com.example.naresh.demoproject_1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.naresh.demoproject_1.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by naresh on 8/3/17.
 */

public class MyDialogFragment extends DialogFragment {
    public Context context = getContext();
    private View rootView;
    private Spinner spinnerOrder, spinnerSort;
    private Button btnFromDatePicker, btnToDatePicker, btnYes, btnNo;
    public String myDateFormat = "dd/MM/yyyy";
    Calendar FromCalender = Calendar.getInstance();
    Calendar ToCalender = Calendar.getInstance();

    public OnInfoChangedListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnInfoChangedListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    DatePickerDialog.OnDateSetListener fromDateListener, toDateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.mydialogfragment, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        spinnerOrder = (Spinner) rootView.findViewById(R.id.spinner_order);
        spinnerSort = (Spinner) rootView.findViewById(R.id.spinner_sort);
        btnFromDatePicker = (Button) rootView.findViewById(R.id.btn_from_date_picker);
        btnToDatePicker = (Button) rootView.findViewById(R.id.btn_to_date_picker);
        btnYes = (Button) rootView.findViewById(R.id.btn_yes);
        btnNo = (Button) rootView.findViewById(R.id.btn_no);

        fromDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                FromCalender.set(year, month, dayOfMonth);

                updateFromDatePicker();
            }
        };
        toDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker ToDatePicker, int year, int month, int dayOfMonth) {

                ToCalender.set(year, month, dayOfMonth);
                updateTODatePicker();
            }
        };
        btnFromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = FromCalender.get(Calendar.YEAR);
                int month = FromCalender.get(Calendar.MONTH);
                int day = FromCalender.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getContext(), fromDateListener, year, month, day)
                        .show();

            }
        });
        btnToDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = ToCalender.get(Calendar.YEAR);
                int month = ToCalender.get(Calendar.MONTH);
                int day = ToCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), toDateListener, year, month, day);
                dialog.getDatePicker().setMinDate(FromCalender.getTimeInMillis());
                dialog.show();

            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order = (String) spinnerOrder.getSelectedItem();
                String sort = (String) spinnerSort.getSelectedItem();


                String fromDate = convertDateFormat(FromCalender.getTimeInMillis(), "yyyy-MM-dd");
                String toDate = convertDateFormat(ToCalender.getTimeInMillis(), "yyyy-MM-dd");

                Log.e(" Order ", order);
                Log.e(" Sort ", sort);
                Log.e(" From Date ", fromDate);
                Log.e(" To Date ", toDate);


                listener.onInfoChanged(order, sort, fromDate, toDate);

                dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }

    public interface OnInfoChangedListener {

        public void onInfoChanged(String order, String sort, String FromDate, String ToDate);

    }

    String convertDateFormat(long timeInMilliSecond, String dateFormat) {

        Date date = new Date(timeInMilliSecond);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);

    }


    private void updateFromDatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);

        btnFromDatePicker.setText(sdf.format(FromCalender.getTime()));
    }

    private void updateTODatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
        btnToDatePicker.setText(sdf.format(ToCalender.getTime()));
    }
}

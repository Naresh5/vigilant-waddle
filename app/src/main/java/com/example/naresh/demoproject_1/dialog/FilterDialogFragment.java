package com.example.naresh.demoproject_1.dialog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.fragments.ActivityFragment;
import com.example.naresh.demoproject_1.fragments.UserFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by naresh on 8/3/17.
 */

public class FilterDialogFragment extends DialogFragment {

    private String TAG = FilterDialogFragment.class.getSimpleName();
    private View rootView;
    private Spinner spinnerOrder, spinnerSort;
    private Button btnFromDatePicker, btnToDatePicker, btnYes, btnNo;
    private String myDateFormat = "dd/MM/yyyy";
    private String toDateValue, fromDateValue;
    Calendar FromCalender = Calendar.getInstance();
    Calendar ToCalender = Calendar.getInstance();
    private static final String ARG_ORDER_VAL = "orderValue", ARG_SORT_VAL = "sortValue";
    private static final String ARG_FROM_DATE  ="FromDateValue", ARG_TO_DATE = "ToDateValue";

    public OnInfoChangedListener listener;

    public void setListener(OnInfoChangedListener listener) {
        this.listener = listener;
    }
    DatePickerDialog.OnDateSetListener fromDateListener, toDateListener;


    public static FilterDialogFragment newInstance(String orderVal, String sortVal, String fromDateVal, String toDateVal) {

        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        Bundle bundle = new Bundle();
            bundle.putString(ARG_ORDER_VAL, orderVal);
            bundle.putString(ARG_SORT_VAL, sortVal);
            bundle.putString(ARG_FROM_DATE, fromDateVal);
            bundle.putString(ARG_TO_DATE, toDateVal);
            filterDialogFragment.setArguments(bundle);
            return filterDialogFragment;
        }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.dialogfragment, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        spinnerOrder = (Spinner) rootView.findViewById(R.id.spinner_order);
        spinnerSort = (Spinner) rootView.findViewById(R.id.spinner_sort);
        btnFromDatePicker = (Button) rootView.findViewById(R.id.btn_from_date_picker);
        btnToDatePicker = (Button) rootView.findViewById(R.id.btn_to_date_picker);
        btnYes = (Button) rootView.findViewById(R.id.btn_yes);
        btnNo = (Button) rootView.findViewById(R.id.btn_no);

        Bundle bundle = getArguments();
        String orderVal = bundle.getString(ARG_ORDER_VAL);
        String sortVal = bundle.getString(ARG_SORT_VAL);
        String fromDateVal = bundle.getString(ARG_FROM_DATE);
        String toDateVal = bundle.getString(ARG_TO_DATE);

        Log.e(TAG, "Order Value" + orderVal);
        Log.e(TAG, "SortValue" + sortVal);
        Log.e(TAG, "FromDate Value" + fromDateVal);
        Log.e(TAG, "ToDate Value" + toDateVal);

        int orderPosition = getItemPosition(R.array.order_arrays, orderVal);
        int sortPosition = getItemPosition(R.array.sort_arrays,  sortVal);

        if (orderPosition != -1) {
            spinnerOrder.setSelection(orderPosition);
        }
        if (sortPosition != -1) {
            spinnerSort.setSelection(sortPosition);
        }
        btnFromDatePicker.setText(fromDateVal);
        btnToDatePicker.setText(toDateVal);

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
                new DatePickerDialog(getContext(), fromDateListener, year, month, day).show();

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
                String fromDate = null;
                String toDate = null;

                if (fromDateValue != null) {
                    fromDate = convertDateFormat(FromCalender.getTimeInMillis(), "yyyy-MM-dd");
                }
                if (toDateValue != null) {
                    toDate = convertDateFormat(ToCalender.getTimeInMillis(), "yyyy-MM-dd");
                }
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
    // Interfaces Declarations

    public interface OnInfoChangedListener {

        void onInfoChanged(String order, String sort, String FromDate, String ToDate);
    }

    String convertDateFormat(long timeInMilliSecond, String dateFormat) {
        Date date = new Date(timeInMilliSecond);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    private void updateFromDatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
        fromDateValue = sdf.format(FromCalender.getTime());
        btnFromDatePicker.setText(fromDateValue);
    }

    private void updateTODatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
        toDateValue = sdf.format(ToCalender.getTime());
        btnToDatePicker.setText(toDateValue);
    }

    private int getItemPosition(int strArray, String value) {

        String[] listArray;
        listArray = getResources().getStringArray(strArray);

        int position = -1;
        for (int i = 0; i < listArray.length; i++) {
            if (listArray[i].equals(value)) {
                position = i;
                break;
            }
        }
        return position;
    }
}

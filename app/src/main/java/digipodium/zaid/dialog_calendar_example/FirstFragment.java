package digipodium.zaid.dialog_calendar_example;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class FirstFragment extends Fragment {

    private long begin = 0;
    private long end = 0;
    private EditText editTitle;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTitle = view.findViewById(R.id.editTitle);
        final EditText editLocation = view.findViewById(R.id.editLocation);
        Button btnStartTime = view.findViewById(R.id.startTime);
        Button btnEndTime = view.findViewById(R.id.endTime);
        final TextView startTimeLabel = view.findViewById(R.id.startTimeLabel);
        final TextView endTimeLabel = view.findViewById(R.id.endTimeLabel);

        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar beginCalendar = Calendar.getInstance();
                        beginCalendar.set(Calendar.YEAR, year);
                        beginCalendar.set(Calendar.MONTH, month + 1);
                        beginCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        begin = beginCalendar.getTimeInMillis();
                        startTimeLabel.setText(year + " " + (month + 1) + "," + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar endCalendar = Calendar.getInstance();
                        endCalendar.set(Calendar.YEAR, year);
                        endCalendar.set(Calendar.MONTH, month + 1);
                        endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        end = endCalendar.getTimeInMillis();
                        endTimeLabel.setText(year + " " + (month + 1) + "," + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = editLocation.getText().toString();
                String title = editTitle.getText().toString();
                if (location.length() > 0 && title.length() > 0) {
                    if (begin > 0 && end > 0) {
                        addEvent(title, location, begin, end);
                    }
                }
            }
        });
    }

    public void addEvent(String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Snackbar.make(editTitle, "error, no activity can perform the task", Snackbar.LENGTH_INDEFINITE).show();
        }
    }
}
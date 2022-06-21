package com.example.smartcalendar.ViewCalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.smartcalendar.EditEventFragment;
import com.example.smartcalendar.Event;
import com.example.smartcalendar.EventList;
import com.example.smartcalendar.R;
import java.time.Year;
import java.util.Calendar;

public class ViewCalendarActivity extends AppCompatActivity implements ViewCalendarMonthFragment.IFromViewCalendarMonth,
        ViewCalendarDayFragment.IFromViewCalendarDay, EditEventFragment.IFromEditEvent {

    private ViewCalendarMonthFragment viewMonths;
    private ViewCalendarDayFragment viewDays;
    private EditEventFragment editEvent;
    private EventList allEvents;
    private Button buttonSettings;
    private Button buttonViewSmartEvents;
    private FragmentContainerView calendarFragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonViewSmartEvents = findViewById(R.id.buttonViewSmartEvents);
        calendarFragmentContainer = findViewById(R.id.fragmentContainerCalendar);

        viewMonths = new ViewCalendarMonthFragment(Year.now());
        allEvents = new EventList();
        editEvent = new EditEventFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerCalendar, viewMonths)
                .addToBackStack("months")
                .commit();
    }

    @Override
    public void selectedDay(Calendar date) {
        viewDays = new ViewCalendarDayFragment(allEvents, date);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerCalendar, viewDays)
                .addToBackStack("day")
                .commit();
    }

    @Override
    public void selectedEvent(Event event) {
        editEvent.setEventToEdit(event);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerMain, editEvent)
                .addToBackStack("edit event")
                .commit();
    }

    @Override
    public void selectedAddEvent(Calendar day) {
        editEvent.createNewEvent(day);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerMain, editEvent)
                .addToBackStack("edit event")
                .commit();
    }

    @Override
    public void saveEvent(Event event) {
        getSupportFragmentManager().popBackStackImmediate();
        for (int i = 0; i < allEvents.size(); i ++) {
            if (allEvents.getEventList().get(i).getCreationMillis() == event.getCreationMillis()) {
                allEvents.getEventList().remove(i);
                break;
            }
        }
        allEvents.add(event);
    }
}
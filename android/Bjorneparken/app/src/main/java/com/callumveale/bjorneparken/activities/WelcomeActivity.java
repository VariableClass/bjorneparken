package com.callumveale.bjorneparken.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.callumveale.bjorneparken.R;
import com.google.api.client.util.DateTime;

import java.util.Calendar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private final Handler mHideHandler = new Handler();
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private DateTime mVisitStart;
    private DateTime mVisitEnd;
    private boolean mStartDateSet;
    private boolean mEndDateSet;

    // UI Components
    private TextView mTitle;
    private DatePicker mDatePicker;
    private Button mBackButton;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Neither date has yet to be set
        mStartDateSet = false;
        mEndDateSet = false;

        // Set date from passed content
        Intent intent = getIntent();
        mVisitStart = (DateTime) intent.getSerializableExtra(HomeActivity.ARG_VISIT_START);
        mVisitEnd = (DateTime) intent.getSerializableExtra(HomeActivity.ARG_VISIT_END);

        // Set both times to midnight
        mVisitStart = setToMidnight(mVisitStart);
        mVisitEnd = setToMidnight(mVisitEnd);

        // Set title
        mTitle = (TextView) findViewById(R.id.welcome_title);
        mTitle.setText(getString(R.string.welcome_start));

        // Set up date picker
        Calendar now = Calendar.getInstance();
        mDatePicker = (DatePicker) findViewById(R.id.date_picker);
        mDatePicker.setMinDate(now.getTimeInMillis());
        mDatePicker.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                setDate(year, monthOfYear, dayOfMonth);
            }
        });

        // Buttons
        mBackButton = (Button) findViewById(R.id.back_date_button);
        mNextButton = (Button) findViewById(R.id.next_date_button);

        // Set up next button
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Return to main activity if both dates set
                if (mStartDateSet && mEndDateSet) {

                    Intent intent = new Intent();
                    intent.putExtra(HomeActivity.ARG_VISIT_START, mVisitStart);
                    intent.putExtra(HomeActivity.ARG_VISIT_END, mVisitEnd);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                } else {

                    // Confirm start date has been set
                    mStartDateSet = true;

                    // Set initial title and next button
                    mTitle.setText(getString(R.string.welcome_end));
                    mBackButton.setVisibility(View.VISIBLE);
                    mNextButton.setText(getString(R.string.welcome_save));

                    // Set Calendar minimum value to visit start date
                    Calendar now = Calendar.getInstance();
                    now.setTimeInMillis(mVisitStart.getValue());

                    // Update year to 0, to workaround bug in min date not updating in DatePicker library
                    mDatePicker.setMinDate(0);
                    mDatePicker.setMinDate(now.getTimeInMillis());

                    // If minimum date is before current visit end value
                    if (mDatePicker.getMinDate() < mVisitEnd.getValue()) {

                        // Set calendar selected date to that of the visit end date
                        now.setTimeInMillis(mVisitEnd.getValue());
                        mDatePicker.updateDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                    } else {

                        // Set date picker to minimum date
                        mDatePicker.updateDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                    }
                }
            }
        });

        // Set up back button
        mBackButton.setVisibility(View.GONE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Clear date set flags
                mStartDateSet = false;
                mEndDateSet = false;

                // Set initial title and next button
                mTitle.setText(getString(R.string.welcome_start));
                mBackButton.setVisibility(View.GONE);
                mNextButton.setText(getString(R.string.welcome_next));

                // Set Calendar minimum value to now
                Calendar now = Calendar.getInstance();

                // Update year to 0, to workaround bug in min date not updating in DatePicker library
                mDatePicker.setMinDate(0);
                mDatePicker.setMinDate(now.getTimeInMillis());

                // Set calendar selected date to that of the visit start date
                now.setTimeInMillis(mVisitStart.getValue());
                mDatePicker.updateDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.next_date_button).setOnTouchListener(mDelayHideTouchListener);
    }

    private void setDate(int year, int monthOfYear, int dayOfMonth){

        Calendar date = Calendar.getInstance();
        date.set(year, monthOfYear, dayOfMonth);

        if (!mStartDateSet){

            mVisitStart = new DateTime(date.getTimeInMillis());

        } else {

            mVisitEnd = new DateTime(date.getTimeInMillis());
            mEndDateSet = true;
        }
    }

    private DateTime setToMidnight(DateTime dateTime){

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateTime.getValue());

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        date.set(year, month, day, 0, 0, 0);

        return new DateTime(date.getTimeInMillis());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
    }


    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}

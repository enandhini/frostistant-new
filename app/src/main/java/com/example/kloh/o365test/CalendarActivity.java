package com.example.kloh.o365test;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.outlookservices.Event;
import com.microsoft.outlookservices.odata.OutlookClient;
import com.microsoft.services.odata.impl.DefaultDependencyResolver;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.ArrayList;


public class CalendarActivity extends Activity implements View.OnClickListener {


    TextView subject;
    TextView time;
    TextView location;
    TextView weather;
    TextView body;
    TextView suggestion;
    int eventIndex = 0;
    ArrayList Data = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        this.subject = (TextView)findViewById(R.id.tv_calendar_subject);
        this.time = (TextView)findViewById(R.id.tv_calendar_time);
        this.location = (TextView)findViewById(R.id.tv_calendar_location);
        this.body = (TextView)findViewById(R.id.tv_calendar_event_body);
        this.weather=(TextView) findViewById(R.id.weather);
        this.suggestion=(TextView)findViewById(R.id.suggestion);

        Button previous = (Button)findViewById(R.id.button_calendar_previous);
        previous.setOnClickListener(this);
        Button next = (Button)findViewById(R.id.button_calendar_next);
        next.setOnClickListener(this);

        initializeData();
        getEvent(true);
    }

    /**
     * handles actions for buttons on the view
     * @param view
     */
    @Override
    public void onClick(View view) {

        Boolean choice = true;
        switch (view.getId()) {
            case R.id.button_calendar_previous:
            {
                choice = false;
                break;
            }
            default:
                break;
        }

        final Boolean next = choice;
        Controller.getInstance().postASyncTask(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                getEvent(next);
                return null;
            }
        });
    }

    private void getEvent(final Boolean next) {

//        // create a client object
//        OutlookClient client = new OutlookClient(ServiceConstants.ENDPOINT_ID, (DefaultDependencyResolver)Controller.getInstance().getDependencyResolver());
//
//        // fetch next batch of events and select the first only
//        ListenableFuture<List<Event>> events = client
//                .getMe()
//                .getCalendar()
//                .getEvents()
//                .read();
//
//        // handle success and failure cases
//        Futures.addCallback(events, new FutureCallback<List<Event>>() {
//
//            @Override
//            public void onSuccess(final List<Event> result) {
//                Log.e("CalendarActivity", "onsuccess in cal activity");
//                if (result.isEmpty()) {
//                    displayEmptyCalendarMessage(next);
//                }
//
//                Collections.sort(result, new Comparator<Event>() {
//                    @Override
//                    public int compare(Event event, Event event2) {
//                        if (event.getStart().getTime().getTime() < event2.getStart().getTime().getTime()) {
//                            return -1;
//                        } else if (event.getStart().getTime().getTime() > event2.getStart().getTime().getTime()) {
//                            return 1;
//                        } else {
//                            return 0;
//                        }
//                    }
//                });
//
//                update(result, next);
//            }
//
//            @Override
//            public void onFailure(final Throwable t) {
//                Log.e("CalendarActivity", "onfailure in cal activity");
//
//                Controller.getInstance().handleError(CalendarActivity.this, t.getMessage());
//            }
//        });
        update(next);
    }

    private void initializeData() {
        CalendarData data = new CalendarData(8,4,0,false, 50);
        this.Data.add(data);
        data = new CalendarData(4,2,0,false, 70);
        this.Data.add(data);
        data = new CalendarData(10,0,1,true, 60);
        this.Data.add(data);
        data = new CalendarData(2,2,0,true, 50);
        this.Data.add(data);
        data = new CalendarData(8,8,0,false, 50);
        this.Data.add(data);
    }

    private void update(boolean next) {
        if (next) {
            ++this.eventIndex;
            if (this.eventIndex >= this.Data.size()) {
                this.eventIndex = this.Data.size() - 1;
            }
        }
        else {
            this.eventIndex--;
            if (this.eventIndex < 0) {
                this.eventIndex = 0;
            }
        }
        final CalendarData data=(CalendarData)this.Data.get(this.eventIndex);
        final TextView subject = this.subject;
        final TextView time = this.time;
        final TextView location = this.location;
        final TextView body = this.body;
        final TextView weather=this.weather;
        final TextView sug=this.suggestion;
        final ImageView background=(ImageView)this.findViewById(R.id.img_calendar_content_view);

        String suggestionString="";
        if(data.numberofevents>2)suggestionString+="You have a busy day. ";

        suggestionString+="I suggest that you wear";

        if(data.numberofformalevents>1)suggestionString+="Formal clothes";
        else suggestionString+="Casual work clothes";

        if(data.numberofeventsindiffbuilding>0) suggestionString+=" and bring along walking shoes";

        if(data.isRainy) suggestionString+=" and carry an umbrella";

        suggestionString+="!!";
        final String suggestion=suggestionString;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subject.setText("You have "+data.numberofevents+" meetings today");
                time.setText("\tOf those "+data.numberofeventsindiffbuilding+" meetings in a different building");
                location.setText("\tOf those "+data.numberofformalevents+" formal events");
                if(data.isRainy)
                    weather.setText("\tIt is "+data.weather+" degrees and rainy!");
                else
                    weather.setText("\tIt is "+data.weather+" degrees and not rainy!");
                sug.setTextColor(Color.RED);
                sug.setTextSize(30);
                sug.setAllCaps(true);
                sug.setText(suggestion);

                if(data.weather>60)
                    background.setBackgroundResource(R.drawable.hot);
                else
                    background.setBackgroundResource(R.drawable.cold);

            }
        });
    }

    private void update(final List<Event> events, boolean next) {

        if (next) {
            ++this.eventIndex;
            if (this.eventIndex >= events.size()) {
                this.eventIndex = events.size() - 1;
            }
        }
        else {
            this.eventIndex--;
            if (this.eventIndex < 0) {
                this.eventIndex = 0;
            }
        }

        final Event event = events.get(this.eventIndex);
        final TextView subject = this.subject;
        final TextView time = this.time;
        final TextView location = this.location;
        final TextView body = this.body;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subject.setText(event.getSubject());
                time.setText(event.getStart().getTime().toString());
                location.setText(event.getLocation().getDisplayName());
                body.setText(Html.fromHtml(event.getBody().getContent()));
            }
        });
    }

    private void displayEmptyCalendarMessage(Boolean lookingAtNext) {

        final String next = "Tomorrow";
        final String previous = "Yesterday";

        final String message =
                String.format(
                        "There are no %s events on your calendar",
                        lookingAtNext ? next : previous);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                        CalendarActivity.this,
                        message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

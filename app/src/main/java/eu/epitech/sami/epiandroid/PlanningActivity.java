package eu.epitech.sami.epiandroid;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import eu.epitech.sami.epiandroid.Tasks.planningTask;

public class PlanningActivity extends AppCompatActivity {
    public static String    todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c.getTime());

        final TextView txt = (TextView) findViewById(R.id.textView);

        System.out.println(date);
        txt.setText(date);

        Thread t1 = new Thread(new planningTask(EpiRestClient.model.token.token, date, date));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) { return ; }
        setData();
    }

    public void setData()
    {
        final ListView list = (ListView) findViewById(R.id.listView);

        EpiRestClient.model.planning.parsePlanning();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, EpiRestClient.model.planning.actititle);
        list.setAdapter(adapter);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                createModuleAlertDialog(position);
            }
        });
    }

    public void createModuleAlertDialog(int position)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.event_alert_layout, null);

        final TextView ViewTitle = (TextView) alertLayout.findViewById(R.id.event_title_text);
        final TextView ViewStart = (TextView) alertLayout.findViewById(R.id.event_start_text);
        final TextView ViewEnd = (TextView) alertLayout.findViewById(R.id.event_end_text);
        final TextView ViewDuration = (TextView) alertLayout.findViewById(R.id.event_duration_text);

//        final Button subscibeButton = (Button) alertLayout.findViewById(R.id.button_event_subscribe);
        final Button validateToken = (Button) alertLayout.findViewById(R.id.button_validate_token);
//        final Button unsubscribeButton = (Button) alertLayout.findViewById(R.id.button_event_unsubscribe);

        String title;
        String start;
        String end;
        String duration;

        title = EpiRestClient.model.planning.actititle[position];
        start = EpiRestClient.model.planning.starthours[position];
        end = EpiRestClient.model.planning.endhours[position];
        duration = EpiRestClient.model.planning.nbhours[position];
        ViewTitle.setText("Titre de l'activité : " + title);
        ViewStart.setText("Début : " + start);
        ViewEnd.setText("Fin : " + end);
        ViewDuration.setText("Durée totale : " + duration);

/*        validateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        alert.setView(alertLayout);
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }
}

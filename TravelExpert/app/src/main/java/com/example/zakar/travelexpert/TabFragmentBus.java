package com.example.zakar.travelexpert;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class TabFragmentBus extends Fragment {


    private Context context;
    private static String url = "http://countdown.tfl.gov.uk/stopBoard/77475";

    private static final String DESTINATION = "Destination";
    private static final String ROUTENAME = "routeName";
    private static final String ESTIMATEDWAIT = "estimatedWait";
    private static final String SCHEDULEDTIME = "scheduledTime";
    ArrayList<Bustop> bustops = new ArrayList<>();
    ArrayList<HashMap<String, String>> busTimes = new ArrayList<HashMap<String, String>>();

TextView lblBustopName;

    ListView busTimesList;

    Button btnBus;
    View rootView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       rootView = inflater.inflate(R.layout.tab_fragment_bus, container, false);

        busTimesList = (ListView) rootView.findViewById(R.id.busListView);
        btnBus = (Button) rootView.findViewById(R.id.btnBus);
lblBustopName = (TextView) rootView.findViewById(R.id.lblBustopName);

        if (isNetworkAvailable() == true) {

            run();

        } else {

            btnBus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = rootView.getContext();
                    CharSequence text = "Not Connected.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();


                }
            });


            Context context = rootView.getContext();
            CharSequence text = "Not Connected.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


        return rootView;

    }


public void run(){

    List<String[]> list = MainActivity.bustops;


    String removeUnwantedCharacters;
    for (int x = 1; x < list.size(); x++) {
        removeUnwantedCharacters = list.get(x)[3];
        removeUnwantedCharacters = removeUnwantedCharacters.replace("#", "");
        removeUnwantedCharacters = removeUnwantedCharacters.replace("<>", "");
        removeUnwantedCharacters = removeUnwantedCharacters.replace("<T>", "");
        removeUnwantedCharacters = removeUnwantedCharacters.replace(">T<", "");
        removeUnwantedCharacters = removeUnwantedCharacters.replace("<R>", "");
        removeUnwantedCharacters = removeUnwantedCharacters.replace(">R<", "");
        if (removeUnwantedCharacters.isEmpty()) {
            //Do nothing.
        } else {
            bustops.add(new Bustop(removeUnwantedCharacters, list.get(x)[1]));
        }
    }

    Collections.sort(bustops);

    final CharSequence colors[] = new CharSequence[bustops.size()];


    for (int x = 0; x < colors.length; x++) {
        colors[x] = bustops.get(x).getStopName();
    }


    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Pick a stop");


    builder.setItems(colors, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // the user clicked on colors[which]

            url = "http://countdown.tfl.gov.uk/stopBoard/" + bustops.get(which).getStopCode();
            busTimes.clear();

            new GetBusTimes(getActivity()).execute();
            lblBustopName.setText(bustops.get(which).getStopName());
            Animation animation1 = AnimationUtils.loadAnimation(rootView.getContext(), R.anim.custom);
            busTimesList.startAnimation(animation1);


        }
    });

    btnBus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isNetworkAvailable() == true) {
                builder.show();
            } else {
                Context context = rootView.getContext();
                CharSequence text = "Not Connected.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


        }
    });

}


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    private class GetBusTimes extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog;

        private Activity activity;

        private Context context;

        public GetBusTimes(Activity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Getting data from TFL");
            //this.dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            ListAdapter adapter = new SimpleAdapter(getActivity(), busTimes,
                    R.layout.list_item, new String[]{DESTINATION, ROUTENAME, ESTIMATEDWAIT,
                    SCHEDULEDTIME}, new int[]{R.id.txtToward,
                    R.id.txtBus, R.id.txtMinute, R.id.txtEstTime});

            busTimesList.setAdapter(adapter);

        }

        JSONArray arrivals;

        @Override
        protected Boolean doInBackground(String... params) {

            JSONParser getBusTimesFromServer = new JSONParser();

            JSONObject getBusTimes = getBusTimesFromServer.getJSONFromUrl(url);

            try {
                arrivals = getBusTimes.getJSONArray("arrivals");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < getBusTimes.length(); i++) {
                try {
                    JSONObject c = arrivals.getJSONObject(i);


                    String Destination = c.getString("destination");
                    String RouteName = c.getString("routeName");
                    String EstimatedWait = c.getString("estimatedWait");
                    String ScheduledTime = c.getString("scheduledTime");

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(DESTINATION, Destination);
                    map.put(ROUTENAME, RouteName);
                    map.put(ESTIMATEDWAIT, EstimatedWait);
                    map.put(SCHEDULEDTIME, ScheduledTime);
                    busTimes.add(map);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }
    }


}

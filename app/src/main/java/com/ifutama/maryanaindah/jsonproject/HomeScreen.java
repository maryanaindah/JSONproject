package com.ifutama.maryanaindah.jsonproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.name;
import static android.R.attr.value;

/**
 * Created by indah on 7/2/2017.
 */

public class HomeScreen extends AppCompatActivity {
    private String classtag= HomeScreen.class.getSimpleName();
    private ListView lv;
    private ProgressDialog progress;
    private String url="https://jsonplaceholder.typicode.com/";

    ArrayList<HashMap<String,String>> studentslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        studentslist=new ArrayList<>();
        lv= (ListView) findViewById(R.id.list);
        new getStudents().execute();
    }
    //--------------------------//-------------------------------//---------------------//
    public class getStudents extends AsyncTask<Void,Void,Void> {
        protected void onPreExecute(){
            super.onPreExecute();
            progress=new ProgressDialog(HomeScreen.this);
            progress.setMessage("Fetching JSON.,.");
            progress.setCancelable(false);
            progress.show();
        }
        protected Void doInBackground(Void...arg0){
            HTTP_Handler hh = new HTTP_Handler();
            String jString = hh.makeHTTPCall(url);
            Log.e(classtag, "Response from URL: " + jString);
            if (jString != null) {
                try {
                    JSONArray students = new JSONArray(jString);

                    for (int i = 0; i < students.length(); i++) {
                        JSONObject obj = students.getJSONObject(i);
                        String userId=obj.getString("userId");
                        String id=obj.getString("id");
                        String title=obj.getString("title");
                        String body=obj.getString("body");
                        HashMap<String, String> studentdata = new HashMap<>();

                        studentdata.put("userId", userId);
                        studentdata.put("id", id);
                        studentdata.put("title", title);
                        studentdata.put("body", body);

                        studentslist.add(studentdata);
                    }
                } catch (final JSONException e) {
                    Log.e(classtag, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(classtag, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check internet
                            connection!",
                        Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
        protected void onPostExecute(Void Result){
            super.onPostExecute(Result);
            if(progress.isShowing()){
                progress.dismiss();
            }
            ListAdapter adapter=new SimpleAdapter(
                    HomeScreen.this,
                    studentslist,
                    R.layout.bucket_list,
                    new String[]{"userId","id","title","body"},
                    new int[]{R.id.list_Name,R.id.list_Email,R.id.list_Address
                            ,R.id.list_Gender});
            lv.setAdapter(adapter);

        }
    }
}

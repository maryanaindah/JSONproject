package com.ifutama.maryanaindah.jsonproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtResult;
    private Button btnItem;
    private Button btnItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        txtResult = (TextView) findViewById(R.id.txt_result);
        btnItem = (Button) findViewById(R.id.btn_item);
        btnItemList = (Button) findViewById(R.id.btn_item_list);

        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetItemTask().execute();
            }
        });

        btnItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetItemListTask().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setResult(Post post) {
        txtResult.setText(post.getName());
    }

    private void setResult(ArrayList<Post> posts) {
        txtResult.setText(posts.get(1).getName());
    }

    private class GetItemTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/users/1");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Post post = new Gson().fromJson(s, Post.class);
            setResult(post);
        }
    }

    private class GetItemListTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL("https://jsonplaceholder.typicode.com/users");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<Post> posts = new Gson().fromJson(s,
                    new TypeToken<ArrayList<Post>>() {
                    }.getType());
            setResult(posts);
        }
    }
}

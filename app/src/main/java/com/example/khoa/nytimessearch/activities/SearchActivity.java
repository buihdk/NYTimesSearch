package com.example.khoa.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.khoa.nytimessearch.fragments.AdvancedSearchDialogFragment;
import com.example.khoa.nytimessearch.models.Article;
import com.example.khoa.nytimessearch.adapters.ArticleArrayAdapter;
import com.example.khoa.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    String beginDate;
    String sort;
    String arts;
    String fashionStyle;
    String sports;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

    }

    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        // hook up listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // create an intent to display the article
            Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
            // get the article to display
            Article article = articles.get(position);
            // pass in that article into intent
            //i.putExtra("url", article.getWebUrl());
            i.putExtra("article", article);
            // launch the activity
            startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_advanced_search) {
            showAdvancedSeachDialogFragment();
            return true;
        }
        //

        return super.onOptionsItemSelected(item);
    }

    private void showAdvancedSeachDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        AdvancedSearchDialogFragment editNameDialog = AdvancedSearchDialogFragment.newInstance("Advanced Search");
        editNameDialog.show(fm, "advanced_search_fragment");
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "b8526afc28519a6ba1bdf965eca0fc47:15:74716163");
        params.put("page", 0);
        params.put("q", query);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            beginDate = extras.getString("beginDate");
            sort = extras.getString("sort");
            arts = extras.getString("arts");
            fashionStyle = extras.getString("fashionStyle");
            sports = extras.getString("sports");
            String filter = arts + " " + fashionStyle + " " + sports;
            filter = filter.trim();
            params.put("begin_date", beginDate);
            params.put("sort", sort);
            params.put("fq=news_desk:", filter);
            Toast.makeText(this, "Searching for " + query + " " + beginDate + " " + sort + " " + filter, Toast.LENGTH_LONG).show();
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

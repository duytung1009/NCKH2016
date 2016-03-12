package com.nckh2016.vuduytung.nckh2016;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nckh2016.vuduytung.nckh2016.Data.AdapterMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.ObjectMonHoc;
import com.nckh2016.vuduytung.nckh2016.Data.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    ArrayList<Object> mListResult = new ArrayList<Object>();
    AdapterMonHoc mAdapterResult;
    CircularProgressView progressBar;
    LinearLayout mainLayout;
    TextView txtTieuDe;
    ListView lvResult;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        query = intent.getStringExtra("query");
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        mainLayout.setVisibility(View.GONE);
        mAdapterResult = new AdapterMonHoc(this, 0);
        lvResult = (ListView)findViewById(R.id.lvResult);
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), ChiTietMonHocActivity.class);
                intent.putExtra("MaMonHoc", ((ObjectMonHoc) mListResult.get(position)).getMamh());
                intent.putExtra("caller", "SearchResultActivity");
                startActivity(intent);
            }
        });
        lvResult.setAdapter(mAdapterResult);
        progressBar = (CircularProgressView) findViewById(R.id.progressBar);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.search);
        txtTieuDe = (TextView)findViewById(R.id.txtTieuDe);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Transition
        LinearLayout searchBar = (LinearLayout) searchView.findViewById(R.id.search_bar);
        searchBar.setLayoutTransition(new LayoutTransition());
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.txtTimKiem));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);
                new SearchResultTask(getParent()).execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new SearchResultTask(this).execute(query);
    }

    public class SearchResultTask extends AsyncTask<String, Long, ArrayList<Object>>{
        private Context mContext;

        public SearchResultTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Object> doInBackground(String... params) {
            // Joining:
            StringBuilder buffer = new StringBuilder();
            for (String each : params)
                buffer.append(",").append(each);
            String joined = buffer.deleteCharAt(0).toString();
            // database access
            query = joined;
            SQLiteDataController data = SQLiteDataController.getInstance(mContext);
            try{
                data.isCreatedDatabase();
            }
            catch (IOException e){
                Log.e("tag", e.getMessage());
            }
            return data.searchMonHoc(joined);
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Object> objects) {
            if(objects != null) {
                mListResult.clear();
                mListResult.addAll(objects);
                txtTieuDe.setText(query + " - " + mListResult.size() + " kết quả");
                mAdapterResult.clear();
                mAdapterResult.addAll(mListResult);
                mainLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            super.onPostExecute(objects);
        }
    }
}

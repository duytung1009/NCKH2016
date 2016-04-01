package com.nckh2016.vuduytung.nckh2016;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;

import com.nckh2016.vuduytung.nckh2016.Data.ObjectCTDT;

public class NganhActivity extends AppCompatActivity {
    //các giá trị để dùng load Fragment
    private static final String FRAG1 = "fragment_nganh_1";
    private static final String FRAG2 = "fragment_nganh_2";
    private static final String FRAG3 = "fragment_nganh_3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nganh);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);
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
                Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void loadFragment2(String maNganh, String tenNganh){
        Bundle bundle = new Bundle();
        bundle.putString("MaNganh", maNganh);
        bundle.putString("TenNganh", tenNganh);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag2 = new Nganh2Fragment();
        frag2.setArguments(bundle);
        ft.addToBackStack(FRAG2);
        ft.replace(R.id.fragment_nganh, frag2, FRAG2);
        ft.commit();
    }

    public void loadFragment3(ObjectCTDT monHoc){
        if(monHoc.getMamh() == null){
            Bundle bundle = new Bundle();
            switch (monHoc.getTuchon()){
                case "A":
                    bundle.putString("MaNganh", monHoc.getMabm());
                    bundle.putInt("HocKy", -3);
                    break;
                case "B":
                    bundle.putString("MaNganh", monHoc.getMabm());
                    bundle.putInt("HocKy", -2);
                    break;
                case "C":
                    bundle.putString("MaNganh", monHoc.getMabm());
                    bundle.putInt("HocKy", -1);
                    break;
                default:
                    bundle.putString("MaNganh", monHoc.getMabm());
                    bundle.putInt("HocKy", 0);
                    break;
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment frag3 = new Nganh3Fragment();
            frag3.setArguments(bundle);
            ft.addToBackStack(FRAG3);
            ft.replace(R.id.fragment_nganh, frag3, FRAG3);
            ft.commit();
        } else {
            Intent intent = new Intent(this, ChiTietMonHocActivity.class);
            intent.putExtra("MaMonHoc", monHoc.getMamh());
            intent.putExtra("caller", "BoMonActivity");
            startActivity(intent);
        }
    }
}

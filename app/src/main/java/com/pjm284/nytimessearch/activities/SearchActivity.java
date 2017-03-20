package com.pjm284.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pjm284.nytimessearch.R;
import com.pjm284.nytimessearch.adapters.ArticleAdapter;
import com.pjm284.nytimessearch.fragments.SearchFilterFragment;
import com.pjm284.nytimessearch.models.Article;
import com.pjm284.nytimessearch.models.Search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements SearchFilterFragment.SearchFilterDialogListener {

    private ArticleAdapter.OnItemClickListener articleItemClickListener = new ArticleAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent i = new Intent(SearchActivity.this, ArticleActivity.class);
            // get the article to display
            Article article = articles.get(position);
            // pass in that article to the intent
            i.putExtra("article", article);
            // launch the activity
            startActivity(i);
        }
    };

    ArrayList<Article> articles;

    ArticleAdapter adapter;

    private Search search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search = new Search();

        RecyclerView rvArticles = (RecyclerView) findViewById(R.id.rvArticles);

        // Initialize contacts
        articles = new ArrayList<Article>();
        // Create adapter passing in the sample user data
        adapter = new ArticleAdapter(this, articles);
        // Attach the adapter to the recyclerview to populate items
        rvArticles.setAdapter(adapter);
        // Set layout manager to position the items
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        rvArticles.setLayoutManager(gridLayoutManager);

        adapter.setOnItemClickListener(articleItemClickListener);

//        showFilterDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onArticleSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                articles.clear();
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_filter:
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchFilterFragment searchFilterFragment = SearchFilterFragment.newInstance("Search Filters", search);
        searchFilterFragment.show(fm, "fragment_search_filter");
    }

    public void onArticleSearch(String query) {
        // clear existing articles in list
        articles.clear();

        search.setSearchString(query);
        search.getResults(searchResultHandler);
    }

    JsonHttpResponseHandler searchResultHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("DEBUG", response.toString());
            JSONArray articleJsonResults = null;

            try {
                articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                articles.addAll(Article.fromJSONArray(articleJsonResults));
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("ERROR", errorResponse.toString());
        }
    };

    @Override
    public void onFinishEditDialog(Search search) {
        this.search = search;
    }

}

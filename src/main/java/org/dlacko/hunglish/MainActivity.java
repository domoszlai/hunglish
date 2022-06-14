package org.dlacko.hunglish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import org.dlacko.hunglish.query.Query;
import org.dlacko.hunglish.query.QueryExecutor;
import org.dlacko.hunglish.query.Result;
import org.dlacko.hunglish.query.SentencePair;
import org.dlacko.hunglish.query.providers.HunglishProvider;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView listView;
    ListViewAdapter adapter;
    View loadingView;
    ImageButton langButton;
    SearchView searchView;

    ExecutorService executorService;
    Handler mainThreadHandler;
    QueryExecutor executor;

    boolean loading = false;
    boolean queryExhausted = false;
    Query currentQuery;
    Integer cursor;
    String currentLang = "en";

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executorService = Executors.newFixedThreadPool(1);
        mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        executor = new QueryExecutor(executorService, mainThreadHandler, new HunglishProvider());

        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        loadingView = this.getLayoutInflater().inflate(R.layout.load_more, null, false);

        adapter = new ListViewAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((++firstVisibleItem) + visibleItemCount > totalItemCount) {
                    loadSome();
                }
            }
        });

        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);

        View langButtonView = this.getLayoutInflater().inflate(R.layout.lang_button, null, false);
        langButton = langButtonView.findViewById(R.id.langButton);
        langButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLang();
            }
        });

        ((LinearLayout) searchView.getChildAt(0)).addView(langButtonView);
        ((LinearLayout) searchView.getChildAt(0)).setGravity(Gravity.CENTER);

        setLang("en");
    }

    @Override
    public boolean onQueryTextSubmit(String filterSentence) {
        resetState();

        if(currentLang == "en"){
            currentQuery = new Query("", filterSentence);
        }
        else
        {
            currentQuery = new Query(filterSentence, "");
        }

        loadSome();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        resetState();
        return false;
    }

    private void setLang(String lang){
        if(lang == "en"){
            langButton.setImageResource(R.drawable.ic_flag_of_the_united_kingdom);
            searchView.setQueryHint("Enter some english word(s)");
        }
        else
        {
            langButton.setImageResource(R.drawable.ic_flag_of_hungary);
            searchView.setQueryHint("Adja meg a magyar kifejezést");
        }

        currentLang = lang;
    }

    private void switchLang(){
        if(currentLang == "en"){
            setLang("hu");
        }
        else
        {
            setLang("en");
        }
    }

    private void resetState(){
        adapter.clearSentencePairs();
        currentQuery = null;
        cursor = null;
        queryExhausted = false;
    }

    private void loadSome(){
        if(currentQuery != null && !loading && !queryExhausted){
            loading = true;
            enableLoadingMessage(true);

            executor.run(result -> {

                if (result instanceof Result.Success) {
                    List<SentencePair> newSentencePairs = ((Result.Success) result).sentencePairs;

                    if(newSentencePairs == null || newSentencePairs.isEmpty()){
                        queryExhausted = true;
                    }
                    else
                    {
                        queryExhausted = false;
                        adapter.addSentencePairs(newSentencePairs);
                    }

                    cursor = ((Result.Success) result).cursor;
                } else {
                    // Show error in UI
                }

                enableLoadingMessage(false);
                loading = false;

            }, currentQuery, cursor);
        }
    }

    public void enableLoadingMessage(boolean enable) {
        if (enable) {
            listView.addFooterView(loadingView);
        } else {
            listView.removeFooterView(loadingView);
        }
    }
}
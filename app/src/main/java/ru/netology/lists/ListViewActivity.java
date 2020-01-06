package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {

    private String stringLargeText;
    private SimpleAdapter listContentAdapter;
    private SharedPreferences savePreferens;
    private List<Map<String, String>> content;
    private static String LARGE_TEXT = "large_text";
    private SwipeRefreshLayout swipeRefresh;
    private ListView list;
    private ArrayList<Integer> indexRemoveItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateList();
        initSwipeRefresh();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("key", indexRemoveItems);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        indexRemoveItems = savedInstanceState.getIntegerArrayList("key");
        for (Integer integer : indexRemoveItems) {
            content.remove(integer.intValue());
        }
    }

    private void updateList() {
        SharedPreferences();
        List<Map<String, String>> values = prepareContent();
        listContentAdapter = createAdapter(values);
        initList();
        listContentAdapter.notifyDataSetChanged();
    }

    private void initSwipeRefresh() {
        swipeRefresh = findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                indexRemoveItems = new ArrayList<>();
                updateList();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initList() {
        list = findViewById(R.id.list);
        list.setAdapter(listContentAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                content.remove(i);
                indexRemoveItems.add(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void SharedPreferences() {
        stringLargeText = getString(R.string.large_text);
        savePreferens = getSharedPreferences("TEXT", MODE_PRIVATE);
        SharedPreferences.Editor editorPref = savePreferens.edit();
        editorPref.putString("LARGE_TEXT", stringLargeText);
        editorPref.apply();
    }

    @NonNull
    private SimpleAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values,
                R.layout.text_main, new String[]{"text", "number"}, new int[]{R.id.TextViewUp, R.id.textViewDown});

    }

    @NonNull
    private List<Map<String, String>> prepareContent() {
        content = new ArrayList<>();
        String[] arrayContent = savePreferens.getString("LARGE_TEXT", stringLargeText).split("\n\n");
        Map<String, String> map;
        for (int i = 0; i < arrayContent.length; i++) {
            map = new HashMap<>();
            map.put("text", arrayContent[i]);
            map.put("number", Integer.toString(arrayContent[i].length()));
            content.add(map);
        }
        return content;
    }
}

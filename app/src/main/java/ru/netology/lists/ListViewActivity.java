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

    private String stringLageText;
    private SimpleAdapter listContentAdapter;
    private SharedPreferences savePreferens;
    private List<Map<String, String>> content;
    private static String LARGE_TEXT = "large_text";
    private SwipeRefreshLayout swipeRefresh;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateList();
        initSwipeRefresh();
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
                listContentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void SharedPreferences() {
        stringLageText = getString(R.string.large_text);
        savePreferens = getSharedPreferences("TEXT", MODE_PRIVATE);
        SharedPreferences.Editor editorPref = savePreferens.edit();
        editorPref.putString("LAGE_TEXT", stringLageText);
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
        String[] arrayContent = savePreferens.getString("LAGE_TEXT", stringLageText).split("\n\n");
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

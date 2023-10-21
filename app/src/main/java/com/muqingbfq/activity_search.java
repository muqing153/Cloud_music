package com.muqingbfq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muqingbfq.fragment.search;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class activity_search extends AppCompatActivity {
    private EditText editText;
    private ArrayAdapter<String> adapter;

    private JSONObject json = new JSONObject();
    private List<String> json_list = new ArrayList<>();
    private final List<String> list = new ArrayList<>();
    ListView listPopupWindow;
    public static AppCompatActivity appCompatActivity;

    @SuppressLint({"RestrictedApi", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        appCompatActivity = this;
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.list_recycler);
        SearchRecordAdapter recordAdapter = new SearchRecordAdapter();
        recyclerView.setAdapter(recordAdapter);

        editText = findViewById(R.id.editview);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String str = v.getText().toString();
                if (!str.equals("")) {
//                    退出activity并返回str数据
                    start(str);
                }
            }
            return false;
        });
        findViewById(R.id.deleat).setOnClickListener(v -> new MaterialAlertDialogBuilder(v.getContext())
                .setTitle("删除")
                .setMessage("清空历史记录？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    wj.sc(wj.filesdri + wj.lishi_json);
                    json = new JSONObject();
                    json_list.clear();
                    recordAdapter.notifyDataSetChanged();
                    findViewById(R.id.xxbj1).setVisibility(View.GONE);
                })
                .show());
        listPopupWindow = findViewById(R.id.search_recycler);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listPopupWindow.setAdapter(adapter);

        //设置项点击监听
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View v = getWindow().peekDecorView();
            if (null != v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            editText.setText(list.get(i));//把选择的选项内容展示在EditText上
            dismiss();//如果已经选择了，隐藏起来
            start(editText.getText().toString());

        });
        editText.setOnFocusChangeListener((view, b) -> {
            if (b) {
                dismiss();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list.clear();
                if (s.length() < 1) {
                    list.clear();
                    adapter.notifyDataSetChanged();
                    editText.clearFocus();
                    dismiss();
                    return;
                }
                if (!editText.hasFocus()) {
                    dismiss();
                    return;
                }
                listPopupWindow.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        String hq = wl.hq("/search/suggest?keywords=" + s + "&type=mobile");
                        try {
                            JSONArray jsonArray = new JSONObject(hq).getJSONObject("result")
                                    .getJSONArray("allMatch");
                            int length = jsonArray.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String keyword = jsonObject.getString("keyword");
                                list.add(keyword);
                            }
                            main.handler.post(() -> adapter.notifyDataSetChanged());
                        } catch (Exception e) {
                            gj.ts(activity_search.this, e);
                        }
                        super.run();
                    }
                }.start();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    public void dismiss() {
        listPopupWindow.setVisibility(View.GONE);
    }

    private void addSearchRecord(String name) {
        try {
            if (!json.has("list")) {
                json.put("list", new JSONArray());
            }
            if (!json_list.contains(name)) {
                json_list.add(name);
                JSONObject record = new JSONObject();
                record.put("name", name);
                json.getJSONArray("list").put(record);
                wj.xrwb(wj.filesdri + wj.lishi_json, json.toString());
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.menu_search) {
            start(editText.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    com.muqingbfq.fragment.search search;
    public void start(String name) {
        dismiss();
        if (name.equals("")) {
            return;
        }
        if (search == null) {
            search = new search(name);
        }
        if (!search.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_fragment, search)
                    .addToBackStack(null).commit();
        } else {
            search.setStart(name);
        }
        addSearchRecord(name);
    }

    class SearchRecordAdapter extends RecyclerView.Adapter<SearchRecordAdapter.ViewHolder> {
        public SearchRecordAdapter() {
            String dqwb = wj.dqwb(wj.filesdri + wj.lishi_json);
            if (dqwb != null) {
                try {
                    json = new JSONObject(dqwb);
                    JSONArray list1 = json.getJSONArray("list");
                    int length = list1.length();
                    for (int i = length - 1; i >= 0; i--) {
                        json_list.add(list1.
                                getJSONObject(i).getString("name"));
                    }
                } catch (JSONException e) {
                    yc.start(activity_search.this, e);
                }
            }
            if (json_list.isEmpty()) {
                findViewById(R.id.xxbj1).setVisibility(View.INVISIBLE);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), android.R.layout.simple_list_item_1, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String keyword = json_list.get(position);
            holder.recordTextView.setText(keyword);
            holder.recordTextView.setOnClickListener(v -> {
                editText.setText(keyword);
                start(keyword);
            });
        }

        @Override
        public int getItemCount() {
            return json_list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView recordTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                recordTextView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
package com.muqingbfq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muqingbfq.databinding.ActivitySearchBinding;
import com.muqingbfq.fragment.search;
import com.muqingbfq.mq.ActivityToolbar;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class activity_search extends ActivityToolbar {
    private EditText editText;
    private ArrayAdapter<String> adapter;
    private SearchRecordAdapter recordAdapter;
    private JSONObject json = new JSONObject();
    private final List<String> json_list = new ArrayList<>();
    private final List<String> list = new ArrayList<>();
    ListView listPopupWindow;
    ActivitySearchBinding inflate;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        RecyclerView recyclerView = findViewById(R.id.list_recycler);
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        //设置主轴排列方式
        manager.setFlexDirection(FlexDirection.ROW);
        //设置是否换行
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setAlignItems(AlignItems.STRETCH);
        recyclerView.setLayoutManager(manager);
        new SearchRecordAdapter();
        recyclerView.setAdapter(recordAdapter);

        editText = findViewById(R.id.editview);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String str = v.getText().toString();
                if (!str.equals("")) {
                    start(str);
                }
            }
            return false;
        });
        inflate.deleat.setOnClickListener(v -> new MaterialAlertDialogBuilder(v.getContext())
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
//历史记录的LayoutManager

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
        Object o = new Object();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    dismiss();
                    return;
                }
                list.clear();
                if (listPopupWindow.getVisibility() == View.GONE) {
                    listPopupWindow.setVisibility(View.VISIBLE);
                }
                new Thread() {
                    @Override
                    public void run() {
                        synchronized (o) {
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
                                gj.sc(e);
                            }
                        }
                    }
                }.start();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void dismiss() {
        listPopupWindow.setVisibility(View.GONE);
    }

    private void addSearchRecord(String name) {
        try {
            if (!findViewById(R.id.xxbj1).isShown()) {
                findViewById(R.id.xxbj1).setVisibility(View.VISIBLE);
            }
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
            gj.sc(e);
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

    public void start(String name) {
        dismiss();
        if (!TextUtils.isEmpty(name)) {
            search sea = (search) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
            if (!name.equals(sea.name)) {
                sea.i = 0;
                sea.inflate.tablayout.getTabAt(0).select();
            }
            sea.setVisibility(true);
            sea.setStart(name);
            addSearchRecord(name);
        }
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
                inflate.xxbj1.setVisibility(View.INVISIBLE);
            }
            recordAdapter = this;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.list_text, null);
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
                recordTextView = itemView.findViewById(R.id.button);
            }
        }
    }

    @Override
    public void onBackPressed() {
        end();
    }

    private void end() {
        search search = (com.muqingbfq.fragment.search) getSupportFragmentManager().
                findFragmentById(R.id.search_fragment);
        if (search.isVisible()) {
            search.setVisibility(false);
        } else {
            finish();
        }
    }
    @Override
    public void finish() {
        super.finish();
        com.muqingbfq.fragment.search.lbspq = null;
    }
}
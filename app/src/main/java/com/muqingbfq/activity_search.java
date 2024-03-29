package com.muqingbfq;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muqingbfq.databinding.ActivitySearchBinding;
import com.muqingbfq.fragment.search;
import com.muqingbfq.mq.FragmentActivity;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.view.Edit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class activity_search extends FragmentActivity {
    private ArrayAdapter<String> adapter;
    private List<String> json_list = new ArrayList<>();
    private final List<String> list = new ArrayList<>();
    ListView listPopupWindow;
    ActivitySearchBinding binding;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        //设置主轴排列方式
        manager.setFlexDirection(FlexDirection.ROW);
        //设置是否换行
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setAlignItems(AlignItems.STRETCH);
        binding.listRecycler.setLayoutManager(manager);
        binding.listRecycler.setAdapter(new SearchRecordAdapter());

        binding.editview.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String str = v.getText().toString();
                if (!str.equals("")) {
                    start(str);
                }
            }
            return false;
        });
        binding.deleat.setOnClickListener(v -> new MaterialAlertDialogBuilder(
                activity_search.this)
                .setTitle("删除")
                .setMessage("清空历史记录？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    wj.sc(wj.filesdri + wj.lishi_json);
                    json_list.clear();
                    binding.listRecycler.getAdapter().notifyDataSetChanged();
                    findViewById(R.id.xxbj1).setVisibility(View.GONE);
                })
                .show());
        listPopupWindow = findViewById(R.id.search_recycler);
//历史记录的LayoutManager

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listPopupWindow.setAdapter(adapter);

        //设置项点击监听
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View v = getWindow().peekDecorView();
            if (null != v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            binding.editview.setText(list.get(i));//把选择的选项内容展示在EditText上
            dismiss();//如果已经选择了，隐藏起来
            start(binding.editview.getText().toString());

        });
        Object o = new Object();
        binding.editview.addTextChangedListener(new Edit.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.editview.iskong();
                if (TextUtils.isEmpty(s)) {
                    listPopupWindow.setVisibility(View.GONE);
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
        gj.tcjp(binding.editview.editText);
    }

    public void dismiss() {
        binding.editview.clearFocus();
        listPopupWindow.setVisibility(View.GONE);
        gj.ycjp(binding.editview.editText);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addSearchRecord(String name) {
        try {
            if (!binding.xxbj1.isShown()) {
                binding.xxbj1.setVisibility(View.VISIBLE);
            }
            json_list.remove(name);
            json_list.add(0, name);
            wj.xrwb(wj.filesdri + wj.lishi_json, new Gson().toJson(json_list));
            binding.listRecycler.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            gj.sc(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem sousuo = menu.add("搜索");
        sousuo.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == 0) {
            start(binding.editview.getText().toString());
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
                    json_list = new Gson().fromJson(dqwb, new TypeToken<List<String>>() {
                    }.getType());
                } catch (Exception e) {
                    wj.sc(wj.filesdri + wj.lishi_json);
                    yc.start(activity_search.this, e);
                }
            }
            if (json_list.isEmpty()) {
                binding.xxbj1.setVisibility(View.INVISIBLE);
            }
            RecyclerView.ItemAnimator animator = new DefaultItemAnimator(){
                @Override
                public boolean animateRemove(RecyclerView.ViewHolder holder) {
                    ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(holder.itemView, "alpha", 1f, 0f);
                    fadeAnimator.setDuration(getRemoveDuration());
                    fadeAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dispatchRemoveFinished(holder);
                            holder.itemView.setAlpha(1f);
                        }
                    });
                    fadeAnimator.start();
                    return false;
                }
            };
            binding.listRecycler.setItemAnimator(animator);
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
                binding.editview.setText(keyword);
                start(keyword);
            });
            holder.recordTextView.setOnLongClickListener(view -> {
                new MaterialAlertDialogBuilder(activity_search.this).
                        setTitle("删除此记录：" + keyword)
                        .setPositiveButton("确定", (dialogInterface, i) -> {
                            json_list.remove(keyword);
                            wj.xrwb(wj.filesdri + wj.lishi_json, new Gson().toJson(json_list));
                            notifyItemRemoved(position);
                        })
                        .setNegativeButton("取消", null)
                        .show();

                return false;
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
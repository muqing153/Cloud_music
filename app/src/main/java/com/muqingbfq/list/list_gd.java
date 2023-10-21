package com.muqingbfq.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muqingbfq.R;
import com.muqingbfq.api.playlist;
import com.muqingbfq.fragment.gd;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.xm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

public class list_gd implements View.OnClickListener, View.OnLongClickListener {
    xm xm;

    public list_gd(com.muqingbfq.xm xm) {
        this.xm = xm;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClick(View view) {
//        if (!gd.gdid.equals(xm.id)) {
//        gd.gdid = xm.id;
        Context context = view.getContext();
        Intent intent = new Intent(context, com.muqingbfq.fragment.mp3.class);
        intent.putExtra("id", xm.id);
        intent.putExtra("name", xm.name);
        context.startActivity(intent);
//            mp3.startactivity(view.getContext(),xm.id);
    }


    @Override
    public boolean onLongClick(View view) {
        String[] stringArray = view.getResources()
                .getStringArray(R.array.gd_list);
        new MaterialAlertDialogBuilder(view.getContext()).setItems(stringArray, (dialog, id) -> {
            new Thread() {
                @Override
                public void run() {
                    if (id == 0) {
                        String hq = wl.hq(playlist.api + xm.id + "&limit=30");
                        if (hq != null) {
                            wj.xrwb(wj.gd + xm.id, hq);
                            xm.cz = true;
                            try {
                                JSONObject jsonObject = new JSONObject();
                                if (wj.cz(wj.gd_xz)) {
                                    jsonObject = new JSONObject(Objects.requireNonNull(wj.dqwb(wj.gd_xz)));
                                }
                                JSONObject json = new JSONObject();
                                json.put("name", xm.name);
                                json.put("picUrl", xm.picurl);
                                jsonObject.put(xm.id, json);
                                wj.xrwb(wj.gd_xz, jsonObject.toString());
                            } catch (JSONException e) {
                                gj.sc("list gd onclick thear " + e);
                            }
                        }

                    } else if (id == 2) {
                        wj.sc(wj.gd + xm.id);
                        if (xm.id.equals("mp3_xz.json")) {
                            wj.sc(new File(wj.mp3));
                        }
                        xm.cz = false;
                        try {
                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(wj.dqwb(wj.gd_xz)));
                            jsonObject.remove(xm.id);
                            wj.xrwb(wj.gd_xz, jsonObject.toString());
                        } catch (JSONException e) {
                            gj.sc(e);
                        }
                    }
                    main.handler.post(() -> gd.lbspq.notifyDataSetChanged());
                }
            }.start();
            // 在这里处理菜单项的点击事件
            dialog.dismiss();
        }).show();
        return false;
    }
}

package com.muqingbfq.api;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.muqingbfq.MP3;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.xm;
import com.muqingbfq.yc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class url extends Thread {
    public static String api = "/song/url/v1";
    MP3 x;

    public url(MP3 x) {
        this.x = x;
        start();
    }

    public static String hq(MP3 x) {
        if (Media.lrcview != null) {
            gc(x.id);
        }
        try {
            if (wj.cz(wj.filesdri + "hc/" + x.id)) {
                return wj.filesdri + "hc/" + x.id;
            }
            if (wj.cz(wj.mp3+x.id)) {
                return wj.mp3 + x.id;
            }
            String level = "standard";
            boolean wiFiConnected = gj.isWiFiConnected();
            if (wiFiConnected) {
                level = "exhigh";
            }
            String hq = wl.hq(api + "?id=" + x.id + "&level=" +
                    level + "&cookie=" + wl.Cookie);
            if (hq == null) {
                return null;
            }
            JSONObject json = new JSONObject(hq);
            JSONArray data = json.getJSONArray("data");
            JSONObject jsonObject = data.getJSONObject(0);
            String url = jsonObject.getString("url");
            if (wiFiConnected) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            if (new File(wj.filesdri + "hc").isDirectory()) {
                                File[] aa = new File(wj.filesdri + "hc").listFiles();
                                if (aa.length >= 30) {
                                    aa[0].delete();
                                }
                            }
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    //访问路径
                                    .url(url)
                                    .build();
                            Call call = client.newCall(request);
                            Response response = call.execute();
                            if (response.isSuccessful()) {
                                ResponseBody body = response.body();
                                if (body != null) {
                                    File file = new File(wj.filesdri + "hc", x.id + ".mp3");
                                    if (!file.getParentFile().exists()) {
                                        file.getParentFile().mkdirs();
                                    }
                                    File parentFile = file.getParentFile();
                                    if (!parentFile.isDirectory()) {
                                        parentFile.mkdirs();
                                    }
                                    InputStream inputStream = body.byteStream();
                                    FileOutputStream fileOutputStream =
                                            new FileOutputStream(file);
                                    // 替换为实际要保存的文件路径
                                    byte[] buffer = new byte[4096];
                                    int bytesRead;
                                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                                        fileOutputStream.write(buffer, 0, bytesRead);
                                    }
                                    fileOutputStream.close();
                                    inputStream.close();
                                    Mp3File mp3file = new Mp3File(file);
                                    if (mp3file.hasId3v2Tag()) {
                                        ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                                        // 设置新的ID值
                                        id3v2Tag.setTitle(x.name);
                                        id3v2Tag.setArtist(x.zz);
                                        id3v2Tag.setAlbum(x.zz);
                                        id3v2Tag.setUrl(x.picurl.toString());
                                        mp3file.save(wj.filesdri + "hc/" + x.id);
                                        file.delete();
                                        // 保存修改后的音乐文件，删除原来的文件
                                    }
                                }
                            }
                        } catch (Exception e) {
                            gj.sc("wl xz " + e);
                        }
                    }
                }.start();
            }
            return url;
        } catch (JSONException e) {
            yc.start("url hq :" + e);
        }
        return null;
    }

    @Override
    public void run() {
        super.run();
        com.muqingbfq.bfqkz.mp3(hq(x));
    }


    public static void gc(String id) {
        String lrc = null, tlyric = null;
        try {
            JSONObject jsonObject = new JSONObject(wl.hq("/lyric?id=" + id));
            lrc = jsonObject.getJSONObject("lrc").getString("lyric");
            tlyric = jsonObject.getJSONObject("tlyric").getString("lyric");
        } catch (Exception e) {
            gj.sc("url gc(int id) lrc: " + e);
        }
        Media.loadLyric(lrc, tlyric);
    }

    public static String picurl(String id) {
        String hq = wl.hq("/song/detail?ids=" + id);
        try {
            return new JSONObject(hq).getJSONArray("songs").getJSONObject(0)
                    .getJSONObject("al").getString("picUrl");
        } catch (Exception e) {
            yc.start(e);
        }
        return null;
    }
}

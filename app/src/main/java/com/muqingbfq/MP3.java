package com.muqingbfq;

import java.util.Objects;

public class MP3 {
    public String id, name, zz;
//    音乐的贴图
    public String picurl;
    public MP3(String id, String name, String zz, String picurl) {
        this.id = id;
        this.name = name;
        this.zz = zz;
        this.picurl = picurl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MP3)) return false;
        MP3 mp3 = (MP3) o;
        return Objects.equals(id, mp3.id) &&
                Objects.equals(name, mp3.name) &&
                Objects.equals(zz, mp3.zz) &&
                Objects.equals(picurl, mp3.picurl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, zz, picurl);
    }
}
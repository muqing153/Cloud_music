package com.muqingbfq.mq;

import android.content.Context;

import com.muqingbfq.yc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class wj {
    public static String filesdri;
    public static String mp3 = "mp3/";
    public static String lishi_json = "lishi.json";
    public static String gd = "gd/";
    public static String tx = "image/";
    public static String gd_json = "gd.json", mp3_xz = "mp3_xz.json", gd_xz = "gd_xz.json",
            gd_phb = "gd_phb.json", mp3_like = "mp3_like.json";

    public wj(Context context) {
        try {
            wj.filesdri = context.getExternalFilesDir("").getAbsolutePath() + "/";
//                context.getFilesDir().toString() + "/";
            gd_json = filesdri + gd_json;
            mp3 = filesdri + mp3;
            gd = filesdri + gd;
            gd_xz = filesdri + gd_xz;
            gd_phb = filesdri + gd_phb;
            mp3_xz = gd + mp3_xz;
            mp3_like = gd + mp3_like;
            tx = filesdri + tx;
        } catch (Exception e) {
            yc.start(context, e);
        }
    }

    /*
     * 这里定义的是一个文件保存的方法，写入到文件中，所以是输出流
     * */
    public static boolean xrwb(String url, String text) {
        if (text == null) {
            text = "";
        }
        File file = new File(url);
//如果文件不存在，创建文件
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.isDirectory()) {
                parentFile.mkdirs();
            }
            if (!file.exists())
                file.createNewFile();
//创建FileOutputStream对象，写入内容
            FileOutputStream fos = new FileOutputStream(file);
//向文件中写入内容
            fos.write(text.getBytes());
            fos.close();
        } catch (Exception e) {
            gj.sc(e);
        }
        return false;
    }

    public static String dqwb(String url) {
        try {
            File file = new File(url);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder str = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                str.append(line);
            }
            br.close();
            fis.close();
            return str.toString();
        } catch (Exception e) {
            gj.sc(e);
        }
        return null;
    }

    public static boolean cz(String url) {
        return new File(url).exists();
    }

    public static boolean cz(File file) {
        return file.exists();
    }

    public static boolean sc(String url) {
        File file = new File(url);
        return file.delete();
    }

    public static void sc(File url) {
        if (url.exists()) {
            File[] files = url.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归调用，删除子文件夹及其内容
                        sc(file);
                    } else {
                        file.delete(); // 删除文件
                    }
                }
            }
            url.delete(); // 删除当前文件夹
        }
    }


    public String convertToMd5(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(url.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte value : messageDigest) {
                String hex = Integer.toHexString(0xFF & value);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void fz(String sourceFilePath, String targetFilePath) {
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try (InputStream in = Files.newInputStream(sourceFile.toPath());
                 OutputStream out = Files.newOutputStream(targetFile.toPath())) {
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buf)) > 0) {
                    out.write(buf, 0, bytesRead);
                }
                // 文件复制完成
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

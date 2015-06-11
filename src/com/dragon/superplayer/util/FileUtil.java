package com.dragon.superplayer.util;

import java.io.File;

public class FileUtil {

    public static boolean isVideoOrAudio(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        if (name.equalsIgnoreCase(".mp4") || name.equalsIgnoreCase(".3gp")
                || name.equalsIgnoreCase(".wmv")
                || name.equalsIgnoreCase(".ts")
                || name.equalsIgnoreCase(".rmvb")
                || name.equalsIgnoreCase(".mov")
                || name.equalsIgnoreCase(".m4v")
                || name.equalsIgnoreCase(".avi")
                || name.equalsIgnoreCase(".m3u8")
                || name.equalsIgnoreCase(".3gpp")
                || name.equalsIgnoreCase(".3gpp2")
                || name.equalsIgnoreCase(".mkv")
                || name.equalsIgnoreCase(".flv")
                || name.equalsIgnoreCase(".divx")
                || name.equalsIgnoreCase(".f4v")
                || name.equalsIgnoreCase(".rm")
                || name.equalsIgnoreCase(".asf")
                || name.equalsIgnoreCase(".ram")
                || name.equalsIgnoreCase(".mpg")
                || name.equalsIgnoreCase(".v8")
                || name.equalsIgnoreCase(".swf")
                || name.equalsIgnoreCase(".m2v")
                || name.equalsIgnoreCase(".asx")
                || name.equalsIgnoreCase(".ra")
                || name.equalsIgnoreCase(".ndivx")
                || name.equalsIgnoreCase(".xvid")) {
            return true;
        }
        return false;
    }

}

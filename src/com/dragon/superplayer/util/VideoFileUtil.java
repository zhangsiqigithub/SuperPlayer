package com.dragon.superplayer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.dragon.superplayer.model.LocalVideoInfo;

public class VideoFileUtil {

    private static final String TAG = "";

    /** 扫描SD卡 */
    private class ScanVideoTask extends AsyncTask<Void, File, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            this.eachAllMedias(Environment.getExternalStorageDirectory());
            return null;
        }

        @Override
        protected void onProgressUpdate(File... values) {
        }

        /** 遍历所有文件夹，查找出视频文件 */
        public void eachAllMedias(File f) {
            if (f != null && f.exists() && f.isDirectory()) {
                File[] files = f.listFiles();
                if (files != null) {
                    for (File file : f.listFiles()) {
                        if (file.isDirectory()) {
                            this.eachAllMedias(file);
                        } else if (file.exists() && file.canRead()
                                && FileUtil.isVideoOrAudio(file)) {
                            this.publishProgress(file);
                        }
                    }
                }
            }
        }
    }

    /** 批量提取视频的缩略图已经视频的宽高 */
    public static ArrayList<LocalVideoInfo> batchBuildThumbnail(
            final Context context, final ArrayList<File> fileList) {
        ArrayList<LocalVideoInfo> result = new ArrayList<LocalVideoInfo>();

        for (File f : fileList) {
            LocalVideoInfo localVideoInfo = new LocalVideoInfo();
            try {
                if (f.exists() && f.canRead()) {
                    // 取出视频的一帧图像
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
                            f.getAbsolutePath(), Video.Thumbnails.MINI_KIND);
                    if (bitmap == null) {
                        // 缩略图创建失败
                        bitmap = Bitmap.createBitmap(100, 200,
                                Bitmap.Config.RGB_565);
                        Log.e(TAG, "batchBuildThumbnail createBitmap faild : "
                                + f.getAbsolutePath());
                    }

                    localVideoInfo.setThumbWidth(bitmap.getWidth());
                    localVideoInfo.setThumbHight(bitmap.getHeight());

                    // 缩略图
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 200,
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    if (bitmap != null) {
                        File thum = new File(f.getParent(), f.getName()
                                + ".jpg");
                        localVideoInfo.setThumbPath(thum.getAbsolutePath());
                        // thum.createNewFile();
                        FileOutputStream iStream = new FileOutputStream(thum);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, iStream);
                        iStream.close();
                    }

                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                continue;
            } finally {
                result.add(localVideoInfo);
            }
        }

        return result;
    }
}

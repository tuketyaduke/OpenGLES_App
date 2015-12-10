package com.example.tauto_otya.opengles_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tauto_otya on 2015/12/08.
 */
public class OpenGLSample_1_4 {
    int screenWidth = 0;
    int screenHeight = 0;

    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        gl10.glViewport(0, 0, w, h);
        screenWidth = w;
        screenHeight = h;

        Bitmap bitmap =
                BitmapFactory.decodeResource(getResources(), R.drawable.image_512);
        log("bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

        gl10.glEnable(GL10.GL_TEXTURE_2D);
        int[] buffers = new int[1];
        // テクスチャ用のメモリを指定数確保
        gl10.glGenTextures(1, buffers, 0);
        // テクスチャ名を保存する
        texture = buffers[0];
        // テクスチャ情報の設定
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, textrure);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // 拡大・縮小時の処理を指定
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        // bitmap を破棄
        bitmap.recycle();
    }

}

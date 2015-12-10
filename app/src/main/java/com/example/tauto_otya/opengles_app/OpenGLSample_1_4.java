package com.example.tauto_otya.opengles_app;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tauto_otya on 2015/12/08.
 */
public class OpenGLSample_1_4 implements GLSurfaceView.Renderer {
    //private final static String TAG = OpenGLSample_1_4.class.getSimpleName();

    int screenWidth = 0;
    int screenHeight = 0;

    int texture = 0;        // texture名を保存する
    Resources res = null;


    OpenGLSample_1_4(Activity activity) {
        super();
        res = activity.getResources();
    }

    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        gl10.glViewport(0, 0, w, h);
        screenWidth = w;
        screenHeight = h;

        Bitmap bitmap =
                BitmapFactory.decodeResource(res,R.drawable.image_512);
        //Log.d(TAG, "bitmap size : " + bitmap.getWidth() + " x " + bitmap.getHeight());

        gl10.glEnable(GL10.GL_TEXTURE_2D);
        int[] buffers = new int[1];
        // テクスチャ用のメモリを指定数確保
        gl10.glGenTextures(1, buffers, 0);
        // テクスチャ名を保存する
        texture = buffers[0];
        // テクスチャ情報の設定
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, texture);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // 拡大・縮小時の処理を指定
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

        // bitmap を破棄
        bitmap.recycle();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglconfig) {


    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // テクスチャ全体のUVを指定
        {
            // UV情報
            float uv[] = {
                // u v
                0.0f, 0.0f, // 左上
                0.0f, 1.0f, // 左下
                1.0f, 0.0f, // 右上
                1.0f, 1.0f, // 右下
            };

            ByteBuffer bb = ByteBuffer.allocateDirect(uv.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(uv);
            fb.position(0);

            gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, fb);
        }

        // 等倍で表示する
        drawQuad(gl10, 0, 0, 512, 512);

        // 拡大縮小して表示する
        drawQuad(gl10, screenWidth / 2, screenHeight /2, 800, 100);
    }

    private void drawQuad(GL10 gl10, int x, int y, int w, int h) {
        float left = ((float) x / (float) screenWidth) * 2.0f - 1.0f;
        float top = ((float) y / (float) screenHeight) * 2.0f - 1.0f;

        float right = left + ((float) w / (float) screenWidth) * 2.0f;
        float bottom = top + ((float) h / (float) screenHeight) * 2.0f;

        // 上下を反転させる
        top = -top;
        bottom = -bottom;

        // 位置情報
        float positions[] = {
            // x, y, z
                left, top, 0.0f,     // 左上
                left, bottom, 0.0f,  // 左下
                right, top, 0.0f,    // 右上
                right, bottom, 0.0f, // 右下
        };

        // OpenGL はビッグ円ディアンではなく、
        // CPUごとのネイティブ円ディアンで数値を伝える必要がある
        // そのためJavaヒープを直接的には扱えず
        // java.nio 配下のクラスへ１度値を格納する必要がある
        ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(positions);
        fb.position(0);
        // バッファの確保終了

        // バッファを有効にする
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // 位置情報の関連付け（メモリをコピーするわけではない）
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, fb);

        // 描画処理
        gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }
}

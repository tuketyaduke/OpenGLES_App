package com.example.tauto_otya.opengles_app;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tauto_otya on 2015/11/25.
 */
public class GLRenderSample1 implements GLSurfaceView.Renderer {

    int screenWidth  = 0;
    int screenHeight = 0;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        // glViewport → 描画領域を決める
        gl10.glViewport(0, 0, w, h);
        screenWidth = w;
        screenHeight = h;
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

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClearColor(0, 1.0f, 1.0f, 1.0f);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // 位置情報
        /*float positions[] = {
                0.0f, 1.0f, 0.0f,   // 左上
                0.0f, 0.0f, 0.0f,   // 左下
                1.0f, 1.0f, 0.0f,   // 右上

                // 上向き
                // 0.0f, 0.0f, 0.0f,   // 左下
                1.0f, 0.0f, 0.0f,   // 右下
                // 1.0f, 1.0f, 1.0f,   // 右上
        };
        */
        // 赤い四角形を描画する
        gl10.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
        drawQuad(gl10, 200, 100, 300, 400);

        // 青い四角形を描画する
        gl10.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
        drawQuad(gl10, screenWidth / 2, screenHeight / 2, 256, 128);

    }


}

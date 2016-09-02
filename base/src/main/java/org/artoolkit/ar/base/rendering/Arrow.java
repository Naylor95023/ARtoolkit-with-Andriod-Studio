package org.artoolkit.ar.base.rendering;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;

/**
 * Simple class to render a coloured cube.
 */
public class Arrow {

    private FloatBuffer	mVertexBuffer;
    private FloatBuffer	mColorBuffer;
    private ByteBuffer	mIndexBuffer;

    public Arrow() {
        this(1.0f);
    }

    public Arrow(float size) {
        this(size, 0.0f, 0.0f, 0.0f);
    }

    public Arrow(float size, float x, float y, float z) {
        setArrays(size, x, y, z);
    }

    private void setArrays(float size, float x, float y, float z) {
        float hs = size / 2.0f;
        float vertices[] = {
                20.0f, 0.0f, 10.0f,//0
                0.0f, -20.0f, 10.0f,//1
                0.0f, -10.0f, 10.0f,//2
                -20.0f, -10f, 10.0f,//3
                -20.0f, 10.0f, 10.0f,//4
                0.0f, 10.0f, 10.0f,//5
                0.0f, 20.0f, 10.0f,//6

                20.0f, 0.0f, 20.0f,//7
                0.0f, -20.0f, 20.0f,//8
                0.0f, -10.0f, 20.0f,//9
                -20.0f, -10f, 20.0f,//10
                -20.0f, 10.0f, 20.0f,//11
                0.0f, 10.0f, 20.0f,//12
                0.0f, 20.0f, 20.0f,//13

        };
        float c = 1.0f;
        float colors[] = {
                0, 0, 0, c, // 0 black
                c, 0, 0, c, // 1 red
                c, c, 0, c, // 2 yellow
                0, c, 0, c, // 3 green
                0, 0, c, c, // 4 blue
                c, 0, c, c, // 5 magenta
                c, c, c, c, // 6 white
                0, c, c, c, // 7 cyan
                0, 0, 0, c, // 0 black
                c, 0, 0, c, // 1 red
                c, c, 0, c, // 2 yellow
                0, c, 0, c, // 3 green
                0, 0, c, c, // 4 blue
                c, 0, c, c, // 5 magenta
                c, c, c, c, // 6 white
                0, c, c, c, // 7 cyan
                0, 0, 0, c, // 0 black
                c, 0, 0, c, // 1 red
                c, c, 0, c, // 2 yellow
                0, c, 0, c, // 3 green
                0, 0, c, c, // 4 blue
                c, 0, c, c, // 5 magenta
                c, c, c, c, // 6 white
                0, c, c, c, // 7 cyan
        };
        byte indices[] = {
                0, 1, 6,
                3, 4, 5,    3, 5, 2,
                7, 8, 13,
                10, 11, 12,  10, 12, 9,

                0, 6, 13,   0, 13, 7,
                0, 1, 8,    0, 8, 7,
                5, 6, 13,   5, 13, 12,
                2, 1, 8,    2, 8, 9,
                5, 4, 11,   5, 11, 12,
                2, 3, 10,   2, 10, 9,
                3, 4, 11,   3, 11, 10
        };

        mVertexBuffer = RenderUtils.buildFloatBuffer(vertices);
        mColorBuffer = RenderUtils.buildFloatBuffer(colors);
        mIndexBuffer = RenderUtils.buildByteBuffer(indices);

    }

    public void draw(GL10 unused) {
        GLES10.glColorPointer(4, GLES10.GL_FLOAT, 0, mColorBuffer);
        GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, mVertexBuffer);

        GLES10.glEnableClientState(GLES10.GL_COLOR_ARRAY);
        GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);

        GLES10.glDrawElements(GLES10.GL_TRIANGLES, 60, GLES10.GL_UNSIGNED_BYTE, mIndexBuffer);

        GLES10.glDisableClientState(GLES10.GL_COLOR_ARRAY);
        GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);

    }


}
package org.artoolkit.ar.base.rendering;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES10;
/**
 * Created by User on 2016/6/13.
 */
public class Mesh {
    List<String> LINes;
    float[] vertices;
    float[] normals ;
    float[] uv ;

    // List<String> lines;
    int numVertices = 0;
    int numNormals = 0;
    int numUV = 0;
    int numFaces = 0;

    short[] facesVerts;
    short[] facesNormals;
    short[] facesUV ;
    int vertexIndex = 0;
    int normalIndex = 0;
    int uvIndex = 0;
    int faceIndex = 0;
    ShortBuffer index;
    ShortBuffer mnormal;
    ShortBuffer text;
    FloatBuffer vertex;
    FloatBuffer normal;
    FloatBuffer texcoords;
    int textureId=-1;
    int ilength;

    public Mesh(GL10 gl, Context parent) {

        //Meshes = new Vector<Mesh>();

        openObjFile("Data/c.bjo", parent, gl);

    }

    public void draw(GL10 gl) {

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);

        // Pass the vertex buffer in
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);
        if(textureId>=0){

            // Get specific texture.
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

            gl.glActiveTexture(GL10.GL_TEXTURE0);

            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

            gl.glEnable(GL10.GL_TEXTURE_2D);

            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);


            // Pass in texture normals
            gl.glNormalPointer(GL10.GL_FLOAT, 0,mnormal);
            // Pass in texture coordinates
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, text);
        }

        //gl.glColor4f (1.0f, 1.0f, 1.0f, 1.0f);

        // Draw the triangles

        gl.glDrawElements(GL10.GL_TRIANGLES, ilength, GL10.GL_UNSIGNED_SHORT, index);

        if(textureId>=0){

            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            // Disable the use of textures.
            gl.glDisable(GL10.GL_TEXTURE_2D);
        }
        // Disable the buffers

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

    }

    // reads the model file in
    private List<String> openFile(String filename, Context context) {

        InputStream instream = null;

        List<String> LINes = new ArrayList<String>();

        try {
            instream = context.getAssets().open(filename);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(instream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLINE;
            // Read File LINE By LINE
            while ((strLINE = br.readLine()) != null) {
                LINes.add(strLINE);
            }
            // Close the Input stream
            instream.close();

        } catch (Exception e) {// Catch exception if any
            e.toString();
        }

        return LINes;
    }

    // high level model file parser
    private void openObjFile(String filename, Context context, GL10 gl) {

        List<String> LINes = openFile(filename, context); // opens the file
        vertices = new float[LINes.size() * 3];
        normals = new float[LINes.size() * 3];
        uv = new float[LINes.size() * 2];
        facesVerts = new short[LINes.size() * 3];
        facesNormals = new short[LINes.size() * 3];
        facesUV = new short[LINes.size() * 3];

        for (int i = 0; i < LINes.size(); i++) {
            String line = LINes.get(i);
            if (line.startsWith("v ")) {
                String[] tokens = line.split("[ ]+");
                vertices[vertexIndex] = Float.parseFloat(tokens[1]);
                vertices[vertexIndex + 1] = Float.parseFloat(tokens[2]);
                vertices[vertexIndex + 2] = Float.parseFloat(tokens[3]);
                vertexIndex += 3;
                numVertices++;
                continue;
            }

            if (line.startsWith("vn ")) {
                String[] tokens = line.split("[ ]+");
                normals[normalIndex] = Float.parseFloat(tokens[1]);
                normals[normalIndex + 1] = Float.parseFloat(tokens[2]);
                normals[normalIndex + 2] = Float.parseFloat(tokens[3]);
                normalIndex += 3;
                numNormals++;
                continue;
            }

            if (line.startsWith("vt")) {
                String[] tokens = line.split("[ ]+");
                uv[uvIndex] = Float.parseFloat(tokens[1]);
                uv[uvIndex + 1] = Float.parseFloat(tokens[2]);
                uvIndex += 2;
                numUV++;
                continue;
            }

            if (line.startsWith("f ")) {
                String[] tokens = line.split("[ ]+");

                String[] parts = tokens[1].split("/");
                facesVerts[faceIndex] = getIndexs(parts[0], numVertices);
                if (parts.length > 2)
                    facesNormals[faceIndex] = getIndexs(parts[2], numNormals);
                if (parts.length > 1)
                    facesUV[faceIndex] = getIndexs(parts[1], numUV);
                faceIndex++;

                parts = tokens[2].split("/");
                facesVerts[faceIndex] = getIndexs(parts[0], numVertices);
                if (parts.length > 2)
                    facesNormals[faceIndex] = getIndexs(parts[2], numNormals);
                if (parts.length > 1)
                    facesUV[faceIndex] = getIndexs(parts[1], numUV);
                faceIndex++;

                parts = tokens[3].split("/");
                facesVerts[faceIndex] = getIndexs(parts[0], numVertices);
                if (parts.length > 2)
                    facesNormals[faceIndex] = getIndexs(parts[2], numNormals);
                if (parts.length > 1)
                    facesUV[faceIndex] = getIndexs(parts[1], numUV);
                faceIndex++;
                numFaces++;
                continue;
            }
        }
        index=createsBuffer(facesVerts);
        ilength=facesVerts.length;
        mnormal=createsBuffer(facesNormals);
        text=createsBuffer(facesUV);
        vertex=createfBuffer(vertices);
        normal=createfBuffer(normals);
        texcoords=createfBuffer(uv);
        // Meshes.add(new Mesh(text,mnormal, index, textureId, facesVerts.length ));

    }

    ShortBuffer createsBuffer(short[] indices) {

        ShortBuffer sbuffer;
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        sbuffer = ibb.asShortBuffer();
        sbuffer.put(indices);
        sbuffer.position(0);
        return sbuffer;

    }

    FloatBuffer createfBuffer(float[] indices) {

        FloatBuffer fbuffer;
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 4);
        ibb.order(ByteOrder.nativeOrder());
        fbuffer = ibb.asFloatBuffer();
        fbuffer.put(indices);
        fbuffer.position(0);
        return fbuffer;

    }

    short getIndexs(String index,int size){
        short idx = Short.parseShort(index);
        if (idx < 0)
            return (short) (size + idx);
        else
            return (short) (idx-1);
    }

    int getIndex(String index, int size) {
        int idx = Integer.parseInt(index);
        if (idx < 0)
            return size + idx;
        else
            return idx - 1;
    }
}

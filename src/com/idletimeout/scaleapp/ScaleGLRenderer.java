package com.idletimeout.scaleapp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class ScaleGLRenderer implements Renderer {
	
	private Scale mScale;
	@Override
	public void onDrawFrame(GL10 gl) {
		
		// Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
		mScale = new Scale();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		// Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
	}
	
	public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
	
	class Scale {
		private final FloatBuffer vertexBuffer;
		
		private final String vertexShaderCode =
		    "attribute vec4 vPosition;" +
		    "void main() {" +
		    "  gl_Position = vPosition;" +
		    "}";

		private final String fragmentShaderCode =
		    "precision mediump float;" +
		    "uniform vec4 vColor;" +
		    "void main() {" +
		    "  gl_FragColor = vColor;" +
		    "}";
		
		private final int mProgram;
		private int mPositionHandle;
		
		static final int COORDS_PER_VERTEX = 3;
		private float scaleCoords[] = { -0.5f,  0.5f, 0.0f,   // top left
						            	-0.5f, -0.5f, 0.0f,   // bottom left
						            	0.5f, -0.5f, 0.0f,   // bottom right
							            0.5f,  0.5f, 0.0f }; // top right
		
		private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

		public Scale() {
			// initialize ByteBuffer
			// (# of coordinate values * 4 bytes per float)
			ByteBuffer bb = ByteBuffer.allocateDirect(scaleCoords.length * 4);
			bb.order(ByteOrder.nativeOrder());
			vertexBuffer = bb.asFloatBuffer();
			vertexBuffer.put(scaleCoords);
			vertexBuffer.position(0);
			
			// prepare shaders and OpenGL program
	        int vertexShader = ScaleGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
	                                                   vertexShaderCode);
	        int fragmentShader = ScaleGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
	                                                     fragmentShaderCode);
	        
	        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
	        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
	        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
	        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		}
		
		public void draw() {
			// Add program to OpenGL ES environment
		    GLES20.glUseProgram(mProgram);
		    
		    // get handle to vertex shader's vPosition member
	        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
	        
	        // Enable a handle to the scale vertices
	        GLES20.glEnableVertexAttribArray(mPositionHandle);
	        
	        // Prepare the triangle coordinate data
	        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
	                                     GLES20.GL_FLOAT, false,
	                                     vertexStride, vertexBuffer);
		    
		}
		
	}

}

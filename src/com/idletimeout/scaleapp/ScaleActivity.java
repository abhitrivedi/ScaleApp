package com.idletimeout.scaleapp;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

public class ScaleActivity extends Activity {
	
	private GLSurfaceView mGLView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create new instance of GLView and set as content view
		mGLView = new ScaleGLView(this);
		setContentView(mGLView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scale, menu);
		return true;
	}
	
	class ScaleGLView extends GLSurfaceView {

		public ScaleGLView(Context context) {
			super(context);
			
			// set client version
			setEGLContextClientVersion(2);
			
			// Set the Renderer for drawing on the GLSurfaceView
	        setRenderer(new ScaleGLRenderer());
			
			// Render the view only when there is a change in the drawing data
	        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
		
	}

}

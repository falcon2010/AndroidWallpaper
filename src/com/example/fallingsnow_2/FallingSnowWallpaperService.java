package com.example.fallingsnow_2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.//////////////Log;
import android.view.SurfaceHolder;

public class FallingSnowWallpaperService extends WallpaperService {

	int x, y, z , x1 , y1;
	private boolean First;

	public void onCreate() {
		super.onCreate();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	class MyWallpaperEngine extends Engine {

		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};
		private boolean visible = true;
		public Bitmap image1, backgroundImage , image2;
		

		MyWallpaperEngine() {
			// get the fish and background image references
			image1 = BitmapFactory.decodeResource(getResources(),
					R.drawable.fish);
			backgroundImage = BitmapFactory.decodeResource(getResources(),
					R.drawable.water1);
			
			
			x = -130; // initialize x position
			y = 200; // initialize y position
			z = 50;
			//////////////Log.i("app", "object was created");
		}

		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);

			//////////////Log.i("app", "onCreated Method called");
		}

		@Override
		public void onVisibilityChanged(boolean visible) {

			//////////////Log.i("app", "onVisibilityChanged Method called with value :"	+ visible);
			this.visible = visible;
			// if screen wallpaper is visible then draw the image otherwise do
			// not draw
			if (visible) {
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			//////////////Log.i("app", "onSurfaceDestroyed Method was called");
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}

		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			//////////////Log.i("app", "onOffsetChanged Method was called");
			draw();
		}

		void draw() {

			//////////////Log.i("app", "draw Method was called");

			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {

				c = holder.lockCanvas();
				// clear the canvas
				c.drawColor(Color.BLACK);
				if (c != null) {

					// draw the background image
					// get the width of canvas
					int width = c.getWidth();

					// if x crosses the width means x has reached to right edge
					if (x > width + 50)
					{
						// assign initial value to start with
						z *= -1;
						image1 = fillBitmap(image1);
					}
						// x = -10- image1.getWidth();

					if (x < -width - 50) {

						image1 = fillBitmap(image1);
						z *= -1;
					}

					// change the x position/value by 1 pixel

					x += z;

					//////////////Log.i("app", "x : " + x);

					// draw background
					c.drawBitmap(backgroundImage, 0, 0, null);
					// draw the fish
					c.drawBitmap(image1, x, y, null);

				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			handler.removeCallbacks(drawRunner);
			if (visible) {
				handler.postDelayed(drawRunner, 10); // delay 10 mileseconds
			}

		}

		private Bitmap fillBitmap(Bitmap d) {

			
			Matrix m = new Matrix();
			m.preScale(-1, 1);
			Bitmap dst = Bitmap.createBitmap(d, 0, 0, d.getWidth(),
					d.getHeight(), m, false);
			dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
			return dst;
		}
	}

}

package com.lgvalle.beaufitulnews.utils;

import android.graphics.*;
import com.squareup.picasso.Transformation;

/**
 * Created by luis.gonzalez on 13/08/14.
 */
// enables hardware accelerated rounded corners
// original idea here : http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
public class TransformGradient implements Transformation {
	private final int radius;
	private final int margin;  // dp
	private Shader[] shaders;

	// radius is corner radii in dp
	// margin is the board in dp
	public TransformGradient(final int radius, final int margin) {
		this.radius = radius;
		this.margin = margin;
		this.shaders = new Shader[2];
	}

	@Override
	public Bitmap transform(final Bitmap source) {

		Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());

		shaders[0] = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		shaders[1] = new LinearGradient(0, source.getHeight()/2, 0, source.getHeight(), Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);
		ComposeShader composeShader = new ComposeShader(shaders[0], shaders[1], PorterDuff.Mode.DST_OUT);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(composeShader);

		Canvas canvas = new Canvas(bitmap);
		canvas.drawPaint(paint);


		if (source != bitmap) {
			source.recycle();
		}

		return bitmap;
	}

	@Override
	public String key() {
		return "rounded";
	}
}

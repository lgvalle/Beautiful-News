package com.lgvalle.beaufitulnews.utils;

import android.graphics.*;
import com.squareup.picasso.Transformation;

/**
 * Created by luis.gonzalez on 13/08/14.
 */
// enables hardware accelerated rounded corners
// original idea here : http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
public class TransformRound implements Transformation {
	private final int radius;
	private final int margin;  // dp

	// radius is corner radii in dp
	// margin is the board in dp
	public TransformRound(final int radius, final int margin) {
		this.radius = radius;
		this.margin = margin;
	}

	@Override
	public Bitmap transform(final Bitmap source) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

		Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);

		if (source != output) {
			source.recycle();
		}

		return output;
	}

	@Override
	public String key() {
		return "rounded";
	}
}

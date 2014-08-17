package com.lgvalle.beaufitulnews.utils;

import android.graphics.*;
import com.squareup.picasso.Transformation;

/**
 * Created by luis.gonzalez on 13/08/14.
 */
// enables hardware accelerated rounded corners
// original idea here : http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
public class TransformGradient implements Transformation {


	@Override
	public Bitmap transform(final Bitmap source) {

		Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());

		BitmapShader bitmapShader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		LinearGradient gradientShader = new LinearGradient(0, source.getHeight() / 2, 0, source.getHeight(), Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);
		ComposeShader composeShader = new ComposeShader(bitmapShader, gradientShader, PorterDuff.Mode.SRC_OVER);

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
		return "gradient";
	}
}

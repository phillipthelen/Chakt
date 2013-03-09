package net.pherth.chakt;

import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorSet;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.internal.nineoldandroids.animation.ValueAnimator;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * Displays image with "fade in" animation
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.6.4
 */
public class SlideInBitmapDisplayer implements BitmapDisplayer {

	private final int durationMillis;
	private final Context context;

	public SlideInBitmapDisplayer(int durationMillis, Context context) {
		this.durationMillis = durationMillis;
		this.context = context;
	}

	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView) {
		imageView.setImageBitmap(bitmap);

		animate(imageView, durationMillis, context);

		return bitmap;
	}

	/**
	 * Animates {@link ImageView} with "fade-in" effect
	 * 
	 * @param imageView {@link ImageView} which display image in
	 * @param durationMillis The length of the animation in milliseconds
	 */
	public static void animate(ImageView imageView, int durationMillis, Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();;
		Integer height = (int) (display.getWidth() * 0.56);
		imageView.setScaleType(ScaleType.FIT_XY);
		ValueAnimator heightanimator = ValueAnimator.ofObject(new HeightEvaluator(imageView), 0, height);
		heightanimator.setDuration(durationMillis);
		heightanimator.start();
		
	}

}
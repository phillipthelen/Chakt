package net.pherth.chakt;

import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.internal.nineoldandroids.animation.IntEvaluator;

class HeightEvaluator extends IntEvaluator {

    private View v;
    public HeightEvaluator(View v) {
        this.v = v;
    }

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int num = (Integer)super.evaluate(fraction, startValue, endValue);
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = num;
        v.setLayoutParams(params);
        return num;
    }
}
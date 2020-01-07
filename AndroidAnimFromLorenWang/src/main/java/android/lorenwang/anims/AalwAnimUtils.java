package android.lorenwang.anims;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 功能作用：动画单例
 * 创建时间：2020-01-07 13:48
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AalwAnimUtils {
    private final String TAG = getClass().getName();
    private static volatile AalwAnimUtils optionsInstance;
    /**
     * 平移动画
     */
    private final int ANIM_TYPE_TRANSLATE = 0;
    /**
     * 缩放动画
     */
    private final int ANIM_TYPE_SCALE = 1;
    /**
     * 旋转动画
     */
    private final int ANIM_TYPE_ROTATE = 2;
    /**
     * 透明动画
     */
    private final int ANIM_TYPE_ALPHA = 3;

    private AalwAnimUtils() {
    }

    public static AalwAnimUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AalwAnimUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AalwAnimUtils();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 开启y轴平移动画
     *
     * @param from     起始y轴坐标
     * @param to       目标y轴坐标
     * @param duration 动画时间
     * @return 动画
     */
    public Animation startTranslateYAnim(View view, float from, float to, long duration) {
        return startTranslateAnimation(view, 0, from, 0, to, duration, null);
    }

    /**
     * 开启x轴平移动画
     *
     * @param from     起始x轴坐标
     * @param to       目标x轴坐标
     * @param duration 动画时间
     * @return 动画
     */
    public Animation startTranslateXAnim(View view, float from, float to, long duration) {
        return startTranslateAnimation(view, from, 0, to, 0, duration, null);
    }

    /**
     * 开启平移动画
     *
     * @param view         执行动画的view
     * @param fromX        动画起点X
     * @param fromY        动画起点Y
     * @param toX          动画结束点X
     * @param toY          动画结束点Y
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画实体
     */
    public Animation startTranslateAnimation(View view, float fromX, float fromY, float toX, float toY, long duration, Interpolator interpolator) {
        return startAnimation(ANIM_TYPE_TRANSLATE, view, fromX, fromY, toX, toY, duration, interpolator);
    }

    /**
     * 开启y轴缩放动画
     *
     * @param from     起始y轴坐标
     * @param to       目标y轴坐标
     * @param duration 动画时间
     * @return 动画
     */
    public Animation startScaleYAnim(View view, float from, float to, long duration) {
        return startScaleAnimation(view, 0, from, 0, to, duration, null);
    }

    /**
     * 开启x轴缩放动画
     *
     * @param from     起始x轴坐标
     * @param to       目标x轴坐标
     * @param duration 动画时间
     * @return 动画
     */
    public Animation startScaleXAnim(View view, float from, float to, long duration) {
        return startScaleAnimation(view, from, 0, to, 0, duration, null);
    }

    /**
     * 开启缩放动画
     *
     * @param view         执行动画的view
     * @param fromX        动画起点X
     * @param fromY        动画起点Y
     * @param toX          动画结束点X
     * @param toY          动画结束点Y
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画实体
     */
    public Animation startScaleAnimation(View view, float fromX, float fromY, float toX, float toY, long duration, Interpolator interpolator) {
        return startAnimation(ANIM_TYPE_SCALE, view, fromX, fromY, toX, toY, duration, interpolator);
    }

    /**
     * 开启旋转动画
     *
     * @param view        旋转view
     * @param fromDegrees 旋转动画起始旋转角度
     * @param toDegrees   旋转动画结束选中角度
     * @param duration    动画执行时间
     * @return 动画执行的anim
     */
    public Animation startRotateAnimation(View view, float fromDegrees, float toDegrees, long duration) {
        return startRotateAnimation(view, fromDegrees, toDegrees, 0, 0, duration, null);
    }

    /**
     * 开启旋转动画
     *
     * @param view         旋转view
     * @param fromDegrees  旋转动画起始旋转角度
     * @param toDegrees    旋转动画结束选中角度
     * @param pivotX       旋转动画旋转点X
     * @param pivotY       旋转动画旋转点Y
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画执行的anim
     */
    public Animation startRotateAnimation(View view, float fromDegrees, float toDegrees, float pivotX, float pivotY, long duration, Interpolator interpolator) {
        return startAnimation(ANIM_TYPE_ROTATE, view, fromDegrees, toDegrees, pivotX, pivotY, duration, interpolator);
    }

    /**
     * 开启透明动画
     *
     * @param view      旋转view
     * @param fromAlpha 旋转动画起始旋转角度
     * @param toAlpha   旋转动画结束选中角度
     * @param duration  动画执行时间
     * @return 动画执行的anim
     */
    public Animation startAlphaAnimation(View view, float fromAlpha, float toAlpha, long duration) {
        return startAlphaAnimation(view, fromAlpha, toAlpha, duration, null);
    }

    /**
     * 开启透明动画
     *
     * @param view         旋转view
     * @param fromAlpha    旋转动画起始旋转角度
     * @param toAlpha      旋转动画结束选中角度
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画执行的anim
     */
    public Animation startAlphaAnimation(View view, float fromAlpha, float toAlpha, long duration, Interpolator interpolator) {
        return startAnimation(ANIM_TYPE_ALPHA, view, fromAlpha, toAlpha, 0, 0, duration, interpolator);
    }

    /**
     * 开启执行动画
     *
     * @param animType     执行动画类型
     * @param view         动画view
     * @param value1       动画起始点X、旋转动画起始旋转角度、透明动画的起始透明度
     * @param value2       东湖起始点Y、旋转动画结束选中角度、透明动画的结束透明度
     * @param value3       动画结束的X、旋转动画旋转点X
     * @param value4       动画结束点Y、旋转动画旋转点Y
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画执行的anim
     */
    private Animation startAnimation(int animType, View view, float value1, float value2, float value3, float value4, long duration, Interpolator interpolator) {
        Animation animation = null;
        switch (animType) {
            case ANIM_TYPE_TRANSLATE:
                animation = new TranslateAnimation(value1, value3, value2, value4);
                break;
            case ANIM_TYPE_SCALE:
                animation = new ScaleAnimation(value1, value3, value2, value4);
                break;
            case ANIM_TYPE_ROTATE:
                animation = new RotateAnimation(value1, value2, value3, value4);
                break;
            case ANIM_TYPE_ALPHA:
                animation = new AlphaAnimation(value1, value2);
                break;
            default:
                break;
        }
        if (animation != null) {
            animation.setDuration(duration);
            if (interpolator != null) {
                animation.setInterpolator(interpolator);
            }
            if (view != null) {
                view.startAnimation(animation);
            }
        }
        return animation;
    }

    /**
     * 获取x轴平移动画
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getTranslateYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(from, to, duration, interpolator, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                view.setTranslationY(distance);
            }
        });
    }

    /**
     * 获取y轴平移动画
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getTranslateXAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(from, to, duration, interpolator, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float distance = (float) animation.getAnimatedValue();
                view.setTranslationX(distance);
            }
        });
    }

    /**
     * 获取缩放动画
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getScaleAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(from, to, duration, interpolator, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                view.setScaleX(scale);
                view.setScaleY(scale);
            }
        });
    }

    /**
     * 获取x轴缩放动画
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getScaleXAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(from, to, duration, interpolator, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                view.setScaleX(scale);
            }
        });
    }

    /**
     * 获取y轴缩放动画
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getScaleYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(from, to, duration, interpolator, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                view.setScaleY(scale);
            }
        });
    }

    /**
     * 获取旋转动画
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getRotateAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(from, to, duration, interpolator, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rotate = (float) animation.getAnimatedValue();
                view.setRotation(rotate);
            }
        });
    }

    /**
     * 获取透明动画
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getAlphaAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(from, to, duration, interpolator, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                view.setAlpha(alpha);
            }
        });
    }

    /**
     * 获取动画
     *
     * @param from         起始
     * @param to           结束
     * @param duration     动画时间
     * @return 动画实体
     */
    public ValueAnimator getValueAnimator(float from, float to, long duration) {
        return getValueAnimator(from, to, duration,null,null);
    }


    /**
     * 获取动画
     *
     * @param from         起始
     * @param to           结束
     * @param duration     动画时间
     * @param interpolator 拦截器
     * @param listener     监听
     * @return 动画实体
     */
    private ValueAnimator getValueAnimator(float from, float to, long duration, Interpolator interpolator, ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator animator = ObjectAnimator.ofFloat(from, to).setDuration(duration);
        if (interpolator != null) {
            animator.setInterpolator(interpolator);
        }
        if (listener != null) {
            animator.addUpdateListener(listener);
        }
        return animator;
    }


}

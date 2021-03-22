package android.lorenwang.anims;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 功能作用：动画单例
 * 初始注释时间： 2021/3/22 09:54
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 开启y轴平移动画--startTranslateYAnim(view, from, to, duration)
 * 开启x轴平移动画--startTranslateXAnim(view, from, to, duration)
 * 开启平移动画--startTranslateAnimation(View view,fromX,fromY,toX,toY,duration,interpolator)
 * 开启y轴缩放动画--startScaleYAnim(view,from,to,duration)
 * 开启x轴缩放动画--startScaleXAnim(view,from,to,duration)
 * 开启缩放动画--startScaleAnimation(view,fromX,fromY,toX,toY,duration,interpolator)
 * 开启旋转动画--startRotateAnimation(view,fromDegrees,toDegrees,duration)
 * 开启旋转动画--startRotateAnimation(view,fromDegrees,toDegrees,pivotX,pivotY,duration,interpolator)
 * 开启透明动画--startAlphaAnimation(view,fromAlpha,toAlpha,duration)
 * 开启透明动画--startAlphaAnimation(view,fromAlpha,toAlpha,duration,interpolator)
 * 获取x轴平移动画（属性动画）--getTranslateYAnimator(view,from,to,duration,interpolator)
 * 获取y轴平移动画（属性动画）--getTranslateXAnimator(view,from,to,duration,interpolator)
 * 获取xy轴平移动画（属性动画）--getTranslateAnimator(float fromX,toX,fromY,toY,duration,interpolator)
 * 获取x轴缩放动画（属性动画）--getScaleXAnimator(view,from,to,duration,interpolator)
 * 获取y轴缩放动画（属性动画）--getScaleYAnimator(view,from,to,duration,interpolator)
 * 获取缩放动画（属性动画）--getScaleAnimator(float fromX,toX,fromY,toY,pivotX,pivotY,duration,interpolator)
 * 获取X轴旋转动画（属性动画）--getRotateXAnimator(view,from,to,duration,interpolator)
 * 获取Y轴旋转动画（属性动画）--getRotateYAnimator(view,from,to,duration,interpolator)
 * 获取旋转动画（属性动画）--getRotateAnimator(view,fromDegrees,toDegrees,duration,interpolator)
 * 获取透明动画（属性动画）--getAlphaAnimator(view,from,to,duration,interpolator)
 * 获取动画（属性动画）--getValueAnimator(float from,to,duration)
 * 开启翻转动画（属性动画）--startFlipAnim(viewFront, viewBack,duration,isX,useAlpha,interpolator)
 * 获取动画（属性动画）--getValueAnimator(view, String property,from,to,duration,interpolator,listener)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AalwAnimUtil {
    private static volatile AalwAnimUtil optionsInstance;
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

    private AalwAnimUtil() {
    }

    public static AalwAnimUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AalwAnimUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AalwAnimUtil();
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
        return startScaleAnimation(view, 1, from, 1, to, duration, null);
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
        return startScaleAnimation(view, from, 1, to, 1, duration, null);
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
    public Animation startRotateAnimation(View view, float fromDegrees, float toDegrees, float pivotX, float pivotY, long duration,
            Interpolator interpolator) {
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
     * @param value2       动画起始点Y、旋转动画结束选中角度、透明动画的结束透明度
     * @param value3       动画结束的X、旋转动画旋转点X
     * @param value4       动画结束点Y、旋转动画旋转点Y
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画执行的anim
     */
    private Animation startAnimation(int animType, View view, float value1, float value2, float value3, float value4, long duration,
            Interpolator interpolator) {
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
     * 获取x轴平移动画（属性动画）
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getTranslateYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(view, "TranslationY", from, to, duration, interpolator, null);
    }

    /**
     * 获取y轴平移动画（属性动画）
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getTranslateXAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(view, "TranslationX", from, to, duration, interpolator, null);
    }

    /**
     * 获取xy轴平移动画（属性动画）
     *
     * @param fromX        动画起点X
     * @param fromY        动画起点Y
     * @param toX          动画结束点X
     * @param toY          动画结束点Y
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画
     */
    public AnimationSet getTranslateAnimator(float fromX, float toX, float fromY, float toY, long duration, Interpolator interpolator) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(interpolator);
        //添加动画
        animationSet.addAnimation(new TranslateAnimation(fromX, toX, fromY, toY));
        //设置插值器
        animationSet.setInterpolator(interpolator);
        //设置动画持续时长
        animationSet.setDuration(duration);
        //设置动画结束之后是否保持动画的目标状态
        animationSet.setFillAfter(true);
        //设置动画结束之后是否保持动画开始时的状态
        animationSet.setFillBefore(false);
        return animationSet;
    }

    /**
     * 获取x轴缩放动画（属性动画）
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getScaleXAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(view, "ScaleX", from, to, duration, interpolator, null);
    }

    /**
     * 获取y轴缩放动画（属性动画）
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getScaleYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(view, "ScaleY", from, to, duration, interpolator, null);
    }

    /**
     * 获取缩放动画（属性动画）
     *
     * @param fromX        动画起点X
     * @param fromY        动画起点Y
     * @param toX          动画结束点X
     * @param toY          动画结束点Y
     * @param pivotX       动画中心点X
     * @param pivotY       动画中心点Y
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画
     */
    public AnimationSet getScaleAnimator(float fromX, float toX, float fromY, float toY, float pivotX, float pivotY, long duration,
            Interpolator interpolator) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(interpolator);
        //添加动画
        animationSet.addAnimation(new ScaleAnimation(fromX, toX, fromY, toY, pivotX, pivotY));
        //设置插值器
        animationSet.setInterpolator(interpolator);
        //设置动画持续时长
        animationSet.setDuration(duration);
        //设置动画结束之后是否保持动画的目标状态
        animationSet.setFillAfter(true);
        //设置动画结束之后是否保持动画开始时的状态
        animationSet.setFillBefore(false);
        return animationSet;
    }

    /**
     * 获取X轴旋转动画（属性动画）
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getRotateXAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(view, "RotationX", from, to, duration, interpolator, null);
    }

    /**
     * 获取Y轴旋转动画（属性动画）
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getRotateYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(view, "RotationY", from, to, duration, interpolator, null);
    }

    /**
     * 获取旋转动画（属性动画）
     *
     * @param fromDegrees  动画起点角度
     * @param toDegrees    动画结束角度
     * @param duration     动画执行时间
     * @param interpolator 动画拦截器
     * @return 动画
     */
    public ValueAnimator getRotateAnimator(final View view, float fromDegrees, float toDegrees, long duration, Interpolator interpolator) {
        return getValueAnimator(view, "rotation", fromDegrees, toDegrees, duration, interpolator, null);
    }

    /**
     * 获取透明动画（属性动画）
     *
     * @param view         视图控件
     * @param from         起始位置
     * @param to           结束位置
     * @param duration     时间
     * @param interpolator 拦截器
     * @return 动画
     */
    public ValueAnimator getAlphaAnimator(final View view, float from, float to, long duration, Interpolator interpolator) {
        return getValueAnimator(view, "alpha", from, to, duration, interpolator, null);
    }

    /**
     * 获取动画（属性动画）
     *
     * @param from     起始
     * @param to       结束
     * @param duration 动画时间
     * @return 动画实体
     */
    public ValueAnimator getValueAnimator(float from, float to, long duration) {
        return getValueAnimator(null, null, from, to, duration, null, null);
    }

    /**
     * 开启翻转动画（属性动画）
     *
     * @param viewFront    动画正面
     * @param viewBack     动画反面
     * @param duration     动画时间
     * @param isX          是否是x轴翻转，否则的话就是y轴翻转
     * @param useAlpha     是否使用透明
     * @param interpolator 拦截处理
     */
    public void startFlipAnim(View viewFront, View viewBack, long duration, boolean isX, boolean useAlpha, Interpolator interpolator) {
        AnimatorSet animatorSet = new AnimatorSet();
        //xy翻转判断处理
        if (isX) {
            animatorSet.play(getRotateXAnimator(viewFront, 0F, 180F, duration, interpolator)).with(
                    getRotateXAnimator(viewBack, -180F, 0F, duration, interpolator));
        } else {
            animatorSet.play(getRotateYAnimator(viewFront, 0F, 180F, duration, interpolator)).with(
                    getRotateYAnimator(viewBack, -180F, 0F, duration, interpolator));
        }
        //透明处理
        if (useAlpha) {
            animatorSet.play(getAlphaAnimator(viewFront, 1F, 0F, duration, interpolator)).with(
                    getAlphaAnimator(viewBack, 0F, 1F, duration, interpolator));
        }
        animatorSet.start();
    }


    /**
     * 获取动画（属性动画）
     *
     * @param from         起始
     * @param to           结束
     * @param duration     动画时间
     * @param interpolator 拦截器
     * @param listener     监听
     * @return 动画实体
     */
    public ValueAnimator getValueAnimator(View view, String property, float from, float to, long duration, Interpolator interpolator,
            ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator animator = ObjectAnimator.ofFloat(view, property, from, to);
        if (interpolator != null) {
            animator.setInterpolator(interpolator);
        }
        if (listener != null) {
            animator.addUpdateListener(listener);
        }
        animator.setDuration(duration);
        return animator;
    }


}

安卓动画相关框架库


<h3>AalwAnimUtil | -(动画单例)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 每天的时间毫秒数 | DAY_TIME_MILLISECOND | 无（字段为常量） |
| 开启y轴平移动画 | startTranslateYAnim(View view, float from, float to, long duration) | Animation |
| 开启x轴平移动画 | startTranslateXAnim(View view, float from, float to, long duration) | Animation |
| 开启平移动画 | startTranslateAnimation(View view, float fromX, float fromY, float toX, float toY, long duration, Interpolator interpolator) | Animation |
| 开启y轴缩放动画 | startScaleYAnim(View view, float from, float to, long duration) | Animation |
| 开启x轴缩放动画 | startScaleXAnim(View view, float from, float to, long duration) | Animation |
| 开启缩放动画 | startScaleAnimation(View view, float fromX, float fromY, float toX, float toY, long duration, Interpolator interpolator) | Animation |
| 开启旋转动画 | startRotateAnimation(View view, float fromDegrees, float toDegrees, long duration) | Animation |
| 开启旋转动画 | startRotateAnimation(View view, float fromDegrees, float toDegrees, float pivotX, float pivotY, long duration, Interpolator interpolator) | Animation |
| 开启透明动画 | startAlphaAnimation(View view, float fromAlpha, float toAlpha, long duration) | Animation |
| 开启透明动画 | startAlphaAnimation(View view, float fromAlpha, float toAlpha, long duration, Interpolator interpolator) | Animation |
| 获取x轴平移动画（属性动画） | getTranslateXAnimator(final View view, float from, float to, long duration, Interpolator interpolator) | ValueAnimator |
| 获取y轴平移动画（属性动画） | getTranslateYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) | ValueAnimator |
| 获取xy轴平移动画（属性动画） | getTranslateAnimator(float fromX, float toX, float fromY, float toY, long duration, Interpolator interpolator) | AnimationSet |
| 获取x轴缩放动画（属性动画） | getScaleXAnimator(final View view, float from, float to, long duration, Interpolator interpolator) | ValueAnimator |
| 获取y轴缩放动画（属性动画） | getScaleYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) | ValueAnimator |
| 获取缩放动画（属性动画） | getScaleYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) | AnimationSet |
| 获取X轴旋转动画（属性动画） | getRotateXAnimator(final View view, float from, float to, long duration, Interpolator interpolator) | ValueAnimator |
| 获取Y轴旋转动画（属性动画） | getRotateYAnimator(final View view, float from, float to, long duration, Interpolator interpolator) | ValueAnimator |
| 获取旋转动画（属性动画） | getRotateAnimator(final View view, float fromDegrees, float toDegrees, long duration, Interpolator interpolator) | ValueAnimator |
| 获取透明动画（属性动画） | getAlphaAnimator(final View view, float from, float to, long duration, Interpolator interpolator) | ValueAnimator |
| 获取动画（属性动画） | getValueAnimator(float from, float to, long duration) | ValueAnimator |
| 开启翻转动画（属性动画） | startFlipAnim(View viewFront, View viewBack, long duration, boolean isX, boolean useAlpha, Interpolator interpolator) | void |
| 获取动画（属性动画） | getValueAnimator(View view, String property, float from, float to, long duration, Interpolator interpolator, ValueAnimator.AnimatorUpdateListener listener) | ValueAnimator |

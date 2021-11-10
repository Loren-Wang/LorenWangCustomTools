package android.lorenwang.customview.imageview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.FloatRange;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 功能作用：热区图片控件
 * 创建时间：2020-11-11 3:25 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AvlwImageHotSpotsView extends AppCompatImageView {
    /**
     * 热区数据
     */
    private final Map<Integer, ImageHotSpotsInfo> locationMap = new ConcurrentHashMap<>();

    /**
     * 热区点击
     */
    private HotSpotsOnClick hotSpotsOnClick;

    /**
     * 上一次的点击时间
     */
    private long lastClickTime = 0;

    /**
     * 点击时间间隔
     */
    private final long CLICK_TIME_INTERVAL = 1000L;

    /**
     * 上一次记录的宽度
     */
    private int lastWidth = 0;

    /**
     * 上一次记录的高度
     */
    private int lastHeight = 0;

    public AvlwImageHotSpotsView(Context context) {
        super(context);
    }

    public AvlwImageHotSpotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvlwImageHotSpotsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //同步代码块处理数据信息
        if (lastWidth != getWidth() && lastHeight != getHeight()) {
            synchronized (ImageHotSpotsInfo.class) {
                lastWidth = getWidth();
                lastHeight = getHeight();
                //初始化信息
                for (ImageHotSpotsInfo item : locationMap.values()) {
                    item.rect = new Rect(
                            (int) (item.getLeftTopPercentX() * lastWidth),
                            (int) (item.getLeftTopPercentY() * lastHeight),
                            (int) ((item.getLeftTopPercentX() + item.getWidthPercent()) * lastWidth),
                            (int) ((item.getLeftTopPercentY() + item.getHeightPercent()) * lastHeight)
                    );
                    locationMap.put(item.hashCode(), item);
                }
            }
        }
        if (hotSpotsOnClick != null && isEnabled() && System.currentTimeMillis() - lastClickTime > CLICK_TIME_INTERVAL) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float upX = event.getX();
                float upY = event.getY();
                //手拿起
                Iterator<ImageHotSpotsInfo> iterator = locationMap.values().iterator();
                ImageHotSpotsInfo bean;
                while (iterator.hasNext()) {
                    bean = iterator.next();
                    if (bean != null && bean.rect != null &&
                            upX > bean.rect.left && upX <= bean.rect.right &&
                            upY > bean.rect.top && upY <= bean.rect.bottom) {
                        lastClickTime = System.currentTimeMillis();
                        //查找到点击区域，执行点击回调
                        hotSpotsOnClick.onClick(this, bean);
                        return true;
                    }
                }
            }
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 设置热区数据
     *
     * @param list            热区数据列表
     * @param hotSpotsOnClick 热区点击事件
     * @param <T>             泛型数据
     */
    public <T> void setHotSpotsData(ArrayList<ImageHotSpotsInfo<T>> list, HotSpotsOnClick<T> hotSpotsOnClick) {
        this.hotSpotsOnClick = hotSpotsOnClick;
        if (list != null) {
            for (ImageHotSpotsInfo item : list) {
                locationMap.put(item.hashCode(), item);
            }
        }
    }

    /**
     * 图片热区所使用的参数
     */
    public static class ImageHotSpotsInfo<T> {
        /**
         * 左上角x占图片百分比
         */
        private float leftTopPercentX = 0F;
        /**
         * 左上角y占图片百分比
         */
        private float leftTopPercentY = 0F;
        /**
         * 区域占图片宽度百分比
         */
        private float widthPercent = 0F;
        /**
         * 区域占图片高度百分比
         */
        private float heightPercent = 0F;
        /**
         * 范围，内部转换后使用的
         */
        private Rect rect;
        /**
         * 数据
         */
        private T data;

        public ImageHotSpotsInfo(@FloatRange(from = 0, to = 1) float leftTopPercentX, @FloatRange(from = 0, to = 1) float leftTopPercentY,
                @FloatRange(from = 0, to = 1) float widthPercent, @FloatRange(from = 0, to = 1) float heightPercent, T data) {
            this.leftTopPercentX = leftTopPercentX;
            this.leftTopPercentY = leftTopPercentY;
            this.widthPercent = widthPercent;
            this.heightPercent = heightPercent;
            this.data = data;
        }

        public ImageHotSpotsInfo() {
        }

        public ImageHotSpotsInfo(@FloatRange(from = 0, to = 1) float leftTopPercentX, @FloatRange(from = 0, to = 1) float leftTopPercentY,
                @FloatRange(from = 0, to = 1) float widthPercent, @FloatRange(from = 0, to = 1) float heightPercent) {
            this.leftTopPercentX = leftTopPercentX;
            this.leftTopPercentY = leftTopPercentY;
            this.widthPercent = widthPercent;
            this.heightPercent = heightPercent;
        }

        public void setData(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public float getLeftTopPercentX() {
            return leftTopPercentX;
        }

        public void setLeftTopPercentX(@FloatRange(from = 0, to = 1) float leftTopPercentX) {
            this.leftTopPercentX = leftTopPercentX;
        }

        public float getLeftTopPercentY() {
            return leftTopPercentY;
        }

        public void setLeftTopPercentY(@FloatRange(from = 0, to = 1) float leftTopPercentY) {
            this.leftTopPercentY = leftTopPercentY;
        }

        public float getWidthPercent() {
            return widthPercent;
        }

        public void setWidthPercent(@FloatRange(from = 0, to = 1) float widthPercent) {
            this.widthPercent = widthPercent;
        }

        public float getHeightPercent() {
            return heightPercent;
        }

        public void setHeightPercent(@FloatRange(from = 0, to = 1) float heightPercent) {
            this.heightPercent = heightPercent;
        }
    }

    /**
     * 热区点击回调接口
     */
    public interface HotSpotsOnClick<T> {
        /**
         * 点击事件
         *
         * @param hotSpotsView 当前控件
         * @param data         数据
         */
        void onClick(AvlwImageHotSpotsView hotSpotsView, ImageHotSpotsInfo<T> data);
    }
}


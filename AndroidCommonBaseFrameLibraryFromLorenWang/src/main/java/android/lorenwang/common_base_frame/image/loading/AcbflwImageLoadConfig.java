package android.lorenwang.common_base_frame.image.loading;

import android.graphics.Bitmap;

import com.bumptech.glide.load.Transformation;

/**
 * 功能作用：图片加载配置类
 * 创建时间：2019-12-19 16:56
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwImageLoadConfig {

    private AcbflwImageLoadConfig() {
    }

    /**
     * 回调
     */
    private AcbflwImageLoadBitmapCallback callback;
    private boolean scaleTypeFitCenter = false;
    private boolean scaleTypeCenterGroup = false;
    private boolean scaleTypeCenterInside = false;
    private boolean scaleTypeFitStart = false;
    private boolean scaleTypeFitEnd = false;
    private boolean scaleTypeFocusCrop = false;
    private boolean scaleTypeCenter = false;
    private boolean scaleTypeFitXy = false;
    private boolean scaleTypeFitBottomStart = false;
    private Transformation<Bitmap>[] bitmapTransformations;

    public AcbflwImageLoadBitmapCallback getCallback() {
        return callback;
    }

    public Transformation<Bitmap>[] getBitmapTransformations() {
        return bitmapTransformations;
    }

    public boolean isScaleTypeFitCenter() {
        return scaleTypeFitCenter;
    }

    public boolean isScaleTypeCenterGroup() {
        return scaleTypeCenterGroup;
    }

    public boolean isScaleTypeCenterInside() {
        return scaleTypeCenterInside;
    }

    public boolean isScaleTypeFitStart() {
        return scaleTypeFitStart;
    }

    public boolean isScaleTypeFitEnd() {
        return scaleTypeFitEnd;
    }

    public boolean isScaleTypeFocusCrop() {
        return scaleTypeFocusCrop;
    }

    public boolean isScaleTypeCenter() {
        return scaleTypeCenter;
    }

    public boolean isScaleTypeFitXy() {
        return scaleTypeFitXy;
    }

    public boolean isScaleTypeFitBottomStart() {
        return scaleTypeFitBottomStart;
    }

    public static class Build {
        /**
         * 回调
         */
        private AcbflwImageLoadBitmapCallback callback;
        private boolean scaleTypeFitCenter = false;
        private boolean scaleTypeCenterGroup = false;
        private boolean scaleTypeCenterInside = false;
        private boolean scaleTypeFitStart = false;
        private boolean scaleTypeFitEnd = false;
        private boolean scaleTypeFocusCrop = false;
        private boolean scaleTypeCenter = false;
        private boolean scaleTypeFitXy = false;
        private boolean scaleTypeFitBottomStart = false;
        private Transformation<Bitmap>[] bitmapTransformations;

        public AcbflwImageLoadBitmapCallback getCallback() {
            return callback;
        }

        public Build setScaleTypeFitCenter(boolean scaleTypeFitCenter) {
            this.scaleTypeFitCenter = scaleTypeFitCenter;
            return this;
        }

        public Build setScaleTypeCenterGroup(boolean scaleTypeCenterGroup) {
            this.scaleTypeCenterGroup = scaleTypeCenterGroup;
            return this;
        }

        public Build setScaleTypeCenterInside(boolean scaleTypeCenterInside) {
            this.scaleTypeCenterInside = scaleTypeCenterInside;
            return this;
        }

        public Build setScaleTypeFitStart(boolean scaleTypeFitStart) {
            this.scaleTypeFitStart = scaleTypeFitStart;
            return this;
        }

        public Build setScaleTypeFitEnd(boolean scaleTypeFitEnd) {
            this.scaleTypeFitEnd = scaleTypeFitEnd;
            return this;
        }

        public Build setScaleTypeFocusCrop(boolean scaleTypeFocusCrop) {
            this.scaleTypeFocusCrop = scaleTypeFocusCrop;
            return this;
        }

        public Build setScaleTypeCenter(boolean scaleTypeCenter) {
            this.scaleTypeCenter = scaleTypeCenter;
            return this;
        }

        public Build setScaleTypeFitXy(boolean scaleTypeFitXy) {
            this.scaleTypeFitXy = scaleTypeFitXy;
            return this;
        }

        public Build setScaleTypeFitBottomStart(boolean scaleTypeFitBottomStart) {
            this.scaleTypeFitBottomStart = scaleTypeFitBottomStart;
            return this;
        }

        public Build setCallback(AcbflwImageLoadBitmapCallback callback) {
            this.callback = callback;
            return this;
        }

        public Build setBitmapTransformations(Transformation<Bitmap>[] bitmapTransformations) {
            this.bitmapTransformations = bitmapTransformations;
            return this;
        }

        public AcbflwImageLoadConfig build() {
            AcbflwImageLoadConfig config = new AcbflwImageLoadConfig();
            config.scaleTypeFitCenter = this.scaleTypeFitCenter;
            config.scaleTypeCenterGroup = this.scaleTypeCenterGroup;
            config.scaleTypeCenterInside = this.scaleTypeCenterInside;
            config.scaleTypeFitStart = this.scaleTypeFitStart;
            config.scaleTypeFitEnd = this.scaleTypeFitEnd;
            config.scaleTypeFocusCrop = this.scaleTypeFocusCrop;
            config.bitmapTransformations = this.bitmapTransformations;
            config.scaleTypeCenter = this.scaleTypeCenter;
            config.scaleTypeFitXy = this.scaleTypeFitXy;
            config.scaleTypeFitBottomStart = this.scaleTypeFitBottomStart;
            return config;
        }
    }
}

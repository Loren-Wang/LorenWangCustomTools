package android.lorenwang.commonbaseframe.image;

import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.provider.MediaStore;

import com.luck.picture.lib.entity.LocalMedia;

/**
 * 功能作用：本地图片选择实例
 * 创建时间：2019-12-17 17:02
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwLocalImageSelectBean extends LocalMedia {
    /**
     * x轴偏转
     */
    private int offsetX = 0;
    /**
     * y轴偏转
     */
    private int offsetY = 0;
    /**
     * 裁剪旋转比例
     */
    private float cropAspectRatio = 0;

    @Override
    public String getPath() {
        String path = super.getPath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setPath(AtlwFileOptionUtils.getInstance().getUriPath(Uri.parse(path), MediaStore.Images.ImageColumns.DATA));
        }
        return super.getPath();
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public float getCropAspectRatio() {
        return cropAspectRatio;
    }

    public void setCropAspectRatio(float cropAspectRatio) {
        this.cropAspectRatio = cropAspectRatio;
    }

    public AcbflwLocalImageSelectBean() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.offsetX);
        dest.writeInt(this.offsetY);
        dest.writeFloat(this.cropAspectRatio);
    }

    protected AcbflwLocalImageSelectBean(Parcel in) {
        super(in);
        this.offsetX = in.readInt();
        this.offsetY = in.readInt();
        this.cropAspectRatio = in.readFloat();
    }

    public static final Creator<AcbflwLocalImageSelectBean> CREATOR = new Creator<AcbflwLocalImageSelectBean>() {
        @Override
        public AcbflwLocalImageSelectBean createFromParcel(Parcel source) {
            return new AcbflwLocalImageSelectBean(source);
        }

        @Override
        public AcbflwLocalImageSelectBean[] newArray(int size) {
            return new AcbflwLocalImageSelectBean[size];
        }
    };
}

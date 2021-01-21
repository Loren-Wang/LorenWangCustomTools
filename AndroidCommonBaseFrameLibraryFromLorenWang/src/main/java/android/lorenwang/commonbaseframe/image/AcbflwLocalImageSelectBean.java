package android.lorenwang.commonbaseframe.image;

import android.lorenwang.tools.file.AtlwFileOptionUtil;
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
            setPath(AtlwFileOptionUtil.getInstance().getUriPath(Uri.parse(path), MediaStore.Images.ImageColumns.DATA));
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



    /**
     * 复制到基类数据类型
     *
     * @return 基类数据类型
     */
    public AcbflwLocalImageSelectBean copyToLocalCurrent(LocalMedia media) {
        this.setId(media.getId());
        this.setPath(media.getPath());
        this.setRealPath(media.getRealPath());
        this.setOriginalPath(media.getOriginalPath());
        this.setCompressPath(media.getCompressPath());
        this.setCutPath(media.getCutPath());
        this.setAndroidQToPath(media.getAndroidQToPath());
        this.setDuration(media.getDuration());
        this.setChecked(media.isChecked());
        this.setCut(media.isCut());
        this.position = media.position;
        this.setNum(media.getNum());
        this.setMimeType(media.getMimeType());
        this.setChooseModel(media.getChooseModel());
        this.setCompressed(media.isCompressed());
        this.setWidth(media.getWidth());
        this.setHeight(media.getHeight());
        this.setSize(media.getSize());
        this.setOriginal(media.isOriginal());
        this.setFileName(media.getFileName());
        this.setParentFolderName(media.getParentFolderName());
        this.setOrientation(media.getOrientation());
        this.loadLongImageStatus = media.loadLongImageStatus;
        this.isLongImage = media.isLongImage;
        this.setBucketId(media.getBucketId());
        this.setMaxSelectEnabledMask(media.isMaxSelectEnabledMask());
        return this;
    }

    /**
     * 复制到基类数据类型
     *
     * @return 基类数据类型
     */
    public LocalMedia copyToLocalMedia() {
        LocalMedia localMedia = new LocalMedia();
        localMedia.setId(super.getId());
        localMedia.setPath(super.getPath());
        localMedia.setRealPath(super.getRealPath());
        localMedia.setOriginalPath(super.getOriginalPath());
        localMedia.setCompressPath(super.getCompressPath());
        localMedia.setCutPath(super.getCutPath());
        localMedia.setAndroidQToPath(super.getAndroidQToPath());
        localMedia.setDuration(super.getDuration());
        localMedia.setChecked(super.isChecked());
        localMedia.setCut(super.isCut());
        localMedia.position = super.position;
        localMedia.setNum(super.getNum());
        localMedia.setMimeType(super.getMimeType());
        localMedia.setChooseModel(super.getChooseModel());
        localMedia.setCompressed(super.isCompressed());
        localMedia.setWidth(super.getWidth());
        localMedia.setHeight(super.getHeight());
        localMedia.setSize(super.getSize());
        localMedia.setOriginal(super.isOriginal());
        localMedia.setFileName(super.getFileName());
        localMedia.setParentFolderName(super.getParentFolderName());
        localMedia.setOrientation(super.getOrientation());
        localMedia.loadLongImageStatus = super.loadLongImageStatus;
        localMedia.isLongImage = super.isLongImage;
        localMedia.setBucketId(super.getBucketId());
        localMedia.setMaxSelectEnabledMask(super.isMaxSelectEnabledMask());
        return localMedia;
    }
}

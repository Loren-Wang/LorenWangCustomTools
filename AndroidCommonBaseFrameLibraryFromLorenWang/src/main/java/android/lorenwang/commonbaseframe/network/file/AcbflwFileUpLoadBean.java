package android.lorenwang.commonbaseframe.network.file;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 功能作用：文件请求类
 * 创建时间：2019-12-20 15:38
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwFileUpLoadBean implements Parcelable {
    /**
     * 文件地址
     */
    private String path;
    /**
     * 文件类型
     */
    private String mimeType;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.mimeType);
    }

    public AcbflwFileUpLoadBean() {
    }

    protected AcbflwFileUpLoadBean(Parcel in) {
        this.path = in.readString();
        this.mimeType = in.readString();
    }

    public static final Creator<AcbflwFileUpLoadBean> CREATOR = new Creator<AcbflwFileUpLoadBean>() {
        @Override
        public AcbflwFileUpLoadBean createFromParcel(Parcel source) {
            return new AcbflwFileUpLoadBean(source);
        }

        @Override
        public AcbflwFileUpLoadBean[] newArray(int size) {
            return new AcbflwFileUpLoadBean[size];
        }
    };
}

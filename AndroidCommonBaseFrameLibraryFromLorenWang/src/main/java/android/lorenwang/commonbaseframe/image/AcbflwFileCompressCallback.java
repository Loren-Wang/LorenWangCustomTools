package android.lorenwang.commonbaseframe.image;

import java.io.File;
import java.util.List;

/**
 * 功能作用：
 * 创建时间：2021-01-05 12:27 下午
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
public interface AcbflwFileCompressCallback {
    /**
     * 压缩成功
     * @param files 压缩后文件列表
     */
    void success(List<File> files);

    /**
     * 压缩失败
     */
    void fail();
}

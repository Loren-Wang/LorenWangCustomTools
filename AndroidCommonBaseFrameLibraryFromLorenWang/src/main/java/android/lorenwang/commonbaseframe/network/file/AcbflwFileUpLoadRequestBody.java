package android.lorenwang.commonbaseframe.network.file;

import android.lorenwang.commonbaseframe.network.callback.AcbflwNetOptionsByModelCallback;
import java.io.IOException;

import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * 功能作用：文件上传请求体
 * 创建时间：2019-12-20 14:43
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class AcbflwFileUpLoadRequestBody<D, T extends KttlwBaseNetResponseBean<D>> extends RequestBody {
    private final RequestBody requestBody;
    private final AcbflwNetOptionsByModelCallback<D,T> callback;
    private final AcbflwFileUpLoadBean bean;
    private BufferedSink bufferedSink;

    public AcbflwFileUpLoadRequestBody(RequestBody requestBody, AcbflwNetOptionsByModelCallback<D,T> callback, AcbflwFileUpLoadBean bean) {
        this.requestBody = requestBody;
        this.callback = callback;
        this.bean = bean;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(new ForwardingSink(sink) {
                /**
                 * 当前长度
                 */
                private long total = 0;
                /**
                 * 当前上传字节数
                 */
                private Long nowUpload = 0L;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    if (total == 0) {
                        total = contentLength();
                    }
                    if (nowUpload.compareTo(total) > 0) {
                        nowUpload += byteCount;
                    }
                    //回传进度
                    if(callback != null){
                        callback.fileUpLoadProcess(bean,total,nowUpload,nowUpload * 1.0 / total);
                    }
                }
            });
        }
    }
}

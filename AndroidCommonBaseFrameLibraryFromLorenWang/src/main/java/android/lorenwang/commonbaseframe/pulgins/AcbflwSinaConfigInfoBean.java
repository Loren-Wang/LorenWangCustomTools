package android.lorenwang.commonbaseframe.pulgins;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * 功能作用：微博配置信息
 * 创建时间：2019-12-31 14:32
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@Builder
@Getter
public class AcbflwSinaConfigInfoBean {
    private final String appKey;
    private final String redirectUrl;
    private final String scope;
}

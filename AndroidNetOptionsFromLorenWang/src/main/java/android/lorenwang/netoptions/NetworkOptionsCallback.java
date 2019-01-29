package android.lorenwang.netoptions;

/**
 * Created by LorenWang on 2018/7/23.
 * 创建时间：2018/7/23 下午 03:33
 * 创建人：王亮（Loren wang）
 * 功能作用：网络请求数据返回实体类框架
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public abstract class NetworkOptionsCallback {
    public abstract void successForData(String contentData, Object object);//非json数据返回
    public abstract void fail(int code, Object object);
    public abstract void error(int code, Object object);

    public void progress(int progress) {

    }
}

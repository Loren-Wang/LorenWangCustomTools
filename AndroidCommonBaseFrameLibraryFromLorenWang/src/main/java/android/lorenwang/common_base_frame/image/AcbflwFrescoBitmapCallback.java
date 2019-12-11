package android.lorenwang.common_base_frame.image;

/**
 * Created by zhaoyong on 16/4/18.
 *
 */
public interface AcbflwFrescoBitmapCallback<T> {

    void onSuccess(T result);

    void onFailure(Throwable throwable);

    void onCancel();
}

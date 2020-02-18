package android.lorenwang.common_base_frame.network

import io.reactivex.Observable
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import retrofit2.Response

/**
 * 功能作用：自定义的retrofit的Observable
 * 创建时间：2020-02-18 下午 15:58:29
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class AcbflwBaseRetrofitObservable<D, T : KttlwBaseNetResponseBean<D>> : Observable<Response<T>>()

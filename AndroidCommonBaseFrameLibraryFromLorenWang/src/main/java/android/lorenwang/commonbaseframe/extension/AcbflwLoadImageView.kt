package android.lorenwang.commonbaseframe.extension

import android.lorenwang.commonbaseframe.AcbflwBaseConfig.defaultImageLoadingLibrary
import android.lorenwang.commonbaseframe.image.loading.AcbflwImageLoadConfig
import android.lorenwang.commonbaseframe.image.loading.AcbflwImageLoadingFactory
import android.lorenwang.tools.AtlwConfig
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * 功能作用：图片加载扩展类
 * 创建时间：2020-09-14 10:38 上午
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


/**
 * 加载网络图片
 * @param urlPath 网络图片地址,可以直接为接口返回参数
 * @param emptyUrlGone 当地址为空时是否隐藏,也就是不为空要显示，默认情况下为空不处理
 * @param loadWidth 图片加载宽度
 * @param loadHeight 图片加载高度
 * @param config 图片加载配置信息
 */
fun ImageView?.acbflwLoadNetImageConfig(urlPath: String?, emptyUrlGone: Boolean = false, loadWidth: Int? = null, loadHeight: Int? = null,
    config: AcbflwImageLoadConfig?) {
    if (this != null && !urlPath.isNullOrEmpty()) {
        //显示控件
        this.visibility = View.VISIBLE
        val build = if (config == null) {
            AcbflwImageLoadConfig.Build()
        } else {
            config.copyBuild()
        }

        if (loadWidth != null && loadWidth > 0 || loadHeight != null && loadHeight > 0) {
            AcbflwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary)
                .loadingNetImage(urlPath, this, build.setShowViewHeight(loadHeight!!).setShowViewWidth(loadWidth!!).build())
        } else {
            AcbflwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary).loadingNetImage(urlPath, this, build.build())
        }
    } else {
        if (emptyUrlGone) {
            this?.visibility = View.GONE
        }
    }
}

/**
 * 加载网络图片
 * @param urlPath 网络图片地址,可以直接为接口返回参数
 * @param emptyUrlGone 当地址为空时是否隐藏,也就是不为空要显示，默认情况下为空不处理
 * @param build 图片加载配置信息
 */
fun ImageView?.acbflwLoadNetImageConfig(urlPath: String?, emptyUrlGone: Boolean = false,
    build: AcbflwImageLoadConfig.Build = AcbflwImageLoadConfig.Build()) {
    if (this != null && !urlPath.isNullOrEmpty()) {
        //显示控件
        this.visibility = View.VISIBLE
        val config: AcbflwImageLoadConfig = build.build()
        AcbflwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary).loadingNetImage(urlPath, this, config)
    } else {
        if (emptyUrlGone) {
            this?.visibility = View.GONE
        }
    }
}

/**
 * 加载网络图片
 * @param resId 资源图片地址,可以直接为接口返回参数
 * @param config 图片加载配置信息
 */
fun ImageView?.acbflwLoadResImageConfig(@DrawableRes resId: Int, config: AcbflwImageLoadConfig = AcbflwImageLoadConfig.Build().build()) {
    if (this != null) {
        AcbflwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary).loadingResImage(resId, this, config)
    }
}

/**
 * 加载网络图片
 * @param resId 资源图片地址,可以直接为接口返回参数
 * @param build 图片加载配置信息
 */
fun ImageView?.acbflwLoadResImageConfig(@DrawableRes resId: Int, build: AcbflwImageLoadConfig.Build = AcbflwImageLoadConfig.Build()) {
    if (this != null) {
        AcbflwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary).loadingResImage(resId, this, build.build())
    }
}

/**
 * 加载网络原始图片
 */
fun ImageView?.acbflwLoadNetOriginImageConfig(urlPath: String?, emptyUrlGone: Boolean = false,
    build: AcbflwImageLoadConfig.Build = AcbflwImageLoadConfig.Build()) {
    if (this != null && !urlPath.isNullOrEmpty()) {
        //显示控件
        this.visibility = View.VISIBLE
        val config: AcbflwImageLoadConfig = build.setUseOriginImage(true).build()
        AcbflwImageLoadingFactory.getImageLoading(AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_IMAGE_LOAD).loadingNetImage(urlPath, this, config)
    } else {
        if (emptyUrlGone) {
            this?.visibility = View.GONE
        }
    }
}

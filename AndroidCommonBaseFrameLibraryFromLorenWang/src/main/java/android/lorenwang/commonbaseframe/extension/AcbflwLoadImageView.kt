package android.lorenwang.commonbaseframe.extension

import android.lorenwang.commonbaseframe.AcbflwBaseConfig.defaultImageLoadingLibrary
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.image.loading.AtlwImageLoadConfig
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
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
fun <V : ImageView> V?.acbflwLoadNetImageConfig(urlPath: String?, emptyUrlGone: Boolean = false, loadWidth: Int? = null, loadHeight: Int? = null,
    config: AtlwImageLoadConfig?) {
    if (this != null && !urlPath.isNullOrEmpty()) {
        //显示控件
        this.visibility = View.VISIBLE
        val build = if (config == null) {
            AtlwImageLoadConfig.Build()
        } else {
            config.copyBuild()
        }

        if (loadWidth != null && loadWidth > 0 || loadHeight != null && loadHeight > 0) {
            AtlwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary)
                .loadingNetImage(urlPath, this, build.setShowViewHeight(loadHeight!!).setShowViewWidth(loadWidth!!).build())
        } else {
            AtlwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary).loadingNetImage(urlPath, this, build.build())
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
fun <V : ImageView> V?.acbflwLoadNetImageConfig(urlPath: String?, emptyUrlGone: Boolean = false,
    build: AtlwImageLoadConfig.Build = AtlwImageLoadConfig.Build()) {
    if (this != null && !urlPath.isNullOrEmpty()) {
        //显示控件
        this.visibility = View.VISIBLE
        val config: AtlwImageLoadConfig = build.build()
        AtlwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary).loadingNetImage(urlPath, this, config)
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
fun <V : ImageView> V?.acbflwLoadResImageConfig(@DrawableRes resId: Int, config: AtlwImageLoadConfig = AtlwImageLoadConfig.Build().build()) {
    if (this != null) {
        AtlwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary).loadingResImage(resId, this, config)
    }
}

/**
 * 加载网络图片
 * @param resId 资源图片地址,可以直接为接口返回参数
 * @param build 图片加载配置信息
 */
fun <V : ImageView> V?.acbflwLoadResImageConfig(@DrawableRes resId: Int, build: AtlwImageLoadConfig.Build = AtlwImageLoadConfig.Build()) {
    if (this != null) {
        AtlwImageLoadingFactory.getImageLoading(defaultImageLoadingLibrary).loadingResImage(resId, this, build.build())
    }
}

/**
 * 加载网络原始图片
 */
fun <V : ImageView> V?.acbflwLoadNetOriginImageConfig(urlPath: String?, emptyUrlGone: Boolean = false,
    build: AtlwImageLoadConfig.Build = AtlwImageLoadConfig.Build()) {
    if (this != null && !urlPath.isNullOrEmpty()) {
        //显示控件
        this.visibility = View.VISIBLE
        val config: AtlwImageLoadConfig = build.setUseOriginImage(true).build()
        AtlwImageLoadingFactory.getImageLoading(AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_IMAGE_LOAD).loadingNetImage(urlPath, this, config)
    } else {
        if (emptyUrlGone) {
            this?.visibility = View.GONE
        }
    }
}

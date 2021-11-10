package com.lorenwang.test.android.activity.customToolsAndroid.image

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.lorenwang.tools.image.AtlwImageCommonUtil
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.BuildConfig
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomToolsAndroidImageCommonBinding
import java.io.File

/**
 * 功能作用：图片处理工具类
 * 初始注释时间： 2021/10/7 17:08
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
class ImageCommonActivity : BaseActivity() {
    /**
     * 权限请求code
     */
    private val permissionRequestCode = 1

    /**
     * 页面布局
     */
    private lateinit var binding: ActivityCustomToolsAndroidImageCommonBinding

    /**
     * 操作的位图
     */
    private lateinit var optionsBitmap: Bitmap
    private lateinit var optionsDrawable: Drawable
    private lateinit var optionsBitmapAdd: Bitmap

    override fun initView(savedInstanceState: Bundle?) {
        binding = ActivityCustomToolsAndroidImageCommonBinding.inflate(layoutInflater)
        addShowContentView(true, binding)
        binding.btnInit.performClick()
    }

    fun mainClick(view: View?) {
        if (view != null) {
            binding.ivShow.background = null
            //app文件夹地址
            val appSystemStorageDirPath = AtlwFileOptionUtil.getInstance().getAppSystemStorageDirPath(BuildConfig.APPLICATION_ID)
            binding.tvShow.text = ""
            when (view.id) {
                R.id.btn_init -> {
                    AtlwActivityUtil.getInstance()
                        .goToRequestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                            permissionRequestCode, object : AtlwPermissionRequestCallback {
                                override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                    optionsBitmap = BitmapFactory.decodeResource(resources, R.drawable.image_default)
                                    optionsBitmapAdd = BitmapFactory.decodeResource(resources, R.drawable.icon_empty_add)
                                    binding.ivShow.setImageBitmap(optionsBitmap)
                                    //存储位图
                                    AtlwFileOptionUtil.getInstance()
                                        .writeToFile(true, File(appSystemStorageDirPath + "test.png"), optionsBitmap, Bitmap.CompressFormat.PNG)
                                    optionsDrawable = BitmapDrawable.createFromPath(appSystemStorageDirPath + "test.png")!!
                                }

                                override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {

                                }
                            })
                }
                //文件转base64
                R.id.btn_file_to_base64 -> {
                    binding.tvShow.text = AtlwImageCommonUtil.getInstance().imageFileToBase64String(appSystemStorageDirPath + "test.png")
                }
                //位图转base64
                R.id.btn_bitmap_to_base64 -> {
                    binding.tvShow.text = AtlwImageCommonUtil.getInstance().imageToBase64String(optionsBitmap)
                }
                //获取drawable宽度
                R.id.btn_get_drawable_width -> {
                    binding.tvShow.text = AtlwImageCommonUtil.getInstance().getDrawableWidth(optionsDrawable).toString()
                }
                //获取drawable高度
                R.id.btn_get_drawable_height -> {
                    binding.tvShow.text = AtlwImageCommonUtil.getInstance().getDrawableHeight(optionsDrawable).toString()
                }
                //获取圆角位图
                R.id.btn_get_rounded_bitmap_1 -> {
                    binding.ivShow.setImageBitmap(
                        AtlwImageCommonUtil.getInstance().getRoundedCornerBitmap(optionsBitmap, (Math.random() * 100).toInt()))
                }
                //获取圆角位图（左上右下角度）
                R.id.btn_get_rounded_bitmap_2 -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance()
                        .getRoundedCornerBitmap(optionsBitmap, (Math.random() * 100).toFloat(), (Math.random() * 100).toFloat(),
                            (Math.random() * 100).toFloat(), (Math.random() * 100).toFloat()))
                }
                //获取圆角位图（宽高左上右下角度）
                R.id.btn_get_rounded_bitmap_3 -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance()
                        .getRoundedCornerBitmap(optionsDrawable, (Math.random() * 100).toInt(), (Math.random() * 100).toInt(),
                            (Math.random() * 100).toFloat(), (Math.random() * 100).toFloat(), (Math.random() * 100).toFloat(),
                            (Math.random() * 100).toFloat()))
                }
                //获取圆形位图
                R.id.btn_get_circle -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance().getCircleBitmap(optionsBitmap))
                }
                //获取位图字节
                R.id.btn_get_bitmap_size -> {
                    binding.tvShow.text = AtlwImageCommonUtil.getInstance().getBitmapBytes(optionsBitmap).size.toString()
                }
                //十进制转16进制颜色值
                R.id.btn_color -> {
                    binding.tvShow.text = AtlwImageCommonUtil.getInstance().toHexEncoding((Math.random() * 1000 % 255).toInt())
                }
                //图片缩放到指定宽高
                R.id.btn_zoom -> {
                    binding.ivShow.setImageBitmap(
                        AtlwImageCommonUtil.getInstance().zoomImage(optionsBitmap, Math.random() * 100, Math.random() * 100))
                }
                //给位图设置背景
                R.id.btn_bitmap_set_bg -> {
                    binding.ivShow.setBackgroundDrawable(AtlwImageCommonUtil.getInstance()
                        .bitmapToDrawable(AtlwImageCommonUtil.getInstance().setBitmapBg(optionsBitmapAdd, Color.RED, (Math.random() * 100).toInt())))
                    binding.ivShow.setImageBitmap(null)
                }
                //按宽高比例给图片设置背景
                R.id.btn_bitmap_set_bg_percent -> {
                    binding.ivShow.setBackgroundDrawable(AtlwImageCommonUtil.getInstance().bitmapToDrawable(
                        AtlwImageCommonUtil.getInstance().fillBgAspectRatio(optionsBitmapAdd, (Math.random() * 100).toFloat(), Color.RED)))
                    binding.ivShow.setImageBitmap(null)
                }
                //位图合并
                R.id.btn_bitmap_merge -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance()
                        .mergeBitmap(optionsBitmap, optionsBitmapAdd, optionsBitmapAdd.width, optionsBitmapAdd.height, 0F, 0F, 0F))
                }
                //获取两个位图重叠部分
                R.id.btn_bitmap_overlap -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance().getOverlapBitmap(optionsBitmap, optionsBitmapAdd))
                }
                //位图添加水印
                R.id.btn_bitmap_watermark -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance()
                        .addWatermarkBitmap(optionsBitmap, 12, Color.BLUE, "打发加快速度发哈是快递费as看", optionsBitmap.width, optionsBitmap.height, 30))
                }
                //生成水印
                R.id.btn_generate_watermark -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance()
                        .generateWatermarkBitmap(12, Color.BLUE, "打发加快速度发哈是快递费as看", optionsBitmap.width, optionsBitmap.height, 30))
                }
                //图片格式转换
                R.id.btn_image_cover -> {
                    binding.ivShow.setImageBitmap(BitmapFactory.decodeFile(AtlwImageCommonUtil.getInstance()
                        .coverImage(appSystemStorageDirPath + "test.png", appSystemStorageDirPath + "test11.jpeg", Bitmap.CompressFormat.JPEG)))
                }
                //裁剪位图
                R.id.btn_image_crop -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance()
                        .cropBitmap(optionsBitmap, (Math.random() * 100).toInt(), (Math.random() * 100).toInt(), (Math.random() * 100).toInt(),
                            (Math.random() * 100).toInt()))
                }
                //旋转位图
                R.id.btn_image_turn -> {
                    binding.ivShow.setImageBitmap(AtlwImageCommonUtil.getInstance().toTurnPicture(optionsBitmap, (Math.random() * 100).toInt()))
                }
                //获取图片在存储中的旋转角度
                R.id.btn_image_degree -> {
                    binding.tvShow.text = AtlwImageCommonUtil.getInstance().readPictureDegree(appSystemStorageDirPath + "test.png").toString()
                }

                else -> {

                }
            }
        }
    }
}

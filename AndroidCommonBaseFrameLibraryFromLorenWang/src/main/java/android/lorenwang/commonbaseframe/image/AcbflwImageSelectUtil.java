package android.lorenwang.commonbaseframe.image;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.lorenwang.commonbaseframe.AcbflwBaseApplication;
import android.lorenwang.commonbaseframe.AcbflwBaseCommonKey;
import android.os.Build;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.thread.PictureThreadUtils;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.file.JtlwFileOptionUtils;

/**
 * 功能作用：图片选择器工具
 * 创建时间：2019-12-10 13:55
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 打开相机选择图片--openSelectImageCamera(activity,requestCode)
 * 开启图片选择--openSelectImage（activity，maxSelectNum，selectList，requestCode）
 * 开启图片选择--openSelectImage（activity，maxSelectNum，selectList,useTakePhoto，requestCode）
 * 开启图片选择--openSelectImage(activity, maxSelectNum, maxFileSize,selectList,useTakePhoto, callback)
 * 开启图片裁剪--openCropImage（activity，path，requestCode）
 * 开启图片压缩--openCompressImage(list, maxSize, callback)
 * 从intent中获取图片列表--getList（intent）
 * 获取操作图片地址--getOptionsImagePath(AcbflwLocalImageSelectBean)
 * 清除缓存--clearCache(activity)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * // 进入相册 以下是例子：用不到的api可以不写
 * PictureSelector.create(MainActivity.this)
 * .openGallery()//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
 * .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
 * .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题  注意：此方法最好不要与.theme();同时存在， 二选一
 * .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题 注意：此方法最好不要与.theme();同时存在， 二选一
 * .setPictureWindowAnimationStyle(windowAnimationStyle)// 自定义相册启动退出动画
 * .loadImageEngine(QtImageSelectUtils.createGlideEngine())// 外部传入图片加载引擎，必传项   参考Demo MainActivity中代码
 * .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
 * .isOriginalImageControl(cb_original.isChecked())// 是否显示原图控制按钮，如果用户勾选了 压缩、裁剪功能将会失效
 * .isWeChatStyle(isWeChatStyle)// 是否开启微信图片选择风格，此开关开启了才可使用微信主题！！！
 * .isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && enableCrop(false);有效
 * .maxSelectNum()// 最大图片选择数量 int
 * .minSelectNum()// 最小选择数量 int
 * .imageSpanCount(4)// 每行显示个数 int
 * .isNotPreviewDownload(true)// 预览图片长按是否可以下载
 * .queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
 * .querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// 查询指定后缀格式资源
 * .cameraFileName("test.png") // 重命名拍照文件名、注意这个只在使用相机时可以使用
 * .renameCompressFile("test.png")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
 * .renameCropFileName("test.png")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
 * .isSingleDirectReturn(false)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
 * .setTitleBarBackgroundColor(titleBarBackgroundColor)//相册标题栏背景色
 * .isChangeStatusBarFontColor(isChangeStatusBarFontColor)// 是否关闭白色状态栏字体颜色
 * .setStatusBarColorPrimaryDark(statusBarColorPrimaryDark)// 状态栏背景色
 * .setUpArrowDrawable(upResId)// 设置标题栏右侧箭头图标
 * .setDownArrowDrawable(downResId)// 设置标题栏右侧箭头图标
 * .isOpenStyleCheckNumMode(isOpenStyleCheckNumMode)// 是否开启数字选择模式 类似QQ相册
 * .selectionMode()// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
 * .previewImage()// 是否可预览图片 true or false
 * .previewVideo()// 是否可预览视频 true or false
 * .enablePreviewAudio() // 是否可播放音频 true or false
 * .isCamera()// 是否显示拍照按钮 true or false
 * .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
 * .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
 * .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
 * .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
 * .enableCrop()// 是否裁剪 true or false
 * .compress()// 是否压缩 true or false
 * .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
 * .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
 * .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
 * .isGif()// 是否显示gif图片 true or false
 * .compressSavePath(getPath())//压缩图片保存地址
 * .freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
 * .circleDimmedLayer()// 是否圆形裁剪 true or false
 * .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
 * .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
 * .openClickSound()// 是否开启点击声音 true or false
 * .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
 * .previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
 * .cropCompressQuality(90)// 废弃 改用cutOutQuality()
 * .cutOutQuality(90)// 裁剪输出质量 默认100
 * .minimumCompressSize(100)// 小于100kb的图片不压缩
 * .synOrAsy(true)//同步true或异步false 压缩 默认同步
 * .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
 * .rotateEnabled() // 裁剪是否可旋转图片 true or false
 * .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
 * .videoQuality()// 视频录制质量 0 or 1 int
 * .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
 * .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
 * .recordVideoSecond()//视频秒数录制 默认60s int
 * .isDragFrame(false)// 是否可拖动裁剪框(固定)
 * .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
 */
public class AcbflwImageSelectUtil {
    private static AcbflwImageSelectUtil optionsInstance;

    private AcbflwImageSelectUtil() {
    }

    public static AcbflwImageSelectUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AcbflwImageSelectUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AcbflwImageSelectUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 打开相机选择图片
     *
     * @param activity    页面上实例
     * @param requestCode 请求code
     */
    public void openSelectImageCamera(Activity activity, int requestCode) {
        PictureSelector.create(activity).openCamera(PictureMimeType.ofImage())
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.getInstance())
                //结果回调onActivityResult code
                .forResult(requestCode);

    }

    /**
     * 开启图片选择
     *
     * @param activity     activity实例
     * @param maxSelectNum 最大选择数量
     * @param selectList   已选择列表
     * @param requestCode  请求code
     */
    public void openSelectImage(Activity activity, int maxSelectNum, List<AcbflwLocalImageSelectBean> selectList, int requestCode) {
        openSelectImage(activity, maxSelectNum, selectList, true, requestCode);
    }

    /**
     * 开启图片选择
     *
     * @param activity     activity实例
     * @param maxSelectNum 最大选择数量
     * @param selectList   已选择列表
     * @param requestCode  请求code
     * @param useTakePhoto 是否使用拍照
     */
    public void openSelectImage(Activity activity, int maxSelectNum, List<AcbflwLocalImageSelectBean> selectList, boolean useTakePhoto,
            int requestCode) {
        getBasePictureSelectionModel(activity, PictureMimeType.ofImage(), maxSelectNum, 10, 0, 0, 0, selectList, useTakePhoto).forResult(requestCode);
    }

    /**
     * 开启图片选择
     *
     * @param activity     activity实例
     * @param maxSelectNum 最大选择数量
     * @param selectList   已选择列表
     * @param callback     请求code
     * @param useTakePhoto 是否使用拍照
     * @param maxFileSize  最大文件大小，单位M
     */
    public void openSelectImage(Activity activity, int maxSelectNum, int maxFileSize, List<AcbflwLocalImageSelectBean> selectList,
            boolean useTakePhoto, AcbflwFileSelectCallback callback) {
        getBasePictureSelectionModel(activity, PictureMimeType.ofImage(), maxSelectNum, maxFileSize, 0, 0, 0, selectList, useTakePhoto).forResult(
                new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (callback != null) {
                            callback.onResult(localMediaToImageSelectBean(result));
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (callback != null) {
                            callback.onCancel();
                        }
                    }
                });
    }

    /**
     * 开启图片选择
     *
     * @param activity     activity实例
     * @param maxSelectNum 最大选择数量
     * @param selectList   已选择列表
     * @param requestCode  请求code
     * @param useTakePhoto 是否使用拍照
     * @param maxFileSize  最大文件大小，单位M
     */
    public void openSelectImage(Activity activity, int maxSelectNum, int maxFileSize, List<AcbflwLocalImageSelectBean> selectList,
            boolean useTakePhoto, int requestCode) {
        getBasePictureSelectionModel(activity, PictureMimeType.ofImage(), maxSelectNum, maxFileSize, 0, 0, 0, selectList, useTakePhoto).forResult(
                requestCode);
    }

    /**
     * 开启视频选择
     *
     * @param activity       页面实例
     * @param maxFileSize    最大文件大小
     * @param minVideoSecond 最小视频时间
     * @param maxVideoSecond 最大视频时间
     * @param selectBean     已选择实例
     * @param callback       回调监听
     */
    public void openSelectVideo(Activity activity, int maxFileSize, int minVideoSecond, int maxVideoSecond, AcbflwLocalImageSelectBean selectBean,
            AcbflwFileSelectCallback callback) {
        List<AcbflwLocalImageSelectBean> selectList = new ArrayList<>();
        if (selectBean != null) {
            selectList.add(selectBean);
        }
        getBasePictureSelectionModel(activity, PictureMimeType.ofVideo(), 1, maxFileSize, 1, minVideoSecond, maxVideoSecond, selectList, false)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (callback != null) {
                            callback.onResult(localMediaToImageSelectBean(result));
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (callback != null) {
                            callback.onCancel();
                        }
                    }
                });
    }

    /**
     * 开启图片裁剪
     *
     * @param activity    activity实例
     * @param cropPath    图片裁剪地址
     * @param requestCode 请求code
     */
    public void openCropImage(Activity activity, String cropPath, int requestCode) {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(activity)
                //全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .openGallery(PictureMimeType.ofImage())
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.getInstance())
                // 是否开启微信图片选择风格，此开关开启了才可使用微信主题！！！
                .isWeChatStyle(true)
                // 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对isCompress(false); && isEnableCrop(false);有效
                .isAndroidQTransform(false)
                // 设置相册Activity方向，不设置默认使用系统
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                // 裁剪输出质量 默认100
                .cutOutQuality(90)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                // 是否可拖动裁剪框(固定)
                .isDragFrame(false);

        Intent intent = new Intent(activity, AcbflwPictureCropActivity.class);
        intent.putExtra(AcbflwBaseCommonKey.KEY_PICTURE_CROP_IMAGE_PATH, cropPath);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启图片压缩
     *
     * @param list 要压缩的列表
     */
    public void openCompressImage(@NotNull final List<AcbflwLocalImageSelectBean> list, final int maxSize,
            @NotNull final AcbflwFileCompressCallback callback) {
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<List<File>>() {
            @Override
            public List<File> doInBackground() throws Exception {
                return compressImage(list, maxSize, 100);
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                callback.fail();
            }

            @Override
            public void onSuccess(List<File> files) {
                if (files != null && files.size() > 0 && files.size() == list.size()) {
                    callback.success(files);
                } else {
                    callback.fail();
                }
            }
        });
    }

    /**
     * 从intent中获取列表
     *
     * @param data intent数据
     * @return 列表数据
     */
    public ArrayList<AcbflwLocalImageSelectBean> getList(Intent data) {
        return localMediaToImageSelectBean(PictureSelector.obtainMultipleResult(data));
    }

    /**
     * 获取操作图片地址
     */
    public String getOptionsImagePath(AcbflwLocalImageSelectBean media) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && JtlwCheckVariateUtils.getInstance().isNotEmpty(media.getAndroidQToPath())) {
            return media.getAndroidQToPath();
        } else {
            return media.getPath();
        }
    }

    /**
     * 清除缓存
     *
     * @param activity 上下文
     */
    public void clearCache(Activity activity) {
        try {
            PictureFileUtils.deleteCacheDirFile(activity, PictureMimeType.ofAll());
            PictureFileUtils.deleteCacheDirFile(activity, PictureMimeType.ofImage());
            PictureFileUtils.deleteAllCacheDirFile(activity);
        } catch (Exception ignore) {
        }
    }

    /**
     * 本地图片实例转框架实例
     *
     * @param selectList 本地实例列表
     * @return 框架实例列表
     */
    private List<LocalMedia> imageSelectBeanToLocalMedia(List<AcbflwLocalImageSelectBean> selectList) {
        if (selectList == null) {
            return new ArrayList<>();
        }
        ArrayList<LocalMedia> list = new ArrayList<>();
        Iterator<AcbflwLocalImageSelectBean> iterator = selectList.iterator();
        AcbflwLocalImageSelectBean AcbflwLocalImageSelectBean;
        while (iterator.hasNext()) {
            AcbflwLocalImageSelectBean = iterator.next();
            if (AcbflwLocalImageSelectBean != null) {
                list.add(AcbflwLocalImageSelectBean.copyToLocalMedia());
            }
        }
        return list;
    }

    /**
     * 框架实例转本地图片选择实例
     *
     * @param selectList 框架实例列表
     * @return 本地实例列表
     */
    private ArrayList<AcbflwLocalImageSelectBean> localMediaToImageSelectBean(List<LocalMedia> selectList) {
        ArrayList<AcbflwLocalImageSelectBean> list = new ArrayList<>();
        if (selectList != null) {
            Iterator<LocalMedia> iterator = selectList.iterator();
            LocalMedia localMedia;
            while (iterator.hasNext()) {
                localMedia = iterator.next();
                if (localMedia != null) {
                    list.add(new AcbflwLocalImageSelectBean().copyToLocalCurrent(localMedia));
                }
            }
        }
        return list;
    }

    /**
     * 压缩比例
     *
     * @param list    图片列表
     * @param maxSize 压缩最大文件大小，单位kb
     * @return 压缩后文件列表
     */
    private List<File> compressImage(@NotNull List<AcbflwLocalImageSelectBean> list, int maxSize, int compressQuality) throws Exception {
        List<File> files = Luban.with(AcbflwBaseApplication.getAppContext()).loadMediaData(imageSelectBeanToLocalMedia(list)).isCamera(false)
                .setFocusAlpha(false).setCompressQuality(compressQuality).ignoreBy(100).get();
        List<File> returnFileList = new ArrayList<>(list.size());
        List<AcbflwLocalImageSelectBean> reloadCompressList = new ArrayList<>(list.size());
        File file;
        for (int i = 0; i < files.size(); i++) {
            file = files.get(i);
            if (JtlwFileOptionUtils.getInstance().getFileSize(file, null) > (long) maxSize << 10) {
                //压缩前后对应关系、位置下标一样
                reloadCompressList.add(list.get(i));
            } else {
                returnFileList.add(file);
            }
        }
        //加入重新压缩的数据
        if (reloadCompressList.size() > 0) {
            returnFileList.addAll(compressImage(reloadCompressList, maxSize, compressQuality - 10));
        }
        return returnFileList;
    }

    /**
     * 获取基础配置
     *
     * @param activity       页面实例
     * @param chooseMode     选择模式
     * @param maxSelectNum   最大选择数量
     * @param maxFileSize    最大选择文件大小
     * @param maxVideoSelect 最大的视频选择数量
     * @param minVideoSecond 最小选择的视频时间
     * @param maxVideoSecond 最大选择的视频时间
     * @param selectList     已选择列表
     * @param useTakePhoto   是否可以拍照
     * @return 基础配置model
     */
    private PictureSelectionModel getBasePictureSelectionModel(Activity activity, int chooseMode, int maxSelectNum, int maxFileSize,
            int maxVideoSelect, int minVideoSecond, int maxVideoSecond, List<AcbflwLocalImageSelectBean> selectList, boolean useTakePhoto) {
        // 进入相册 以下是例子：用不到的api可以不写
        return PictureSelector.create(activity)
                //全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .openGallery(chooseMode)
                // 外部传入图片加载引擎，必传项
                .imageEngine(GlideEngine.getInstance())
                // 是否显示原图控制按钮，如果用户勾选了 压缩、裁剪功能将会失效
                .isOriginalImageControl(false)
                // 是否开启微信图片选择风格，此开关开启了才可使用微信主题！！！
                .isWeChatStyle(true)
                // 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对isCompress(false); && isEnableCrop(false);有效
                .isAndroidQTransform(false)
                // 最大图片选择数量 int
                .maxSelectNum(maxSelectNum)
                // 最小选择数量 int
                .minSelectNum(1)
                // 每行显示个数 int
                .imageSpanCount(4)
                // 设置相册Activity方向，不设置默认使用系统
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                // 预览图片长按是否可以下载
                .isNotPreviewDownload(false)
                // 只查多少M以内的图片、视频、音频  单位M
                .queryMaxFileSize(maxFileSize)
                // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isSingleDirectReturn(false)
                // 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig
                .selectionMode(maxSelectNum > 0 ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)
                // .SINGLE
                // 是否可预览图片 true or false
                .isPreviewImage(true)
                // 是否显示拍照按钮 true or false
                .isCamera(useTakePhoto)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                // 是否裁剪 true or false
                .isEnableCrop(false)
                // 是否压缩 true or false
                .isCompress(false)
                // 是否显示gif图片 true or false
                .isGif(false)
                // 是否开启点击声音 true or false
                .isOpenClickSound(false)
                // 是否传入已选图片 List<LocalMedia> list
                .selectionData(imageSelectBeanToLocalMedia(selectList))
                // 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .isPreviewEggs(true)
                // 裁剪输出质量 默认100
                .cutOutQuality(90)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                //视频最大选择数量
                .maxVideoSelectNum(maxVideoSelect)
                //视频最小选择数量
                .minVideoSelectNum(0)
                // 查询多少秒以内的视频
                .videoMaxSecond(maxVideoSecond)
                // 查询多少秒以内的视频
                .videoMinSecond(minVideoSecond)
                //录制视频秒数 默认60s
                .recordVideoSecond(maxVideoSecond)
                //是否预览视频
                .isPreviewVideo(true)
                // 是否可拖动裁剪框(固定)
                .isDragFrame(false);
    }
}

package android.lorenwang.commonbaseframe.image;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.luck.picture.lib.PictureBaseActivity;
import com.luck.picture.lib.config.PictureMimeType;

import androidx.annotation.Nullable;

import static android.lorenwang.commonbaseframe.AcbflwBaseCommonKey.KEY_PICTURE_CROP_IMAGE_PATH;
import static android.lorenwang.commonbaseframe.AcbflwBaseCommonKey.KEY_PICTURE_CROP_RESULT;


/**
 * 功能作用：
 * 创建时间：2019-12-18 18:42
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwPictureCropActivity extends PictureBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startCrop(getIntent().getStringExtra(KEY_PICTURE_CROP_IMAGE_PATH));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 69 && data != null
                && data.getExtras() != null) {
            Bundle extras = data.getExtras();
            Uri resultUri = (Uri) extras.get("com.yalantis.ucrop.OutputUri");
            int imageHeight = extras.getInt("com.yalantis.ucrop.ImageHeight");
            int imageWidth = extras.getInt("com.yalantis.ucrop.ImageWidth");
            int offsetX = extras.getInt("com.yalantis.ucrop.OffsetX");
            int offsetY = extras.getInt("com.yalantis.ucrop.OffsetY");
            float cropAspectRatio = extras.getFloat("com.yalantis.ucrop.CropAspectRatio");
            assert resultUri != null;
            String cutPath = resultUri.getPath();
            AcbflwLocalImageSelectBean media = new AcbflwLocalImageSelectBean();
            media.setPath(cutPath);
            media.setDuration(0);
            media.setChecked(false);
            media.setOffsetX(offsetX);
            media.setOffsetY(offsetY);
            media.setCropAspectRatio(cropAspectRatio);
            if (imageWidth > 0) {
                media.setWidth(imageWidth);
            }
            if (imageHeight > 0) {
                media.setHeight(imageHeight);
            }
            media.position = config.isCamera ? 1 : 0;
            media.setNum(0);
            media.setChooseModel(config.chooseMode);
            media.setCut(true);
            media.setCutPath(cutPath);
            String mimeType = PictureMimeType.getImageMimeType(cutPath);
            media.setMimeType(mimeType);
            extras.putParcelable(KEY_PICTURE_CROP_RESULT, media);
            data.putExtras(extras);
        }
        setResult(resultCode, data);
        finish();
    }

    @Override
    public int getResourceId() {
        return 0;
    }
}

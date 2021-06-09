package android.lorenwang.customview.editText;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

/**
 * 功能作用：左右图标显示的编辑控件
 * 初始注释时间： 2021/5/27 14:03
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
public class AvlwLeftRightIconShowEditText extends AppCompatEditText {
    /**
     * 左侧图标
     */
    private Drawable leftIconDrawable = null;
    /**
     * 右侧图标
     */
    private Drawable rightIconDrawable = null;
    /**
     * 左侧icon宽度
     */
    private int leftIconWidth = 0;
    /**
     * 左侧icon高度
     */
    private int leftIconHeight = 0;
    /**
     * 右侧icon宽度
     */
    private int rightIconWidth = 0;
    /**
     * 右侧icon高度
     */
    private int rightIconHeight = 0;
    /**
     * 右侧icon点击是否清除文本
     */
    private boolean rightIconClickClearText = false;
    /**
     * 左侧按钮点击
     */
    private View.OnClickListener leftOnClickListener;
    /**
     * 右侧按钮点击
     */
    private View.OnClickListener rightOnClickListener;

    public AvlwLeftRightIconShowEditText(@NonNull @NotNull Context context) {
        super(context);
        init(context, null);
    }

    public AvlwLeftRightIconShowEditText(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvlwLeftRightIconShowEditText(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwLeftRightIconShowEditText);
        leftIconWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwLeftRightIconShowEditText_avlwLeftIconWidth, leftIconWidth);
        leftIconHeight = attributes.getDimensionPixelOffset(R.styleable.AvlwLeftRightIconShowEditText_avlwLeftIconHeight, leftIconHeight);
        rightIconWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwLeftRightIconShowEditText_avlwRightIconWidth, rightIconWidth);
        rightIconHeight = attributes.getDimensionPixelOffset(R.styleable.AvlwLeftRightIconShowEditText_avlwRightIconHeight, rightIconHeight);
        rightIconClickClearText = attributes.getBoolean(R.styleable.AvlwLeftRightIconShowEditText_avlwRightIconClickClearText,
                rightIconClickClearText);
        leftIconDrawable = attributes.getDrawable(R.styleable.AvlwLeftRightIconShowEditText_avlwLeftIconRes);
        rightIconDrawable = attributes.getDrawable(R.styleable.AvlwLeftRightIconShowEditText_avlwRightIconRes);
        attributes.recycle();
        //更新图标
        updateIconDrawable();
        //设置空背景
        setBackground(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (rightIconDrawable != null) {
                if (event.getX() > (getWidth() - rightIconDrawable.getIntrinsicWidth()) && event.getX() < getWidth()) {
                    if (getCompoundDrawables()[2] != null) {
                        onRightIconClick();
                        return false;
                    }
                }
            }
            if (leftIconDrawable != null) {
                if (event.getX() > 0 && event.getX() < leftIconDrawable.getIntrinsicWidth()) {
                    if (getCompoundDrawables()[0] != null) {
                        onLeftIconClick();
                        return false;
                    }
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        updateIconDrawable();
    }

    /**
     * 设置左侧icon点击
     */
    public void setLeftOnClickListener(OnClickListener leftOnClickListener) {
        this.leftOnClickListener = leftOnClickListener;
    }

    /**
     * 设置右侧icon点击
     */
    public void setRightOnClickListener(OnClickListener rightOnClickListener) {
        this.rightOnClickListener = rightOnClickListener;
    }

    /**
     * 设置左侧图标
     *
     * @param drawableRes 图标资源
     * @param width       宽
     * @param height      高
     */
    public void setLeftIconDrawable(@DrawableRes int drawableRes, Integer width, Integer height) {
        leftIconDrawable = ContextCompat.getDrawable(AtlwConfig.nowApplication, drawableRes);
        if (width != null) {
            leftIconWidth = width;
        }
        if (height != null) {
            leftIconHeight = height;
        }
        updateIconDrawable();
    }

    /**
     * 设置右侧图标
     *
     * @param drawableRes 图标资源
     * @param width       宽
     * @param height      高
     */
    public void setRightIconDrawable(@DrawableRes int drawableRes, Integer width, Integer height) {
        rightIconDrawable = ContextCompat.getDrawable(AtlwConfig.nowApplication, drawableRes);
        if (width != null) {
            rightIconWidth = width;
        }
        if (height != null) {
            rightIconHeight = height;
        }
        updateIconDrawable();
    }

    /**
     * 更新图标
     */
    private void updateIconDrawable() {
        if (leftIconDrawable != null) {
            leftIconDrawable.setBounds(0, 0, leftIconWidth, leftIconHeight);
        }
        if (rightIconDrawable != null) {
            rightIconDrawable.setBounds(0, 0, rightIconWidth, rightIconHeight);
        }
        if (rightIconClickClearText && rightIconDrawable != null) {
            if (length() > 0) {
                if (getCompoundDrawables()[2] == null) {
                    setCompoundDrawables(leftIconDrawable, null, rightIconDrawable, null);
                }
            } else {
                if (getCompoundDrawables()[2] != null) {
                    setCompoundDrawables(leftIconDrawable, null, null, null);
                }
            }
        } else {
            if (leftIconDrawable != null && getCompoundDrawables()[0] == null || rightIconDrawable != null && getCompoundDrawables()[2] == null) {
                setCompoundDrawables(leftIconDrawable, null, rightIconDrawable, null);
            }
        }
    }

    /**
     * 右侧图标点击
     */
    private void onRightIconClick() {
        AtlwLogUtil.logUtils.logI(getClass().getName(), "点击了右侧icon");
        if (rightIconDrawable != null) {
            if (rightIconClickClearText) {
                clearFocus();
                setText("");
                updateIconDrawable();
            }
            if (rightOnClickListener != null) {
                rightOnClickListener.onClick(this);
            }
        }
    }

    /**
     * 左侧图标点击
     */
    private void onLeftIconClick() {
        AtlwLogUtil.logUtils.logI(getClass().getName(), "点击了左侧icon");
        if (leftIconDrawable != null) {
            if (leftOnClickListener != null) {
                leftOnClickListener.onClick(this);
            }
        }
    }
}

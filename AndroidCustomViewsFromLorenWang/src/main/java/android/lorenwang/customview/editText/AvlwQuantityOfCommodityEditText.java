package android.lorenwang.customview.editText;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.lorenwang.customview.R;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.image.AtlwImageCommonUtils;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 功能作用：商品数量修改textView
 * 创建时间：2020-01-10 14:07
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwQuantityOfCommodityEditText extends AppCompatEditText {
    private final String TAG = getClass().getName();
    /**
     * 初始左侧内边距
     */
    private int firstPaddingLeft = 0;
    /**
     * 初始右侧内边距
     */
    private int firstPaddingRight = 0;
    /**
     * 初始左侧上边距
     */
    private int firstPaddingTop = 0;
    /**
     * 初始右侧下边距
     */
    private int firstPaddingBottom = 0;
    /**
     * 边框宽度
     */
    private int borderWidth = 0;
    /**
     * 边框角度半径
     */
    private int borderRadio = 0;
    /**
     * 操作按钮和文本之间 的间距
     */
    private int optionsButtonAndTextDistance = 0;
    /**
     * 操作按钮宽度
     */
    private int optionsButtonWidth = 0;
    /**
     * 操作按钮左右边距
     */
    private int optionsButtonLeftRightDistance = 0;
    /**
     * 是否允许新增
     */
    private boolean allowAdd = false;
    /**
     * 是否允许减少
     */
    private boolean allowReduce = false;
    /**
     * 单次加减数量，默认数量1
     */
    private long addReduceQuantity = 1;
    /**
     * 总量数量
     */
    private Long quantity = 0L;
    /**
     * 最小总量数量
     */
    private long minQuantity = 1;
    /**
     * 最大总量数量
     */
    private long maxQuantity = 10000;
    /**
     * 新增点击事件
     */
    private OnClickListener addClickListener;
    /**
     * 减少点击事件
     */
    private OnClickListener reduceClickListener;

    /*****************************************绘制参数*****************************************/
    /**
     * 边框画笔
     */
    private Paint borderPaint;
    /**
     * 添加按钮允许位图
     */
    private Bitmap addButtonAllowBitmap = null;
    /**
     * 添加按钮禁止位图
     */
    private Bitmap addButtonNotAllowBitmap = null;
    /**
     * 减少按钮允许位图
     */
    private Bitmap reduceButtonAllowBitmap = null;
    /**
     * 减少按钮禁止位图
     */
    private Bitmap reduceButtonNotAllowBitmap = null;
    /**
     * 添加按钮允许位图坐标矩形
     */
    private Rect addButtonAllowBitmapRect = null;
    /**
     * 添加按钮禁止位图坐标矩形
     */
    private Rect addButtonNotAllowBitmapRect = null;
    /**
     * 减少按钮允许位图坐标矩形
     */
    private Rect reduceButtonAllowBitmapRect = null;
    /**
     * 减少按钮禁止位图坐标矩形
     */
    private Rect reduceButtonNotAllowBitmapRect = null;


    public AvlwQuantityOfCommodityEditText(Context context) {
        super(context);
        init(context, null);
    }

    public AvlwQuantityOfCommodityEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvlwQuantityOfCommodityEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwQuantityOfCommodityEditText);
        //获取间距
        optionsButtonAndTextDistance = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEOptionsButtonAndTextDistance, optionsButtonAndTextDistance);
        //获取操作按钮宽度
        optionsButtonWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEOptionsButtonWidth, optionsButtonWidth);
        //操作按钮左右边距
        optionsButtonLeftRightDistance = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEOptionsButtonLeftRightDistance, optionsButtonLeftRightDistance);
        //边框角度半径
        borderRadio = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEQuantityBorderRadio, borderRadio);
        //边框宽度
        borderWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEQuantityBorderWidth, borderWidth);
        //按钮部分数据参数
        try {
            addButtonAllowBitmap = AtlwImageCommonUtils.getInstance().drawableToBitmap(attributes.getDrawable(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEAddButtonDrawableAllow));
            addButtonNotAllowBitmap = AtlwImageCommonUtils.getInstance().drawableToBitmap(attributes.getDrawable(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEAddButtonDrawableNotAllow));
            reduceButtonAllowBitmap = AtlwImageCommonUtils.getInstance().drawableToBitmap(attributes.getDrawable(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEReduceButtonDrawableAllow));
            reduceButtonNotAllowBitmap = AtlwImageCommonUtils.getInstance().drawableToBitmap(attributes.getDrawable(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEReduceButtonDrawableNotAllow));
        } catch (Exception e) {
            AtlwLogUtils.logE(TAG, "数量控件初始化异常");
        }
        if (addButtonAllowBitmap != null) {
            addButtonAllowBitmapRect = new Rect(0, 0, addButtonAllowBitmap.getWidth(), addButtonAllowBitmap.getHeight());
        }
        if (addButtonNotAllowBitmap != null) {
            addButtonNotAllowBitmapRect = new Rect(0, 0, addButtonNotAllowBitmap.getWidth(), addButtonNotAllowBitmap.getHeight());
        }
        if (reduceButtonAllowBitmap != null) {
            reduceButtonAllowBitmapRect = new Rect(0, 0, reduceButtonAllowBitmap.getWidth(), reduceButtonAllowBitmap.getHeight());
        }
        if (reduceButtonNotAllowBitmap != null) {
            reduceButtonNotAllowBitmapRect = new Rect(0, 0, reduceButtonNotAllowBitmap.getWidth(), reduceButtonNotAllowBitmap.getHeight());
        }


        //边框画笔
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(attributes.getColor(R.styleable.AvlwQuantityOfCommodityEditText_avlwQOCEQuantityBorderColor, Color.TRANSPARENT));
        borderPaint.setStyle(Paint.Style.STROKE);


        //默认禁用输入
        setEnabled(false);
        //设置允许输入的字符串
        setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        //设置输入类型是数字
        setInputType(InputType.TYPE_CLASS_NUMBER);
        firstPaddingLeft = getPaddingLeft();
        firstPaddingRight = getPaddingRight();
        firstPaddingTop = getPaddingTop();
        firstPaddingBottom = getPaddingBottom();
        attributes.recycle();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Long quantity = AvlwQuantityOfCommodityEditText.this.quantity;
                try {
                    quantity = Long.parseLong(s.toString());
                } catch (NumberFormatException e) {
                    AtlwLogUtils.logE(TAG, "传递的参数非整数参数");
                }
                //判断修改后是否超范围
                if (quantity.compareTo(maxQuantity) > 0) {
                    setText(null);
                    return;
                }
                if (quantity.compareTo(minQuantity) < 0) {
                    setText(null);
                    return;
                }
                //更新显示数据
                AvlwQuantityOfCommodityEditText.this.quantity = quantity;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        firstPaddingLeft = left;
        firstPaddingRight = right;
        firstPaddingTop = top;
        firstPaddingBottom = bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int borderPosi = (int) (borderWidth / 2.0f);
        super.setPadding((int) (firstPaddingLeft + optionsButtonAndTextDistance + borderWidth * 1.5 + optionsButtonWidth + optionsButtonLeftRightDistance * 2),
                firstPaddingTop + borderPosi,
                (int) (firstPaddingRight + optionsButtonAndTextDistance + borderWidth * 1.5 + optionsButtonWidth + optionsButtonLeftRightDistance * 2),
                firstPaddingBottom + borderPosi);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int showHeight = Math.min(optionsButtonWidth, getHeight());
        if (allowAdd && addButtonAllowBitmap != null && addButtonAllowBitmapRect != null) {
            canvas.drawBitmap(addButtonAllowBitmap, addButtonAllowBitmapRect, getAddRectF(showHeight), null);
        }
        if (!allowAdd && addButtonNotAllowBitmap != null && addButtonNotAllowBitmapRect != null) {
            canvas.drawBitmap(addButtonNotAllowBitmap, addButtonNotAllowBitmapRect, getAddRectF(showHeight), null);
        }
        if (allowReduce && reduceButtonAllowBitmap != null && reduceButtonAllowBitmapRect != null) {
            canvas.drawBitmap(reduceButtonAllowBitmap, reduceButtonAllowBitmapRect, getReduceRectF(showHeight), null);
        }
        if (!allowReduce && reduceButtonNotAllowBitmap != null && reduceButtonNotAllowBitmapRect != null) {
            canvas.drawBitmap(reduceButtonNotAllowBitmap, reduceButtonNotAllowBitmapRect, getReduceRectF(showHeight), null);
        }
        //绘制矩形
        int borderPosi = (int) (borderWidth / 2.0f);
        borderPaint.setStyle(Paint.Style.STROKE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(borderPosi, borderPosi,
                    getWidth() - borderPosi, getHeight() - borderPosi, borderRadio, borderRadio, borderPaint);
        } else {
            canvas.drawRoundRect(new RectF(borderPosi, borderPosi,
                    getWidth() - borderPosi, getHeight() - borderPosi), borderRadio, borderRadio, borderPaint);
        }
        //绘制分隔线
        borderPaint.setStyle(Paint.Style.FILL);
        int leftStartX = optionsButtonWidth + firstPaddingLeft + borderWidth + optionsButtonLeftRightDistance * 2;
        int rightStartX = getWidth() - optionsButtonWidth - firstPaddingRight - borderWidth - optionsButtonLeftRightDistance * 2;
        int stopY = getHeight() - borderWidth;
        canvas.drawLine(leftStartX, borderWidth, leftStartX, stopY, borderPaint);
        canvas.drawLine(rightStartX, borderWidth, rightStartX, stopY, borderPaint);

    }

    private float downX;
    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //判断点击范围移动
                if (Math.abs(downX - event.getRawX()) < 100 && Math.abs(downY - event.getRawY()) < 100) {
                    //判断抬起位置是否是增加或者减少区域
                    if (event.getX() > firstPaddingLeft && event.getX() < firstPaddingLeft + optionsButtonWidth + optionsButtonLeftRightDistance * 2
                            && event.getY() > 0 && event.getY() < getHeight()) {
                        if (reduceClickListener != null) {
                            reduceClickListener.onClick(this);
                        } else {
                            reduceQuantity();
                        }
                        return true;
                    }
                    if (event.getX() > getWidth() - firstPaddingRight - optionsButtonLeftRightDistance * 2 - optionsButtonWidth
                            && event.getX() < getWidth() - firstPaddingRight
                            && event.getY() > 0 && event.getY() < getHeight()) {
                        if (addClickListener != null) {
                            addClickListener.onClick(this);
                        } else {
                            addQuantity();
                        }
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取添加按钮要显示的位置
     *
     * @param rectHeight 要显示的高度
     * @return 显示位置
     */
    private Rect getAddRectF(int rectHeight) {
        int right = (int) (getWidth() - firstPaddingRight - borderWidth / 2.0f - optionsButtonLeftRightDistance);
        int top = (getHeight() - rectHeight) / 2;
        return new Rect(right - optionsButtonWidth, top, right, top + rectHeight);
    }

    /**
     * 获取减少按钮显示的位置
     *
     * @param rectHeight 显示的高度
     * @return 显示位置
     */
    private Rect getReduceRectF(int rectHeight) {
        int left = (int) (firstPaddingLeft + borderWidth / 2.0f + optionsButtonLeftRightDistance);
        int top = (getHeight() - rectHeight) / 2;
        return new Rect(left, top, left + optionsButtonWidth, top + rectHeight);
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 新增数量
     */
    public void addQuantity() {
        addQuantity(addReduceQuantity);
    }

    /**
     * 新增数量
     *
     * @param quantity 数量
     */
    public void addQuantity(long quantity) {
        if (!allowAdd) {
            return;
        }
        this.quantity += Math.abs(quantity);
        setText(null);
    }

    /**
     * 减少数量
     */
    public void reduceQuantity() {
        reduceQuantity(-addReduceQuantity);
    }

    /**
     * 减少数量
     *
     * @param quantity 数量
     */
    public void reduceQuantity(long quantity) {
        if (!allowReduce) {
            return;
        }
        this.quantity -= Math.abs(quantity);
        setText(null);
    }

    /**
     * 设置每次加减数量
     *
     * @param addReduceQuantity 加减数量
     * @return 当前实例
     */
    public AvlwQuantityOfCommodityEditText setAddReduceQuantity(long addReduceQuantity) {
        this.addReduceQuantity = addReduceQuantity;
        return this;
    }

    /**
     * 是否允许新增数据
     *
     * @return 是否允许新增数据
     */
    public boolean isAllowAdd() {
        return allowAdd;
    }

    /**
     * 是否允许减少数据
     *
     * @return 允许减少数据
     */
    public boolean isAllowReduce() {
        return allowReduce;
    }

    /**
     * 获取当前数量
     *
     * @return 当前操作的数量
     */
    public Long getQuantity() {
        return quantity;
    }

    /**
     * 设置最小数量
     *
     * @param minQuantity 最小数量
     * @return 当前实例
     */
    public AvlwQuantityOfCommodityEditText setMinQuantity(long minQuantity) {
        this.minQuantity = minQuantity;
        return this;
    }

    /**
     * 设置允许操作的最大数据
     *
     * @param maxQuantity 最大数据数量
     * @return 当前实例
     */
    public AvlwQuantityOfCommodityEditText setMaxQuantity(long maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    /**
     * 设置减少点击事件
     *
     * @param reduceClickListener 点击事件
     * @return 当前实例
     */
    public AvlwQuantityOfCommodityEditText setReduceClickListener(OnClickListener reduceClickListener) {
        this.reduceClickListener = reduceClickListener;
        return this;
    }

    /**
     * 设置新增点击事件
     *
     * @param addClickListener 点击事件
     * @return 当前实例
     */
    public AvlwQuantityOfCommodityEditText setAddClickListener(OnClickListener addClickListener) {
        this.addClickListener = addClickListener;
        return this;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null) {
            try {
                this.quantity = Long.parseLong(String.valueOf(text));
            } catch (NumberFormatException e) {
                AtlwLogUtils.logE(this.TAG, "传递的参数非整数参数");
            }
        }
        text = String.valueOf(this.quantity);
        if (this.quantity.compareTo(maxQuantity) >= 0) {
            this.quantity = maxQuantity;
            allowAdd = false;
        } else {
            allowAdd = true;
        }
        if (this.quantity.compareTo(minQuantity) <= 0) {
            this.quantity = minQuantity;
            allowReduce = false;
        } else {
            allowReduce = true;
        }
        super.setText(text, type);
    }
}

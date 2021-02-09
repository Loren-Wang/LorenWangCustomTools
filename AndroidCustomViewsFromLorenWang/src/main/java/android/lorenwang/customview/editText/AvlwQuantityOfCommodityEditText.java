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
import android.lorenwang.tools.app.AtlwViewUtil;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import javabase.lorenwang.tools.JtlwLogUtils;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

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
     * 输入文本正则
     */
    private final String TEXT_MATCHES = "[0-9]+";
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
    private Integer borderWidth = 0;
    /**
     * 是否只给内容加边框
     */
    private boolean borderOnlyShowContent = false;
    /**
     * 边框角度半径
     */
    private int borderRadio = 0;
    /**
     * 文本内部左右边距
     */
    private int textInsideDistance = 0;
    /**
     * 操作按钮和文本分隔之间的间距
     */
    private int optionsButtonAndTextSeparatedDistance = 0;
    /**
     * 操作按钮内部边距
     */
    private int optionsButtonInsideDistance = 0;
    /**
     * 操作按钮宽度
     */
    private int optionsButtonWidth = 0;
    /**
     * 文本显示宽度
     */
    private int textShowWidth = 0;
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
    private long quantity = 0L;
    /**
     * 最小总量数量
     */
    private long minQuantity = 0L;
    /**
     * 最大总量数量
     */
    private long maxQuantity = 10000;
    /**
     * 新增点击事件
     */
    private View.OnClickListener addClickListener;
    /**
     * 减少点击事件
     */
    private View.OnClickListener reduceClickListener;

    /*-----------------------------------------绘制参数--------------------------------------------*/
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
    /**
     * 改变监听
     */
    private OnChangeListener onChangeListener;
    /**
     * 使用的数据
     */
    private Object data;

    /**
     * 控件宽度
     */
    private int viewLayoutSetWidth = -1;

    /**
     * 控件高度
     */
    private int viewLayoutSetHeight = -1;

    public AvlwQuantityOfCommodityEditText(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwQuantityOfCommodityEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwQuantityOfCommodityEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwQuantityOfCommodityEditText);
        //获取间距
        textInsideDistance = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwTextInsideDistance,
                textInsideDistance);
        //操作按钮内部边距
        optionsButtonInsideDistance = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwOptionsButtonInsideDistance,
                optionsButtonInsideDistance);
        //操作按钮和文本分隔之间的间距
        optionsButtonAndTextSeparatedDistance = attributes.getDimensionPixelOffset(
                R.styleable.AvlwQuantityOfCommodityEditText_avlwOptionsButtonAndTextSeparatedDistance, optionsButtonAndTextSeparatedDistance);
        //获取操作按钮宽度
        optionsButtonWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwOptionsButtonWidth,
                optionsButtonWidth);
        //边框角度半径
        borderRadio = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwQuantityBorderRadio, borderRadio);
        //边框宽度
        borderWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwQuantityBorderWidth, borderWidth);
        //是否只给内容加边框
        borderOnlyShowContent = attributes.getBoolean(R.styleable.AvlwQuantityOfCommodityEditText_avlwBorderOnlyShowContent, borderOnlyShowContent);
        //文本显示宽度
        textShowWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwQuantityOfCommodityEditText_avlwTextShowWidth, textShowWidth);

        //按钮部分数据参数
        try {
            addButtonAllowBitmap = AtlwImageCommonUtil.getInstance().drawableToBitmap(
                    attributes.getDrawable(R.styleable.AvlwQuantityOfCommodityEditText_avlwAddButtonDrawableAllow));
            addButtonNotAllowBitmap = AtlwImageCommonUtil.getInstance().drawableToBitmap(
                    attributes.getDrawable(R.styleable.AvlwQuantityOfCommodityEditText_avlwAddButtonDrawableNotAllow));
            reduceButtonAllowBitmap = AtlwImageCommonUtil.getInstance().drawableToBitmap(
                    attributes.getDrawable(R.styleable.AvlwQuantityOfCommodityEditText_avlwReduceButtonDrawableAllow));
            reduceButtonNotAllowBitmap = AtlwImageCommonUtil.getInstance().drawableToBitmap(
                    attributes.getDrawable(R.styleable.AvlwQuantityOfCommodityEditText_avlwReduceButtonDrawableNotAllow));
        } catch (Exception e) {
            JtlwLogUtils.logUtils.logE(TAG, "数量控件初始化异常");
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
        borderPaint.setColor(attributes.getColor(R.styleable.AvlwQuantityOfCommodityEditText_avlwQuantityBorderColor, Color.TRANSPARENT));
        borderPaint.setStyle(Paint.Style.STROKE);

        //设置允许输入的字符串
        setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        //设置输入类型是数字
        setInputType(InputType.TYPE_CLASS_NUMBER);
        attributes.recycle();

        //添加监听(重写函数进行了处理)
        addTextChangedListener(null);

        //控件布局设置宽高处理
        attributes = context.obtainStyledAttributes(attrs,
                new int[]{android.R.attr.layout_width, android.R.attr.layout_height, android.R.attr.paddingStart, android.R.attr.paddingLeft,
                        android.R.attr.paddingEnd, android.R.attr.paddingRight, android.R.attr.paddingTop, android.R.attr.paddingBottom});
        String value = attributes.getString(0);
        String regex = "-[0-9]+";
        if (value != null && !value.matches(regex)) {
            viewLayoutSetWidth = attributes.getDimensionPixelOffset(0, -1);
        }
        value = attributes.getString(1);
        if (value != null && !value.matches(regex)) {
            viewLayoutSetHeight = attributes.getDimensionPixelOffset(1, -1);
        }
        //内边距处理
        firstPaddingLeft = Math.max(attributes.getDimensionPixelOffset(2, firstPaddingLeft),
                Math.max(attributes.getDimensionPixelOffset(3, firstPaddingLeft), firstPaddingLeft));
        firstPaddingRight = Math.max(attributes.getDimensionPixelOffset(4, firstPaddingRight),
                Math.max(attributes.getDimensionPixelOffset(5, firstPaddingRight), firstPaddingRight));
        firstPaddingTop = attributes.getDimensionPixelOffset(6, firstPaddingTop);
        firstPaddingBottom = attributes.getDimensionPixelOffset(7, firstPaddingBottom);

        //设置布局设置数据
        changeQuantity(getText(), true);
        //设置初始数据
        if (getText().toString().isEmpty()) {
            setText(String.valueOf(this.quantity));
        }
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && JtlwCheckVariateUtils.getInstance().isNotEmpty(s.toString())) {
                    changeQuantity(s, true);
                } else {
                    changeQuantity(quantity, false);
                }
            }
        });
    }

    /**
     * 设置改变监听
     *
     * @param onChangeListener 改变监听
     */
    public void setOnChangeListener(AvlwQuantityOfCommodityEditText.OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    /**
     * 设置使用的数据
     *
     * @param data 使用的数据
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 禁用长按事件
     *
     * @return 已执行长按
     */
    @Override
    public boolean performLongClick() {
        return true;
    }

    private float downX;
    private float downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            //手拿起时判断为移动的最小距离
            int touchMinDistance = 100;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getRawX();
                    downY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    //判断点击范围移动
                    if (Math.abs(downX - event.getRawX()) < touchMinDistance && Math.abs(downY - event.getRawY()) < touchMinDistance) {
                        Rect rect = getReduceRectF();
                        //判断抬起位置是否是增加或者减少区域
                        if (event.getX() > rect.left - optionsButtonInsideDistance && event.getX() < rect.right + optionsButtonInsideDistance &&
                                event.getY() > rect.top - optionsButtonInsideDistance && event.getY() < rect.bottom + optionsButtonInsideDistance) {
                            if (reduceClickListener != null) {
                                reduceClickListener.onClick(this);
                            } else {
                                reduceQuantity(true);
                            }
                            return true;
                        }
                        rect = getAddRectF();
                        //判断抬起位置是否是增加或者减少区域
                        if (event.getX() > rect.left - optionsButtonInsideDistance && event.getX() < rect.right + optionsButtonInsideDistance &&
                                event.getY() > rect.top - optionsButtonInsideDistance && event.getY() < rect.bottom + optionsButtonInsideDistance) {
                            if (addClickListener != null) {
                                addClickListener.onClick(this);
                            } else {
                                addQuantity(true);
                            }
                            return true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制图标
        if (allowAdd && addButtonAllowBitmap != null && addButtonAllowBitmapRect != null) {
            canvas.drawBitmap(addButtonAllowBitmap, addButtonAllowBitmapRect, getAddRectF(), null);
        }
        if (!allowAdd && addButtonNotAllowBitmap != null && addButtonNotAllowBitmapRect != null) {
            canvas.drawBitmap(addButtonNotAllowBitmap, addButtonNotAllowBitmapRect, getAddRectF(), null);
        }
        if (allowReduce && reduceButtonAllowBitmap != null && reduceButtonAllowBitmapRect != null) {
            canvas.drawBitmap(reduceButtonAllowBitmap, reduceButtonAllowBitmapRect, getReduceRectF(), null);
        }
        if (!allowReduce && reduceButtonNotAllowBitmap != null && reduceButtonNotAllowBitmapRect != null) {
            canvas.drawBitmap(reduceButtonNotAllowBitmap, reduceButtonNotAllowBitmapRect, getReduceRectF(), null);
        }
        if (borderWidth > 0) {
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            if (borderOnlyShowContent) {
                int left = getReduceRectF().right + optionsButtonInsideDistance + optionsButtonAndTextSeparatedDistance;
                int right = getAddRectF().left - optionsButtonInsideDistance - optionsButtonAndTextSeparatedDistance;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(left, borderWidth.floatValue(), right, getHeight() - borderWidth, borderRadio, borderRadio, borderPaint);
                } else {
                    canvas.drawRoundRect(new RectF(left, borderWidth.floatValue(), right, getHeight() - borderWidth), borderRadio, borderRadio,
                            borderPaint);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(getReduceRectF().left - borderWidth / 2.0F - optionsButtonInsideDistance, borderWidth.floatValue(),
                            getAddRectF().right + optionsButtonInsideDistance + borderWidth / 2.0F, getHeight() - borderWidth.floatValue(),
                            borderRadio, borderRadio, borderPaint);
                } else {
                    canvas.drawRoundRect(new RectF(getReduceRectF().left - borderWidth / 2.0F - optionsButtonInsideDistance, borderWidth.floatValue(),
                                    getAddRectF().right + optionsButtonInsideDistance + borderWidth / 2.0F, getHeight() - borderWidth.floatValue()),
                            borderRadio, borderRadio, borderPaint);
                }
                //绘制分隔线
                borderPaint.setStyle(Paint.Style.FILL);
                float x1 = getReduceRectF().right + optionsButtonInsideDistance + borderWidth / 2.0F + optionsButtonAndTextSeparatedDistance;
                float x2 = getAddRectF().left - borderWidth / 2.0F - optionsButtonInsideDistance - optionsButtonAndTextSeparatedDistance;
                canvas.drawLine(x1, borderWidth.floatValue(), x1, getHeight() - borderWidth, borderPaint);
                canvas.drawLine(x2, borderWidth.floatValue(), x2, getHeight() - borderWidth, borderPaint);
            }
        }
    }

    /**
     * 获取减少按钮显示的位置
     *
     * @return 显示位置
     */
    private Rect getReduceRectF() {
        int left = firstPaddingLeft + borderWidth;
        int top = (int) Math.max((getHeight() - optionsButtonWidth - borderWidth * 2) / 2.0f + borderWidth, firstPaddingTop + borderWidth);
        return new Rect(left + optionsButtonInsideDistance, top + optionsButtonInsideDistance,
                left + optionsButtonWidth - optionsButtonInsideDistance, top + optionsButtonWidth - optionsButtonInsideDistance);
    }

    /**
     * 获取添加按钮要显示的位置
     *
     * @return 显示位置
     */
    private Rect getAddRectF() {
        int left = getWidth() - borderWidth * 2 - optionsButtonWidth - firstPaddingRight;
        int top = (int) Math.max((getHeight() - optionsButtonWidth - borderWidth * 2) / 2.0f + borderWidth, firstPaddingTop + borderWidth);
        return new Rect(left + optionsButtonInsideDistance, top + optionsButtonInsideDistance,
                left + optionsButtonWidth - optionsButtonInsideDistance, top + optionsButtonWidth - optionsButtonInsideDistance);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        resetViewWidthHeight();
    }

    @Override
    public void setGravity(int gravity) {
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        resetViewWidthHeight();
    }

    /**
     * 新增数量
     *
     * @param allowCallbackChange 是否回调修改
     */
    public void addQuantity(boolean allowCallbackChange) {
        long change = quantity % addReduceQuantity;
        if (change == 0) {
            addQuantity(addReduceQuantity, allowCallbackChange);
        } else {
            addQuantity(addReduceQuantity - change, allowCallbackChange);
        }
    }

    /**
     * 新增数量
     *
     * @param allowCallbackChange 是否回调修改
     * @param quantity            数量
     */
    public void addQuantity(long quantity, boolean allowCallbackChange) {
        if (!allowAdd) {
            return;
        }
        changeQuantity(this.quantity + Math.abs(quantity), allowCallbackChange);
    }

    /**
     * 减少数量
     *
     * @param allowCallbackChange 是否回调修改
     */
    public void reduceQuantity(boolean allowCallbackChange) {
        long change = quantity % addReduceQuantity;
        if (change == 0) {
            reduceQuantity(-addReduceQuantity, allowCallbackChange);
        } else {
            reduceQuantity(-change, allowCallbackChange);
        }
    }

    /**
     * 减少数量
     *
     * @param allowCallbackChange 是否回调修改
     * @param quantity            数量
     */
    public void reduceQuantity(long quantity, boolean allowCallbackChange) {
        if (!allowReduce) {
            return;
        }
        changeQuantity(this.quantity - Math.abs(quantity), allowCallbackChange);
    }

    /**
     * 设置每次加减数量
     *
     * @param addReduceQuantity 加减数量
     * @return 当前实例
     */
    public AvlwQuantityOfCommodityEditText setAddReduceQuantity(long addReduceQuantity, boolean allowCallbackChange) {
        this.addReduceQuantity = addReduceQuantity;
        setQuantity(this.quantity, allowCallbackChange);
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
     * 设置当前
     *
     * @param quantity            当前
     * @param allowCallbackChange 是否回调修改
     */
    public void setQuantity(long quantity, boolean allowCallbackChange) {
        changeQuantity(String.valueOf(quantity), allowCallbackChange);
    }

    /**
     * 设置当前，内部修改使用
     *
     * @param quantity            当前
     * @param allowCallbackChange 是否回调修改
     */
    private void changeQuantity(long quantity, boolean allowCallbackChange) {
        if (maxQuantity < addReduceQuantity) {
            quantity = 0;
        } else if (quantity < addReduceQuantity) {
            quantity = addReduceQuantity;
        } else if (quantity > maxQuantity) {
            quantity = maxQuantity;
        } else {
            quantity = quantity / addReduceQuantity * addReduceQuantity;
        }
        changeQuantity(String.valueOf(quantity), allowCallbackChange);
    }

    /**
     * 设置最小数量
     *
     * @param minQuantity         最小数量
     * @param allowCallbackChange 是否回调修改
     * @return 当前实例
     */
    public AvlwQuantityOfCommodityEditText setMinQuantity(long minQuantity, boolean allowCallbackChange) {
        this.minQuantity = minQuantity;
        setQuantity(quantity, allowCallbackChange);
        return this;
    }

    /**
     * 设置允许操作的最大数据
     *
     * @param allowCallbackChange 是否回调修改
     * @param maxQuantity         最大数据数量
     * @return 当前实例
     */
    public AvlwQuantityOfCommodityEditText setMaxQuantity(long maxQuantity, boolean allowCallbackChange) {
        this.maxQuantity = maxQuantity;
        setQuantity(quantity, allowCallbackChange);
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

    /**
     * 设置更新数据
     *
     * @param text                文本显示
     * @param allowCallbackChange 是否回调修改
     */
    private void changeQuantity(CharSequence text, boolean allowCallbackChange) {
        if (text != null && text.toString().matches("[0-9 ]+")) {
            try {
                String contentText = text.toString().replaceAll(" ", "");
                if (contentText.isEmpty()) {
                    this.quantity = 0;
                    String value = String.valueOf(this.quantity);
                    setText(value);
                    setSelection(value.length());
                    resetViewWidthHeight();
                    if (allowCallbackChange && onChangeListener != null) {
                        onChangeListener.changeEnd(data, this.quantity);
                    }
                } else {
                    long quantity = Long.parseLong(contentText);
                    if (quantity >= maxQuantity) {
                        quantity = maxQuantity;
                        allowAdd = false;
                    } else {
                        allowAdd = true;
                    }
                    if (quantity <= minQuantity) {
                        quantity = minQuantity;
                        allowReduce = false;
                    } else {
                        allowReduce = true;
                    }
                    if (!contentText.equals(String.valueOf(this.quantity))) {
                        this.quantity = quantity;
                        String value = String.valueOf(this.quantity);
                        setText(value);
                        setSelection(value.length());
                        resetViewWidthHeight();
                        if (allowCallbackChange && onChangeListener != null) {
                            onChangeListener.changeEnd(data, this.quantity);
                        }
                    }
                }
            } catch (Exception e) {
                JtlwLogUtils.logUtils.logE(this.TAG, "传递的参数非整数参数");
            }
        }
        postInvalidate();
    }

    /**
     * 重置控件宽高
     */
    private void resetViewWidthHeight() {
        int realWidth = viewLayoutSetWidth;
        int realHeight = viewLayoutSetHeight;
        float textWidth = AtlwViewUtil.getInstance().getStrTextWidth(getPaint(), getText() != null ? getText().toString() : "");
        float textHeight = AtlwViewUtil.getInstance().getStrTextHeight(getPaint());
        if (realWidth < 0) {
            //配置设置宽度
            realWidth = (int) (Math.max(textWidth, textShowWidth) + optionsButtonWidth * 2 + firstPaddingLeft + firstPaddingRight + borderWidth * 4 +
                    textInsideDistance * 2 + optionsButtonAndTextSeparatedDistance * 2);
        }
        if (realHeight < 0) {
            realHeight = (int) (Math.max(textHeight, optionsButtonWidth + borderWidth * 2) + firstPaddingTop + firstPaddingBottom);
        }
        super.setPadding(Math.max((int) ((realWidth - firstPaddingLeft - firstPaddingRight - textWidth) / 2.0F + firstPaddingLeft), 0),
                (int) ((realHeight - firstPaddingTop - firstPaddingBottom - textHeight) / 2.0F + firstPaddingTop),
                (int) ((realWidth - firstPaddingLeft - firstPaddingRight - textWidth) / 2.0F + firstPaddingRight),
                (int) ((realHeight - firstPaddingTop - firstPaddingBottom - textHeight) / 2.0F + firstPaddingBottom));
        setMeasuredDimension(realWidth, realHeight);
    }

    /**
     * 改变监听
     */
    public interface OnChangeListener {
        /**
         * 改变结束数量
         *
         * @param count 当前数量
         */
        void changeEnd(Object data, long count);
    }
}

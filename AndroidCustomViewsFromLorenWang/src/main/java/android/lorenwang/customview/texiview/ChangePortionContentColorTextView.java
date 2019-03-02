package android.lorenwang.customview.texiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.lorenwang.customview.R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 创建时间：2016.8.11
 * 创建人：王亮
 * 功能作用：可变字符串文字大小颜色
 * 逻辑：重绘制文字，使用记录类（MySpannableString）记录所要修改的区间信息，在使用重绘重新绘制，绘制之前根据用户给的位置信息计算位置坐标，
 * 缺点：只能单行显示，一行排满不会切换到下一行显示
 */
public class ChangePortionContentColorTextView extends View {
    private Paint paint;
    private String changeText = "123456";
    private Float defaltTextSize ;
    private static Integer defaltTextColor = Color.BLACK;
    private MySpannableString mySpannableString;
    private static Context context;

    public ChangePortionContentColorTextView(Context context) {
        super(context);
        this.context = context;
    }
    public ChangePortionContentColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public ChangePortionContentColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ChangePortionContentColorTextView, defStyleAttr, 0);
        defaltTextSize = attr.getDimension(R.styleable.ChangePortionContentColorTextView_defaltTextSize, Float.valueOf(sp2px(context,10f)));
        defaltTextColor = attr.getColor(R.styleable.ChangePortionContentColorTextView_defaltTextColor,defaltTextColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        setMeasuredDimension(width, height);

        viewWidth = getWidth();
        viewHeight = getHeight();

        paint = new Paint();
        paint.setColor(defaltTextColor);
        paint.setTextSize(defaltTextSize);
        paint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getHeight() != 0 && mySpannableString != null){

            List<MySpannableStringSetInfoDto> list = mySpannableString.getList();

            if(list .size() > 0 && list.get(0).getStart() > 0){//绘制第一个改变的起始点之前的文字
                canvas.drawText(changeText.substring(0,list.get(0).getStart())
                        ,showTextCenterX,showTextBaselineY,paint);
            }
            float showTextWidth = list.size() > 0 ?  paint.measureText(changeText,0,list.get(0).getStart()) : 0;//计算并保存每次已经绘制过得总的宽度，以保证下次绘制的部分在前一部分之后
            //绘制中间部分（不包含起始改变点之前以及最后一个绘制的结束点之后的部分）
            for(int i = 0 ; i < list.size();i++){
                Paint paintChange = new Paint();
                paintChange.setTextSize(list.get(i).getTextSize());
                paintChange.setColor(list.get(i).getTextColor());
                paintChange.setAntiAlias(true);
                canvas.drawText(changeText.substring(list.get(i).getStart(),list.get(i).getStop())
                        ,showTextCenterX + showTextWidth,showTextBaselineY,paintChange);//绘制需要改变的部分

                //计算并保存每次已经绘制过得总的宽度，以保证下次绘制的部分在前一部分之后
                showTextWidth += paintChange.measureText(changeText,list.get(i).getStart(),list.get(i).getStop());

                //绘制两个要修改的区间的部分
                if(i + 1 != list.size() && list.get(i).getStop() < list.get(i + 1).getStart()){
                    canvas.drawText(changeText.substring(list.get(i).getStop(),list.get(i + 1).getStart())
                            ,showTextCenterX + showTextWidth,showTextBaselineY,paint);

                    //计算并保存每次已经绘制过得总的宽度，以保证下次绘制的部分在前一部分之后
                    showTextWidth += paint.measureText(changeText,list.get(i).getStop(),list.get(i + 1).getStart());
                }
            }
            //绘制最后一个区间的终点之后的部分
            if(list.get(list.size() - 1).getStop() < changeText.length() ) {
                canvas.drawText(changeText.substring(list.get(list.size() - 1).getStop(), changeText.length())
                        , showTextCenterX + showTextWidth, showTextBaselineY, paint);
            }
        }
    }

    //设置文字以及显示位置
    public void setText(MySpannableString mySpannableString,int showTextAlign){
        this.mySpannableString = mySpannableString;
        this.showTextAlign = showTextAlign;
        this.changeText = mySpannableString.text;
        viewWidth = getWidth();
        viewHeight = getHeight();
    }



    /** 文字的方位 */
    private int showTextAlign;
    /** 左上角显示  */
    public static final int TEXT_ALIGN_LEFT_TOP                 = 0x00000001;
    /** 左侧中间显示  */
    public static final int TEXT_ALIGN_LEFT_CENTER              = 0x00000010;
    /** 左侧下方显示  */
    public static final int TEXT_ALIGN_LEFT_BOTTOM              = 0x00000100;
    /** 右上角显示  */
    public static final int TEXT_ALIGN_RIGHT_TOP                = 0x00001000;
    /** 右侧中间显示 */
    public static final int TEXT_ALIGN_RIGHT_CENTER             = 0x00010000;
    /** 右侧下方显示  */
    public static final int TEXT_ALIGN_RIGHT_BOTTOM             = 0x00100000;
    /** 中间顶部显示  */
    public static final int TEXT_ALIGN_CENTER_HORIZONTAL_TOP    = 0x00001000|0x00010000;
    /** 中间下方显示  */
    public static final int TEXT_ALIGN_CENTER_HORIZONTAL_BOTTOM = 0x10000010;
    /** 居中显示  */
    public static final int TEXT_ALIGN_CENTER                   = 0x10000100;
    /** 居中靠上显示  */
    public static final int TEXT_ALIGN_CENTER_TOP               = 0x10001000;
    /** 居中靠下显示  */
    public static final int TEXT_ALIGN_CENTER_BOTTOM            = 0x10010000;
    /** 居中靠左显示  */
    public static final int TEXT_ALIGN_CENTER_LEFT              = 0x10100000;
    /** 居中靠右显示  */
    public static final int TEXT_ALIGN_CENTER_RIGHT             = 0x11000000;
    /** 居中靠左上显示  */
    public static final int TEXT_ALIGN_CENTER_LEFT_TOP          = 0x11000001;
    /** 居中靠左下显示  */
    public static final int TEXT_ALIGN_CENTER_LEFT_BOTTOM       = 0x11000010;
    /** 居中靠右上显示  */
    public static final int TEXT_ALIGN_CENTER_RIGHT_TOP         = 0x11000100;
    /** 居中靠右下显示  */
    public static final int TEXT_ALIGN_CENTER_RIGHT_BOTTOM      = 0x11001000;
    /**   */
    /** 文本中轴线X坐标 */
    private Float showTextCenterX;
    /** 文本baseline线Y坐标 */
    private Float showTextBaselineY;
    /** 控件的宽度 */
    private Integer viewWidth;
    /** 控件的高度 */
    private Integer viewHeight;
    private Paint.FontMetrics fm;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        viewWidth = getWidth();
        viewHeight = getHeight();
        if(changeText != null) {
            setTextLocation();
            postInvalidate();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 定位文本绘制的位置
     */
    private void setTextLocation() {
        List<MySpannableStringSetInfoDto> list = mySpannableString.getList();
        int maxTextSize = 0;//最大的文字的大小
        float textWidth = 0;  //文本的宽度
        for(int i = 0 ; i < list.size() ; i++){//找出最大的字体的大小
            if(list.get(i).getTextSize() > maxTextSize){
                maxTextSize = list.get(i).getTextSize();
            }
            Paint paint = new Paint();
            paint.setTextSize(list.get(i).getTextSize());
            textWidth += paint.measureText(mySpannableString.text,list.get(i).getStart(),list.get(i).getStop());
        }

        Paint paint1 = new Paint();
        paint1.setTextSize(defaltTextSize);
        textWidth += paint1.measureText(mySpannableString.text,0,mySpannableString.text.length() - mySpannableString.changeCharNum);

        Paint paint = new Paint();
        paint.setTextSize(maxTextSize);
        fm = paint.getFontMetrics();

        float textHeight = fm.descent - fm.ascent;
        float textCenterVerticalBaselineY = viewHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
        switch (showTextAlign) {
            case TEXT_ALIGN_CENTER://居中
                showTextCenterX = (float)viewWidth / 2 - textWidth / 2;
                showTextBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_LEFT_CENTER://居中靠最左
                showTextCenterX = 0f;
                showTextBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_RIGHT_CENTER://居中靠最右
                showTextCenterX = viewWidth - textWidth;
                showTextBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_CENTER_HORIZONTAL_BOTTOM://居中靠最下
                showTextCenterX = Float.valueOf(viewWidth / 2 - textWidth / 2);
                showTextBaselineY = viewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_CENTER_HORIZONTAL_TOP://居中靠最上
                showTextCenterX = Float.valueOf(viewWidth / 2 - textWidth / 2);
                showTextBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_LEFT_TOP://左上
                showTextCenterX = 0f;
                showTextBaselineY =  -fm.ascent;
                break;
            case TEXT_ALIGN_LEFT_BOTTOM://左下
                showTextCenterX = 0f;//
                showTextBaselineY = viewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_RIGHT_TOP://右上
                showTextCenterX = viewWidth - textWidth;
                showTextBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_RIGHT_BOTTOM://右下
                showTextCenterX = viewWidth - textWidth;
                showTextBaselineY = viewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_CENTER_TOP://居中靠上
                showTextCenterX = (float)viewWidth / 2 - textWidth / 2;
                showTextBaselineY = textCenterVerticalBaselineY - textHeight / 2;
                break;
            case TEXT_ALIGN_CENTER_BOTTOM:/** 居中靠下显示  */
                showTextCenterX = (float)viewWidth / 2 - textWidth / 2;
                showTextBaselineY = textCenterVerticalBaselineY + textHeight / 2;
                break;
            case TEXT_ALIGN_CENTER_LEFT:/** 居中靠左显示  */
                showTextCenterX = (float)viewWidth / 2 - textWidth ;
                showTextBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_CENTER_RIGHT:/** 居中靠右显示  */
                showTextCenterX = (float)viewWidth / 2 + textWidth ;
                showTextBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_CENTER_LEFT_TOP:/** 居中靠左上显示  */
                showTextCenterX = (float)viewWidth / 2 - textWidth ;
                showTextBaselineY = textCenterVerticalBaselineY - textHeight ;
                break;
            case TEXT_ALIGN_CENTER_LEFT_BOTTOM:/** 居中靠左下显示  */
                showTextCenterX = (float)viewWidth / 2 - textWidth ;
                showTextBaselineY = textCenterVerticalBaselineY + textHeight ;
                break;
            case TEXT_ALIGN_CENTER_RIGHT_TOP:/** 居中靠右上显示  */
                showTextCenterX = (float)viewWidth / 2 + textWidth ;
                showTextBaselineY = textCenterVerticalBaselineY - textHeight ;
                break;
            case TEXT_ALIGN_CENTER_RIGHT_BOTTOM:/** 居中靠右下显示  */
                showTextCenterX = (float)viewWidth / 2 + textWidth ;
                showTextBaselineY = textCenterVerticalBaselineY + textHeight ;
                break;
        }
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     *
     * @param context
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 负责存储要修改的文字以及要修改的区间的参数
     */
    public static class MySpannableString{
        private List<MySpannableStringSetInfoDto> list = new ArrayList<>();
        private String text;
        private int changeCharNum = 0;

        public MySpannableString(String text) {
            this.text = text;
        }

        protected List<MySpannableStringSetInfoDto> getList() {
            return list;
        }

        /**
         *  设置要改变的文字所在的位置以及要改变的文字的大小以及颜色，可以多次设置，多次设置则改变多处位置
         * @param start
         * @param stop
         * @param textSize
         * @param textColor
         */
        public void setMySpans(int start,int stop,int textSize,Integer textColor){
            try {
                for(int i = 0; i < list.size() ; i++){
                    if((start >= list.get(i).getStart() && start < list.get(i).getStop())
                            || (stop > list.get(i).getStart() && stop <= list.get(i).getStop())
                            || start > stop){
                        new Exception();
                        return;
                    }
                }
            }catch (Exception e){
                Log.e("MySpannableString","End position can not be more than the previous starting point! "
                        +"or"
                        +" Starting position can not exceed the previous end! "
                        +"start > stop");//结束位不能超过以前的起点！或 起始位置不能超过以前的终点！或起点大于终点
            }
            MySpannableStringSetInfoDto dto;
            if(textColor == null){
                dto = new MySpannableStringSetInfoDto(start,stop,sp2px(context, textSize),defaltTextColor);
            }else {
                dto = new MySpannableStringSetInfoDto(start,stop,sp2px(context, textSize),textColor);
            }
            changeCharNum += stop - start;
            list.add(dto);
        }

    }

    /**
     * 修改区间参数类
     */
    private static class MySpannableStringSetInfoDto implements Comparator<MySpannableStringSetInfoDto> {
        private int start;
        private int stop;
        private int textSize;
        private int textColor;


        protected MySpannableStringSetInfoDto(int start, int stop, int textSize, int textColor) {
            this.start = start;
            this.stop = stop;
            this.textSize = textSize;
            this.textColor = textColor;
        }

        protected int getStart() {
            return start;
        }

        protected int getStop() {
            return stop;
        }

        protected int getTextSize() {
            return textSize;
        }

        protected int getTextColor() {
            return textColor;
        }

        @Override
        public int compare(MySpannableStringSetInfoDto t1, MySpannableStringSetInfoDto t2) {
            if(t1.start > t2.start){
                return -1;
            }else if(t1.start < t2.start){
                return 1;
            }else {
                return 0;
            }
        }
    }

}


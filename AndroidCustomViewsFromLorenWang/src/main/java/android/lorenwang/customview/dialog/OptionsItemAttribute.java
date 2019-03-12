package android.lorenwang.customview.dialog;

/**
 * 创建时间：2019-03-06 下午 12:03:39
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class OptionsItemAttribute {
    private String title;
    private Float textSize;
    private Integer textSizeUnit;
    private Integer textColor;
    private Integer textHeight;
    private Integer paddingLeft;
    private Integer paddingTop;
    private Integer paddingRight;
    private Integer paddingBottom;
    private Integer gravity;

    public OptionsItemAttribute(String title, Float textSize, Integer textSizeUnit, Integer textColor, Integer textHeight
            , Integer paddingLeft, Integer paddingTop, Integer paddingRight, Integer paddingBottom, Integer gravity) {
        this.title = title;
        this.textSize = textSize;
        this.textSizeUnit = textSizeUnit;
        this.textColor = textColor;
        this.textHeight = textHeight;
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
        this.gravity = gravity;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTextSizeUnit() {
        return textSizeUnit;
    }

    public Float getTextSize() {
        return textSize;
    }

    public Integer getTextColor() {
        return textColor;
    }

    public Integer getTextHeight() {
        return textHeight;
    }

    public Integer getPaddingLeft() {
        return paddingLeft;
    }

    public Integer getPaddingTop() {
        return paddingTop;
    }

    public Integer getPaddingRight() {
        return paddingRight;
    }

    public Integer getPaddingBottom() {
        return paddingBottom;
    }

    public Integer getGravity() {
        return gravity;
    }
}

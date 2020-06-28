import React from "react";

/**
 * 功能作用：
 * 初始注释时间： 2020/6/22 2:58 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：da
 * 备注：
 *
 * @author LorenWang（王亮）
 */
const RcblwCommonUtil = {
    /**
     *
     * @param hex 例如:"#23ff45"
     * @param opacity 透明度
     * @returns {string}
     */
    hexToRgba(hex, opacity) {
        return "rgba(" + parseInt("0x" + hex.slice(1, 3)) + "," + parseInt("0x" + hex.slice(3, 5)) + "," + parseInt("0x" + hex.slice(5, 7)) + "," + opacity + ")";
    }
}
export default RcblwCommonUtil

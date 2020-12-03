/**
 * 功能作用：js通用工具
 * 初始注释时间： 2020/12/3 1:40 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 16禁止hae颜色值转rgb带透明度颜色（hexToRgba）
 * 字符串转字符流（str2Ab）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
const JstlwCommonUtil = {
    /**
     * 16禁止hae颜色值转rgb带透明度颜色
     * @param hex 例如:"#23ff45"
     * @param opacity 透明度
     * @returns {string}
     */
    hexToRgba(hex, opacity) {
        return "rgba(" + parseInt("0x" + hex.slice(1, 3)) + "," + parseInt("0x" + hex.slice(3, 5)) + "," + parseInt("0x" + hex.slice(5, 7)) + "," + opacity + ")";
    },
    /**
     * 字符串转字符流
     * @param s 字符串
     * @returns {ArrayBuffer} 字节流
     */
    str2Ab(s) {
        const buf = new ArrayBuffer(s.length);
        const view = new Uint8Array(buf);
        for (let i = 0; i !== s.length; ++i) {
            view[i] = s.charCodeAt(i) & 0xFF
        }
        return buf
    }
}
export default JstlwCommonUtil

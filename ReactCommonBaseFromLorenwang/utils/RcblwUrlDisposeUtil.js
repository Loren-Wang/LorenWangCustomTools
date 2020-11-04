import RcblwVariableDisposeUtil from "./RcblwVariableDisposeUtil";

/**
 * 功能作用：url相关处理
 * 初始注释时间： 2020/3/13 15:28
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
const RcblwUrlDisposeUtil = {
    /**
     * 格式化url地址获取参数值
     * @param key 参数名称
     * @returns {string} 参数值
     */
    getUrlParams(key) {
        if (RcblwVariableDisposeUtil.isParamsTypeString(key)) {
            //地址转码
            const url = decodeURI(document.URL);
            const reg = new RegExp("[?|&]" + key + "=[^&]+");
            let arg = url.substr(1).match(reg);
            if (arg != null) {
                arg = unescape(arg.toString())
                return arg.substr(arg.indexOf("=") + 1);
            }
        }
        return key
    }
};
export default RcblwUrlDisposeUtil;

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
            let arg = url.substr(1).match(new RegExp("[?|&]" + key + "=[^&]+"));
            if (arg != null) {
                arg = unescape(arg.toString())
                return arg.substr(arg.indexOf("=") + 1);
            }
        }
        return key
    },
    /**
     * 向url中添加参数，如果已有参数则直接替换
     * @param url 当前url
     * @param key 参数key
     * @param value 参数值
     */
    addUrlParams(url, key, value) {
        if (!RcblwVariableDisposeUtil.isParamsEmptyStr(url)
            && !RcblwVariableDisposeUtil.isParamsEmptyStr(key)
            && !RcblwVariableDisposeUtil.isParamsEmptyStr(value)) {
            const oldValue = this.getUrlParams(key)
            if (!RcblwVariableDisposeUtil.isParamsEmptyStr(oldValue)) {
                //替换旧数据
                return url.replace(key + "=" + oldValue, key + "=" + value)
            } else {
                //没有数据则新增
                if (url.indexOf("?") >= 0) {
                    return url + "&" + key + "=" + value
                } else {
                    return url + "?" + key + "=" + value
                }
            }
        } else {
            return url
        }
    }
};
export default RcblwUrlDisposeUtil;

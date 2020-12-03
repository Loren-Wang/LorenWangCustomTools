import JstlwVariableDisposeUtil from "./JstlwVariableDisposeUtil";

/**
 * 功能作用：网络相关处理工具类
 * 初始注释时间： 2020/12/3 1:48 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 格式化url地址获取参数值(getUrlParams)
 * 向url中添加参数，如果已有参数则直接替换(addUrlParams)
 * 文件下载响应处理（downLoadFileResponseDispose）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
const JstlwNetworkDisposeUtil = {
    /**
     * 格式化url地址获取参数值
     * @param key 参数名称
     * @param netUrl 要格式化的url
     * @returns {string} 参数值
     */
    getUrlParams(key, netUrl) {
        if (JstlwVariableDisposeUtil.isParamsTypeString(key)) {
            //地址转码
            const url = decodeURI(JstlwVariableDisposeUtil.isParamsEmptyStr(netUrl) ? document.URL : netUrl);
            let arg = url.substr(1).match(new RegExp("[?|&]" + key + "=[^&]+"));
            if (arg != null) {
                arg = unescape(arg.toString())
                return arg.substr(arg.indexOf("=") + 1);
            }
        }
        return null
    },
    /**
     * 向url中添加参数，如果已有参数则直接替换
     * @param url 当前url
     * @param key 参数key
     * @param value 参数值
     */
    addUrlParams(url, key, value) {
        if (!JstlwVariableDisposeUtil.isParamsEmptyStr(url)
            && !JstlwVariableDisposeUtil.isParamsEmptyStr(key)
            && !JstlwVariableDisposeUtil.isParamsEmptyStr(value)) {
            const oldValue = this.getUrlParams(key, url)
            if (!JstlwVariableDisposeUtil.isParamsEmptyStr(oldValue)) {
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
    },
    /**
     * 文件下载响应处理
     * @param responseBlob 响应的数据体，blob类型
     * @param fileName 文件下载名称（全名称，包括后缀）
     * @returns {boolean} 执行流程是否成功，但是成功并不代表下载成功，true为成功
     */
    downLoadFileResponseDispose(responseBlob, fileName) {
        if (JstlwVariableDisposeUtil.isParamsTypeBlob(responseBlob)
            && !JstlwVariableDisposeUtil.isParamsEmptyStr(fileName)) {
            let r = new FileReader();
            r.onload = function () {
                const link = document.createElement('a');
                link.style.display = 'none';
                link.href = URL.createObjectURL(responseBlob);
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            };
            r.readAsText(responseBlob);
            return true;
        }
        return false;
    }
}
export default JstlwNetworkDisposeUtil

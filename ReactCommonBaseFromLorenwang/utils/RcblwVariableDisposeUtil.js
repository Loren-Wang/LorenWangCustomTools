/**
 * 功能作用：变量管理工具
 * 初始注释时间： 2020/3/13 14:33
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
const RcblwVariableDisposeUtil = {
    /**
     *
     * 获取参数类型
     * @param params 参数
     * @returns {any} 参数不为空返回参数类型，否则
     */
    getParamsType(params) {
        return params != null ? Object.prototype.toString.call(params).constructor : null
    },
    /**
     * 判断参数是否是字符串
     * @param params 参数
     * @returns {boolean} 是否是字符串，true为是
     */
    isParamsTypeString(params) {
        return params != null && this.getParamsType(params) === String
    },
    /**
     * 判断参数是否是blob
     * @param params 参数
     * @returns {boolean} true为是
     */
    isParamsTypeBlob(params) {
        return params != null && this.getParamsType(params) === Blob
    },
    /**
     * 判断参数是否为空
     * @param params 参数
     * @return boolean|boolean 空为true
     */
    isParamsEmpty(params) {
        return params == null || this.getParamsType(params) === undefined
    },
    /**
     * 判断参数是否为空字符串或者空
     * @param params 参数
     * @return boolean|boolean 空为true
     */
    isParamsEmptyStr(params) {
        return this.isParamsEmpty(params) || (this.isParamsTypeString(params) && params.length === 0)
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
};
export default RcblwVariableDisposeUtil

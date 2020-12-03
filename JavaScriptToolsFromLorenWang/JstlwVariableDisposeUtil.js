/**
 * 功能作用：变量处理工具类
 * 初始注释时间： 2020/12/3 1:44 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取参数类型（getParamsType）
 * 判断参数是否是字符串（isParamsTypeString）
 * 判断参数是否是blob（isParamsTypeBlob）
 * 判断参数是否为空（isParamsEmpty）
 * 判断参数是否为空字符串或者空（isParamsEmptyStr）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
const JstlwVariableDisposeUtil = {
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
    }
}
export default JstlwVariableDisposeUtil

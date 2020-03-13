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
     * 获取参数类型
     * @param params 参数
     * @returns  参数类型
     */
    getParamsType(params) {
        return Object.prototype.toString.call(params).constructor
    },
    /**
     * 判断参数是否是字符串
     * @param params 参数
     * @returns {boolean} 是否是字符串，true为是
     */
    paramsIsString(params) {
        return jstlwGetParamsType(params) === String
    },
    /**
     * 判断参数是否为空
     * @param params 参数
     * @return boolean|boolean 空为true
     */
    paramsIsEmpty(params) {
        return params == null || this.getParamsType(params) === undefined || (this.paramsIsString(params) && params === "")
    },
    /**
     * 判断参数是否为空字符串或者空
     * @param params 参数
     * @return boolean|boolean 空为true
     */
    paramsIsEmptyStr(params) {
        return this.paramsIsEmpty(params) || (this.paramsIsString(params) && params.length === 0)
    }
};
export default RcblwVariableDisposeUtil

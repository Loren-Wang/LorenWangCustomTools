import RcblwVariableDisposeUtil from "./RcblwVariableDisposeUtil";

/**
 * 功能作用：网络相关处理
 * 初始注释时间： 2020/3/13 15:28
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
const RcblwNetworkDisposeUtil = {
    /**
     * 文件下载响应处理,针对于response成功状态
     * @param responseBlob 响应的数据体，blob类型
     * @param fileName 文件下载名称（全名称，包括后缀）
     * @returns {boolean} 执行流程是否成功，但是成功并不代表下载成功，true为成功
     */
    downLoadFileResponseDispose(responseBlob, fileName) {
        if (RcblwVariableDisposeUtil.isParamsTypeBlob(responseBlob)
            && !RcblwVariableDisposeUtil.isParamsEmptyStr(fileName)) {
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
};
export default RcblwNetworkDisposeUtil;

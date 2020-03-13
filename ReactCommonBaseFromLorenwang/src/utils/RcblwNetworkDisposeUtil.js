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
const RcblwNetworkDisposeUtil = {
    /**
     * 文件下载响应处理
     */
    downLoadFileResponseDispose(responseBlob:Blob,){
        let r = new FileReader();
        r.onload = function () {
            const filename = response.headers["content-disposition"];
            const index = filename.search(/filename=/);
            const filenames = filename.substring(index + 9, filename.length);
            const link = document.createElement('a');
            link.style.display = 'none';
            link.href = URL.createObjectURL(response.data);
            link.setAttribute('download', filenames);
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        };
        r.readAsText(response.data)
    }
};
export default RcblwNetworkDisposeUtil;

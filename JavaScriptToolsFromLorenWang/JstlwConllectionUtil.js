/**
 * 功能作用：集合工具类
 * 初始注释时间： 2020/12/3 1:42 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 向列表中添加key字段,并返回数据
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
const JstlwConllectionUtil = {
    /**
     * 向列表中添加key字段,并返回数据
     * @param list 数据列表
     * @param keyStr key字段值为item中该keyStr对应的字段值
     * @return 返回处理后的数据列表
     */
    addKeyAndResultList(list, keyStr = null) {
        //返回数据
        const resultList = [];
        //开始遍历
        try {
            if (list && list.length) {
                list.forEach((item, index) => {
                    resultList.push({
                        key: keyStr != null && keyStr !== "" ? item[keyStr] : index,
                        ...item
                    })
                });
            }
        } catch (e) {
        }
        return resultList;
    }
}
export default JstlwConllectionUtil

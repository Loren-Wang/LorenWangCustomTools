/**
 * 功能作用：集合相关操作工具类
 * 初始注释时间： 2020/5/6 3:41 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 *@author LorenWang（王亮）
 */
const RcblwConllectionUtil = {
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

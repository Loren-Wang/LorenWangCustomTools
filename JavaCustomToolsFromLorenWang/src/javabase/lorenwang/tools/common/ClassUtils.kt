package javabase.lorenwang.tools.common

/**
 * 创建时间：2019-03-21 下午 13:44:29
 * 创建人：王亮（Loren wang）
 * 功能作用：class 工具类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class ClassUtils {
    companion object {
        private var classUtils:ClassUtils? = null
        fun getInstance():ClassUtils{
            if(classUtils == null){
                classUtils = ClassUtils()
            }
            return classUtils!!
        }
    }

    /**
     * 获取指定类的实体
     */
    fun <T> getClassEntity(clazz: Class<T>): T? {
        // 声明一个空的BaseModel
        var model: T? = null
        // 判断class对象是不是BaseModel的实例
        try {
            model = clazz.newInstance()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }

        return model
    }
}

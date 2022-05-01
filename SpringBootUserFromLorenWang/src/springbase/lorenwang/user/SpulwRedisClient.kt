package springbase.lorenwang.user

import com.google.gson.Gson
import kotlinbase.lorenwang.tools.extend.kttlwFormatConversion
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

/**
 * 功能作用：redis工具类
 * 初始注释时间： 2022/5/1 13:03
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
abstract class SpulwRedisClient {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    /**
     * 添加key到redis数据库中
     */
    @Throws(Exception::class)
    operator fun set(key: String, value: String) {
        val operations = redisTemplate.opsForValue()
        operations.set(key, value)
    }

    @Throws(Exception::class)
    operator fun set(key: String, value: String, timeOut: Int) {
        val operations = redisTemplate.opsForValue()
        val times = if (timeOut > 0) {
            timeOut * 60.0
        } else {
            getRedisCacheTimeout()
        }
        operations.set(key, value, times.toLong(), TimeUnit.SECONDS)
    }

    /**
     * 取值key到redis数据库中
     */
    @Throws(Exception::class)
    operator fun get(key: String): Any? {
        val operations = redisTemplate.opsForValue()
        return operations[key]
    }

    /**
     * 删除指定key
     */
    fun del(key: String) {
        redisTemplate.delete(key)
    }

    /**
     * Set集合的赋值去取
     */
    fun setSetCollections(key: String, value: Set<*>?) {
        redisTemplate.opsForSet().add(key, value)
    }

    /**
     * Set集合的赋值去取
     */
    fun getSetCollections(key: String): String {
        val result = Gson().toJson(redisTemplate.opsForSet().members(key))
        return result.substring(1, result.length - 1)
    }

    fun getMapKeys(key: String): Set<String> {
        return redisTemplate.opsForHash<String, Any>().keys(key)
    }

    /**
     * Map集合的赋值去取
     */
    fun setMapCollections(key: String, value: Map<String?, Any?>) {
        redisTemplate.opsForHash<String, Any>().putAll(key, value)
    }

    fun getMapCollections(key: String): String {
        return Gson().toJson(redisTemplate.opsForHash<String, Any>().entries(key))
    }

    /**
     * List集合的赋值去取
     */
    fun <T> setLists(key: String, list: List<T>) {
        redisTemplate.opsForList().leftPush(key, list)
    }

    /**
     * 获取list集合
     */
    fun <T> getListStartEnd(key: String, start: Long = 0, end: Long = Long.MAX_VALUE): MutableList<T> {
        return redisTemplate.opsForList().range(key, start, end).kttlwFormatConversion<MutableList<T>>().kttlwGetNotEmptyData { mutableListOf() }
    }

    /**查询key的剩余存活时间 */
    fun getKeyExpireTime(key: String): Long {
        return redisTemplate.getExpire(key)
    }

    /**设置key的剩余存活时间 */
    fun setKeyExpireTime(key: String, timeOut: Int): Boolean {
        var times: Long = 0
        times = if (timeOut > 0) {
            (timeOut * 60).toLong()
        } else {
            getRedisCacheTimeout()
        }
        return java.lang.Boolean.TRUE == redisTemplate.expire(key, times, TimeUnit.SECONDS)
    }

    /**判断key是否存在 */
    fun exitsKey(key: String): Boolean {
        val obj = redisTemplate.execute { connection -> connection.exists(key.toByteArray()) }!!
        val flag = true
        return if (obj.toString() == "false") {
            false
        } else flag
    }

    /**
     * 获取redis缓存超时是阿金
     */
    abstract fun getRedisCacheTimeout(): Long

}
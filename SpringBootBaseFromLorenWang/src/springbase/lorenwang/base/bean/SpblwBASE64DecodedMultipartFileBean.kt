package springbase.lorenwang.base.bean

import org.springframework.web.multipart.MultipartFile
import java.io.*

/**
 * 功能作用：将base64图片字节数组转换为文件MultipartFile类
 * 创建时间：2019-12-16 10:55
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SpblwBASE64DecodedMultipartFileBean(private val imgContent: ByteArray, private val contentType: String) : MultipartFile {

    override fun getName(): String {
        return "xxxx.${contentType.replace(Regex(".*/"), "")}"
    }

    override fun getOriginalFilename(): String? {
        return "xxxx.${contentType.replace(Regex(".*/"), "")}"
    }

    override fun getContentType(): String? {
        return contentType
    }

    override fun isEmpty(): Boolean {
        return imgContent.isEmpty()
    }

    override fun getSize(): Long {
        return imgContent.size.toLong()
    }

    @Throws(IOException::class)
    override fun getBytes(): ByteArray {
        return imgContent
    }

    @Throws(IOException::class)
    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(imgContent)
    }

    @Throws(IOException::class, IllegalStateException::class)
    override fun transferTo(dest: File) {
        FileOutputStream(dest).write(imgContent)
    }
}

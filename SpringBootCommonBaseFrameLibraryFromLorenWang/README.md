spingboot基础框架包

<h3>配置文件  SbcbflwPropertiesConfig

      配置参数1、阿里云oss系统域名---aLiYunOssDomain
      配置参数2、阿里云oss访问域名---aLiYunOssEndpoint
      配置参数3、阿里云oss系统keyid---aLiYunOssAccessKeyId
      配置参数4、阿里云oss系统密钥---aLiYunOssAccessKeySecret
      配置参数5、阿里云存储空间名---aLiYunOssBucket
      配置参数6、加密用的Key---encryptDecryptKey(可以用26个字母和数字组成 使用AES-128-CBC加密模式，key需要为16位。)
      配置参数7、加密解密的算法参数---encryptDecryptIvParameter
      配置参数8、邮箱端口---emailHost
      配置参数9、邮箱用户名称---emailUserName
      配置参数10、邮箱密码---emailUserPassword
      配置参数11、邮箱配置文件---emailProperties(配置文件名称：application-email.properties

<h3>一、SbcbflwALiYunOssUtils---（阿里云文件上传工具类）

      1、获取图片地址---getImageUrl(isFullPath,imgPath)
      2、清除图片域名信息---clearImageDomain(imgPath)
      3、是否有地址域名---haveImageDomain(imgPath)
      4、上传MultipartFile文件---upLoadFile(file,savePath)
      5、上传文件流---upLoadFile(inputStream,savePath)
      6、下载其他网络文件并将其上传到oss当中---downLoadAndUpLoadFile(downLoadPath,savePath)
      7、上传字节数组文件---upLoadFile(bytes,contentType,savePath)
      
<h3>二、SbcbflwEncryptDecryptUtils---（字符串加密解密工具）

      1、加密字符串---encrypt(str)
      2、解密字符串---decrypt(str)
      3、带key和加密参数的加密字符串---encrypt(str,ketStr,ivsStr)
      4、带key和加密参数的解密字符串---decrypt(str,ketStr,ivsStr)
      
<h3>三、SbcbflwPasswordUtils---（密码处理相关）

<h3>四、SbcbflwCommonUtils---（通用方法）

      1、获取配置文件内容---getProperties(propertiesName)
      
      
<h3>五、SbcbflwEmailUtils---（邮件发送工具）

      1、发送邮件---sendEmailMessage(title,content,tiEmail)


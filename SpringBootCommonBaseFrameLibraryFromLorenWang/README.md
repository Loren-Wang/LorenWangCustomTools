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
      配置参数12、当前编译器环境---runCompilingEnvironment

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
      
      
<h3>六、SbcbflwBaseController---（基础接口实例类）

      1、从资源文件中获取文本字符---getMessage(msgCode)
      2、从资源文件中获取文本字符---getMessage(msgCode,defaultMsg)
      3、接口处理之后的响应返回---responseContent(stateCode,stateMsg,data)
      4、接口处理之后的数据列表响应返回---responseDataListContent(stateCode,stateMsg,pageIndex,pageSize,sumCount,dataList)



<h4>七、SbcbflwBaseHttpServletRequestWrapper---（请求数据拦截，用来对请求头做处理）

      1、添加header---addHeader(name,value)
      
      
      
<h4>八、SbcbflwBaseRequestBean---（基础请求实体）

      配置参数1：是否返回完整图片地址---returnFullImagePath
      配置参数2：分页页码---pageIndex
      配置参数3：每页请求数量---pageSize
      
      
      
<h4>九、SbcbflwBaseResponseBean---（基础响应实例）

      配置参数1：响应的状态码---stateCode
      配置参数2：响应的状态信息---stateMessage
      配置参数3：响应数据---data
      
      
      
<h4>十、SbcbflwPageResponseBean---（列表分页数据请求响应实体）

      配置参数1：分页的页码---pageIndex
      配置参数2：分页的每页请求数量---pageSize
      配置参数3：当前条件下的取到的数据总数---sumCount
      配置参数4：列表数据实体，不能为空，但是可以为空数组---dataList

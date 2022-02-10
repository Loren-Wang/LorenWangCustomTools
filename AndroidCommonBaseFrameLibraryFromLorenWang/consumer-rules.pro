
#lorenwang所有框架
-keep class android.lorenwang.**{*;}
-keep class javabase.lorenwang.**{*;}
-keep class kotlinbase.lorenwang.**{*;}

#微博混淆
-keep class com.sina.weibo.sdk.** { *; }

#ARouter混淆
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
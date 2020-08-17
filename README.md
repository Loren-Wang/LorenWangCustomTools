# LorenWangCustomTools
自定义工具类地址


暂时合并显示到当前主分支，因为两个版本相差太大，所以暂时先合并显示，后续整理完成之后会更新。

使用方式：
1、在project的根目录下找到builde.gradle文件，添加以下代码

`--------------------------

      allprojects {
           repositories {
               google()
               jcenter()
               maven { url "https://dl.bintray.com/lorenwang/maven" }
           }
       }`

2、在app的根目录下找到builde.gradle文件，添加以下代码

` --------------------------

      //自定义工具类
     implementation 'com.github.lorenwang:AndroidCustomToolsFromLorenWang:1.0.66'
     //自定义控件库
     implementation 'com.github.lorenwang:AndroidCustomViewsFromLorenWang:1.0.46'
     //自定义动画库
     implementation 'com.github.lorenwang:AndroidAnimFromLorenWang:1.0.0'
     //java工具类库
     implementation 'com.github.lorenwang:JavaCustomToolsFromLorenWang:1.0.44'`

ps:

1、最新版本信息可以查看个源码部分的build.gradle，也可以使用项目的自动更新

   2、上面只是部分库，其他的还没有添加进来，工具类和控件库等正在持续添加

Kotlin自定义工具类

<h3>KttlwConfig---(配置文件,使用该库必须配置)

| 参数含义 | 参数名称 | 类型 | 默认值 |
| :----: | :----: | :----:| :----: |
| 网络操作默认页面数量 | DEFAULT_NET_PAGE_SIZE | Int | 10 | 
| 网络操作默认页码 | DEFAULT_NET_PAGE_INDEX | Int | 0 | 

<br> 

-----
<br>

<h3>KttlwBaseNetRequestBean---(基础请求体)

| 参数含义 | 参数名称 | 类型 | 默认值 |
| :----: | :----: | :----:| :----: |
| 是否返回完整图片地址 | returnFullImagePath | Boolea | true | 
| 分页页码 | pageIndex | Int | DEFAULT_NET_PAGE_INDEX | 
| 每页请求数量 | pageIndex | Int | DEFAULT_NET_PAGE_INDEX | 
| 响应的状态码 | pageSize | Int | DEFAULT_NET_PAGE_SIZE | 

<br> 

-----
<br>

<h3>KttlwBaseNetResponseBean<T>---(基础响应实例)

| 参数含义 | 参数名称 | 类型 | 默认值 |
| :----: | :----: | :----:| :----: |
| 响应数据 | data | T | null | 
| 响应的状态码 | stateCode | String | "0" | 
| 响应的状态信息 | stateMessage | String | null | 

<br> 

-----
<br>

<h3>KttlwBaseNetUpDateRankReqBean<T>---(更新排行请求实体,继承：KttlwBaseNetRequestBean)

| 参数含义 | 参数名称 | 类型 | 默认值 |
| :----: | :----: | :----:| :----: |
| 要操作的id列表 | ids | Array<T>? | null | 

<br> 

-----
<br>

<h3>KttlwNetPageResponseBean<T>---(列表分页数据请求响应实体)

| 参数含义 | 参数名称 | 类型 | 默认值 |
| :----: | :----: | :----:| :----: |
| 分页的页码 | pageIndex | Int | null | 
| 分页的每页请求数量 | pageSize | Int | null | 
| 当前条件下的取到的数据总数 | sumDataCount | Int | null | 
| 数据总页数 | sumPageCount | Int | null | 
| 列表数据实体 | dataList | ArrayList<T> | null | 
| 是否有下一页 | hasNextPage | Boolean | null | 
| 是否有上一页 | hasPrePage | Boolean | null | 

<br> 

-----
<br>

<h3>KttlwExtendAny---(基础函数扩展)

| 功能 | 函数 | 返回值 |
| :----: | :----: | :----: |
| 获取实例的json数据 | kttlwToJsonData() | String |
| 获取实例的json数据 | kttlwToJsonData() | string | 
| 检测基础数据是否为空，包括空字符串 | kttlwIsEmpty() | Boolean | 
| 数据检测，根据检测结果调用不同方法，调用结束后返回数据，包括空字符串检测 | kttlwEmptyCheck(emptyFun: () -> R, notEmptyFun: (T) -> R) | R | 
| 空检测，如果为空调用空方法，包括空字符串检测 | kttlwIsEmpty(emptyFun: () -> R) |
| 检测数据是否为空，仅仅是空，仅仅是null判断 | kttlwIsNull() | Boolean | 
| 检测数据是否不为空，仅仅是空，仅仅是null判断 | kttlwIsNotNull() | Boolean | 
| 是否为非null或者空 | kttlwIsNotNullOrEmpty() | Boolean | 
| 数据为null检测，为null时执行fun | kttlwNullCheck(exec: () -> Unit) |
| 数据不为null检测，不为null时执行fun | kttlwNotNullCheck(exec: (T) -> Unit) |
| 数据null检测，分情况执行fun | kttlwNullCheck(nullFun: () -> R, notNullFun: () -> R) | R | 
| 数据转换成指定的数据格式类型 | kttlwFormatConversion() | T | 
| 判断数据值是否是确定状态，默认返回非确定状态也就是默认认定数据为false | kttlwIfTrue() | Boolean | 
| 判断数据值是否是否定状态，默认返回非确定状态也就是默认认定数据为false | kttlwIfFalse() | Boolean | 
| 待检测参数中是否包含空数据，包括空字符串检测 | kttlwHaveEmptyCheck(emptyFun: () -> R, notEmptyFun: () -> R, vararg params: P) | R | 
| 待检测参数中是否包含空数据，包括空字符串检测 | kttlwHaveEmptyCheck(vararg params: P) | Boolean | 
| 待检测参数中是否全部是空数据，包括空字符串检测 | kttlwAllEmptyCheck(vararg params: P) | Boolean | 
| 待检测参数中是否有null数据 | kttlwHaveNullCheck(vararg params: P, nullFun: () -> R, notNullFun: () -> R) | R | 
| 待检测参数中是否包含null | kttlwHaveNullCheck(vararg params: P) | Boolean | 
| 待检测参数中是否全部是null | kttlwAllNullCheck(vararg params: P) | Boolean | 
| 获取非空数据 | kttlwGetNotEmptyData(getDefaultData: () -> T) | T | 

<br> 

-----
<br>

<h3>KttlwExtendBigDecimal---(BigDecimal扩展)

| 功能 | 函数 | 返回值 |
| :----: | :----: | :----: |
| 获取非空数据 | kttlwGetNotEmptyData(defaultData: BigDecimal = BigDecimal.ZERO) | BigDecimal |

<br> 

-----
<br>

<h3>KttlwExtendBoolean---(布尔型数据扩展)

| 功能 | 函数 | 返回值 |
| :----: | :----: | :----: |
| 如果是false值情况下处理 | kttlwIfFalse(exe: () -> R) | R? |
| 如果是true值的情况下处理 | kttlwIfTrue(exe: () -> R) | R? |
| 值状态处理，分true和false处理 | kttlwValueStatus(trueFun: () -> R, falseFun: () -> R) | R |
| 布尔值对应的整型数值 | kttlwToInt() | Int |
| 获取非空数据 | kttlwGetNotEmptyData(defaultData: Boolean = false) | Boolean |
| 判断数据值是否是确定状态，默认返回非确定状态也就是默认认定数据为false | kttlwIfTrue() | Boolean |
| 判断数据值是否是否定状态，默认返回非确定状态也就是默认认定数据为false | kttlwIfFalse() | Boolean |

<br> 

-----
<br>

<h3>KttlwExtendCollection---(集合扩展)

| 功能 | 函数 | 返回值 |
| :----: | :----: | :----: |
| 集合item由原本上下排列转左右排列，以逻辑显示居多 | kttlwItemUpDownToLeftRight(rowCount : Int)  | ArrayList<T> |
| 集合item由原本上下排列转为水平排列欢唱显示，为空代表着只需要占位 | kttlwItemUpDownToHorizontal(columnCount : Int) | ArrayList<T?> |

<br> 

-----
<br>

<h3>KttlwExtendInt---(整型相关函数扩展)

| 功能 | 函数 | 返回值 |
| :----: | :----: | :----: |
| 获取非空数据 | kttlwGetNotEmptyData(defaultStr: Int) | Int |

<br> 

-----
<br>

<h3>KttlwExtendMap---(Map函数扩展)

| 功能 | 函数 | 返回值 |
| :----: | :----: | :----: |
| 获取实例的json数据 | kttlwParseJsonData(cls: Class<R>) | R? |
| 获取非空数据 | kttlwGetNotEmptyData(defaultData: Map<K, V> = mapOf()) | Map<K, V> |

<br> 

-----
<br>

<h3>KttlwExtendString---(字符串相关函数扩展)

| 功能 | 函数 | 返回值 |
| :----: | :----: | :----: |
| 格式化json数据 | kttlwParseJsonData(cls: Class<T>) | T? |
| 获取非空数据 | kttlwGetNotEmptyData(defaultStr: String = "") | String |
| 判断这个字符串是否是长整型时间戳 | kttlwIsLongTime() | Boolean |
| 将字符串转为驼峰法 | kttlwToCamelCase() | String? |
| 将字符串分离(以大写字母为分隔添加位置) | kttlwToSeparatedCase(separated: String?) | String? |
| 清除起始字符串 | kttlwClearFirstString(str: String, zero: Boolean) | String? |
| 清除结束字符串 | kttlwClearEndString(str: String, last: Boolean) | String? |

<br> 

-----
<br>

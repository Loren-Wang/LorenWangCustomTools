**V2.2.9**

    新增正则---所有的标点符号（包含空格）--EXP_PUNCTUATION
    JtlwBeanUtil---(实体类相关单例)
      -复制相同参数数据--copyWithTheParameters(origin,target)
      -复制相同参数数据--copyWithTheParameters(origin,target,filterNull)
      -复制相同参数数据--copyWithTheParameters(originList,target,filterNull)
      -获取实体类所有参数map集合--getBeanParameters(data)
      -获取实体类痛的参数以及对应返回值--getBeanFieldsAndReturnType(beanClass)


**V2.2.8**

    新增是否是图片检测类型：heic图片检测
    
**V2.2.7**

    检测国内身份证号是否正确，支持15位至18位--JtlwCheckVariateUtils.checkChineseIdCard(idCard)
    通过身份证号检测年龄是否超过限制--JtlwCheckVariateUtils.checkAgeMoreThanLimitByIdCard(idCard,limit, judgeYear)
    国内身份证号正则，强校验--JtlwMatchesRegularCommon.ID_CARD_CHINESE
    
**V2.2.4**

    新增时间处理工具函数，
    获取年龄--getAge(birthTime,isReal)
    修改根据月日判断星座--getConstellation(time)


**V2.2.3**

    新增时间处理工具函数，
    根据输入的年份判断该年份是否是闰年，是则返回true--isLeapYearForTime(yearTime)
    获取年份列表--getYearList(leftYearCount,rightYearCount)
    获取月份列表--getMonthList(yearTime,asOfCurrent)
    获取日期列表--getDayList(monthTime,asOfCurrent)

**V2.2.2**

    修改时间处理工具类型，getLastMonthStartDayTime


**V2.2.1**

    修改时间处理工具类型，新增日历处理等函数

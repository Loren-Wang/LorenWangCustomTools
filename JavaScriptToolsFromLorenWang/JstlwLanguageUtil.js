import JstlwVariableDisposeUtil from "./JstlwVariableDisposeUtil";

/**
 * 功能作用：语言工具类
 * 初始注释时间： 2020/12/3 2:17 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 初始化，必须调用(init)
 * 设置当前语言（setCurrentLanguage）
 * 获取语言显示文本（getShowText）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
const JstlwLanguageUtil = {
    /**
     * 初始化
     * @param allLanguageText 所有语言文本记录，key-value组合，value为require文本文件
     */
    init(allLanguageText) {
        showAllLanguageText = allLanguageText;
    },
    /**
     * 设置当前语言
     * @param languageKey 在所有语言文本记录当中的key值
     */
    setCurrentLanguage(languageKey) {
        currentLanguage = languageKey;
    },
    /**
     * 获取语言显示文本
     * @param textKey 文本key
     * @param language 要查找的语言，不传为获取当前语言
     *
     * @return {string|*} 语言显示文本
     */
    getShowText(textKey, language) {
        if (showAllLanguageText != null && showAllLanguageText[currentLanguage] != null) {
            let text = showAllLanguageText[JstlwVariableDisposeUtil.isParamsEmpty(language) ? currentLanguage : language][textKey];
            if (JstlwVariableDisposeUtil.isParamsEmpty(text)) {
                return "";
            } else {
                return text;
            }
        }
        return ""
    }

}
export default JstlwLanguageUtil
//当前语言
let currentLanguage = "";
//所有语言文本记录
let showAllLanguageText = {}

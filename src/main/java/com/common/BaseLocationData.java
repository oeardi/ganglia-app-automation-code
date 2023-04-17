package com.common;

/**
 * doLocation() 方法中用到的变量与常量
 *
 * @author 冷枫红舞
 */
public class BaseLocationData {

    public static final String LOCATION_ELEMENT = "element";
    public static final String LOCATION_WAY = "way";
    public static final String LOCATION_REQUISITE = "requisite";

    /**
     * yaml 文件中，location 部分的扩展字段，使用内部 “缓存变量” 时需要指定变量类型。
     * paramType 取值：string 或 int。示例：[- paramType: "int"]
     */
    public static final String LOCATION_PARAM_TYPE = "paramType";

    /**
     * 元素定位方式，用于 doLocation() 方法。
     */
    public interface LocationWay {
        String BY_ID = "BYID";
        String BY_XPATH = "BYXPATH";
        String BY_TEXT = "BYTEXT";
        String BY_CLASS_NAME = "BYCLASSNAME";
        String BY_NAME = "BYNAME";
        String BY_LINK_TEXT = "BYLINKTEXT";
        String BY_UI_SELECTOR = "BYUISELECTOR";
    }

}

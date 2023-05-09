package com.common;

import java.util.List;

/**
 * 本地中转，临时缓存参数。
 *
 * @author 冷枫红舞
 */
public class CacheParamData {

    public static String caseName;
    public static int screenshotCount;

    /**
     * pageName 和 screenshotCount 用于截图时的文件名拼接。
     */
    public static String pageName = null;

    /**
     * 缓存变量，缓存 doAction() 方法中，某些操作结果的值。
     */
    public static int elementCount = 0;
    public static String elementText = null;
    public static List<String> elementList = null;

    /**
     * 缓存 toast
     */
    public static String toastCacheString = null;

    /**
     * 用于 while 循环中使用到的 “全局变量” 缓存，用于保存 [whhw - parameter - elements] 的值。
     */
    public static String whhwElementCache = null;

    /**
     * 缓存 select 查询结果
     */
    public static String selectResultCacheString = null;

    public interface CacheParam {
        /**
         * 共两个地方用到 CacheParam 接口定义的参数：
         * <p>
         * 1. AnalysisLogicProcessUtils 类的 public static void doIffi(ArrayList iffiList) 方法。
         * |--- 根据 yaml 文件中 iffi -> asserts -> result 的值（整型还是字符型），取对应的 “缓存变量” 内容，这个值在会脚本运行时赋值给 “缓存变量”。
         * <p>
         * 2. AnalysisSubjectUtils 类的 public static MobileElement doLocation(ArrayList locationList) 方法的 else 处理逻辑。
         * |--- 如果 yaml 文件中的定位信息，没有取到 location 的 element 元素（element == null）时，则执行 else 代码逻辑，为此元素赋 “缓存变量” 的值。（localElement = CacheParamData.elementText）
         * |--- 注：[action] 的 [paramType] 元素必须填写一个值。（如：paramType: "string"）
         */
        String PARAM_INT = "int";
        String PARAM_STRING = "string";
    }

}

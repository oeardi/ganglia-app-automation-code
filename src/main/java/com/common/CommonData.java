package com.common;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * 公共参数
 *
 * @author 冷枫红舞
 */
public class CommonData {

    public static final String ID_TAG_1 = "id/";
    public static final String ID_TAG_2 = ":id/";
    public static final String XPATH_TAG_1 = "//*[@";
    public static final String XPATH_TAG_3 = "//";
    public static final String XPATH_TAG_2 = "//.";

    // 元素查找失败时，重试查找次数。
    public static int findElementLoopCount = 0;
    // 元素操作失败时，重试操作次数。
    public static int operateElementLoopCount = 0;
    // 全局等待时间
    public static long sleepTime = 0;

    public static long endOfRunWaitingTime = 10000;

    /**
     * 用于获取 toast（注：必须使用 by xpath 方式。 ）
     */
    public static final String TOAST_XPATH_STRING = "//*[@class='android.widget.Toast']";

    /**
     * “是否找到元素”标志位。找到元素 flag = 1，未找到元素 flag = 0，当 0 时，才会执行 “是否为必须元素” 的逻辑。
     */
    public static int isRequisiteFlag = 0;

    /**
     * requisite，标识为 “是否必须元素”。如果是必须元素（requisite =Y），定位不到元素时，程序会调用 quit() 方法停止运行。
     */
    public static final String Y = "Y";

    public static String testReportFolder = "test-report-output/report-" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "/";

    /**
     * 用于 uiAtuo() 方法，判断 yaml 中是否包含以下字符串，进而进行相应处理。
     */
    public interface ContainKeywork {
        String CASE_NAME = "name";
        String DESCRIPTION = "description";
        String MODULES = "modules";
        String PAGE = "page";
        String LOCATION = "location";
        String ACTION = "action";
        String IFFI = "iffi";
        String WHHW = "whhw";
        String ASSERT = "assert";
        String EXIST = "exist";
        String COMPARE = "compare";
    }

}

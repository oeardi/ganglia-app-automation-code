package com.common;

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

    /**
     * 重试查找次数 BaseLocationUtils.findElementWithBy()
     */
    public static int findElementLoopCount = 0;
    public static long sleepTime = 500;

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
    public static final String _Y = "Y";

    /**
     * 等待时间（超时时间）单位：秒
     */
    public interface CacheExpireTime {
        long WAIT_TIME_30 = 30;
        long WAIT_TIME_60 = 60;
    }

    /**
     * 用于 uiAtuo() 方法，判断 yaml 中是否包含以下字符串，进而进行相应处理。
     */
    public interface ContainKeywork {
        String CONTAINT_modules = "modules";
        String CONTAINT_page = "page";
        String CONTAINT_location = "location";
        String CONTAINT_action = "action";
        String CONTAINT_iffi = "iffi";
        String CONTAINT_whhw = "whhw";
        String CONTAINT_assert = "assert";
    }

}

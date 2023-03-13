package com.utils;

import com.alibaba.fastjson.JSONObject;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;

import java.util.HashMap;
import java.util.List;

import static com.common.CommonData.findElementLoopCount;
import static com.utils.BaseActionUtils.click;
import static com.utils.BaseLocationUtils.findElement;

/**
 * 处理白名单
 *
 * @author 冷枫红舞
 */
@Slf4j
public class BaseWhiteListUtils {

    public static void doWhiteList(String whiteListFile) {
        log.info("[调试信息] [doWhiteList] 开始执行 “白名单” 代码逻辑。");
        Reporter.log("【调试信息】 [doWhiteList] 开始执行 “白名单” 代码逻辑。");

        JsonFileUtils mustacheJsonFileUtils = new JsonFileUtils();

        String jsonStr = mustacheJsonFileUtils.getStringWithJsonFile(whiteListFile);
        if (null == jsonStr) {
            log.info("[调试信息] [doWhiteList] 获取到的 “白名单” 为空，doWhiteList() 方法终止执行。[return;]");
            return;
        }

        HashMap<String, Object> map = JSONObject.parseObject(jsonStr, HashMap.class);

        int tempLoopCount = findElementLoopCount;
        findElementLoopCount = 0;
        log.info("[调试信息] [doWhiteList] 设置 [findElementLoopCount = 0]，找不到元素时，不执行循环查找。");

        List whiteList = (List) map.get("whiteList");

        for (int i = 0; i < whiteList.size(); i++) {
            log.info("[调试信息] [doWhiteList] [++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++]");
            String element = (String) whiteList.get(i);
            log.info("[调试信息] [doWhiteList] 当前元素 [element = {}]", element);
            Reporter.log("【调试信息】 [doWhiteList] 当前元素 [element = " + element + "]");

            MobileElement mobileElement = findElement(element);
            if (mobileElement == null) {
                log.info("[调试信息] [doWhiteList] 调用 findElement() 返回 [mobileElement == null]，没有找到元素。");
            } else {
                log.info("[调试信息] [doWhiteList] 调用 findElement() 找到元素，即将执行 [click] 操作。");
                Reporter.log("【调试信息】 [doWhiteList] 调用 findElement() 找到元素，即将执行 [click] 操作。");
                click(mobileElement);
            }
        }

        // 恢复 loop（元素在 “白名单” 时不循环查找，正常的元素定位，会执行 loop 次重新查找。）
        findElementLoopCount = tempLoopCount;
        log.info("[调试信息] [doWhiteList] “白名单” 执行完毕。");
        Reporter.log("【调试信息】 [doWhiteList] “白名单” 执行完毕。");
    }
}

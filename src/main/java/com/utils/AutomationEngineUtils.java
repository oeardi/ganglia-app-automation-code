package com.utils;

import com.pages.base.IPage;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.common.CacheParamData.pageName;
import static com.common.CommonData.ContainKeywork.*;
import static com.common.WhiteListData.*;
import static com.utils.BaseActionUtils.quit;
import static com.utils.BaseAssertUtils.doAssert;
import static com.utils.BaseWhiteListUtils.doWhiteList;
import static com.utils.CoreAnalysisEngineUtils.doAction;
import static com.utils.CoreAnalysisEngineUtils.doLocation;
import static com.utils.LogicAnalysisProcessUtils.doIffi;
import static com.utils.LogicAnalysisProcessUtils.doWhhw;

/**
 * 解析 ymal 文件内容，根据 “元素” 做相应处理。
 * 1）whiteList
 * 2）page
 * 3）operate
 * 4）action
 * 5）iffi
 * 6)whhw
 * 7)assert
 *
 * @author 冷枫红舞
 */
@Slf4j
public class AutomationEngineUtils {

    /**
     * 自动解析并执行 case 内容
     *
     * @param yamlToMap
     */
    public static void uiAtuo(Map<String, Object> yamlToMap) {
        log.info("[调试信息] [uiAtuo] [==========================================================================================]");
        log.info("[调试信息] [uiAtuo] [==========================================================================================]");
        log.info("[调试信息] [uiAtuo] [测试开始] [↓] [↓] [↓]");
        Reporter.log("【调试信息】 [uiAtuo] [==========================================================================================]");
        Reporter.log("【调试信息】 [uiAtuo] [==========================================================================================]");
        Reporter.log("【调试信息】 [uiAtuo] [测试开始] [↓] [↓] [↓]");

        List modulesList = (List) yamlToMap.get(CONTAINT_modules);
        if (null == modulesList) {
            log.info("[调试信息] [uiAtuo] yaml 文件中的 [modules] 无内容，uiAtuo() 方法终止执行。脚本强制停止。");
            Reporter.log("【调试信息】 [uiAtuo] yaml 文件中的 [modules] 无内容，uiAtuo() 方法终止执行。脚本强制停止。");
            quit();
        }
        log.info("[调试信息] [uiAtuo] 输出 [yaml] 文件内容：");
        log.info("[调试信息] [uiAtuo] {}。", modulesList.toString());

        IPage pageObject = null;
        MobileElement mobileElement = null;

        Iterator iterator = modulesList.iterator();

        while (iterator.hasNext()) {
            log.info("[调试信息] [uiAtuo] [------------------------------------------------------------------------------------------]");
            Reporter.log("【调试信息】 [uiAtuo] [------------------------------------------------------------------------------------------]");

            Map<String, Object> modulesValueMap = (Map<String, Object>) iterator.next();
            for (Map.Entry<String, Object> entry1 : modulesValueMap.entrySet()) {
//                log.info("[调试信息] [uiAtuo] 输出 [yaml] 文件中 [{}] 元素的内容：{}", entry1.getKey(), entry1.getValue());

                /**
                 * 解析 whiteList 信息
                 */
                if (entry1.getKey().contains(CONTAINT_whiteListFile)) {

                    String whiteListFile = (String) entry1.getValue();
                    log.info("[调试信息] [uiAtuo] 输出 [whiteListFile = {}]", whiteListFile);
                    Reporter.log("【调试信息】 [uiAtuo] 输出 [whiteListFile = " + whiteListFile + "]");

                    if (StringUtils.isEmpty(whiteListFile)) {
                        log.info("[调试信息] [uiAtuo] [whiteListFile == null]，不执行 “白名单” 相关代码逻辑。[return;]");
                        Reporter.log("[调试信息] [uiAtuo] [whiteListFile == null]，不执行 “白名单” 相关代码逻辑。[return;]");
                        return;
                    } else {
                        log.info("[调试信息] [uiAtuo] 即将开始执行 switch (whiteListFile.toUpperCase()) 方法，匹配白名单文件：");
                        Reporter.log("【调试信息】 [uiAtuo] 即将开始匹配白名单文件：");
                        switch (whiteListFile.toUpperCase()) {
                            case NGA_POS:
                                whiteListFile = NGA_POS_WHITE_LIST_FILE;
                                break;
                            case NGA_BAPP:
                                whiteListFile = NGA_BAPP_WHITE_LIST_FILE;
                                break;
                            case PAK_POS:
                                whiteListFile = PAK_POS_WHITE_LIST_FILE;
                                break;
                            case PAK_BAPP:
                                whiteListFile = PAK_BAPP_WHITE_LIST_FILE;
                                break;
                            default:
                                log.info("[调试信息] [uiAtuo] [default] switch 语句中没有匹配到 [whiteListFile = {}]，请确认 “白名单” 文件是否存在。", whiteListFile);
                                Reporter.log("【调试信息】 [uiAtuo] [default] switch 语句中没有匹配到 [whiteListFile = " + whiteListFile + "]，请确认 “白名单” 文件是否存在。");
                        }
                    }

                    log.info("[调试信息] [uiAtuo] [whiteListFile = {}]，即将调用 doWhiteList() 方法，执行 “白名单” 相关代码逻辑。", whiteListFile);
                    Reporter.log("【调试信息】 [uiAtuo] [whiteListFile = " + whiteListFile + "]，即将调用 doWhiteList() 方法，执行 “白名单” 相关代码逻辑。");
                    doWhiteList(whiteListFile);
                }

                /**
                 * 解析 page 信息
                 */
                if (entry1.getKey().contains(CONTAINT_page)) {
                    pageName = (String) entry1.getValue();
                    pageObject = BasePagesUtils.getPage(pageName);
                }

                /**
                 * 解析 location 定位信息
                 */
                if (entry1.getKey().contains(CONTAINT_location)) {
                    ArrayList locationList = (ArrayList) entry1.getValue();
                    mobileElement = doLocation(locationList);
                }

                /**
                 * 解析 action 操作信息
                 */
                if (entry1.getKey().contains(CONTAINT_action)) {
                    ArrayList actionList = (ArrayList) entry1.getValue();
                    doAction(mobileElement, actionList);
                }

                /**
                 * 解析 iffi 相关信息
                 */
                if (entry1.getKey().contains(CONTAINT_iffi)) {
                    ArrayList iffiList = (ArrayList) entry1.getValue();
                    doIffi(iffiList);
                }

                /**
                 * 解析 whhw 相关信息
                 */
                if (entry1.getKey().contains(CONTAINT_whhw)) {
                    ArrayList whhwList = (ArrayList) entry1.getValue();
                    doWhhw(whhwList);
                }

                /**
                 * 解析 assert 相关信息
                 */
                if (entry1.getKey().contains(CONTAINT_assert)) {
                    ArrayList assertList = (ArrayList) entry1.getValue();
                    doAssert(assertList);
                }

            }
        }

        log.info("[调试信息] [uiAtuo] [测试结束] [↑] [↑] [↑]");
        log.info("[调试信息] [uiAtuo] [==========================================================================================]");
        log.info("[调试信息] [uiAtuo] [==========================================================================================]");
        Reporter.log("【调试信息】 [uiAtuo] [测试结束] [↑] [↑] [↑]");
        Reporter.log("【调试信息】 [uiAtuo] [==========================================================================================]");
        Reporter.log("【调试信息】 [uiAtuo] [==========================================================================================]");
    }

}

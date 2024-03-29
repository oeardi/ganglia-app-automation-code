package com.utils;

import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.common.CacheParamData.*;
import static com.common.CommonData.ContainKeywork.*;
import static com.common.CommonData.endOfRunWaitingTime;
import static com.common.WhiteListData.*;
import static com.utils.AndroidDriverUtils.getCommonConfigEntity;
import static com.utils.BaseAssertUtils.doAssert;
import static com.utils.BaseWhiteListUtils.doWhiteList;
import static com.utils.CoreAnalysisEngineUtils.doAction;
import static com.utils.CoreAnalysisEngineUtils.doLocation;
import static com.utils.LogicAnalysisProcessUtils.doIffi;
import static com.utils.LogicAnalysisProcessUtils.doWhhw;

/**
 * 解析 yaml 文件内容，根据 “模块” 做相应处理。
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

    private AutomationEngineUtils() {
    }

    /**
     * 自动解析并执行 case 内容
     *
     * @param yamlToMap
     */
    public static void uiAuto(Map<String, Object> yamlToMap) {
        log.info("[调试信息] [uiAuto] [==========================================================================================]");
        log.info("[调试信息] [uiAuto] [==========================================================================================]");
        log.info("[调试信息] [uiAuto] [测试开始] [↓] [↓] [↓]");
        Reporter.log("【调试信息】 [uiAuto] [==========================================================================================]");
        Reporter.log("【调试信息】 [uiAuto] [==========================================================================================]");
        Reporter.log("【调试信息】 [uiAuto] [测试开始] [↓] [↓] [↓]");

        // 输出 case_name 信息
        caseName = null;
        caseName = (String) yamlToMap.get(CASE_NAME);
        log.info("[调试信息] [uiAuto] 测试用例名称：[{}]", caseName);
        Reporter.log("【调试信息】 [uiAuto] 测试用例名称 [page = " + caseName + "]");

        screenshotCount = 0;

        // 输出 description 信息
        String description = (String) yamlToMap.get(DESCRIPTION);
        log.info("[调试信息] [uiAuto] 用例描述信息：[{}]", description);
        Reporter.log("【调试信息】 [uiAuto] 用例描述信息 [" + description + "]");

        /**
         * 解析 modules 部分
         */
        List modulesList = (List) yamlToMap.get(MODULES);
        if (null == modulesList) {
            log.info("[调试信息] [uiAuto] yaml 文件中的 [modules] 无内容，测试用例停止运行。[return;]");
            Reporter.log("【调试信息】 [uiAuto] yaml 文件中的 [modules] 无内容，测试用例停止运行。[return;]");
            return;
        }
        log.info("[调试信息] [uiAuto] 输出 [yaml] 文件内容：");
        log.info("[调试信息] [uiAuto] {}。", modulesList.toString());

        // 从文件读配置，为全局变量赋值。
        getCommonConfigEntity();

        MobileElement mobileElement = null;
        pageName = null;

        Iterator iterator = modulesList.iterator();
        while (iterator.hasNext()) {
            log.info("[调试信息] [uiAuto] [------------------------------------------------------------------------------------------]");
            Reporter.log("【调试信息】 [uiAuto] [------------------------------------------------------------------------------------------]");

            Map<String, Object> modulesValueMap = (Map<String, Object>) iterator.next();
            for (Map.Entry<String, Object> entry1 : modulesValueMap.entrySet()) {
//                log.info("[调试信息] [uiAuto] 输出 [yaml] 文件中 [{}] 元素的内容：{}", entry1.getKey(), entry1.getValue());

                /**
                 * 解析 whiteList 信息
                 */
                if (entry1.getKey().contains(CONTAINT_WHITE_LIST_FILE)) {

                    String whiteListFile = (String) entry1.getValue();
                    log.info("[调试信息] [uiAuto] 输出 [whiteListFile = {}]", whiteListFile);
                    Reporter.log("【调试信息】 [uiAuto] 输出 [whiteListFile = " + whiteListFile + "]");

                    if (StringUtils.isEmpty(whiteListFile)) {
                        log.info("[调试信息] [uiAuto] [whiteListFile == null]，不执行 “白名单” 相关代码逻辑。[return;]");
                        Reporter.log("[调试信息] [uiAuto] [whiteListFile == null]，不执行 “白名单” 相关代码逻辑。[return;]");
                        return;
                    } else {
                        log.info("[调试信息] [uiAuto] 即将开始执行 switch (whiteListFile.toUpperCase()) 方法，匹配白名单文件：");
                        Reporter.log("【调试信息】 [uiAuto] 即将开始匹配白名单文件：");
                        switch (whiteListFile.toUpperCase()) {
                            case DEFAULT:
                                whiteListFile = DEFAULT_WHITE_LIST_FILE;
                                break;
                            case N_P:
                                whiteListFile = N_P_WHITE_LIST_FILE;
                                break;
                            case N_B:
                                whiteListFile = N_B_WHITE_LIST_FILE;
                                break;
                            case P_P:
                                whiteListFile = P_P_WHITE_LIST_FILE;
                                break;
                            case P_B:
                                whiteListFile = P_B_WHITE_LIST_FILE;
                                break;
                            default:
                                log.info("[调试信息] [uiAuto] [default] switch 语句中没有匹配到 [whiteListFile = {}]，请确认 “白名单” 文件是否存在。", whiteListFile);
                                Reporter.log("【调试信息】 [uiAuto] [default] switch 语句中没有匹配到 [whiteListFile = " + whiteListFile + "]，请确认 “白名单” 文件是否存在。");
                        }
                    }

                    log.info("[调试信息] [uiAuto] [whiteListFile = {}]，即将调用 doWhiteList() 方法，执行 “白名单” 相关代码逻辑。", whiteListFile);
                    Reporter.log("【调试信息】 [uiAuto] [whiteListFile = " + whiteListFile + "]，即将调用 doWhiteList() 方法，执行 “白名单” 相关代码逻辑。");
                    doWhiteList(whiteListFile);
                }

                /**
                 * 解析 page 信息
                 */
                if (entry1.getKey().equals(PAGE)) {
                    pageName = (String) entry1.getValue();
//                    pageObject = BasePagesUtils.getPage(pageName);
                    log.info("[调试信息] [uiAuto] 当前页面：[page = {}]", pageName);
                    Reporter.log("【调试信息】 [uiAuto] 当前页面 [page = " + pageName + "]");
                }

                /**
                 * 解析 location 定位信息
                 */
                if (entry1.getKey().equals(LOCATION)) {
                    ArrayList locationList = (ArrayList) entry1.getValue();
                    mobileElement = doLocation(locationList);
                }

                /**
                 * 解析 action 操作信息
                 */
                if (entry1.getKey().equals(ACTION)) {
                    ArrayList actionList = (ArrayList) entry1.getValue();
                    doAction(mobileElement, actionList);
                }

                /**
                 * 解析 iffi 相关信息
                 */
                if (entry1.getKey().equals(IFFI)) {
                    ArrayList iffiList = (ArrayList) entry1.getValue();
                    doIffi(iffiList);
                }

                /**
                 * 解析 whhw 相关信息
                 */
                if (entry1.getKey().equals(WHHW)) {
                    ArrayList whhwList = (ArrayList) entry1.getValue();
                    doWhhw(whhwList);
                }

                /**
                 * 解析 assert 相关信息
                 */
                if (entry1.getKey().equals(ASSERT)) {
                    ArrayList assertList = (ArrayList) entry1.getValue();
                    doAssert(assertList);
                }

            }
        }

        try {
            Thread.sleep(endOfRunWaitingTime);
            log.info("[调试信息] [uiAuto] [sleeping = {}]", endOfRunWaitingTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("[调试信息] [uiAuto] [测试结束] [↑] [↑] [↑]");
        log.info("[调试信息] [uiAuto] [==========================================================================================]");
        log.info("[调试信息] [uiAuto] [==========================================================================================]");
        Reporter.log("【调试信息】 [uiAuto] [测试结束] [↑] [↑] [↑]");
        Reporter.log("【调试信息】 [uiAuto] [==========================================================================================]");
        Reporter.log("【调试信息】 [uiAuto] [==========================================================================================]");
    }

}

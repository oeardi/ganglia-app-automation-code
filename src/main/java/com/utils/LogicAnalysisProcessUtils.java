package com.utils;

import com.pages.base.IPage;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static com.common.BaseWhhwData.WHHW_PARAMETER;
import static com.common.CacheParamData.pageName;
import static com.common.CacheParamData.whhwElementCache;
import static com.common.CommonData.ContainKeywork.*;
import static com.utils.CoreAnalysisEngineUtils.doAction;
import static com.utils.CoreAnalysisEngineUtils.doLocation;

/**
 * 逻辑处理，通过 yaml 文件，实现 while（whhw）功能。
 *
 * @author 冷枫红舞
 */
@Slf4j
public class LogicAnalysisProcessUtils {

    /**
     * 对 yaml 文件中的 whhw 部分进行解析，具体思路描述：
     * 如果 yaml 中存在 “whhw” 元素，则会根据循环次数 times 和循环参数 parameters 来完成循环操作。
     * 将每一个循环参数 parameters 存放在 CacheParamData 类的 whileGlobalParam 变量中，作为临时缓存。
     * 就获取 “缓存变量” 对 [location - param] 进行赋值。
     *
     * @param whhwList
     */
    public static void whhwAnalysis(ArrayList whhwList) {
        log.info("[调试信息] [whhwAnalysis]");
        Reporter.log("【调试信息】 [whhwAnalysis]");

        MobileElement mobileElement = null;
        IPage pageObject = null;

        Iterator iterator = whhwList.iterator();

        /**
         * 首先调用一次 iterator.next()，作用是跳过 [whhw - parameter] 部分，
         * 从 [whhw - location] 开始取值。
         * 因为 [whhw - parameter] 已经在 doWhhw() 方法中处理过了。
         */
        iterator.next();

        /**
         * 循环 [whhw] 部分，解析 [whhw - location] 和 [whhw - action] 部分，
         * 并调用对应的 doLocation() 和  doAciton() 两个方法。
         */
        while (iterator.hasNext()) {
            log.info("[调试信息] [whhwAnalysis] [----------------------------------------]");
            Reporter.log("【调试信息】 [whhwAnalysis] [----------------------------------------]");
            log.info("[调试信息] [whhwAnalysis] [while (iterator.hasNext())]");

            Map<String, Object> whhwValueMap = (Map<String, Object>) iterator.next();

            for (Map.Entry<String, Object> entry : whhwValueMap.entrySet()) {
                log.info("[调试信息] [whhwAnalysis] 输出 [yaml] 文件中 [{}] 元素的内容：{}", entry.getKey(), entry.getValue());
                Reporter.log("【调试信息】 [whhwAnalysis] 输出 [yaml] 文件中 [" + entry.getKey() + "] 元素的内容：" + entry.getValue());

                /**
                 * 解析 page 信息
                 */
                if (entry.getKey().contains(CONTAINT_page)) {
                    pageName = (String) entry.getValue();
                    pageObject = BasePagesUtils.getPage(pageName);
                }

                /**
                 * 解析 location 定位信息
                 */
                if (entry.getKey().contains(CONTAINT_location)) {
                    ArrayList locationList = (ArrayList) entry.getValue();
                    mobileElement = doLocation(locationList);
                }

                /**
                 * 解析 action 操作信息
                 */
                if (entry.getKey().contains(CONTAINT_action)) {
                    ArrayList actionList = (ArrayList) entry.getValue();
                    doAction(mobileElement, actionList);
                }
            }
        }

        log.info("[调试信息] [whhwAnalysis] 执行完毕。");
    }

    /**
     * 解析 whhw 相关信息
     *
     * @param whhwList
     */
    public static void doWhhw(ArrayList whhwList) {
        log.info("[调试信息] [doWhhw] 开始执行 doWhhw(ArrayList whhwList) 方法：");

        /**
         * 如果 whhwList 为 null，说明 yaml 文件的 whhw 部分没有内容，所以跳过程序对 whhw 部分的执行。
         */
        if (null == whhwList) {
            log.info("[调试信息] [doWhhw] 传入参数 [whhwList == null]，doWhhw() 方法终止执行。[return;]");
            return;
        }

        /**
         * elementsList 和 times 变量，用于下面 for 循环。
         */
        ArrayList elementsList = new ArrayList();
        int times = 0;

        Iterator iteratorWhhw = whhwList.iterator();

        while (iteratorWhhw.hasNext()) {
            Map<String, Object> whhwMap = (Map<String, Object>) iteratorWhhw.next();

            for (Map.Entry<String, Object> entry2 : whhwMap.entrySet()) {
//                log.info("[调试信息] [doWhhw] [key: {}], [value: {}]", entry2.getKey(), entry2.getValue());

                /**
                 * 解析 [whhw - parameter] 部分的数据信息
                 */
                if (entry2.getKey().equals(WHHW_PARAMETER)) {
                    ArrayList parameterList = (ArrayList) whhwMap.get("parameter");
//                    log.info("[调试信息] [doWhhw] 打印 [parameterList] 的值：{}", parameterList.toString());

                    Map<String, Object> elementsMap = (Map<String, Object>) parameterList.get(0);
//                    log.info("[调试信息] [doWhhw] 打印 [elementsMap] 的值：{}", elementsMap.toString());

                    elementsList = (ArrayList) elementsMap.get("elements");
                    log.info("[调试信息] [doWhhw] 打印 [elementsList] 的值：{}", elementsList);
                    Reporter.log("【调试信息】 [doWhhw] 打印 [elementsList] 的值：" + elementsList);

                    times = elementsList.size();
                    log.info("[调试信息] [doWhhw] 打印 [times] 的值：[{}]", times);
                    Reporter.log("【调试信息】 [doWhhw] 打印 [times] 的值：[ " + times + "]");
                }
            }
        }

        for (int i = 0; i < times; i++) {
            /**
             * 每次循环时，将 [whhw - parameter - elements] 的其中一个值赋给 “缓存变量” whhwElementCache，
             * 在程序运行时，如果 [location - element] 元素值为空，程序就会读取 “缓存变量” 作为元素的定位信息。
             */
            whhwElementCache = (String) elementsList.get(i);
//            log.info("[调试信息] [doWhhw] 打印 [whhwElementCache] 的值：[{}]", whhwElementCache);

//            log.info("[调试信息] [doWhhw] 即将执行 whhwAnalysis(whhwList) 方法：");
            whhwAnalysis(whhwList);
        }

    }

}

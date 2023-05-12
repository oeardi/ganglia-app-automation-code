package com.utils;

import com.common.BaseActionData;
import com.common.BaseAssertData;
import com.common.BaseLocationData;
import com.common.CacheParamData;
import com.entity.CapabilitiesEntity;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static com.common.BaseActionData.ActionOperate.*;
import static com.common.BaseLocationData.LOCATION_ELEMENT;
import static com.common.BaseLocationData.LocationWay.*;
import static com.common.CacheParamData.CacheParam.PARAM_STRING;
import static com.common.CacheParamData.selectResultCacheString;
import static com.common.CommonData.isRequisiteFlag;
import static com.common.CommonData.testReportFolder;
import static com.utils.BaseActionDBOperateUtils.dbOperate;
import static com.utils.BaseActionUtils.*;
import static com.utils.BaseLocationUtils.*;

/**
 * 解析 yaml 的主体部分，如：
 * 1. location 定位
 * 2. action 操作
 * 3. assert 断言
 * （不包含 iffi，whhw 逻辑部分）
 *
 * @author 冷枫红舞
 */
@Slf4j
public class CoreAnalysisEngineUtils {

    private CoreAnalysisEngineUtils() {
    }

    private static CoreAnalysisEngineUtils coreAnalysisEngineUtils = new CoreAnalysisEngineUtils();

    public static CoreAnalysisEngineUtils getCoreAnalysisEngineUtils() {
        return coreAnalysisEngineUtils;
    }

    /**
     * 解析并执行 yaml 中的 location 定位信息.
     * 注：switch 部分代码需要后续优化，与 ElementOperateUtils 整合。
     *
     * @param locationList
     * @return
     */
    public static MobileElement doLocation(ArrayList locationList) {
        log.info("[调试信息] [doLocation] 开始执行 doLocation(ArrayList locationList) 方法：");
        Reporter.log("【调试信息】 [doLocation] 开始执行。");

        if (null == locationList) {
            log.info("[调试信息] [doLocation] 传入参数 [locationList == null]，doLocation() 方法终止执行。[return null;]");
            return null;
        }

        Map<String, Object> elementMap = null;
        Map<String, Object> wayMap = null;
        Map<String, Object> requisiteMap = null;
        Map<String, Object> paramTypeMap = null;

        String element = null;
        String way = null;
        String requisite = null;
        String paramType = null;

        Iterator iteratorLocation = locationList.iterator();

        while (iteratorLocation.hasNext()) {
            Map<String, Object> locationMap = (Map<String, Object>) iteratorLocation.next();

            for (Map.Entry<String, Object> entry2 : locationMap.entrySet()) {
//                log.info("[调试信息] [doLocation] [key: {}], [value: {}]", entry2.getKey(), entry2.getValue());

                if (entry2.getKey().equals(LOCATION_ELEMENT)) {
                    int index = locationList.indexOf(locationMap);
                    elementMap = (Map<String, Object>) locationList.get(index);
                    element = (String) elementMap.get(LOCATION_ELEMENT);

                    /**
                     * 此处逻辑用于 [yaml - whhw] 部分，
                     * 当 [whhw - location - element] 为 null 时，
                     * 从 “缓存变量” 读取元素的标识信息。（如：id，xpath 等。）
                     * 注：
                     * “缓存变量” 的值，在解析 whhw 并进行循环时存入。
                     */
                    if (StringUtils.isEmpty(element)) {
                        element = CacheParamData.whhwElementCache;
                    }
                }

                if (entry2.getKey().equals(BaseLocationData.LOCATION_WAY)) {
                    int index = locationList.indexOf(locationMap);
                    wayMap = (Map<String, Object>) locationList.get(index);
                    way = (String) wayMap.get(BaseLocationData.LOCATION_WAY);
                }

                if (entry2.getKey().equals(BaseLocationData.LOCATION_REQUISITE)) {
                    int index = locationList.indexOf(locationMap);
                    requisiteMap = (Map<String, Object>) locationList.get(index);
                    requisite = (String) requisiteMap.get(BaseLocationData.LOCATION_REQUISITE);
                }

                if (entry2.getKey().equals(BaseLocationData.LOCATION_PARAM_TYPE)) {
                    int index = locationList.indexOf(locationMap);
                    paramTypeMap = (Map<String, Object>) locationList.get(index);
                    paramType = (String) paramTypeMap.get(BaseLocationData.LOCATION_PARAM_TYPE);
                }
            }
        }

        log.info("[调试信息] [doLocation] 输出 [yaml] 文件中 [location] 元素的内容：");
        log.info("[调试信息] [doLocation] [element = {}]", element);
        log.info("[调试信息] [doLocation] [way = {}]", way);
        log.info("[调试信息] [doLocation] [requisite = {}]", requisite);
        log.info("[调试信息] [doLocation] [paramType = {}]", paramType);
        Reporter.log("【调试信息】 [doLocation] 输出 [yaml] 文件中 [location] 元素的内容：");
        Reporter.log("【调试信息】 [doLocation] [element = " + element + "]");
        Reporter.log("【调试信息】 [doLocation] [way = " + way + "]");
        Reporter.log("【调试信息】 [doLocation] [requisite = " + requisite + "]");
        Reporter.log("【调试信息】 [doLocation] [paramType = " + paramType + "]");

        if (StringUtils.isEmpty(element) && StringUtils.isEmpty(way) && StringUtils.isEmpty(paramType)) {
            log.info("[调试信息] [doLocation] 获取到的 [element == way == paramType == null]，doLocation() 方法终止执行。[return null;]");
            return null;
        }

        if (StringUtils.isEmpty(way)) {
            log.info("[调试信息] [doLocation] 获取到的 [way == null]，doLocation() 方法终止执行。[return null;]");
            Reporter.log("【调试信息】 [doLocation] 获取到的 [way == null]，doLocation() 方法终止执行。[return null;]");
        }

        MobileElement mobileElement = null;

        if (null != element) {
            log.info("[调试信息] [doLocation] 即将开始执行 switch(way.toUpperCase()) 方法，匹配元素的定位方式：");
            Reporter.log("【调试信息】 [doLocation] 即将开始匹配元素的定位方式：");
            switch (way.toUpperCase()) {
                case "ID":
                case "XPATH":
                case BY_ID:
                case BY_XPATH:
                    log.info("[调试信息] [doLocation] [switch] [BY_ID || BY_XPATH] 调用 findElement({}) 获取元素：", element);
                    mobileElement = findElementByBase(element);
                    // 判断 “必要元素” 是否存在，不存在则截屏并退出。（存在则什么都不做）
                    requisiteElementIsExist(mobileElement, isRequisiteFlag, requisite);
                    break;

                case BY_LINK_TEXT:
                    log.info("[调试信息] [doLocation] [switch] [BY_LINK_TEXT] 调用 findElementByLinkText({}) 获取元素：", element);
                    mobileElement = findElementByLinkText(element);
                    requisiteElementIsExist(mobileElement, isRequisiteFlag, requisite);
                    break;

                case BY_TEXT:
                    log.info("[调试信息] [doLocation] [switch] [BY_TEXT] 调用 findElementByText({}) 获取元素：", element);
                    mobileElement = findElementByText(element);  // 判断是否存在，如果元素不存在，则返回 null
                    requisiteElementIsExist(mobileElement, isRequisiteFlag, requisite);
                    break;

                case BY_CLASS_NAME:
                    log.info("[调试信息] [doLocation] [switch] [BY_CLASS_NAME] 调用 findElementByClassName({}) 获取元素：", element);
                    mobileElement = findElementByClassName(element);
                    requisiteElementIsExist(mobileElement, isRequisiteFlag, requisite);
                    break;

                case BY_UI_SELECTOR:
                    log.info("[调试信息] [doLocation] [switch] [BY_UI_SELECTOR] 调用 findElementByUiSelectorWithText({}) 获取元素：", element);
                    mobileElement = findElementByUiSelectorWithText(element);
                    requisiteElementIsExist(mobileElement, isRequisiteFlag, requisite);
                    break;

                default:
                    log.info("[调试信息] [doLocation] [default] switch 语句中没有匹配到 [way = {}]，请确认 [yaml] 文件中的 way 元素填写是否正确。", way);
                    Reporter.log("【调试信息】 [doLocation] [default] switch 语句中没有匹配到 [way = " + way + "]，请确认 [yaml] 文件中的 way 元素填写是否正确。");
            }
        } else {
            /**
             * 当 yaml 中 element == null 时，触发此逻辑：
             * 1）判断 location 的 param 的类型，然后使用 CacheParamData.elementText 类中的 “本地变量” 进行替换；
             * 2）然后再根据 way 来定位元素。
             */
            log.info("[调试信息] [doLocation] 参数 [element == null] 时，程序会使用 “缓存变量” 作为 findElementByText() 方法的参数。");
            String cacheData = null;

            if (null != paramType) {

                switch (paramType) {
                    case PARAM_STRING:
                        cacheData = CacheParamData.elementText;
                        log.info("[调试信息] [doLocation] 当 [yaml] 中的 [param == null] 时，使用 “缓存变量” 为其赋值 [param = {}]。（注：“缓存变量” 也有可能为 null。）", cacheData);
                        break;

                    default:
                        log.info("[调试信息] [doLocation] [default] switch 语句中没有匹配到 [paramType = {}]，请确认 [yaml] 文件中的 paramType 元素填写是否正确。", paramType);
                }

                switch (way.toUpperCase()) {
                    case BY_TEXT:
                        log.info("[调试信息] [doLocation] 调用 findElementByText({}) 获取元素信息：", cacheData);
                        mobileElement = findElementByText(cacheData);
                        break;

                    default:
                        log.info("[调试信息] [doLocation] [default] switch 语句中没有匹配到 [way = {}]，请确认 [yaml] 文件中的 way 元素填写是否正确。", way);
                }
            }

        }

        log.info("[调试信息] [doLocation] doLocation(ArrayList locationList) 方法执行完毕。");
        return mobileElement;
    }


    /**
     * 解析并执行 yaml 中的 action 操作信息。
     * 注：switch 部分代码需要后续优化，与 ElementOperateUtils 整合。
     *
     * @param actionMobileElement
     * @param actionList
     * @return
     */
    public static void doAction(MobileElement actionMobileElement, ArrayList actionList) {
        log.info("[调试信息] [doAction] 开始执行 doAction(MobileElement actionMobileElement, ArrayList actionList) 方法：");
        log.info("[调试信息] [doAction] 输出 actionMobileElement 参数：{}", actionMobileElement);
        log.info("[调试信息] [doAction] 输出 actionList 参数：{}", actionList);
        Reporter.log("【调试信息】 [doAction] 开始执行：");

        /**
         * 不需要判断 actionMobileElement 入参是否为 null，因为在 yaml 文件 location 中的元素是可以不填写的，
         * 所以这种情况下 actionMobileElement 一定为 null。
         * 程序会在具体使用到 actionMobileElement 变量时，判断该变量是否为 null。
         */
        if (null == actionList) {
            /**
             * 如果 actionList 为 null，说明 yaml 文件的 action 部分没有内容，所以跳过程序对 action 部分的执行。
             */
            log.info("[调试信息] [doAction] 传入参数 [actionList == null]，doAction() 方法终止执行。[return;]");
            return;
        }

        Map<String, Object> operateMap = null;
        Map<String, Object> paramMap = null;

        String operateString = null;
        String paramString = null;

        Iterator iteratorAction = actionList.iterator();

        while (iteratorAction.hasNext()) {
            Map<String, Object> actionMap = (Map<String, Object>) iteratorAction.next();

            for (Map.Entry<String, Object> entry2 : actionMap.entrySet()) {
//                log.info("[调试信息] [doAction] key: [{}], value: [{}]", entry2.getKey(), entry2.getValue());

                if (entry2.getKey().equals(BaseActionData.ACTION_OPERATE)) {
                    int index = actionList.indexOf(actionMap);
                    operateMap = (Map<String, Object>) actionList.get(index);
                    operateString = (String) operateMap.get(BaseActionData.ACTION_OPERATE);
                }

                if (entry2.getKey().equals(BaseActionData.ACTION_PARAM)) {
                    int index = actionList.indexOf(actionMap);
                    paramMap = (Map<String, Object>) actionList.get(index);
                    paramString = (String) paramMap.get(BaseActionData.ACTION_PARAM);
                }
            }
        }

        log.info("[调试信息] [doAction] 输出 [yaml] 文件中 [action] 元素的内容：");
        log.info("[调试信息] [doAction] [operate = {}]", operateString);
        log.info("[调试信息] [doAction] [param = {}]", paramString);
        Reporter.log("【调试信息】 [doAction] 输出 [yaml] 文件中 [action] 元素的内容：");
        Reporter.log("【调试信息】 [doAction] [operate = " + operateString + "]");
        Reporter.log("【调试信息】 [doAction] [param = " + paramString + "]");

        /**
         * yaml -> action -> operate 不为 null 时，
         * 执行对具体元素的操作。
         */
        if (null != operateString) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("[调试信息] [doAction] 即将开始执行 switch(operateString.toUpperCase()) 方法，匹配元素的具体操作：");
            Reporter.log("【调试信息】 [doAction] 即将开始匹配元素的具体操作：");
            switch (operateString.toUpperCase()) {
                case CLICK:
                    log.info("[调试信息] [doAction] [switch] [CLICK] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [CLICK] 开始执行：[↓]");
                    if (null != actionMobileElement) {
                        click(actionMobileElement);
                        log.info("[调试信息] [doAction] [switch] [CLICK] 执行完毕。[↑]");
                        Reporter.log("【调试信息】 [doAction] [switch] [CLICK] 执行完毕。[↑]");
                    } else {
                        log.info("[调试信息] [doAction] [switch] [CLICK] element == null，不执行 click 操作。");
                        Reporter.log("【调试信息】 [doAction] [switch] [CLICK] element == null，不执行 click 操作。");
                    }
                    break;

                case "SENDKEY":
                case SEND_KEYS:
                    log.info("[调试信息] [doAction] [switch] [SEND_KEYS] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [SEND_KEYS] 开始执行：[↓]");
                    if (StringUtils.isEmpty(paramString)) {
                        paramString = CacheParamData.whhwElementCache;
                    }
                    sendKeys(actionMobileElement, paramString);

                    log.info("[调试信息] [doAction] [switch] [SEND_KEYS] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [SEND_KEYS] 执行完毕。[↑]");
                    break;

                case SEND_SELECT_RESULT:
                    log.info("[调试信息] [doAction] [switch] [SEND_SELECT_RESULT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [SEND_SELECT_RESULT] 开始执行：[↓]");

                    if (StringUtils.isEmpty(selectResultCacheString)) {
                        log.info("[调试信息] [switch] [SEND_SELECT_RESULT] sql 缓存变量为 null，不执行 sendKeys(actionMobileElement, selectResultCacheString) 方法。");
                    } else {
                        sendKeys(actionMobileElement, selectResultCacheString);
                    }

                    log.info("[调试信息] [doAction] [switch] [SEND_SELECT_RESULT] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [SEND_SELECT_RESULT] 执行完毕。[↑]");
                    break;

                case GET_ELEMENT_TEXT:
                    log.info("[调试信息] [doAction] [switch] [GET_ELEMENT_TEXT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_ELEMENT_TEXT] 开始执行：[↓]");
                    String text = getText(actionMobileElement);
                    CacheParamData.elementText = text;
                    BaseAssertData.resultCacehForAssert = text;
                    log.info("[调试信息] [doAction] [switch] [GET_ELEMENT_TEXT] 执行 {}() 获取到 [text = {}]", operateString, CacheParamData.elementText);
                    log.info("[调试信息] [doAction] [switch] [GET_ELEMENT_TEXT] [resultCacehForAssert = {}] ", BaseAssertData.resultCacehForAssert);
                    log.info("[调试信息] [doAction] [switch] [GET_ELEMENT_TEXT] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_ELEMENT_TEXT] 执行 " + operateString + "() 获取到 [text = " + CacheParamData.elementText + "]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_ELEMENT_TEXT] [resultCacehForAssert = " + BaseAssertData.resultCacehForAssert + "]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_ELEMENT_TEXT] 执行完毕。[↑]");
                    break;

                case PRINT_ELEMENT_TEXT:
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMENT_TEXT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMENT_TEXT] 开始执行：[↓]");
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMENT_TEXT] 打印 “缓存变量” elementText = [{}]，注：此变量通过 getText() 方法获取。", CacheParamData.elementText);
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMENT_TEXT] 打印 “缓存变量” elementText = [" + CacheParamData.elementText + "]，注：此变量通过 getText() 方法获取。");
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMENT_TEXT] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMENT_TEXT] 执行完毕。[↑]");
                    break;

                case GET_ELEMENT_LIST:
                    log.info("[调试信息] [doAction] [switch] [GET_ELEMENT_LIST] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_ELEMENT_LIST] 开始执行：[↓]");
                    CacheParamData.elementList = getElementsList(paramString);
                    log.info("[调试信息] [doAction] [switch] [GET_ELEMENT_LIST] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_ELEMENT_LIST] 执行完毕。[↑]");
                    break;

                case PRINT_ELEMNET_LIST:
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMNET_LIST] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMNET_LIST] 开始执行：[↓]");
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMNET_LIST] 打印 “缓存变量” elementList = {}，注：此变量通过 getElementList() 方法获取。", CacheParamData.elementList);
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMNET_LIST] 打印 “缓存变量” elementList = " + CacheParamData.elementList + "，注：此变量通过 getElementList() 方法获取。");
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMNET_LIST] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMNET_LIST] 执行完毕。[↑]");
                    break;

                case GET_ELEMENT_COUNT:
                    log.info("[调试信息] [doAction] [switch] [GET_ELEMENT_COUNT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_ELEMENT_COUNT] 开始执行：[↓]");
                    CacheParamData.elementCount = getElementsCount(paramString);
                    log.info("[调试信息] [doAction] [switch] [GET_ELEMENT_COUNT] 打印 “缓存变量” elementCount = [{}]", CacheParamData.elementCount);
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_ELEMENT_COUNT] 执行完毕。[↑]");
                    break;

                case PRINT_ELEMENT_COUNT:
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMENT_COUNT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMENT_COUNT] 开始执行：[↓]");
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMENT_COUNT] 打印 “缓存变量” elementCount = [{}]，注：此变量通过 getElementCount() 方法获取。", CacheParamData.elementCount);
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMENT_COUNT] 打印 “缓存变量” elementCount = [" + CacheParamData.elementCount + "]，注：此变量通过 getElementCount() 方法获取。");
                    log.info("[调试信息] [doAction] [switch] [PRINT_ELEMENT_COUNT] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [PRINT_ELEMENT_COUNT] 执行完毕。[↑]");
                    break;

                case MOVE_COORDINATE:
                    log.info("[调试信息] [doAction] [switch] [MOVE_COORDINATE] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [MOVE_COORDINATE] 开始执行：[↓]");
                    moveWithCoordinate(paramString);
                    log.info("[调试信息] [doAction] [switch] [MOVE_COORDINATE] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [MOVE_COORDINATE] 执行完毕。[↑]");
                    break;

                case MOVE_FIXED_LENGTH:
                    log.info("[调试信息] [doAction] [switch] [MOVE_FIXED_LENGTH] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [MOVE_FIXED_LENGTH] 开始执行：[↓]");
                    moveFixedUpDownLeftRight(paramString);
                    log.info("[调试信息] [doAction] [switch] [MOVE_FIXED_LENGTH] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [MOVE_FIXED_LENGTH] 执行完毕。[↑]");
                    break;

                case MOVE_UNTIL:
                    log.info("[调试信息] [doAction] [switch] [MOVE_UNTIL] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [MOVE_UNTIL] 开始执行：[↓]");
                    moveUntilAndClick(paramString);
                    log.info("[调试信息] [doAction] [switch] [MOVE_UNTIL] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [MOVE_UNTIL] 执行完毕。[↑]");
                    break;

                case SCREENSHOT:
                    log.info("[调试信息] [doAction] [switch] [SCREENSHOT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [SCREENSHOT] 开始执行：[↓]");
                    screenshot(testReportFolder);
                    log.info("[调试信息] [doAction] [switch] [SCREENSHOT] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [SCREENSHOT] 执行完毕。[↑]");
                    break;

                case WAITING:
                    log.info("[调试信息] [doAction] [switch] [WAITING] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [WAITING] 开始执行：[↓]");
                    waiting(paramString);
                    log.info("[调试信息] [doAction] [switch] [WAITING] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [WAITING] 执行完毕。[↑]");
                    break;

                case CLEAR:
                    log.info("[调试信息] [doAction] [switch] [CLEAR] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [CLEAR] 开始执行：[↓]");
                    clear(actionMobileElement);
                    log.info("[调试信息] [doAction] [switch] [CLEAR] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [CLEAR] 执行完毕。[↑]");
                    break;

                case GET_TOAST:
                    log.info("[调试信息] [doAction] [switch] [GET_TOAST] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_TOAST] 开始执行：[↓]");
                    CacheParamData.toastCacheString = getToast();
                    log.info("[调试信息] [doAction] [switch] [GET_TOAST] 输出缓存变量 [CacheParamData.toastCacheString] 的值：{}。", CacheParamData.toastCacheString);
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_TOAST] 输出缓存变量 [CacheParamData.toastCacheString] 的值：" + CacheParamData.toastCacheString + "。");
                    log.info("[调试信息] [doAction] [switch] [GET_TOAST] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GET_TOAST] 执行完毕。[↑]");
                    break;

                case GO_ACTIVITY:
                    log.info("[调试信息] [doAction] [switch] [GO_ACTIVITY] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GO_ACTIVITY] 开始执行：[↓]");

                    if (null == paramString) {
                        log.info("[调试信息] [doAction] [switch] [GO_ACTIVITY] [yaml] 文件中 [action - param] 元素为 null。");
                    } else {
                        String appPackage = new CapabilitiesEntity().getAppPackage();
                        if (StringUtils.isEmpty(appPackage)) {
                            goActivity(appPackage, paramString);
                        } else {
                            log.info("[调试信息] [doAction] [switch] [GO_ACTIVITY] [yaml] 文件中 [action - param] 元素为 null。");
                        }
                    }

                    log.info("[调试信息] [doAction] [switch] [GO_ACTIVITY] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [GO_ACTIVITY] 执行完毕。[↑]");
                    break;

                case BACK:
                    log.info("[调试信息] [doAction] [switch] [BACK] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [BACK] 开始执行：[↓]");
                    back();
                    log.info("[调试信息] [doAction] [switch] [BACK] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [BACK] 执行完毕。[↑]");
                    break;

                case TAB:
                    log.info("[调试信息] [doAction] [switch] [TAB] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [TAB] 开始执行：[↓]");
                    tab();
                    log.info("[调试信息] [doAction] [switch] [TAB] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [TAB] 执行完毕。[↑]");
                    break;

                case DIGIT:
                    log.info("[调试信息] [doAction] [switch] [DIGIT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [DIGIT] 开始执行：[↓]");
                    digit(paramString);
                    log.info("[调试信息] [doAction] [switch] [DIGIT] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [DIGIT] 执行完毕。[↑]");
                    break;

                case DIGIT_SELECT_RESULT:
                    log.info("[调试信息] [doAction] [switch] [DIGIT_SELECT_RESULT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [DIGIT_SELECT_RESULT] 开始执行：[↓]");
                    if (StringUtils.isEmpty(selectResultCacheString)) {
                        log.info("[调试信息] [switch] [DIGIT_SELECT_RESULT] sql 缓存变量为 null，不执行 digit(selectResultCacheString) 方法。");
                    } else {
                        digit(selectResultCacheString);
                    }
                    log.info("[调试信息] [doAction] [switch] [DIGIT] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [DIGIT] 执行完毕。[↑]");
                    break;

                case RECORD_START:
                    log.info("[调试信息] [doAction] [switch] [RECORD_START] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [RECORD_START] 开始执行：[↓]");
                    recordStart();
                    log.info("[调试信息] [doAction] [switch] [RECORD_START] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [RECORD_START] 执行完毕。[↑]");
                    break;

                case RECORD_STOP:
                    log.info("[调试信息] [doAction] [switch] [RECORD_STOP] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [RECORD_STOP] 开始执行：[↓]");
                    recordStop();
                    log.info("[调试信息] [doAction] [switch] [RECORD_STOP] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [RECORD_STOP] 执行完毕。[↑]");
                    break;

                case DATA_INIT:
                    log.info("[调试信息] [doAction] [switch] [DATA_INIT] 开始执行：[↓]");
                    Reporter.log("【调试信息】 [doAction] [switch] [DATA_INIT] 开始执行：[↓]");
                    dbOperate(paramString);
                    log.info("[调试信息] [doAction] [switch] [DATA_INIT] 执行完毕。[↑]");
                    Reporter.log("【调试信息】 [doAction] [switch] [DATA_INIT] 执行完毕。[↑]");
                    break;

                default:
                    log.info("[调试信息] [doAction] [default] switch 语句中没有匹配到 [operate = {}] 请确认 [yaml] 文件中的 operate 元素填写是否正确。", operateString);
                    Reporter.log("【调试信息】 [doAction] [default] switch 语句中没有匹配到 [operate = " + operateString + "] 请确认 [yaml] 文件中的 operate 元素填写是否正确。");
            }
        } else {
            log.info("[调试信息] [doAction] [yaml] 文件 [action - operate] 元素无内容，不执行 switch (operateString) 代码逻辑，程序 [return;]。");
            return;
        }

        log.info("[调试信息] [doAction] doAction(MobileElement actionMobileElement, ArrayList actionList) 方法执行完毕。");
    }


}

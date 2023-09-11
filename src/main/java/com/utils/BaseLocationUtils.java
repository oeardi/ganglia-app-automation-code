package com.utils;

import com.common.CommonData;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.common.CacheParamData.caseName;
import static com.common.CommonData.*;
import static com.utils.AndroidDriverUtils.driver;

/**
 * Location 部分的操作
 *
 * @author 冷枫红舞
 */
@Slf4j
public class BaseLocationUtils {

    private BaseLocationUtils() {
    }

    /**
     * 根据 By，获取元素，底层调用 driver.findElement(by) 方法。
     *
     * @param by
     * @return
     */
    private static MobileElement findElementWithBy(By by) {
        log.info("[调试信息] [findElementWithBy]");
        Reporter.log("【调试信息】 [findElementWithBy]");
        CommonData.isRequisiteFlag = 0;
        MobileElement mobileElement = null;

        for (int i = 0; i <= findElementLoopCount; i++) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                mobileElement = driver.findElement(by);

                if (null != mobileElement) {
                    /**
                     * 找到元素，“是否找到元素” 标志位置 1。[return mobileElement;]
                     */
                    CommonData.isRequisiteFlag = 1;
                    log.info("[调试信息] [findElementWithBy] 已定位到元素，[isRequisiteFlag = {}]", isRequisiteFlag);
                    Reporter.log("【调试信息】 [findElementWithBy] 已定位到元素，[isRequisiteFlag = " + isRequisiteFlag + "]");

                    return mobileElement;
                }
            } catch (Exception e) {
//                log.info("[调试信息] [findElementWithBy] findElementLoopCount = {}，i = {}", findElementLoopCount, i);
                int tempNum = findElementLoopCount - i;
                if (tempNum > 0) {
                    log.info("[调试信息] [findElementWithBy] 未定位到元素，即将重试 [{}] 次，当前 [isRequisiteFlag = {}]（应为 0）。", tempNum, isRequisiteFlag);
                    Reporter.log("【调试信息】 [findElementWithBy] 未定位到元素，即将重试 [" + tempNum + "] 次， 当前 [isRequisiteFlag = " + isRequisiteFlag + "]（应为 0）。");
                }
            }
        }

        Assert.assertEquals(isRequisiteFlag, 0); // 未找到元素，“是否找到元素” 标志位应为 0，程序继续执行 “是否必须元素” 处理逻辑。
        log.info("[调试信息] [findElementWithBy] 没有定位到元素，方法返回：[mobileElement = {}]。", mobileElement);
        Reporter.log("【调试信息】 [findElementWithBy] 没有定位到元素，方法返回：[mobileElement = " + mobileElement + "]。");
        log.info("[调试信息] [findElementWithBy] 执行完毕。");
        Reporter.log("【调试信息】 [findElementWithBy] 执行完毕。");

        return mobileElement;
    }

    /**
     * 根据传入 element 参数，判断执行 findById 还是 findByXpath
     *
     * @param element
     * @return
     */
    public static MobileElement findElementByBase(String element) {
        log.info("[调试信息] [findElementByBase]");

        if (StringUtils.isEmpty(element)) {
            log.info("[调试信息] [findElementByBase] 输入参数 [element == null]，findElement() 方法终止执行。[return null;]");
            return null;
        }

        By by = null;

        if (element.startsWith(ID_TAG_1) || element.contains(ID_TAG_2)) {
            log.info("[调试信息] [findElementByBase] 输出元素定位信息：By.id({})", element);
            Reporter.log("【调试信息】 [findElementByBase] 输出元素定位信息：By.id(" + element + ")");
            by = By.id(element);
        } else if (element.startsWith(XPATH_TAG_1) || element.startsWith(XPATH_TAG_2) || element.startsWith(XPATH_TAG_3)) {
            log.info("[调试信息] [findElementByBase] 输出元素定位信息：By.xpath({})", element);
            Reporter.log("【调试信息】 [findElementByBase] 输出元素定位信息：By.xpath(" + element + ")");
            by = By.xpath(element);
        } else {
            log.info("[调试信息] [findElementByBase] 输出元素定位信息：By.id({})", element);
            Reporter.log("【调试信息】 [findElementByBase] 输出元素定位信息：By.id(" + element + ")");
            by = By.id(element);
        }

        /**
         * 如果 by == null，则直接返回 null。
         */
        if (null == by) {
            log.info("[调试信息] [findElementByBase] 获取到的 [by == null]，findElement() 方法结束运行。[return null;]");
            return null;
        }

        MobileElement mobileElement = findElementWithBy(by);
        if (null == mobileElement) {
            log.info("[调试信息] [findElementByBase] [element = {}] 元素不存在，findElement() 方法结束运行，[return null;]。", element);
            Reporter.log("【调试信息】 [findElementByBase] [element = " + element + "] 元素不存在，findElement() 方法结束运行，[return null;]。");
            return null;
        }

        log.info("[调试信息] [findElementByBase] [element = {}] 元素存在。", element);
        Reporter.log("【调试信息】 [findElementByBase] [ element = " + element + "] 元素存在。");
        return mobileElement;
    }

    /**
     * 根据 class name 定位元素
     *
     * @param elementClassName
     * @return
     */
    public static MobileElement findElementByClassName(String elementClassName) {
        log.info("[调试信息] [findElementByClassName]");

        if (StringUtils.isEmpty(elementClassName)) {
            log.info("[调试信息] [findElementByClassName] 输入参数 [elementClassName == null]，findElementByClassName() 方法终止执行。[return null;]");
            return null;
        }

        log.info("[调试信息] [findElementByClassName] 输出元素定位信息 elementClassName = {}", elementClassName);
        Reporter.log("【调试信息】 [findElementByClassName] 输出元素定位信息 elementClassName = " + elementClassName);

        By by = By.className(elementClassName);

        log.info("[调试信息] [findElementByClassName] 即将调用：findElementWithBy(by) 方法.");
        MobileElement mobileElement = findElementWithBy(by);

        log.info("[调试信息] [findElementByClassName] 执行完毕。");
        return mobileElement;
    }

    /**
     * 根据 By.linkText 定位元素
     *
     * @param elementLinkText
     * @return
     */
    public static MobileElement findElementByLinkText(String elementLinkText) {
        log.info("[调试信息] [findElementByLinkText]");

        if (StringUtils.isEmpty(elementLinkText)) {
            log.info("[调试信息] [findElementByLinkText] 输入参数 [elementLinkText == null]，findElementByLinkText() 方法终止执行。[return null;]");
            return null;
        }

        log.info("[调试信息] [findElementByLinkText] 输出元素定位信息 elementLinkText = {}", elementLinkText);
        Reporter.log("【调试信息】 [findElementByLinkText] 输出元素定位信息 elementLinkText = " + elementLinkText);

        By by = By.linkText(elementLinkText);

        log.info("[调试信息] [findElementByLinkText] 即将调用：findElementWithBy(by) 方法。");
        MobileElement mobileElement = findElementWithBy(by);

        log.info("[调试信息] [findElementByLinkText] 执行完毕。");
        return mobileElement;
    }

    /**
     * 拼接 text 字符串为 Xpath 格式，通过 By.xpath 来查找元素。
     *
     * @param elementString
     * @return
     */
    public static MobileElement findElementByText(String elementString) {
        log.info("[调试信息] [findElementByText]");

        if (StringUtils.isEmpty(elementString)) {
            log.info("[调试信息] [findElementByText] 输入参数 [elementString == null]，findElementByText() 方法终止执行。[return null;]");
            return null;
        }

        String xpathString = "//*[@text='" + elementString + "']";
        log.info("[调试信息] [findElementByText] 输出元素定位信息 xpathString = {}", xpathString);
        Reporter.log("【调试信息】 [findElementByText] 输出元素定位信息 xpathString = " + xpathString);

        By by = By.xpath(xpathString);

        log.info("[调试信息] [findElementByText] 即将调用：findElementWithBy() 方法。");
        MobileElement mobileElement = findElementWithBy(by);

        log.info("[调试信息] [findElementByText] 执行完毕。");
        return mobileElement;
    }

    /**
     * 向下滑动屏幕，根据 text 查找元素，直到找到为止。
     *
     * @param elementText
     * @return
     */
    public static MobileElement findElementByUiSelectorWithText(String elementText) {
        log.info("[调试信息] [findElementByUiSelectorWithText]");

        if (StringUtils.isEmpty(elementText)) {
            log.info("[调试信息] [findElementByUiSelectorWithText] 输入参数 [elementText == null]，findElementByUiSelectorWithText() 方法终止执行。[return null;]");
            return null;
        }

        String uiScrollables = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().text(\"" + elementText + "\").instance(0))";
        log.info("[调试信息] [findElementByUiSelectorWithText] 输出 uiScrollables：[{}]。", uiScrollables);

        MobileElement mobileElement = driver.findElementByAndroidUIAutomator(uiScrollables);
        if (null != mobileElement) {
            log.info("[调试信息] [findElementByUiSelectorWithText] 元素 [{}] 存在。", elementText);
            Reporter.log("【调试信息】 [findElementByUiSelectorWithText] 元素 [" + elementText + "] 存在。");
            log.info("[调试信息] [findElementByUiSelectorWithText] 执行完毕。");
            Reporter.log("[调试信息] [findElementByUiSelectorWithText] 执行完毕。");
            return mobileElement;
        }

        log.info("[调试信息] [findElementByUiSelectorWithText] 没有定位到元素，findElementByUiSelectorWithText() 方法即将返回 [mobileElement = null]。");
        Reporter.log("[调试信息] [findElementByUiSelectorWithText] 没有定位到元素，findElementByUiSelectorWithText() 方法即将返回 [mobileElement = null]。");
        log.info("[调试信息] [findElementByUiSelectorWithText] 执行完毕。");
        Reporter.log("[调试信息] [findElementByUiSelectorWithText] 执行完毕。");
        return mobileElement;
    }

    /**
     * 获取元素集合
     *
     * @param element
     * @return
     */
    public static List<String> myFindElements(String element) {
        log.info("[调试信息] [myFindElements]");

        if (StringUtils.isEmpty(element)) {
            log.info("[调试信息] [myFindElements] 传入参数 [element == null]，myFindElements() 方法终止执行。[return null;]");
            return null;
        }

        List<String> list = new ArrayList<>();

        if (element.startsWith(ID_TAG_1) || element.contains(ID_TAG_2)) {
            log.info("[调试信息] [myFindElements] By.id() 获取元素信息：[{}]", element);
            for (MobileElement mobileElement : driver.findElements(By.id(element))) {
                list.add(mobileElement.getText());
            }
        } else if (element.startsWith(XPATH_TAG_1) || element.startsWith(XPATH_TAG_2) || element.startsWith(XPATH_TAG_3)) {
            log.info("[调试信息] [myFindElements] By.xpath() 获取元素信息：[{}]", element);
            for (MobileElement mobileElement : driver.findElements(By.xpath(element))) {
                list.add(mobileElement.getText());
            }
        } else {
            String tempXpath = "//*[@text='" + element + "']";
            log.info("[调试信息] [myFindElements] By.xpath() 获取元素信息：[{}]", tempXpath);
            for (MobileElement mobileElement : driver.findElements(By.xpath(tempXpath))) {
                list.add(mobileElement.getText());
            }
        }

        if (null == list) {
            log.info("[调试信息] [myFindElements] 获取到的 [list == null]，myFindElements() 方法终止执行。[return null;]");
            return null;
        }

        log.info("[调试信息] [myFindElements] 执行完毕。");
        return list;
    }

    /**
     * 如果 “必要元素” 没有找到，即：requisite = Y 且 isRequisiteFlag = 0，
     * 则终止程序执行。并在当前页面截图。
     *
     * @param mobileElement
     * @param isRequisiteFlag 元素是否 “找到” 标志位
     * @param requisite       是否是 “必要元素” 标志位
     */
    public static void requisiteElementIsExist(MobileElement mobileElement, int isRequisiteFlag, String requisite) {

        if (null == mobileElement && isRequisiteFlag == 0 && requisite.toUpperCase().equals(_Y)) {
            log.info("[调试信息] [requisiteElementIsExist] 赋值 [mobileElement = null], [isRequisiteFlag = 0], [requisite = Y]。");

            String fileName = caseName + "_" + "Before-Exit" + ".png";
            File destFile = new File(testReportFolder, fileName);
            try {
                if (null != driver) {
                    File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(sourceFile, destFile);
                    log.info("[调试信息] [requisiteElementIsExist] 截图保存路径：{}{}", testReportFolder, fileName);
                    Reporter.log("【调试信息】 [requisiteElementIsExist] 截图保存路径：" + testReportFolder + fileName);
                    Thread.sleep(sleepTime);
                } else {
                    log.info("[调试信息] [requisiteElementIsExist] driver == null.");
                }
            } catch (Exception e) {
                log.info("[调试信息] [requisiteElementIsExist] 打印操作元素失败异常信息：{}", e.toString());
//            throw new WebDriverException();
            }

            log.info("[调试信息] [requisiteElementIsExist] “必要元素” 不存在，程序停止运行。（手动抛出 RuntimeException 异常）");
            throw new RuntimeException();

        }

    }

}

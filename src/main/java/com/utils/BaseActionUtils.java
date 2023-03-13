package com.utils;

import com.common.CommonData;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static com.common.CacheParamData.pageName;
import static com.common.CacheParamData.screenshotCount;
import static com.utils.AndroidDriverUtils.driver;
import static com.utils.BaseLocationUtils.findElement;
import static com.utils.BaseLocationUtils.myFindElements;

/**
 * Action 部分的操作
 *
 * @author 冷枫红舞
 */
@Slf4j
public class BaseActionUtils {

    /**
     * 点击
     *
     * @param mobileElement
     */
    public static void click(MobileElement mobileElement) {
        log.info("[调试信息] [click]");
        Reporter.log("【调试信息】 [click]");
        /**
         * 框架设计思想，是将 “元素的定位”，和定位后的 “元素的操作” 分为两个步骤处理。
         * 当没有定位到元素时，MobileElement 的返回值就会为 null 值，
         * 那么在 click() 的时候就会报错。所以需要 if (mobileElement != null) 判断。
         */
        if (null == mobileElement) {
            log.info("[调试信息] [click] 输入参数 [mobileElement == null]，click() 方法终止执行。[return;]");
            return;
        }

        mobileElement.click();

        log.info("[调试信息] [click] 执行完毕。");
    }

    /**
     * 输入 keyword
     *
     * @param mobileElement
     * @param keyword
     */
    public static void sendKeys(MobileElement mobileElement, String keyword) {
        log.info("[调试信息] [sendKeys]");
        Reporter.log("【调试信息】 [sendKeys]");

        if (StringUtils.isEmpty(keyword)) {
            log.info("[调试信息] [sendKeys] 输入参数 [keyword == null]，sendKeys() 方法终止执行。[return;]");
            return;
        }

        log.info("[调试信息] [sendKeys] 输入参数 [keyword = {}]", keyword);
        Reporter.log("【调试信息】 [sendKeys] 输入参数 [keyword = ]" + keyword);

        if (null == mobileElement) {
            log.info("[调试信息] [sendKeys] 输入参数 [mobileElement == null]，sendKeys() 方法终止执行。[return;]");
            return;
        }

        mobileElement.sendKeys(keyword);

        log.info("[调试信息] [sendKeys] 执行完毕。");
    }

    /**
     * 获取元素的 text
     *
     * @param mobileElement
     * @return
     */
    public static String getText(MobileElement mobileElement) {
        log.info("[调试信息] [getText]");
        Reporter.log("【调试信息】 [getText]");

        if (null == mobileElement) {
            log.info("[调试信息] [getText] 输入参数 [mobileElement == null]，getText() 方法终止执行。[return null;]");
            return null;
        }

        String string = mobileElement.getText();
        if (StringUtils.isEmpty(string)) {
            log.info("[调试信息] [getText] 获取到的 [text == null]，getText() 方法终止执行。[return null;]");
            return null;
        }

        log.info("[调试信息] [getText] 获取到的 [text = {}]", string);
        Reporter.log("【调试信息】 [getText] 获取到的 [text = " + string + "]");
        log.info("[调试信息] [getText] 执行完毕。");

        return string;
    }


    /**
     * 返回根据 element 找到的所有元素
     *
     * @param element
     * @return
     */
    public static List<String> getElementsList(String element) {
        log.info("[调试信息] [getElementsList]");
        Reporter.log("【调试信息】 [getElementsList]");

        if (StringUtils.isEmpty(element)) {
            log.info("[调试信息] [getElementsList] 传入参数 [element == null]，getElementsList() 方法终止执行。[return null;]");
            return null;
        }

        List<String> list = myFindElements(element);
        if (null == list) {
            log.info("[调试信息] [getElementsList] 获取到的 [list == null]，getElementsList() 方法终止执行。[return null;]");
            return null;
        }

        log.info("[调试信息] [getElementsList] 根据元素 [{}] 检索出 [{}] 条数据：{}", element, list.size(), list.toString());
        Reporter.log("【调试信息】 [getElementsList] 根据元素 [" + element + "] 检索出 [" + list.size() + "] 条数据：" + list.toString());
        log.info("[调试信息] [getElementsList] 执行完毕。");

        return list;
    }

    /**
     * 返回根据 element 找到的元素的总数
     *
     * @param element
     * @return
     */
    public static int getElementsCount(String element) {
        log.info("[调试信息] [getElementsCount]");
        Reporter.log("【调试信息】 [getElementsCount]");

        if (StringUtils.isEmpty(element)) {
            log.info("[调试信息] [getElementsCount] 传入参数 [element == null]，getElementsCount() 方法终止执行。[return -1;]");
            return -1;
        }

        List<String> list = getElementsList(element);
        if (null == list) {
            log.info("[调试信息] [getElementsCount] 获取到的 [list == null]，getElementsCount() 方法终止执行。[return -1;]");
            return -1;
        }

        int count = list.size();

        log.info("[调试信息] [getElementsCount] 获取到的元素数量 [list = {}]", count);
        Reporter.log("【调试信息】 [getElementsCount] 获取到的元素数量 [list = " + count + "]");
        log.info("[调试信息] [getElementsCount] 执行完毕。");

        return count;
    }

    /**
     * 传入滑动屏幕的起止坐标，并执行滑动操作。
     *
     * @param pointString 字符串格式的 x，y 坐标，解析时以 “ ”（空格）分割。
     */
    public static void moveWithCoordinate(String pointString) {
        log.info("[调试信息] [moveWithCoordinate]");
        Reporter.log("【调试信息】 [moveWithCoordinate]");

        if (StringUtils.isEmpty(pointString)) {
            log.info("[调试信息] [moveWithCoordinate] 传入参数 [pointString == null]，moveWithCoordinate() 方法终止执行。[return;]");
            return;
        }

        String[] strings = pointString.split(" ");
//        log.info("[调试信息] [move] split() 解析字符串格式的坐标为：{}", Arrays.toString(strings));
//        for (int i = 0; i < strings.length; i++) {
//            log.info("[调试信息] [BaseActionUtils] [move] 输出 pointString 坐标数据：{}", strings[i]);
//        }

        double startX = Double.parseDouble(strings[0]);
        double startY = Double.parseDouble(strings[1]);
        double endX = Double.parseDouble(strings[2]);
        double endY = Double.parseDouble(strings[3]);
        // double startX = 0.9d;  double startY = 0.5d;  double endX = 0.5d;  double endY = 0.8d;
        log.info("[调试信息] [moveWithCoordinate] 备注：屏幕左上方坐标为 [0.0, 0.0]，屏幕右下方坐标为 [1.0, 1.0]。");
        Reporter.log("【调试信息】 [moveWithCoordinate] 备注：屏幕左上方坐标为 [0.0, 0.0]，屏幕右下方坐标为 [1.0, 1.0]。");
        log.info("[调试信息] [moveWithCoordinate] 传入的坐标参数：[startX = {}，startY = {}]，[endX = {}，endY = {}]", startX, startY, endX, endY);
        Reporter.log("【调试信息】 [moveWithCoordinate] 传入的坐标参数：[startX = " + startX + "，startY = " + startY + "]，[endX = " + endX + "，endY = " + endY + "]");

        /**
         * 获取屏幕的大小
         */
        Dimension dimension = driver.manage().window().getSize();
        int width = dimension.getWidth();
        int heigth = dimension.getHeight();
        log.info("[调试信息] [moveWithCoordinate] 获取屏幕大小：[width = {}]，[heigth = {}]。", width, heigth);

        int fromX = (int) (width * startX);
        int toX = (int) (width * endX);
        int fromY = (int) (heigth * startY);
        int toY = (int) (heigth * endY);
        log.info("[调试信息] [moveWithCoordinate] 从 [fromX = {}, fromY = {}] 移动到 [toX = {}, toY = {}]", fromX, fromY, toX, toY);
        Reporter.log("【调试信息】 [moveWithCoordinate] 从 [fromX = " + fromX + ", fromY = " + fromY + "] 移动到 [toX = " + toX + ", toY = " + toY + "]");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(fromX, fromY));
        touchAction.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
        touchAction.moveTo(PointOption.point(toX, toY));
        touchAction.release().perform();

        log.info("[调试信息] [moveWithCoordinate] 执行完毕。");
    }

    /**
     * 固定的移动距离
     *
     * @param param moveUp，moveDown，moveLeft，moveRight
     */
    public static void moveFixedUpDownLeftRight(String param) {
        log.info("[调试信息] [moveFixedUpDownLeftRight]");
        Reporter.log("【调试信息】 [moveFixedUpDownLeftRight]");

        if (StringUtils.isEmpty(param)) {
            log.info("[调试信息] [moveFixedUpDownLeftRight] 传入参数 [element == null]，moveFixedUpDownLeftRight() 方法终止执行。[return;]");
            return;
        }

        double startX = 0.0d;
        double startY = 0.0d;
        double endX = 0.0d;
        double endY = 0.0d;

        final String moveUp = "up";
        final String moveDown = "down";
        final String moveLeft = "left";
        final String moveRight = "right";

        if (param.equals(moveUp)) {
            startX = 0.5d;
            startY = 0.8d;
            endX = 0.5d;
            endY = 0.3d;
        }

        if (param.equals(moveDown)) {
            startX = 0.5d;
            startY = 0.2d;
            endX = 0.5d;
            endY = 0.7d;
        }

        if (param.equals(moveLeft)) {
            startX = 0.8d;
            startY = 0.5d;
            endX = 0.2d;
            endY = 0.5d;
        }

        if (param.equals(moveRight)) {
            startX = 0.2d;
            startY = 0.5d;
            endX = 0.8d;
            endY = 0.5d;
        }

        log.info("[调试信息] [moveFixedUpDownLeftRight] 备注：屏幕左上方坐标为 [0.0, 0.0]，屏幕右下方坐标为 [1.0, 1.0]。");
        log.info("[调试信息] [moveFixedUpDownLeftRight] 传入的坐标参数：[startX = {}，startY = {}]，[endX = {}，endY = {}]", startX, startY, endX, endY);

        /**
         * 获取屏幕的大小
         */
        Dimension dimension = driver.manage().window().getSize();
        int width = dimension.getWidth();
        int heigth = dimension.getHeight();
        log.info("[调试信息] [moveFixedUpDownLeftRight] 获取屏幕大小：[width = {}]，[heigth = {}]。", width, heigth);

        int fromX = (int) (width * startX);
        int toX = (int) (width * endX);
        int fromY = (int) (heigth * startY);
        int toY = (int) (heigth * endY);
        log.info("[调试信息] [moveFixedUpDownLeftRight] 从 [fromX = {}, fromY = {}] 移动到 [toX = {}, toY = {}]", fromX, fromY, toX, toY);
        Reporter.log("【调试信息】 [moveFixedUpDownLeftRight] 从 [fromX = " + fromX + ", fromY = " + fromY + "] 移动到 [toX = " + toX + ", toY = " + toY + "]");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(fromX, fromY));
        touchAction.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
        touchAction.moveTo(PointOption.point(toX, toY));
        touchAction.release().perform();

        log.info("[调试信息] [moveFixedUpDownLeftRight] 执行完毕。");
    }

    /**
     * 截屏（如果不指定路径，则图片存储在工程路径下）
     */
    public static void screenshot() {
        log.info("[调试信息] [screenshot]");
        Reporter.log("【调试信息】 [screenshot]");
        log.info("[调试信息] [screenshot] 屏幕截图保存在 test-screenshot/ 目录下。");
        Reporter.log("【调试信息】 [screenshot] 屏幕截图保存在 test-screenshot/ 目录下。");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String fileFolder = "test-screenshot/";
        String fileName = screenshotCount + "_" + pageName + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + ".png";

        File reportDir = new File(fileFolder);
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdir();
        }

        File file = new File(fileFolder, fileName);

        try {
            FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE).getCanonicalFile(), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        screenshotCount++;

        log.info("[调试信息] [screenshot] 输出截图文件名：{}", fileName);
        Reporter.log("【调试信息】 [screenshot] 输出截图文件名：" + fileName);

        log.info("[调试信息] [screenshot] 执行完毕。");
    }

    /**
     * 等待时间，在 yaml 文件中的 [action - operate 和 param] 两个元素设置。
     *
     * @param param 单位：毫秒。
     */
    public static void waiting(String param) {
        log.info("[调试信息] [waiting]");
        Reporter.log("【调试信息】 [waiting]");

        long millisecond = 0;

        if (StringUtils.isEmpty(param)) {
            millisecond = 1000;
            log.info("[调试信息] [waiting] 传入参数 [param == null]，默认赋值 millisecond = 1000（毫秒）。");
        } else {
            log.info("[调试信息] [waiting] 程序 waiting {}（毫秒）...", param);
            Reporter.log("【调试信息】 [waiting] 程序 waiting " + param + "（毫秒）...");
            millisecond = Long.parseLong(param);
        }

        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("[调试信息] [waiting] 执行完毕。");
    }

    /**
     * 清空控件内容
     *
     * @param mobileElement
     */
    public static void clear(MobileElement mobileElement) {
        log.info("[调试信息] [clear]");
        Reporter.log("【调试信息】 [clear]");

        if (null == mobileElement) {
            log.info("[调试信息] [clear] 传入参数 [mobileElement == null]，clear() 方法终止执行。[return;]");
            return;
        }

        mobileElement.clear();

        log.info("[调试信息] [clear] 执行完毕。");
    }

    /**
     * 获取 toast
     */
    public static String getToast() {
        log.info("[调试信息] [getToast]");
        Reporter.log("【调试信息】 [getToast]");

        MobileElement mobileElement = findElement(CommonData.TOAST_XPATH_STRING);

        String toastText = mobileElement.getText();

        if (StringUtils.isEmpty(toastText)) {
            log.info("[调试信息] [getToast] 获取到的 [toast = null]，getToast() 方法终止执行。[return null;]");
            return null;
        }

        log.info("[调试信息] [getToast] 获取到的 [toast] 为：{}", toastText);
        Reporter.log("【调试信息】 [getToast] 获取到的 [toast] 为：" + toastText);

        log.info("[调试信息] [getToast] 执行完毕。");
        return toastText;
    }

    /**
     * 进入指定的 activity 页面
     *
     * @param appPackage
     * @param appActivity
     */
    public static void goActivity(String appPackage, String appActivity) {
        log.info("[调试信息] [goActivity]");
        Reporter.log("【调试信息】 [goActivity]");

        if (StringUtils.isEmpty(appPackage) || StringUtils.isEmpty(appActivity)) {
            log.info("[调试信息] [goActivity] 传入参数 [appPackage 或 appActivity 为 null]，goActivity() 方法终止执行。[return;]");
            return;
        }

        log.info("[调试信息] [goActivity] [appPackage = {}]", appPackage);
        Reporter.log("【调试信息】 [goActivity] [appPackage = " + appPackage + "]");
        log.info("[调试信息] [goActivity] [appActivity = {}]", appActivity);
        Reporter.log("【调试信息】 [goActivity] [appActivity = " + appActivity + "]");

        Activity activity = new Activity(appPackage, appActivity);

        if (null == activity) {
            log.info("[调试信息] [goActivity] 获取到的 [activity == null]，goActivity() 方法终止执行。[return;]");
            return;
        }

        driver.startActivity(activity);

        log.info("[调试信息] [goActivity] 执行完毕。");
    }

    /**
     * android 返回键
     */
    public static void back() {
        log.info("[调试信息] [back]");
        Reporter.log("【调试信息】 [back]");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("[调试信息] [back] 点击 Android 键盘 [返回] 键");
        Reporter.log("【调试信息】 [back] 点击 Android 键盘 [返回] 键");
        driver.pressKey(new KeyEvent().withKey(AndroidKey.BACK));

        log.info("[调试信息] [back] 执行完毕。");
    }

    public static void digit(String digit) {
        log.info("[调试信息] [digit]");
        Reporter.log("【调试信息】 [digit]");

        log.info("[调试信息] [digit] 输入参数 [digit = {}]", digit);
        Reporter.log("【调试信息】 [digit] 输入参数 [digit = ]" + digit);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        char[] chars = digit.toCharArray();
        for (int i = 0; i < chars.length; i++) {
//            log.info("[调试信息] [chars]：{}", chars[i]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            switch (chars[i]) {
                case '1':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [1] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [1] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_1));
                    break;
                case '2':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [2] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [2] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_2));
                    break;
                case '3':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [3] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [3] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_3));
                    break;
                case '4':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [4] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [4] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_4));
                    break;
                case '5':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [5] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [5] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_5));
                    break;
                case '6':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [6] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [6] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_6));
                    break;
                case '7':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [7] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [7] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_7));
                    break;
                case '8':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [8] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [8] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_8));
                    break;
                case '9':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [9] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [9] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_9));
                    break;
                case '0':
                    log.info("[调试信息] [digit] 点击 Android 键盘 [0] 键");
//                    Reporter.log("【调试信息】 [digit] 点击 Android 键盘 [0] 键");
                    driver.pressKey(new KeyEvent().withKey(AndroidKey.DIGIT_0));
                    break;
                default:
                    log.info("[调试信息] [digit] [default] 输入参数中包含 [0 - 9] 以外的字符。");
//                    Reporter.log("[调试信息] [digit] [default] 输入参数中包含 [0 - 9] 以外的字符。");
            }
        }
        log.info("[调试信息] [digit] 执行完毕。");
    }

    /**
     * 开始 - 屏幕录制
     */
    public static void recordStart() {
        log.info("[调试信息] [recordStart]");
        Reporter.log("【调试信息】 [recordStart]");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("[调试信息] [recordStart] [开始录制屏幕]");
        Reporter.log("【调试信息】 [recordStart] [开始录制屏幕]");
        driver.startRecordingScreen();

        log.info("[调试信息] [recordStart] 执行完毕。");
    }

    /**
     * 结束 - 屏幕录制
     */
    public static void recordStop() {
        log.info("[调试信息] [recordStop]");
        Reporter.log("【调试信息】 [recordStop]");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("[调试信息] [recordStart] [结束录制屏幕]");
        Reporter.log("【调试信息】 [recordStart] [结束录制屏幕]");
        String video = driver.stopRecordingScreen();


        log.info("[调试信息] [recordStop] 执行完毕。");
    }

    /**
     * 退出，并释放 sessionId。
     */
    public static void quit() {
        log.info("[调试信息] [quit]");
        Reporter.log("【调试信息】 [quit]");

        String fileFolder = "test-before-quit-screenshot/";

        if (StringUtils.isEmpty(pageName)) {
            pageName = "InitPage";
        }
        String fileName = "quit" + "_" + pageName + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".png";

        File reportDir = new File(fileFolder);
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdir();
        }

        File file = new File(fileFolder, fileName);

        try {
            FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE).getCanonicalFile(), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("[调试信息] [quit] 输出截图文件名：{}", fileName);
        Reporter.log("【调试信息】 [quit] 输出截图文件名：" + fileName);

        log.info("[调试信息] [quit] 退出前截图，图片保存在 {} 目录下。", fileFolder);
        Reporter.log("【调试信息】 [quit] 退出前截图，图片保存在 " + fileFolder + " 目录下。");

        driver.quit();
        log.info("[调试信息] [quit] 执行完毕。（已释放 sessionId）");

        throw new RuntimeException();
    }

    /**
     * 关闭 APP，不释放 driver 对象。
     */
    public static void closeApp() {
        log.info("[调试信息] [closeApp]");
        Reporter.log("【调试信息】 [closeApp]");

        driver.closeApp();

        log.info("[调试信息] [closeApp] 执行完毕。");
    }

    /**
     * element 是否可见
     */
    public static boolean isDisplayed() {
        return true;
    }

    /**
     * element 是否可用
     */
    public static boolean isEnabled() {
        return true;
    }

    /**
     * element 是否被选中
     */
    public static boolean isSelected() {
        return true;
    }


}

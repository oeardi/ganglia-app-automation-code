package com.utils;

import com.alibaba.fastjson.JSONObject;
import com.entity.CapabilitiesEntity;
import com.entity.CommonConfigEntity;
import com.entity.RemoteUrlEntity;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.common.CacheParamData.pageName;
import static com.common.CapabilityData.*;
import static com.common.CommonData.*;

/**
 * 用于直接获取不同设备的 DesiredCapabilities（真机 & 模拟器）
 *
 * @author 冷枫红舞
 */
@Slf4j
public class AndroidDriverUtils {

    private AndroidDriverUtils() {
    }

    private static AndroidDriverUtils androidDriverUtils = new AndroidDriverUtils();

    public static AndroidDriverUtils getAndroidDriverUtils() {
        return androidDriverUtils;
    }

    public static AndroidDriver<MobileElement> driver = null;

    // 隐式等待
    private static final long implicitly_wait_time = 20;

    /**
     * 初始化 Driver，调用 public AndroidDriver<MobileElement> getInitDriver()
     */
    public static void initDriver() {
        log.info("[调试信息] [initDriver]");
        log.info("[调试信息] [initDriver] 准备初始化 AndroidDriver 对象：");

        URL url = getUrl();
        if (null == url) {
            log.info("[调试信息] [initDriver] 获取到的 [url == null]，initDriver() 终止执行。[return;]");
            return;
        }

        DesiredCapabilities desiredCapabilities = getDesiredCapabilities();

        driver = new AndroidDriver(url, desiredCapabilities);
        if (null == driver) {
            log.info("[调试信息] [initDriver] 获取到的 [driver == null]，initDriver() 终止执行。[return;]");
            return;
        } else {
            log.info("[调试信息] [initDriver] 初始化 [driver] 对象完毕。");
            Reporter.log("【调试信息】 [initDriver] 初始化 [driver] 对象完毕。");
        }

        // implicitlyWait 隐式等待
        driver.manage().timeouts().implicitlyWait(implicitly_wait_time, TimeUnit.SECONDS);
//        String context = driver.getContext();
//        String pageSource = driver.getPageSource();
//        String currentPackage = driver.getCurrentPackage();
//        String currentActivity = driver.currentActivity();
//        log.info("[调试信息] [getInitDriver] 打印 [driver.getContext() = {}]", context);
//        log.info("[调试信息] [getInitDriver] 打印 [driver.getPageSource() = {}]", pageSource);
//        log.info("[调试信息] [getInitDriver] 打印 [driver.getCurrentPackage() = {}]", currentPackage);
//        log.info("[调试信息] [getInitDriver] 打印 [driver.currentActivity() = {}]", currentActivity);

        /**
         * 创建测试报告目录
         */
        String folder = testReportFolder;
        File reportDir = new File(folder);
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdir();
        }

        log.info("[调试信息] [initDriver] 执行完毕。");
    }

    /**
     * 创建 URL 实例，初始化 appium-server 连接，后续用于创建 AndrodiDriver 对象。
     *
     * @return
     */
    private static URL getUrl() {
        log.info("[调试信息] [getUrl]");

        RemoteUrlEntity remoteUrl = new RemoteUrlEntity();
        if (null == remoteUrl) {
            log.info("[调试信息] [getUrl] remoteUrl 为 null，方法返回 null。");
            return null;
        }

        URL url = null;

        try {
            url = new URL(remoteUrl.getServer() + remoteUrl.getPort() + remoteUrl.getPath());
            log.info("[调试信息] [getUrl] 打印 [url = {}]", url.toString());

            if (null == url) {
                log.info("[调试信息] [getUrl] url 为 null，方法返回 null。");
                return null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return url;
    }

    /**
     * 初始化 DesiredCapabilities 对象，后续用于创建 AndrodiDriver 对象。
     *
     * @return
     */
    private static DesiredCapabilities getDesiredCapabilities() {
        log.info("[调试信息] [getDesiredCapabilities]");

        /**
         * capabilities.json 处理
         */
        CapabilitiesEntity capabilities = getCapabilitiesWithJsonFile();
        if (null == capabilities) {
            log.info("[调试信息] [getDesiredCapabilities] capabilities 为 null，方法返回 null。");
            return null;
        }

        String platformName = capabilities.getPlatformName();
        if (StringUtils.isEmpty(platformName)) {
            platformName = "android";
        }

        String platformVersion = capabilities.getPlatformVersion();
        String appPackage = capabilities.getAppPackage();
        String appActivity = capabilities.getAppActivity();
        String automationName = capabilities.getAutomationName();
        String deviceName = capabilities.getDeviceName();

        if (StringUtils.isEmpty(platformVersion) || StringUtils.isEmpty(appPackage) || StringUtils.isEmpty(appActivity)
                || StringUtils.isEmpty(automationName) || StringUtils.isEmpty(deviceName)) {
            log.info("[调试信息] [getDesiredCapabilities] capabilities 所需要的必要参数为 null，程序结束运行，方法返回 null。");
            return null;
        }

        String newCommandTimeout = capabilities.getNewCommandTimeout();
        if (StringUtils.isEmpty(newCommandTimeout)) {
            newCommandTimeout = "3000";
        }

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(DEVICE_NAME, deviceName);
        desiredCapabilities.setCapability(PLATFORM_NAME, platformName);
        desiredCapabilities.setCapability(PLATFORM_VERSION, platformVersion);
        desiredCapabilities.setCapability(APP_PACKAGE, appPackage);
        desiredCapabilities.setCapability(APP_ACTIVITY, appActivity);
        desiredCapabilities.setCapability(AUTOMATION_NAME, automationName);
        desiredCapabilities.setCapability(NEW_COMMAND_TIMEOUT, newCommandTimeout);

        String language = capabilities.getLanguage();
        if (!StringUtils.isEmpty(language)) {
            desiredCapabilities.setCapability(LANGUAGE, "");
        }

        Boolean autoLaunch = capabilities.getAutoLaunch();
        if (null != autoLaunch) {
            desiredCapabilities.setCapability(AUTO_LAUNCH, autoLaunch);
        }

        Boolean autoWebview = capabilities.getAutoWebview();
        if (null != autoWebview) {
            desiredCapabilities.setCapability(AUTO_WEB_VIEW, autoWebview);
        }

        Boolean noReset = capabilities.getNoReset();
        if (null != noReset) {
            desiredCapabilities.setCapability(NO_RESET, noReset);
        }

        Boolean fullRest = capabilities.getFullReset();
        if (null != fullRest) {
            desiredCapabilities.setCapability(FULL_REST, fullRest);
        }

        Boolean dontStopAppOnReset = capabilities.getDontStopAppOnReset();
        if (null != dontStopAppOnReset) {
            desiredCapabilities.setCapability(DONT_STOP_APP_ON_RESET, dontStopAppOnReset);
        }

        Boolean skipDeviceInitialization = capabilities.getSkipDeviceInitialization();
        if (null != skipDeviceInitialization) {
            desiredCapabilities.setCapability(SKIP_DEVICE_INITIALIZATION, skipDeviceInitialization);
        }

        Boolean skipServerInstallation = capabilities.getSkipServerInstallation();
        if (null != skipServerInstallation) {
            desiredCapabilities.setCapability(SKIP_SERVER_INSTALLATION, skipServerInstallation);
        }

        Boolean unicodeKeyboard = capabilities.getUnicodeKeyboard();
        if (null != unicodeKeyboard) {
            desiredCapabilities.setCapability(UNICODE_KEYBOARD, unicodeKeyboard);
        }

        Boolean resetKeyboard = capabilities.getResetKeyboard();
        if (null != resetKeyboard) {
            desiredCapabilities.setCapability(RESET_KEYBOARD, resetKeyboard);
        }

        log.info("[调试信息] [getDesiredCapabilities] 输出 [desiredCapabilities]：{}", desiredCapabilities.toJson());
        log.info("[调试信息] [getDesiredCapabilities] 执行完毕。");
        return desiredCapabilities;
    }

    /**
     * 创建 Capabilities 实例，从配置文件中读 json 数据，再转化为 Capabilities 对象。
     *
     * @return
     */
    private static CapabilitiesEntity getCapabilitiesWithJsonFile() {
        log.info("[调试信息] [getCapabilitiesWithJsonFile]");
        ResourceBundleUtils resourceBundleUtils = new ResourceBundleUtils();
        /**
         * 读取 application.properties 文件
         */
        String jsonFile = resourceBundleUtils.getValue("default", "device");
        if (null == jsonFile) {
            log.info("[调试信息] [getCapabilitiesWithJsonFile] jsonFile 为 null，getCapabilitiesWithJsonFile() 方法返回 null。");
            return null;
        }

        /**
         * 读取 capabilities.json 文件，获取文件中的 json string
         */
        JsonFileUtils mustacheJsonFileUtils = new JsonFileUtils();

        String capJsonString = mustacheJsonFileUtils.getStringWithJsonFile(jsonFile);
        if (null == capJsonString) {
            log.info("[调试信息] [getCapabilitiesWithJsonFile] capJsonString 为 null，getCapabilitiesWithJsonFile() 方法返回 null。");
            return null;
        }

        /**
         * json string 转 JavaBean
         */
        CapabilitiesEntity capabilities = JSONObject.parseObject(capJsonString, CapabilitiesEntity.class);
        if (null == capabilities) {
            log.info("[调试信息] [getCapabilitiesWithJsonFile] capabilities 为 null，getCapabilitiesWithJsonFile() 方法返回 null。");
            return null;
        }

        log.info("[调试信息] [getCapabilitiesWithJsonFile] 返回 capabilities 对象，方法执行完毕。");

        return capabilities;
    }

    private static int screenshotCountQuit = 0;

    /**
     * 退出，并在退出前截图。
     */
    public static void errorBeforeExitScreenshot(String fileFolder) {
        log.info("[调试信息] [beforeExitScreenshot]");
        Reporter.log("【调试信息】 [beforeExitScreenshot]");

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (StringUtils.isEmpty(pageName)) {
            pageName = "UndefinedPage";
        }
        String fileName = "error-before-exit-screenshot_" + screenshotCountQuit + "_" + pageName + ".png";

        File destFile = new File(fileFolder, fileName);
        try {
            if (null != driver) {
                FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE).getCanonicalFile(), destFile);

                log.info("[调试信息] [beforeExitScreenshot] 截图保存路径：{}{}", fileFolder, fileName);
                Reporter.log("【调试信息】 [beforeExitScreenshot] 截图保存路径：" + fileFolder + fileName);

            } else {
                log.info("[调试信息] [beforeExitScreenshot] driver == null.");
            }
        } catch (Exception e) {
            log.info("[调试信息] [beforeExitScreenshot] 打印操作元素失败异常信息：{}", e.toString());
//            throw new WebDriverException();
        }

        screenshotCountQuit++;

        log.info("[调试信息] [beforeExitScreenshot] “必要元素” 不存在，程序停止运行。（手动抛出 RuntimeException 异常）");
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
     * 从文件读配置，为全局变量赋值。
     *
     * @return
     */
    public static CommonConfigEntity getCommonConfigEntity() {
        log.info("[调试信息] [getCommonConfigEntity]");

        JsonFileUtils jsonFileUtils = new JsonFileUtils();

        String jsonString = jsonFileUtils.getStringWithJsonFile("/common_config_data.json");

        CommonConfigEntity commonConfigEntity = JSONObject.parseObject(jsonString, CommonConfigEntity.class);

        findElementLoopCount = commonConfigEntity.getFindElementLoopCount();
        operateElementLoopCount = commonConfigEntity.getOperateElementLoopCount();
        sleepTime = commonConfigEntity.getSleepTime();

        log.info("[调试信息] [getCommonConfigEntity] 全局变量 findElementLoopCount = {}", findElementLoopCount);
        log.info("[调试信息] [getCommonConfigEntity] 全局变量 operateElementLoopCount = {}", operateElementLoopCount);
        log.info("[调试信息] [getCommonConfigEntity] 全局变量 sleepTime = {}", sleepTime);
        Reporter.log("【调试信息】 全局变量 findElementLoopCount = [" + findElementLoopCount + "]");
        Reporter.log("【调试信息】 全局变量 operateElementLoopCount = [" + operateElementLoopCount + "]");
        Reporter.log("【调试信息】 全局变量 sleepTime = [" + sleepTime + "]");

        log.info("[调试信息] [getCommonConfigEntity] 方法执行完毕，返回 commonConfigEntity 对象。");
        return commonConfigEntity;
    }

}

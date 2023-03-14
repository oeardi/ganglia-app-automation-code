package com.utils;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 读取 resources 目录下的 .properties 文件
 *
 * @author 冷枫红舞
 */
@Slf4j
public class ResourceBundleUtils {

    /**
     * 读取 resources 目录下的 .properties 文件
     *
     * @param propertiesFile 如果 propertiesFile 传入 “default”，则默认读取 resources/devices/devices.properties 文件。
     */
    public ResourceBundle getPropertiesFileValue(String propertiesFile) {
        if (null == propertiesFile || propertiesFile.length() == 0) {
            log.info("[调试信息] [getPropertiesFileValue] 输入的 propertiesFile 参数是一个空值。");
            return null;
        } else {
            log.info("[调试信息] [getPropertiesFileValue] 打印输入参数：{}", propertiesFile);
        }

        /**
         * 如果 propertiesFile 传入 “default”，则默认读取 resources/devices/devices.properties 文件。
         */
        String def = "default";
        if (propertiesFile.equals(def)) {
            propertiesFile = "devices/devices";
        }

        /**
         * 创建 ResourceBundle 对象，使用 getBundle() 静态方法获取配置文件内容，存放在 ResourceBundle 对象中。
         * 注：不需要写 .properties 后缀名
         */
        ResourceBundle resourceBundle = ResourceBundle.getBundle(propertiesFile, Locale.CHINA);
        log.info("[调试信息] [getPropertiesFileValue] 输出配置文件及所在路径：{}", resourceBundle.getBaseBundleName());

        return resourceBundle;
    }

    /**
     * 获取 key 对应的 value
     *
     * @param file
     * @param key
     * @return
     */
    public String getValue(String file, String key) {
        ResourceBundle resourceBundle = getPropertiesFileValue(file);
        String str = resourceBundle.getString(key);
        return str;
    }

    @Test
    public void test1() {
        String file = "devices/devices";
        getPropertiesFileValue(file);

        String defaul = "default";
        getPropertiesFileValue(defaul);
    }

    @Test
    public void test2() {
        String file = "devices/devices";
        String key = "device";

        String value = getValue(file, key);
        System.out.println("[Test Case] value = " + value);
    }
}

package com.cases;

import com.utils.YamlFileUtils;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

import static com.utils.AndroidDriverUtils.driver;
import static com.utils.AndroidDriverUtils.getAndroidDriverUtils;
import static com.utils.AutomationEngineUtils.getAutomationEngineUtils;

/**
 * 测试用例
 *
 * @author 冷枫红舞
 */
public class AutomatedTest {

    @BeforeClass
    public void beforeClass() {
        System.out.println("【TEST CASE】 [beforeClass]。");
        getAndroidDriverUtils().initDriver();
    }

    @Parameters("yamlFile")
    @Test
    public void testAutomated(String yamlFile) {
        Reporter.log("【TEST CASE】 [testAutomated] 测试用例开始执行：");

        Map<String, Object> yamlToMap = (Map<String, Object>) new YamlFileUtils().getMapWithYamlFile(yamlFile, Map.class);
        getAutomationEngineUtils().uiAtuo(yamlToMap);

        Reporter.log("【TEST CASE】 [testAutomated] 测试用例执行完毕。");
    }

    @AfterClass
    public void afterClass() throws InterruptedException {
        Thread.sleep(1000);
        driver.quit();
        System.out.println("【TEST CASE】 [afterClass]。");
    }

}

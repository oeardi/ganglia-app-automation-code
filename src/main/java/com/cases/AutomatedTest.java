package com.cases;

import com.utils.YamlFileUtils;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.util.Map;

import static com.utils.AndroidDriverUtils.driver;
import static com.utils.AndroidDriverUtils.initDriver;
import static com.utils.AutomationEngineUtils.uiAtuo;

/**
 * 测试用例
 *
 * @author 冷枫红舞
 */
public class AutomatedTest {

    @BeforeClass
    public void beforeClass() {
        System.out.println("【TEST CASE】 [beforeClass]。");
        initDriver();
    }

    @BeforeTest
    public void beforeTest() {
    }

    @Parameters("yamlFile")
    @Test
    public void testAutomated(String yamlFile) throws InterruptedException {
        Reporter.log("【TEST CASE】 [testAutomated] 测试用例开始执行：");

        Map<String, Object> yamlToMap = (Map<String, Object>) new YamlFileUtils().getMapWithYamlFile(yamlFile, Map.class);
        uiAtuo(yamlToMap);

        Reporter.log("【TEST CASE】 [testAutomated] 测试用例执行完毕。");
    }

    @AfterTest
    public void afterTest() {
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
        System.out.println("【TEST CASE】 [afterClass]。");
    }

}

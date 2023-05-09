package com.cases;

import com.utils.YamlFileUtils;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

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

    @Parameters("yamlFile")
    @Test
    public void testAutomated(String yamlFile) {
        Reporter.log("【TEST CASE】 [testAutomated] 测试用例开始执行：");

        Map<String, Object> yamlToMap = (Map<String, Object>) new YamlFileUtils().getMapWithYamlFile(yamlFile, Map.class);
        uiAtuo(yamlToMap);

        Reporter.log("【TEST CASE】 [testAutomated] 测试用例执行完毕。");
    }

    @AfterClass
    public void afterClass() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        driver.quit();
        System.out.println("【TEST CASE】 [afterClass]。");
    }

}

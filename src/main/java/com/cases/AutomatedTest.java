package com.cases;

import com.utils.AndroidDriverUtils;
import com.utils.AutomationEngineUtils;
import com.utils.YamlFileUtils;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

import static com.utils.AndroidDriverUtils.closeApp;

/**
 * 测试用例
 *
 * @author 冷枫红舞
 */
public class AutomatedTest {

    @BeforeClass
    public void beforeClass() {
        System.out.println("【TEST CASE】 [beforeClass]。");
        AndroidDriverUtils.initDriver();
    }

    @Parameters("yamlFile")
    @Test
    public void testAutomated(String yamlFile) {
        Reporter.log("【TEST CASE】 [testAutomated] 测试用例开始执行：");

        Map<String, Object> yamlToMap = (Map<String, Object>) new YamlFileUtils().getMapWithYamlFile(yamlFile, Map.class);
        AutomationEngineUtils.uiAtuo(yamlToMap);

        Reporter.log("【TEST CASE】 [testAutomated] 测试用例执行完毕。");
    }

    @AfterClass
    public void afterClass() throws InterruptedException {
        Thread.sleep(1000);
        closeApp();
        System.out.println("【TEST CASE】 [afterClass]。");
    }

}

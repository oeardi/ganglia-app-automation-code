package com.cases;

import com.utils.YamlFileUtils;
import org.testng.Reporter;
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
    }

    @Parameters("yamlFile")
    @Test
    public void testAutomated(String yamlFile) {
        Reporter.log("【TEST CASE】 [testAutomated] 测试用例开始执行：");

        initDriver();

        Map<String, Object> yamlToMap = (Map<String, Object>) new YamlFileUtils().getMapWithYamlFile(yamlFile, Map.class);
        uiAtuo(yamlToMap);

        Reporter.log("【TEST CASE】 [testAutomated] 测试用例执行完毕。");
    }

}

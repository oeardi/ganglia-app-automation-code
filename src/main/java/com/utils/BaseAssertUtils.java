package com.utils;

import com.common.BaseAssertData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static com.common.CacheParamData.CacheParam.PARAM_STRING;

/**
 * 断言相关操作
 *
 * @author 冷枫红舞
 */
@Slf4j
public class BaseAssertUtils {

    private BaseAssertUtils() {
    }

    /**
     * 断言部分
     */
    public static void doAssert(ArrayList assertList) {
        log.info("[调试信息] [doAssert] 开始执行 public static void doAssert(ArrayList assertList) 方法：");
        if (null == assertList) {
            log.info("[调试信息] [doAssert] 传入参数 [assertList == null]，doAssert() 方法终止执行。[return null;]");
            return;
        }

        Map<String, Object> resultTypeMap = null;
        Map<String, Object> conditionMap = null;
        Map<String, Object> expectMap = null;

        String resultTypeString = null;
        String conditionString = null;
        String expectString = null;

        Iterator iteratorAssert = assertList.iterator();

        while (iteratorAssert.hasNext()) {
            Map<String, Object> assertMap = (Map<String, Object>) iteratorAssert.next();

            for (Map.Entry<String, Object> entry2 : assertMap.entrySet()) {
//                log.info("[调试信息] [doAssert] key: [{}], value: [{}]", entry2.getKey(), entry2.getValue());

                if (entry2.getKey().equals(BaseAssertData.ASSERT_RESULT_TYPE)) {
                    int index = assertList.indexOf(assertMap);
                    resultTypeMap = (Map<String, Object>) assertList.get(index);
                    resultTypeString = (String) resultTypeMap.get(BaseAssertData.ASSERT_RESULT_TYPE);
                }

                if (entry2.getKey().equals(BaseAssertData.ASSERT_CONDITION)) {
                    int index = assertList.indexOf(assertMap);
                    conditionMap = (Map<String, Object>) assertList.get(index);
                    conditionString = (String) conditionMap.get(BaseAssertData.ASSERT_CONDITION);
                }

                if (entry2.getKey().equals(BaseAssertData.ASSERT_EXPECT)) {
                    int index = assertList.indexOf(assertMap);
                    expectMap = (Map<String, Object>) assertList.get(index);
                    expectString = (String) expectMap.get(BaseAssertData.ASSERT_EXPECT);
                }
            }
        }

        log.info("[调试信息] [doAssert] 输出 [yaml] 文件中 [assert] 元素的内容：");
        log.info("[调试信息] [doAssert] [resultType] 元素的值为：\"{}\"", resultTypeString);
        log.info("[调试信息] [doAssert] [condition] 元素的值为：\"{}\"", conditionString);
        log.info("[调试信息] [doAssert] [expect] 元素的值为：\"{}\"", expectString);
        Reporter.log("【调试信息】 [doAssert] 输出 [yaml] 文件中 [assert] 元素的内容：");
        Reporter.log("【调试信息】 [doAssert] [resultType] 元素的值为：\"" + resultTypeString + "\"");
        Reporter.log("【调试信息】 [doAssert] [condition] 元素的值为：\"" + conditionString + "\"");
        Reporter.log("【调试信息】 [doAssert] [expect] 元素的值为：\"" + expectString + "\"");

        if (StringUtils.isEmpty(expectString)) {
            log.info("[调试信息] [doAssert] 获取到的 [expectString == null]，doAssert() 方法终止执行。[return;]");
            return;
        }

        /**
         * 如果 yaml 文件中 [assert] 部分 [resultType] 的值为 null 或 string 类型，则取 BaseAssertData.resultCacehForAssert 进行比较。
         * 注：resultCacehForAssert 的值由操作元素的 getElementText 时存入。
         */
        if (null == resultTypeString || resultTypeString.equals(PARAM_STRING)) {
            String result = BaseAssertData.resultCacehForAssert;
            String expect = String.valueOf(expectString);
            log.info("[调试信息] [doAssert] 获取元素的 text 字段值 [result = {}]", result);
            Reporter.log("【调试信息】 [doAssert] 获取元素的 text 字段值 [result = " + result + "]");
            log.info("[调试信息] [doAssert] 获取 [yaml] 文件 assert 的 [expect = {}]", expect);
            Reporter.log("【调试信息】 [doAssert] 获取 [yaml] 文件 assert 的 [expect = " + expect + "]");

            /**
             * 判定条件
             */
            if (null == conditionString || conditionString.equals(BaseAssertData.EQUALS_TO)) {
                log.info("[调试信息] [doAssert] 触发判定条件：[=]");
                Reporter.log("【调试信息】 [doAssert] 触发判定条件：[=]");
                Assert.assertEquals(result, expect);
                log.info("[调试信息] [doAssert] 断言执行完毕 [result] 与 [expect] 相等。");
                Reporter.log("【调试信息】 [doAssert] 断言执行完毕 [result] 与 [expect] 相等。");
            }
        }

    }

}

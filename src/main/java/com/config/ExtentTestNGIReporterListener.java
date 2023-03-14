package com.config;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.TestAttribute;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.*;

/**
 * @author 冷枫红舞
 */
public class ExtentTestNGIReporterListener implements IReporter {

    private ExtentReports extent;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        init();
        boolean createSuiteNode = false;
        if (suites.size() > 1) {
            createSuiteNode = true;
        }
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            // 如果 suite 里面没有任何用例，直接跳过，不在报告里生成
            if (result.size() == 0) {
                continue;
            }
            // 统计 suite 下的成功、失败、跳过的总用例数
            int suiteFailSize = 0;
            int suitePassSize = 0;
            int suiteSkipSize = 0;
            ExtentTest suiteTest = null;
            // 存在多个 suite 的情况下，在报告中将同一个一个 suite 的测试结果归为一类，创建一级节点。
            if (createSuiteNode) {
                suiteTest = extent.createTest(suite.getName()).assignCategory(suite.getName());
            }
            boolean createSuiteResultNode = false;
            if (result.size() > 1) {
                createSuiteResultNode = true;
            }
            for (ISuiteResult r : result.values()) {
                ExtentTest resultNode;
                ITestContext context = r.getTestContext();
                if (createSuiteResultNode) {
                    // 没有创建 suite 的情况下，将在 SuiteResult 的创建为一级节点，否则创建为 suite 的一个子节点。
                    if (null == suiteTest) {
                        resultNode = extent.createTest(r.getTestContext().getName());
                    } else {
                        resultNode = suiteTest.createNode(r.getTestContext().getName());
                    }
                } else {
                    resultNode = suiteTest;
                }
                if (resultNode != null) {
                    resultNode.getModel().setName(suite.getName() + " : " + r.getTestContext().getName());
                    if (resultNode.getModel().hasCategory()) {
                        resultNode.assignCategory(r.getTestContext().getName());
                    } else {
                        resultNode.assignCategory(suite.getName(), r.getTestContext().getName());
                    }
                    resultNode.getModel().setStartTime(r.getTestContext().getStartDate());
                    resultNode.getModel().setEndTime(r.getTestContext().getEndDate());
                    // 统计 SuiteResult 下的数据
                    int passSize = r.getTestContext().getPassedTests().size();
                    int failSize = r.getTestContext().getFailedTests().size();
                    int skipSize = r.getTestContext().getSkippedTests().size();
                    suitePassSize += passSize;
                    suiteFailSize += failSize;
                    suiteSkipSize += skipSize;
                    if (failSize > 0) {
                        resultNode.getModel().setStatus(Status.FAIL);
                    }
                    resultNode.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", passSize, failSize, skipSize));
                }
                buildTestNodes(resultNode, context.getFailedTests(), Status.FAIL);
                buildTestNodes(resultNode, context.getSkippedTests(), Status.SKIP);
                buildTestNodes(resultNode, context.getPassedTests(), Status.PASS);
            }
            if (suiteTest != null) {
                suiteTest.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", suitePassSize, suiteFailSize, suiteSkipSize));
                if (suiteFailSize > 0) {
                    suiteTest.getModel().setStatus(Status.FAIL);
                }
            }
        }
//        for (String s : Reporter.getOutput()) {
//            com.extent.setTestRunnerOutput(s);
//        }
        extent.flush();
    }

    private void init() {

        // 生成的路径以及文件名
        String folder = "test-report-output/";
        String fileName = "TestReport-" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + ".html";

        // 文件夹不存在的话进行创建
        File reportDir = new File(folder);
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdir();
        }
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(folder + fileName);
        // 设置静态文件的 DNS【解决 cdn.rawgit.com 访问不了的情况】
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);

        htmlReporter.config().setEncoding("utf-8");
//        htmlReporter.config().setEncoding("gbk");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);  // 报表位置
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);  // 报告颜色
        htmlReporter.config().setDocumentTitle("QA TEST REPORT");
        htmlReporter.config().setReportName("QA TEST REPORT");
        htmlReporter.config().setCSS(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
//        extent.attachReporter(htmlReporter, createExtentXReporter());
        extent.setReportUsesManualConfiguration(true);
    }

    private void buildTestNodes(ExtentTest extenttest, IResultMap tests, Status status) {
        // 存在父节点时，获取父节点的标签
        String[] categories = new String[0];
        if (extenttest != null) {
            List<TestAttribute> categoryList = extenttest.getModel().getCategoryContext().getAll();
            categories = new String[categoryList.size()];
            for (int index = 0; index < categoryList.size(); index++) {
                categories[index] = categoryList.get(index).getName();
            }
        }

        ExtentTest extentTest;

        if (tests.size() > 0) {
            // 调整用例排序，按时间排序
            Set<ITestResult> treeSet = new TreeSet<ITestResult>(new Comparator<ITestResult>() {
                @Override
                public int compare(ITestResult o1, ITestResult o2) {
                    return o1.getStartMillis() < o2.getStartMillis() ? -1 : 1;
                }
            });
            treeSet.addAll(tests.getAllResults());
            for (ITestResult result : treeSet) {
                Object[] parameters = result.getParameters();
                String name = "";
                // 如果有参数，则使用参数的 toString 组合代替报告中的 name
                for (Object param : parameters) {
                    name += param.toString();
                }
                if (name.length() > 0) {
                    if (name.length() > 50) {
                        name = name.substring(0, 49) + "...";
                    }
                } else {
                    name = result.getMethod().getMethodName();
                }
                if (extenttest == null) {
                    extentTest = extent.createTest(name);
                } else {
                    // 作为子节点进行创建时，设置同父节点的标签一致，便于报告检索。
                    extentTest = extenttest.createNode(name).assignCategory(categories);
                }
                // test.getModel().setDescription(description.toString());
                // test = com.extent.createTest(result.getMethod().getMethodName());
                for (String group : result.getMethod().getGroups()) {
                    extentTest.assignCategory(group);
                }

                List<String> outputList = Reporter.getOutput(result);
                for (String output : outputList) {
                    // 将用例的 log 输出报告中
                    extentTest.debug(output);
                }
                if (result.getThrowable() != null) {
                    extentTest.log(status, result.getThrowable());
                } else {
                    extentTest.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }

                extentTest.getModel().setStartTime(getTime(result.getStartMillis()));
                extentTest.getModel().setEndTime(getTime(result.getEndMillis()));
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

//    public static ExtentXReporter createExtentXReporter() {
//        ExtentXReporter extentXReporter = new ExtentXReporter("172.28.38.81", 17017);
//        extentXReporter.report().setProjectName("test1");
//        extentXReporter.report().setReportName("Build-1224");
//        extentXReporter.report().setServerUrl("http://localhost:1337/");
//        return extentXReporter;
//    }
}

# ganglia-app-automation-code

# 【干大坡儿】App 自动化测试框架

【干大坡儿】是由 Java 编写的，基于 Android 端的 App 自动化测试框架。以模块化、步骤化的方式，在测试用例中记录每一步功能操作，从而实现指定业务流程的自动化运行。

该项目已投产，业务上已经实现某端的 Bill 全部业务功能，运行稳定。

## 【适用范围】

- 研发工程师代码自测与调试；
- 测试工程师主要功能及业务流程的回归性测试；
- 测试环境与生产环境指定功能的定期回归。

---

## 【设计思路】

##### 在 yaml 文件中以 “模块” 形式组成完整的 “测试用例”，通过解析 yaml 文件各项 “模块” 内容实现其对应功能。然后使用 testng（xml 方式）运行 test_cases。

包含 “模块” 如下：

- page —— 主要用于标识，在日志中输出 “操作” 所在的 App 功能页面，便于跟踪和调试。由使用者自定义添加 Page 类，无实际功能逻辑，可以不填写。
- whiteListFile —— 白名单，用于批量处理一组元素的。（如：App 启动后的多个弹框。）
- location —— 定位信息，该 “模块” 用于描述如何定位一个元素，以及标识该 “元素” 是否是必须元素。（必须元素如果不被找到，则程序强制退出。）
- action —— 操作信息，该 “模块” 用于描述测试用例运行过程中，每一步所需要执行的操作。
- whhw —— 这个 “模块” 主要以 while 循环的方式执行同质化操作，简化测试用例文件。
- iffi —— 在 yaml 文件中进行描述，判断如果 “元素存在”，则执行相应处理。
- assert —— 断言处理。

#### （具体使用方式参见【resources/test-case-yaml/yaml 测试用例编写说明.yaml】说明文档）

# 【注】在运行 xml 文件之前：

- 先在 src/main/resources/capabilities 目录下添加好 capabilities.json 文件。
- 然后在 src/main/resources/devices/devices.properties 文件中进行设置。

---

## 【主要功能】

- 点击元素
- 输入文本信息
- 获取页面文本信息
- 获取元素数量
- 指定坐标滑屏
- 指定方向滑屏
- 屏幕截图（保存在 test-report-output/report-yyyyMMddHHmmss/ 目录）
- 异常退出截图（保存在 test-report-output/report-yyyyMMddHHmmss/ 目录）
- 设置等待时间
- 清空控件文本
- 获取 Toast 信息
- 跳转指定 Activity
- 调用系统按键
- 初始化业务数据
- 断言功能
- 生成测试报告（保存在 test-report-output/report-yyyyMMddHHmmss/ 目录）
- 同质化操作处理
- 白名单统一处理元素

#### （具体使用方式参见【resources/test-case-yaml/yaml 测试用例编写说明.yaml】说明文档）

#### （后续支持功能敬请期待… ^_^）

---

## 【注】 涉及到的一些前置基础知识点。（建议有兴趣的同学可以做些许了解）

- 了解 Appium 基本原理
- 了解 Appium 的 capabilities 参数
- 了解 Appium 获取元素方式（本框架主要使用 byId 和 byXpath 两种方式）
- 了解 Yaml 文件的编写格式
- 了解 TestNg 的 xml 文件

---

## 【写在最后】

为什么叫 “干大坡儿”，“大坡儿” 是谁？
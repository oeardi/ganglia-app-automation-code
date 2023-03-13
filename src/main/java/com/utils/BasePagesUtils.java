package com.utils;

import com.pages.base.IPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;

import static com.pages.factory.PageFactory.factoryGetPage;

/**
 * yaml 文件中，pages 部分的操作。
 *
 * @author 冷枫红舞
 */
@Slf4j
public class BasePagesUtils {

    /**
     * 解析并执行 yaml 中的 page 页面信息。
     * <p>
     * 注：在 uiAuto 实际自动化解析代码中，并没有真的使用到 BasePageOperate 类。
     * <p>
     * 如果需要使用 PO 模式，则需要在 BasePageOperate.java 类中实现 click(), sendKeys() 等方法，
     * 然后在 doLocation(ArrayList locationList) 方法增加一个 BasePageOperate 参数。
     * 并在方法内部，调用 BasePageOperate.click() 方法来实现点击操作。
     * 如：doLocation(BasePageOperate basepage, ArrayList locationList)
     *
     * @param pageName
     * @return
     */
    public static IPage getPage(String pageName) {
        log.info("[调试信息] [getPage] 开始执行 getPage(String pageName) 方法：");

        if (pageName == null || pageName.length() == 0) {
            log.info("[调试信息] [getPage] 传入 pageName 参数为空。[return null;]");
            return null;

        }

        IPage page = factoryGetPage(pageName);
        log.info("[调试信息] [getPage] 输出页面信息：[pageName = {}]，返回 [{}] 实例。", pageName, page.getClass().getName());
        Reporter.log("【调试信息】 [getPage] 输出页面信息：[pageName = " + pageName + "]，返回 [" + page.getClass().getName() + "] 实例。");

        return page;
    }

}

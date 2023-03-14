package com.pages.factory;

import com.pages.base.IPage;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Page 工厂类
 *
 * @author 冷枫红舞
 */
@Slf4j
public class PageFactory {

    /**
     * 获取 Page 对象
     *
     * @param name
     * @return
     */
    public static IPage factoryGetPage(String name) {
//        log.info("[调试信息] [{}]", Thread.currentThread().getStackTrace()[1].getMethodName());
        if (name == null || name.length() == 0) {
            log.info("[调试信息] [factoryGetPage] 传入 name 参数为空。[return null;]");
            return null;
        }

        String classStr = "com.pages." + name;

        Class<?> cla = null;
        try {
            cla = Class.forName(classStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Object obj = null;
        try {
            obj = cla.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (IPage) obj;
    }

    @Test
    public void testGetPage() {
        String name = "PosMainPage";
        Object obj = factoryGetPage(name);
        System.out.println("[Test Case] 返回 [" + obj.getClass().getName() + "] 实例.");
    }
}

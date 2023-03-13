package com.utils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * 使用 Mustache 第三方工具类，实现以下功能：
 * 1）读取 .json 文件的内容（使用 String 接收）
 * 2）修改 jsonString 中的字段内容
 *
 * @author 冷枫红舞
 */
@Slf4j
public class JsonFileUtils {

    /**
     * 读取。读取 .json 文件，并转化成 String 类型。
     *
     * @param jsonFile
     * @return
     */
    public String getStringWithJsonFile(String jsonFile) {
        log.info("[调试信息] [getStringWithJsonFile]");

        if (StringUtils.isEmpty(jsonFile)) {
            log.info("[调试信息] [getStringWithJsonFile] 输入的 jsonFile 参数是一个空值。");
            return null;
        } else {
            log.info("[调试信息] [getStringWithJsonFile] 打印输入参数：{}", jsonFile);
            Reporter.log("【调试信息】 输出 JSON 文件名及路径：/resources" + jsonFile.toString());
        }

        /**
         * 创建 Mustache 对象，getMustache() 方法内部封装了 MustacheFactory 提供的 compile() 方法
         */
        Mustache mustache = getMustache(jsonFile);

        /**
         * 创建 Writer 对象，调用 mustache.identity(writer) 方法，
         * 将 mustache 获取到的 json 字符串，写入到 writer 对象中。
         */
        Writer writer = new StringWriter();
        mustache.identity(writer);

        log.info("[调试信息] [getStringWithJsonFile] 获取到的 json 数据：{}", writer);
        Reporter.log("【调试信息】 打印从文件中获取到的 JSON 数据：" + writer.toString());

        return writer.toString();
    }

    /**
     * 修改。读取 .json 文件，并使用 params 值对 json 内容进行替换。
     *
     * @param jsonFile
     * @param params
     * @return
     */
    public String editStringWithJsonFile(String jsonFile, Map<String, Object> params) {
        if (jsonFile == null || jsonFile.length() == 0 || params == null || params.isEmpty()) {
            log.info("[调试信息] [{}] 输入的 jsonFile 参数是一个空值.", Thread.currentThread().getStackTrace()[1].getMethodName());
            return null;
        } else {
            log.info("[调试信息] [editStringWithJsonFile] 打印输入参数：{}", jsonFile);
        }

        /**
         * 创建 Mustache 对象，getMustache() 方法内部封装了 MustacheFactory 提供的 compile() 方法
         */
        Mustache mustache = getMustache(jsonFile);

        /**
         * 创建 Writer 对象，调用 mustache.identity(writer) 方法，
         * 将 mustache 获取到的 json 字符串，写入到 writer 对象中。
         */
        Writer writer = new StringWriter();
        mustache.execute(writer, params);

        log.info("[调试信息] [editStringWithJsonFile] 获取到的 json 数据：{}", writer);

        return writer.toString();
    }

    /**
     * 需要传入 resoureces 目录后的目录和文件名，获取到 Mustache 对象，用于后续操作。
     *
     * @param jsonFile
     * @return
     */
    private Mustache getMustache(String jsonFile) {
        log.info("[调试信息] [getMustache]");

        if (StringUtils.isEmpty(jsonFile)) {
            log.info("[调试信息] [{}] 输入的 jsonFile 参数是一个空值.", Thread.currentThread().getStackTrace()[1].getMethodName());
            return null;
        } else {
            log.info("[调试信息] [getMustache] 打印输入参数：{}", jsonFile);
        }

        /**
         * 创建 MustacheFactory 对象
         */
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();

        /**
         * 创建 Mustache 对象，调用 MustacheFactory 提供的 compile() 方法，
         * 注：compile() 方法需要传入一个 json 文件的 path，所以使用 getClass().getResource(file).getPath(); 来获取到这个 path。
         */
        String filePath = null;
        try {
            filePath = getClass().getResource(jsonFile).getPath();
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.info("[调试信息] [getMustache] 方法发生空指针异常，请确认方法入参 {} 是否正确。", jsonFile);
        }
        Mustache mustache = mustacheFactory.compile(filePath);

        return mustache;
    }

    /**
     * 实例 1：
     * 只读取 json 文件中的数据，不对 json 数据进行修改，使用 mustache.identity(writer) 方法。
     *
     * @throws IOException
     */
    @Test
    public void testCase() throws IOException {
        /**
         * 1. 指定文件
         */
        String jsonFile = "/capabilities/ot/huawei_9i_xq_cap.json";
        System.out.println("[Test Case] 输出文件所在路径：" + getClass().getResource(jsonFile));
        System.out.println("[Test Case] 输出 mustache.json 的文件内容：");
        System.out.println(FileUtils.readFileToString(new File(String.valueOf(getClass().getResource(jsonFile).getPath())), "utf-8"));

        /**
         * 2. 创建 MustacheFactory 对象
         */
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();

        /**
         * 3. 创建 Mustache 对象，调用 MustacheFactory 提供的 compile() 方法，
         * 注：compile() 方法需要传入一个 json 文件的 path，所以使用 getClass().getResource(file).getPath(); 来获取到这个 path。
         */
        String filePath = getClass().getResource(jsonFile).getPath();
        Mustache mustache = mustacheFactory.compile(filePath);

        /**
         * 4. 创建 Writer 对象，调用 mustache.identity(writer) 方法，
         * 将 mustache 获取到的 json 字符串，写入到 writer 对象中。
         */
        Writer writer = new StringWriter();
        mustache.identity(writer);

        System.out.println("[Test Case] 获取到的 json 数据：");
        System.out.println(writer);
    }

    /**
     * 读取。读取 .json 文件，并转化成 String 类型。
     */
    @Test
    public void test1() {
        String jsonFile = "/capabilities/ot/huawei_9i_xq_cap.json";
        getStringWithJsonFile(jsonFile);
    }

}

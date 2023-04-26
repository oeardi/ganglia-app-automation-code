package com.utils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.io.Writer;

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
     * @param jsonFilePath
     * @return
     */
    public String getStringWithJsonFile(String jsonFilePath) {
        log.info("[调试信息] [getStringWithJsonFile]");

        if (StringUtils.isEmpty(jsonFilePath)) {
            log.info("[调试信息] [getStringWithJsonFile] 输入的 jsonFile 参数是一个空值。");
            return null;
        } else {
            log.info("[调试信息] [getStringWithJsonFile] 打印输入参数：{}", jsonFilePath);
        }

        /**
         * 创建 Mustache 对象，getMustache() 方法内部封装了 MustacheFactory 提供的 compile() 方法
         */
        Mustache mustache = getMustache(jsonFilePath);

        /**
         * 创建 Writer 对象，调用 mustache.identity(writer) 方法，
         * 将 mustache 获取到的 json 字符串，写入到 writer 对象中。
         */
        Writer writer = new StringWriter();
        mustache.identity(writer);

        log.info("[调试信息] [getStringWithJsonFile] 获取到的 json 数据：{}", writer);

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

}

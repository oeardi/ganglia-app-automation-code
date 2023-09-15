package com.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Objects;

/**
 * JsonFileUtils
 *
 * @Author: Casta
 */
@Slf4j
public class JsonFileUtils {

    /**
     * 读取 /path/file_name.json 文件，返回 Object 对象。
     *
     * @param jsonFilePath fileName and path, for example: /test_data_json/login_user_info.json
     * @param clazz        Object
     * @return Object
     */
    public Object getObjectWithJsonFile(String jsonFilePath, Class clazz) {
        log.info("[调试信息] [getObjectWithJsonFile] 开始执行：");

        if (StringUtils.isEmpty(jsonFilePath)) {
            log.info("[调试信息] [getObjectWithJsonFile] 请求参数 [jsonFilePath == null]，方法终止执行。抛出 IllegalArgumentException 异常。");
            throw new IllegalArgumentException();
        } else {
            log.info("[调试信息] [getObjectWithJsonFile] 请求参数 jsonFilePath 为：{}", jsonFilePath);
        }

        return getJsonObjectWithJsonFile(jsonFilePath).toJavaObject(clazz);
    }

    /**
     * 读取 /path/file_name.json 文件，返回 JSONObject 对象。
     *
     * @param jsonFilePath fileName and path, for example: /test_data_json/login_user_info.json
     * @return JSONObject
     */
    public JSONObject getJsonObjectWithJsonFile(String jsonFilePath) {
//        log.info("[调试信息] [getJsonObjectWithJsonFile] 开始执行：");

        if (StringUtils.isEmpty(jsonFilePath)) {
            log.info("[调试信息] [getJsonObjectWithJsonFile] 请求参数 [jsonFilePath == null]，方法终止执行。抛出 IllegalArgumentException 异常。");
            throw new IllegalArgumentException();
        }
//        log.info("[调试信息] [getJsonObjectWithJsonFile] 请求参数 jsonFilePath 为：{}", jsonFilePath);

        return JSONObject.parseObject(getStringWithJsonFile(jsonFilePath));
    }

    /**
     * 读取 /path/file_name.json 文件，返回 String 对象。
     *
     * @param jsonFilePath fileName and path, for example: /test_data_json/login_user_info.json
     * @return String
     */
    public String getStringWithJsonFile(String jsonFilePath) {
//        log.info("[调试信息] [getStringWithJsonFile] 开始执行：");

        if (StringUtils.isEmpty(jsonFilePath)) {
            log.info("[调试信息] [getStringWithJsonFile] 请求参数 [jsonFilePath == null]，方法终止执行。抛出 IllegalArgumentException 异常。");
            throw new IllegalArgumentException();
        }
//        log.info("[调试信息] [getStringWithJsonFile] 请求参数 jsonFilePath 为：{}", jsonFilePath);

        /**
         * 创建 Mustache 对象，getMustache() 方法内部封装了 MustacheFactory 提供的 compile() 方法
         */
        Mustache mustache = getMustache(jsonFilePath);

        /**
         * 创建 Writer 对象，调用 mustache.identity(writer) 方法，
         * 将 mustache 获取到的 json 字符串，写入到 writer 对象中。
         */
        Writer writer = new StringWriter();
        assert mustache != null;
        mustache.identity(writer);

        System.out.println("[getStringWithJsonFile] 获取到的 json 数据：");
        System.out.println(writer);

        return writer.toString();
    }

    /**
     * 修改 json 字符串内容。
     * 读取 /path/file_name.json 文件，并使用 params 值对 json 内容进行替换。
     *
     * @param jsonFilePath fileName and path, for example: /test_data_json/login_user_info.json
     * @param params       map
     * @return String
     */
    public String editStringWithJsonFile(String jsonFilePath, Map<String, Object> params) {
        log.info("[调试信息] [editStringWithJsonFile] 开始执行：");

        if (StringUtils.isEmpty(jsonFilePath) || null == params || params.isEmpty()) {
            log.info("[调试信息] [editStringWithJsonFile] 请求参数异常，方法终止执行。抛出 IllegalArgumentException 异常。");
            return null;
        } else {
            log.info("[调试信息] [editStringWithJsonFile] 请求参数 jsonFilePath 为：{}", jsonFilePath);
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
        assert mustache != null;
        mustache.execute(writer, params);

        log.info("[调试信息] [editStringWithJsonFile] 获取到的 json 数据：");
        log.info("[调试信息] [editStringWithJsonFile] {}", writer);

        return writer.toString();
    }

    /**
     * 需要传入 resoureces 目录后的目录和文件名，获取到 Mustache 对象，用于后续操作。
     *
     * @param jsonFilePath fileName and path, for example: /test_data_json/login_user_info.json
     * @return Mustache
     */
    private Mustache getMustache(String jsonFilePath) {
//        log.info("[调试信息] [getMustache] 开始执行：");

        if (StringUtils.isEmpty(jsonFilePath)) {
            log.info("[调试信息] [getMustache] 请求参数 [jsonFilePath == null]，方法终止执行。抛出 IllegalArgumentException 异常。");
            return null;
        }
//        log.info("[调试信息] [getMustache] 请求参数 jsonFilePath 为：{}", jsonFilePath);

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
//            filePath = getClass().getResource(jsonFilePath).getPath();
            filePath = Objects.requireNonNull(getClass().getResource(jsonFilePath)).getPath();
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.info("[调试信息] [getMustache] 发生空指针异常，请确认方法入参 [{}] 是否正确。", jsonFilePath);
        }

        return mustacheFactory.compile(filePath);
    }

    /**
     * 只读取 json 文件中的数据，不对 json 数据进行修改，使用 mustache.identity(writer) 方法。
     */
    @Test
    public void testCase() throws IOException {

        /**
         * 1. 指定文件
         */
        String jsonFile = "/element_white_list/N_B_White_List.json";
        System.out.println("[Test Case] 输出文件所在路径：" + getClass().getResource(jsonFile));
        System.out.println("[Test Case] 输出 mustache.json 的文件内容：");
//        System.out.println(FileUtils.readFileToString(new File(String.valueOf(getClass().getResource(jsonFile).getPath())), "utf-8"));
        System.out.println(FileUtils.readFileToString(new File(String.valueOf(Objects.requireNonNull(getClass().getResource(jsonFile)).getPath())), "utf-8"));

        /**
         * 2. 创建 MustacheFactory 对象
         */
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();

        /**
         * 3. 创建 Mustache 对象，调用 MustacheFactory 提供的 compile() 方法，
         * 注：compile() 方法需要传入一个 json 文件的 path，所以使用 getClass().getResource(file).getPath(); 来获取到这个 path。
         */
//        String filePath = getClass().getResource(jsonFile).getPath();
        String filePath = Objects.requireNonNull(getClass().getResource(jsonFile)).getPath();
        Mustache mustache = mustacheFactory.compile(filePath);

        /**
         * 4. 创建 Writer 对象，调用 mustache.identity(writer) 方法，
         * 将 mustache 获取到的 json 字符串，写入到 writer 对象中。
         */
        Writer writer = new StringWriter();
        mustache.identity(writer);

        System.out.println("[Test Case] 获取到的 json 数据：");
        System.out.println("[Test Case]" + writer);
    }

}

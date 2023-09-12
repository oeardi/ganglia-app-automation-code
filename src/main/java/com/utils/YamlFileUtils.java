package com.utils;

import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 读取 yaml 文件
 *
 * @author 冷枫红舞
 */
@Slf4j
public class YamlFileUtils {

    /**
     * 读取 Yaml 文件（默认 yaml 文件放在 resources 目录）
     *
     * @param yamlFile
     * @param clazz    传入 “元类”（后续可修改为直接传入 yaml 对应的 pojo）
     * @param <T>
     * @return
     */
    public <T> Object getMapWithYamlFile(String yamlFile, Class clazz) {
        log.info("[调试信息] [getMapWithYamlFile]");
        if (yamlFile == null || yamlFile.isEmpty()) {
            log.info("[调试信息] [getMapWithYamlFile] 传入 yamlFile 参数为空。[return null;]");
            return null;
        } else {
            log.info("[调试信息] [getMapWithYamlFile] 输出文件名及 classpath 路径：/resource{}", yamlFile);
            Reporter.log("【调试信息】 输出 yaml 文件名及路径：/resource" + yamlFile);
        }

        String filePath = getClass().getResource(yamlFile).getPath();
//        log.info("[调试信息] [getMapWithYamlFile] 输出文件名及其绝对路径：{}", filePath);

        /**
         * 文件流，创建 FileInputStream 对象，加载 yaml 文件。
         */
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**
         * 创建 Yaml 对象
         */
        Yaml yaml = new Yaml();
        /**
         * public <T> T loadAs(InputStream input, Class<T> type) {
         *     return this.loadFromReader(new StreamReader(new UnicodeReader(input)), type);
         */
        return yaml.loadAs(fileInputStream, clazz);
    }

}

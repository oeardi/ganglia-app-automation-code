package com.common;

/**
 * 白名单
 *
 * @author 冷枫红舞
 */
public class WhiteListData {
    /**
     * yaml 文件的 whiteListFile 元素
     */
    public static final String CONTAINT_whiteListFile = "whiteListFile";

    /**
     * 白名单。（暂时还未考虑好多版本时，多白名单如何更便捷的处理。可能考虑放将旧版本白名单存放在 bak 文件夹里，用时粘贴出来。）
     */
    public static final String NGA_POS_WHITE_LIST_FILE = "/element_white_list/NGA_POS_White_List.json";
    public static final String NGA_POS_WHITE_LIST_FILE_version_367 = "/element_white_list/NGA_POS_White_List_version_367.json";
    public static final String NGA_POS_WHITE_LIST_FILE_version_xxxx = "/element_white_list/NGA_POS_White_List_version_xxxx.json";
    public static final String NGA_BAPP_WHITE_LIST_FILE = "/element_white_list/NGA_BusinessApp_White_List.json";
    public static final String PAK_POS_WHITE_LIST_FILE = "/element_white_list/PAK_POS_White_List.json";
    public static final String PAK_BAPP_WHITE_LIST_FILE = "/element_white_list/PAK_BusinessApp_White_List.json";

    public static final String NGA_POS = "NGAPOS";
    public static final String NGA_BAPP = "NGABAPP";
    public static final String PAK_POS = "PAKPOS";
    public static final String PAK_BAPP = "PAKBAPP";
}

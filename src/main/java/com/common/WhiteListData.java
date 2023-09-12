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
    public static final String CONTAINT_WHITE_LIST_FILE = "whiteListFile";

    /**
     * 白名单。（暂时还未考虑好多版本时，多白名单如何更便捷的处理。可能考虑放将旧版本白名单存放在 bak 文件夹里，用时粘贴出来。）
     */
    public static final String N_P_WHITE_LIST_FILE = "/element_white_list/N_P_White_List.json";
    public static final String N_P_WHITE_LIST_FILE_V_367 = "/element_white_list/N_P_White_List_v_367.json";
    public static final String N_P_WHITE_LIST_FILE_V_XXX = "/element_white_list/N_P_White_List_v_xxx.json";
    public static final String N_B_WHITE_LIST_FILE = "/element_white_list/N_B_White_List.json";
    public static final String P_P_WHITE_LIST_FILE = "/element_white_list/P_P_White_List.json";
    public static final String P_B_WHITE_LIST_FILE = "/element_white_list/P_B_White_List.json";
    public static final String DEFAULT_WHITE_LIST_FILE = "/element_white_list/Default_White_List.json";

    public static final String N_P = "np";
    public static final String N_P_V_367 = "NB_v_367";
    public static final String N_P_V_XXX = "NB_v_xxx";
    public static final String N_B = "nb";
    public static final String P_P = "pp";
    public static final String P_B = "pb";

    public static final String DEFAULT = "default";
}

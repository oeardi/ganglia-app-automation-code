package com.common;

/**
 * doAction() 方法中用到的变量与常量
 *
 * @author 冷枫红舞
 */
public class BaseActionData {

    public static final String ACTION_OPERATE = "operate";
    public static final String ACTION_PARAM = "param";

    /**
     * 框架支持的操作方式，用于 doLocation() 方法。
     */
    public interface ActionOperate {
        String CLICK = "CLICK";
        String SEND_KEYS = "SENDKEYS";
        String GET_ELEMENT_TEXT = "GETTEXT";
        String PRINT_ELEMENT_TEXT = "PRINTTEXT";
        String GET_ELEMENT_LIST = "GETLIST";
        String PRINT_ELEMNET_LIST = "PRINTLIST";
        String GET_ELEMENT_COUNT = "GETLISTSIZE";
        String PRINT_ELEMENT_COUNT = "PRINTLISTSIZE";
        String MOVE_COORDINATE = "MOVEPOINT";
        String MOVE_FIXED_LENGTH = "MOVEFIXED";
        String SCREENSHOT = "SCREENSHOT";
        String WAITING = "WAITING";
        String CLEAR = "CLEAR";
        String GET_TOAST = "GETTOAST";
        String GO_ACTIVITY = "GOACTIVITY";
        String BACK = "BACK";
        String DIGIT = "DIGIT";
        String RECORD_START = "RECORDSTART";
        String RECORD_STOP = "RECORDSTOP";
        String DATA_INIT = "DATAINIT";
    }
}

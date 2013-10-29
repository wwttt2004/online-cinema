package com.nmbb.oplayer.ui.exeptions;

import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 11.01.13
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class NoUrlNullPointerException extends Exception{
    public NoUrlNullPointerException(String message){
        LoggerUtil.log("NoUrlNullPointerException: " + message);
    }
}

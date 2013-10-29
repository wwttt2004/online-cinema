package com.nmbb.oplayer.ui.exeptions;

import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 11.01.13
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class ParserParamsNoFoundException extends Exception{
    public ParserParamsNoFoundException(String message){
        LoggerUtil.log("ParserParamsNoFoundException: " + message);
    }
}

package org.example;

public abstract class StringUtils {

    public static String unquote(String str){
        return str.replace("\"", "");
    }
}

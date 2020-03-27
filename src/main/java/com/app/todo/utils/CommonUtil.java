package com.app.todo.utils;

import java.util.List;

public class CommonUtil {

    public static boolean isNull(Object object) {
        boolean isList = object instanceof List;
        boolean isEmpty;
        if(isList) {
            isEmpty = isNull((List<?>) object);
        } else {
            isEmpty = (object == null);
        }
        return isEmpty;
    }

    public static <T> boolean isNull(List<T> list) {
        boolean isEmpty=false;

        if(list == null) {
            isEmpty = true;
        } else {
            if(list.size()==0) isEmpty=true;
        }

        return isEmpty;
    }
}

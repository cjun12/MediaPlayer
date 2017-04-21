package com.coship.app.mediaplayer.toolkit;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 980558 on 2017/4/14.
 */

public class StringConvertor {
    public static List<Object> beanListToBeanFieldList(Collection<? extends Object> collection, String field) {
        List<Object> result = new ArrayList<>();
        field = toFirstCase(field);
        Iterator iter = collection.iterator();
        while (iter.hasNext()) {
            try {
                Object obj = iter.next();
                Class clazz = obj.getClass();
                Object value = clazz.getMethod("get" + field).invoke(obj);
                result.add(value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String toFirstCase(String str) {
        return str.toUpperCase().charAt(0) + str.substring(1);
    }

    public static String toFormatTime(int millisecond) {
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(date);
    }
}

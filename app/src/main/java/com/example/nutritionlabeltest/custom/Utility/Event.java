package com.example.nutritionlabeltest.custom.Utility;

import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Event {
    public EventCallback callback;
    private Map<Context, Set<Method>> objectToListenersMap = new HashMap<>();

    public void addListener(Context context, String listener) {
        try {
            Method method = context.getClass().getDeclaredMethod(listener);

            if (objectToListenersMap.containsKey(context)) {
                // add listener to list
                objectToListenersMap.get(context).add(method);
            } else {
                // put in new entry
                objectToListenersMap.put(context, new HashSet<>(Arrays.asList(method)));
            }

        } catch (NoSuchMethodException e) {
        }
    }

    public void removeListener(Context context, String listener) {
        try {
            Method method = context.getClass().getDeclaredMethod(listener);

            if (objectToListenersMap.containsKey(context)) {
                // remove listener from list
                objectToListenersMap.get(context).remove(method);
            }
        } catch (NoSuchMethodException e) {
        }

    }

    public void invoke()
    {
        for (Context context : objectToListenersMap.keySet()) {

            for (Method method : objectToListenersMap.get(context)) {
                try {
                    method.invoke(context);
                } catch (IllegalAccessException | InvocationTargetException e) {
                }
            }

        }

    }
}



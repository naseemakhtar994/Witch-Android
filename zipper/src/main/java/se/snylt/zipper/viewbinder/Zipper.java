package se.snylt.zipper.viewbinder;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import se.snylt.zipper.BindAction;
import se.snylt.zipper.Binding;
import se.snylt.zipper.BindingSpec;
import se.snylt.zipper.ClassUtils;
import se.snylt.zipper.viewbinder.onbind.OnBind;
import se.snylt.zipper.viewbinder.onbind.WildCardOnBind;
import se.snylt.zipper.viewbinder.utils.BindingNotFoundException;

public class Zipper {

    public static void bind(Object target, Activity viewFinder) {
        long start = System.currentTimeMillis();
        for (BindingSpec bindingSpec : getBinding(target).getBindingSpecs()) {
            View view = viewFinder.findViewById(bindingSpec.viewId);
            bind(bindingSpec, view, target);
        }
        Log.d("bind", target.getClass().getSimpleName() + " took: " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void bind(Object target, View viewFinder) {
        for (BindingSpec bindingSpec : getBinding(target).getBindingSpecs()) {
            View view = viewFinder.findViewById(bindingSpec.viewId);
            bind(bindingSpec, view, target);
        }
    }

    private static void bind(BindingSpec bindingSpec, View view, Object target) {
        Object value = getValue(target, bindingSpec);
        for (BindAction bindAction : getBindActions(bindingSpec, view, value)) {
            doBind(bindAction, view, value);
        }
    }

    private static List<BindAction> getBindActions(BindingSpec bindingSpec, View view, Object value) {
        if(bindingSpec.getBindActions().isEmpty()) {
            BindAction bindAction = WildCardOnBind.findOnBind(view, value);
            if(bindAction != null) {
                return Arrays.asList(bindAction);
            }

            throw new RuntimeException("Could not find binder for "+
                    "View: " + view.getClass().getSimpleName() +
                    "Value: " + value.getClass().getSimpleName());
        }
        return bindingSpec.getBindActions();
    }

    private static void doBind(BindAction bindAction, View view, Object value) {
        if (bindAction instanceof OnPreBind) {
            ((OnPreBind) bindAction).onPreBind(view, value);
        }
        if (bindAction instanceof OnBind) {
            ((OnBind) bindAction).onBind(view, value);
        }
        if (bindAction instanceof OnPostBind) {
            ((OnPostBind) bindAction).onPostBind(view, value);
        }
    }

    private static Binding getBinding(Object target) {
        try {
            Class clazz = ClassUtils.findBinding(target);
            return (Binding) clazz.newInstance();
        } catch (Exception e) {
            throw new BindingNotFoundException("Could not find binding for " + target.getClass().getName());
        }
    }

    private static Object getValue(Object target, BindingSpec bindingSpec) {
        try {
            Field f = target.getClass().getDeclaredField(bindingSpec.key);
            return f.get(target);
        } catch (Exception e) {
            throw new BindingNotFoundException("Could not read value from " + target.getClass().getName());
        }
    }

}
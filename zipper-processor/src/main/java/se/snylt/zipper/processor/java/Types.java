package se.snylt.zipper.processor.java;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

public class Types {

    private final static String LIBRARY_PACKAGE = "se.snylt.zipper.viewbinder";

    public static final ClassName LIST = ClassName.get(List.class);

    public static final TypeName ARRAY_LIST = TypeName.get(ArrayList.class);

    public static final TypeName VIEW_BINDER = ClassName.get(LIBRARY_PACKAGE, "ViewBinder");

    public final static TypeName BINDING_CREATOR = ClassName.get(LIBRARY_PACKAGE, "BindingCreator");

    public final static TypeName BINDING = ClassName.get(LIBRARY_PACKAGE, "Binding");

    public final static TypeName ON_UNBIND_LISTENER = ClassName.get(LIBRARY_PACKAGE, "OnUnbindListener");

    public static final TypeName ANDROID_VIEW = ClassName.get("android.view", "View");

}

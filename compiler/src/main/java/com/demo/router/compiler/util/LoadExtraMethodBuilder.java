package com.demo.router.compiler.util;

import com.demo.router.annotation.Extra;
import com.demo.router.annotation.RouteConfig;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


public class LoadExtraMethodBuilder {
    private MethodSpec.Builder builder;
    private Elements elementUtil;
    private Types typeUtil;
    private TypeMirror parcelableType;
    private TypeElement iProviderType;

    public LoadExtraMethodBuilder(ParameterSpec parameterSpec, Elements elementUtil, Types typeUtil) {
        this.typeUtil = typeUtil;
        this.elementUtil = elementUtil;
        parcelableType = elementUtil.getTypeElement("android.os.Parcelable").asType();
        iProviderType = elementUtil.getTypeElement(RouteConfig.IPROVIDER_CLASS_NAME);
        builder = MethodSpec.methodBuilder("loadExtra").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).addParameter(parameterSpec);
    }

    public void buildStatement(Element element) {
        TypeMirror typeMirror = element.asType();
        TypeKind kind = typeMirror.getKind();
        //属性名 String text 获得text
        String fieldName = element.getSimpleName().toString();
        //获得注解 name值
        String extraName = element.getAnnotation(Extra.class).value();
        extraName = Utils.isEmpty(extraName) ? fieldName : extraName;
        String statement;
        switch (typeMirror.toString()) {
            case "BOOLEAN":
            case "BYTE":
            case "SHORT":
            case "INT":
            case "LONG":
            case "CHAR":
            case "FLOAT":
            case "DOUBLE":
                String middle = kind.name();
                statement = String.format("t.%s = t.getIntent().get%s%sExtra($S, t.%s)", fieldName, middle.charAt(0), middle.toLowerCase().substring(1), fieldName);
                builder.addStatement(statement, extraName);
                break;
            case "ARRAY":
                statement = String.format("t.%s = t.getIntent().", fieldName);
                addArrayStatement(statement, fieldName, extraName, typeMirror, element);
                break;
            default:
                statement = String.format("t.%s = t.getIntent().", fieldName);
                addObjectStatement(statement, fieldName, extraName, typeMirror, element);
        }
    }

    /**
     * 添加数组
     */
    private void addArrayStatement(String statement, String fieldName, String extraName, TypeMirror typeMirror, Element element) {
        //数组
        switch (typeMirror.toString()) {
            case "boolean[]":
                statement += "getBooleanArrayExtra($S)";
                break;
            case "int[]":
                statement += "getIntArrayExtra($S)";
                break;
            case "short[]":
                statement += "getShortArrayExtra($S)";
                break;
            case "float[]":
                statement += "getFloatArrayExtra($S)";
                break;
            case "double[]":
                statement += "getDoubleArrayExtra($S)";
                break;
            case "byte[]":
                statement += "getByteArrayExtra($S)";
                break;
            case "char[]":
                statement += "getCharArrayExtra($S)";
                break;
            case "long[]":
                statement += "getLongArrayExtra($S)";
                break;
            case "java.lang.String[]":
                statement += "getStringArrayExtra($S)";
                break;
            default:
                //Parcelable 数组
                String defaultValue = "t." + fieldName;
                //object数组 componentType获得object类型
                ArrayTypeName arrayTypeName = (ArrayTypeName) ClassName.get(typeMirror);
                TypeElement typeElement = elementUtil.getTypeElement(arrayTypeName.componentType.toString());
                //是否为 Parcelable 类型
                if (!typeUtil.isSubtype(typeElement.asType(), parcelableType)) {
                    throw new RuntimeException("Not Support Extra Type:" + typeMirror + " " + element);
                }
                statement = "$T[] " + fieldName + " = t.getIntent()" + ".getParcelableArrayExtra" + "($S)";
                builder.addStatement(statement, parcelableType, extraName);
                builder.beginControlFlow("if( null != $L)", fieldName);
                statement = defaultValue + " = new $T[" + fieldName + ".length]";
                builder.addStatement(statement, arrayTypeName.componentType).beginControlFlow("for (int i = 0; i < " + fieldName + "" + ".length; " + "i++)").addStatement(defaultValue + "[i] = ($T)" + fieldName + "[i]", arrayTypeName.componentType).endControlFlow();
                builder.endControlFlow();
                return;
        }
        builder.addStatement(statement, extraName);
    }

    /**
     * 添加对象 String/List/Parcelable
     */
    private void addObjectStatement(String statement, String fieldName, String extraName, TypeMirror typeMirror, Element element) {
        //Parcelable
        if (typeUtil.isSubtype(typeMirror, parcelableType)) {
            statement += "getParcelableExtra($S)";
        } else if (typeMirror.toString().equals("java.lang.String")) {
            statement += "getStringExtra($S)";
        } else if (typeUtil.isSubtype(typeMirror, iProviderType.asType())) {
//            TestService testService = (TestService) DNRouter.getInstance().build("/main/service1")
//                    .navigation();
//            testService.test();
            statement = "t." + fieldName + " = ($T) $T.getInstance().build($S).navigation()";
            builder.addStatement(statement, TypeName.get(element.asType()), ClassName.get("com.demo.router.api", "Router"), extraName);
            return;
        } else {
            //List
            TypeName typeName = ClassName.get(typeMirror);
            //泛型
            if (typeName instanceof ParameterizedTypeName) {
                //list 或 arraylist
                ClassName rawType = ((ParameterizedTypeName) typeName).rawType;
                //泛型类型
                List<TypeName> typeArguments = ((ParameterizedTypeName) typeName).typeArguments;
                if (!rawType.toString().equals("java.util.ArrayList") && !rawType.toString().equals("java.util.List")) {
                    throw new RuntimeException("Not Support Inject Type:" + typeMirror + " " + element);
                }
                if (typeArguments.size() != 1) {
                    throw new RuntimeException("List Must Specify Generic Type:" + typeArguments);
                }
                TypeName typeArgumentName = typeArguments.get(0);
                TypeElement typeElement = elementUtil.getTypeElement(typeArgumentName.toString());
                // Parcelable 类型
                if (typeUtil.isSubtype(typeElement.asType(), parcelableType)) {
                    statement += "getParcelableArrayListExtra($S)";
                } else if (typeElement.asType().toString().equals("java.lang.String")) {
                    statement += "getStringArrayListExtra($S)";
                } else if (typeElement.asType().toString().equals("java.lang.Integer")) {
                    statement += "getIntegerArrayListExtra($S)";
                } else {
                    throw new RuntimeException("Not Support Generic Type : " + typeMirror + " " + element);
                }
            } else {
                throw new RuntimeException("Not Support Extra Type : " + typeMirror + " " + element);
            }
        }
        builder.addStatement(statement, extraName);
    }

    /**
     * 加入 $T t = ($T)target
     */
    public void injectTarget(ClassName className) {
        builder.addStatement("$T t = ($T)target", className, className);
    }

    public MethodSpec build() {
        return builder.build();
    }
}

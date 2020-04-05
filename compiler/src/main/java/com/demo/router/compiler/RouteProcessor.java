package com.demo.router.compiler;

import com.demo.router.annotation.Route;
import com.demo.router.annotation.RouteMeta;
import com.demo.router.compiler.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * 在这个类上添加了@AutoService注解，它的作用是用来生成META-INF/services/javax.annotation.processing.Processor文件的
 * auto-service是Google开发的一个库，使用时需要在factory-compiler中添加依赖
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)// 指定使用的Java版本,对应AbstractProcessor.getSupportedSourceVersion()
// 指定需要处理的注解,对应AbstractProcessor.getSupportedAnnotationTypes()
// @SupportedAnnotationTypes("com.demo.router.annotation.Route")
public class RouteProcessor extends AbstractProcessor {
    /**
     * 文件生成器
     */
    private Filer filer;
    /**
     * type(类信息)工具类
     */
    private Types typeUtil;

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtil;

    /**
     * 分组 key:组名 value:对应的路由信息
     */
    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();
    /**
     * log
     */
    private Messager messager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Route.class.getCanonicalName());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        typeUtil = processingEnvironment.getTypeUtils();
        elementUtil = processingEnvironment.getElementUtils();
    }

    /**
     * 相当于main函数，正式处理注解
     *
     * @param set              注解集合
     * @param roundEnvironment 表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true 表示后续处理器不会再处理(已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!Utils.isEmpty(set)) {
            Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(Route.class);
            if (!Utils.isEmpty(elementSet)) {
                processRouteAnnotation(elementSet);
            }
            return true;
        }
        return false;
    }

    /**
     * @param elementSet:使用了Route注解的类的集合
     */
    private void processRouteAnnotation(Set<? extends Element> elementSet) {
        TypeElement activityTypeElement = elementUtil.getTypeElement("android.app.Activity");

        for (Element element : elementSet) {
            if (typeUtil.isSubtype(element.asType(), activityTypeElement.asType())) {
                Route routeAnnotation = element.getAnnotation(Route.class);
                RouteMeta routeMeta = new RouteMeta(
                        RouteMeta.Type.ACTIVITY,
                        element,
                        null,
                        routeAnnotation.path(),
                        routeAnnotation.group()
                );
                addGroupMap(routeMeta);
            } else {
                throw new RuntimeException("[Just Support Activity/IService Route]:" + element);
            }
        }

        //生成类需要实现的接口
        generatedGroupClass();
    }

    private void addGroupMap(RouteMeta routeMeta) {
        if (!Utils.isEmpty(routeMeta.path) && routeMeta.path.startsWith("/")) {
            List<RouteMeta> routeMetaList = groupMap.get(routeMeta.group);
            if (Utils.isEmpty(routeMetaList)) {
                routeMetaList = new ArrayList<>();
                routeMetaList.add(routeMeta);
                groupMap.put(routeMeta.group, routeMetaList);
            } else {
                routeMetaList.add(routeMeta);
            }
        } else {
            log("错误的path格式-->" + routeMeta.path);
        }
    }

    /**
     * 生成Group类
     */
    private void generatedGroupClass() {
        // Map<MapString, RouteMeta>
        ParameterizedTypeName routeMapParameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class)
        );

        // Map<MapString, RouteMeta> routeMap
        ParameterSpec routeMapParameter = ParameterSpec.builder(routeMapParameterizedTypeName, "routeMap").build();

        for (Map.Entry<String, List<RouteMeta>> groupEntry : groupMap.entrySet()) {
            // public void addRoute(Map<MapString, RouteMeta> atlas) {}
            MethodSpec.Builder addRouteMethod = MethodSpec.methodBuilder("addRoute")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(routeMapParameter);

            for (RouteMeta routeMeta : groupEntry.getValue()) {
                // atlas.put("/main/test",new RouteMeta(RouteMeta.Type.ACTIVITY,SecondActivity.class, "/main/test", "main"));
                addRouteMethod.addStatement("routeMap.put($S, new $T($T.$L, $T.class, $S, $S))",
                        routeMeta.path,
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteMeta.Type.class),
                        routeMeta.type,
                        ClassName.get((TypeElement) routeMeta.element),
                        routeMeta.path.toLowerCase(),
                        routeMeta.group.toLowerCase());
            }

            // Router$$ModuleName$$default_group
            String groupClassName = String.format("Router$$%s$$%s", "ModuleName", groupEntry.getKey());
            TypeElement iRouteCroup = elementUtil.getTypeElement("com.demo.router.core.template.IRouteGroup");
            TypeSpec groupClassType = TypeSpec.classBuilder(groupClassName)
                    .addSuperinterface(ClassName.get(iRouteCroup))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(addRouteMethod.build())
                    .build();
            try {
                JavaFile.builder("com.demo.router", groupClassType)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
                log("error..." + e.getMessage());
            }
        }
    }

    private void log(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
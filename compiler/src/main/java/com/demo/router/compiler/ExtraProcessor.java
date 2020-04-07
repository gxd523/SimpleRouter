package com.demo.router.compiler;

import com.demo.router.annotation.Extra;
import com.demo.router.annotation.RouteConfig;
import com.demo.router.compiler.util.LoadExtraMethodBuilder;
import com.demo.router.compiler.util.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
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
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.PUBLIC;


@AutoService(Processor.class)
@SupportedOptions("moduleName")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ExtraProcessor extends AbstractProcessor {
    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtil;
    /**
     * type(类信息)工具类
     */
    private Types typeUtil;
    /**
     * 类/资源生成器
     */
    private Filer filer;
    /**
     * log
     */
    private Messager messager;
    /**
     * 记录所有需要注入的属性 key:类节点 value:需要注入的属性节点集合
     */
    private Map<TypeElement, List<Element>> extraMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Extra.class.getCanonicalName());
    }

    /**
     * 初始化 从 {@link ProcessingEnvironment} 中获得一系列处理器工具
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtil = processingEnv.getElementUtils();
        typeUtil = processingEnvironment.getTypeUtils();
        filer = processingEnv.getFiler();
    }

    /**
     * @param roundEnvironment 表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true 表示后续处理器不会再处理(已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!Utils.isEmpty(set)) {
            Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(Extra.class);
            if (!Utils.isEmpty(elementSet)) {
                for (Element memberElement : elementSet) {
                    TypeElement classElement = (TypeElement) memberElement.getEnclosingElement();
                    if (extraMap.containsKey(classElement)) {
                        extraMap.get(classElement).add(memberElement);
                    } else {
                        List<Element> memberElementList = new ArrayList<>();
                        memberElementList.add(memberElement);
                        extraMap.put(classElement, memberElementList);
                    }
                }
                generateAutoWired();
            }
            return true;
        }

        return false;
    }

    private void generateAutoWired() {
        TypeElement activityTypeElement = elementUtil.getTypeElement("android.app.Activity");
        TypeElement IExtraElement = elementUtil.getTypeElement(RouteConfig.IEXTRA_CLASS_NAME);
        ParameterSpec loadExtraParameter = ParameterSpec.builder(TypeName.OBJECT, "target").build();
        if (!Utils.isEmpty(extraMap)) {
            for (Map.Entry<TypeElement, List<Element>> entry : extraMap.entrySet()) {
                TypeElement rawClassElement = entry.getKey();
                if (!typeUtil.isSubtype(rawClassElement.asType(), activityTypeElement.asType())) {
                    throw new RuntimeException("[Just Support Activity Field]:" + rawClassElement);
                }

                LoadExtraMethodBuilder loadExtraMethodBuilder = new LoadExtraMethodBuilder(loadExtraParameter, elementUtil, typeUtil);
                ClassName className = ClassName.get(rawClassElement);
                loadExtraMethodBuilder.injectTarget(className);
                for (Element element : entry.getValue()) {
                    loadExtraMethodBuilder.buildStatement(element);
                }

                String extraClassName = String.format("Extra$$%s", rawClassElement.getSimpleName());
                TypeSpec extraClassType = TypeSpec.classBuilder(extraClassName)
                        .addSuperinterface(ClassName.get(IExtraElement))
                        .addModifiers(PUBLIC)
                        .addMethod(loadExtraMethodBuilder.build())
                        .build();
                try {
                    JavaFile.builder(className.packageName(), extraClassType)
                            .build()
                            .writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void log(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}

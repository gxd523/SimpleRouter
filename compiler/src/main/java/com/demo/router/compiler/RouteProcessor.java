package com.demo.router.compiler;

import com.demo.router.annotation.Route;
import com.demo.router.compiler.utils.Utils;
import com.google.auto.service.AutoService;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * 在这个类上添加了@AutoService注解，它的作用是用来生成META-INF/services/javax.annotation.processing.Processor文件的
 * auto-service是Google开发的一个库，使用时需要在factory-compiler中添加依赖
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)// 指定使用的Java版本,对应AbstractProcessor.getSupportedSourceVersion()
// 指定需要处理的注解,对应AbstractProcessor.getSupportedAnnotationTypes()
// @SupportedAnnotationTypes("com.demo.router.annotation.Route")
public class RouteProcessor extends AbstractProcessor {
//    private Log log;
//    /**
//     * 文件生成器
//     */
//    private Filer filerUtils;
//
//    /**
//     * type(类信息)工具类
//     */
//    private Types typeUtile;
//
//    /**
//     * 节点工具类 (类、函数、属性都是节点)
//     */
//    private Elements elementsUtils;
//
//    /**
//     * 分组 key:组名 value:对应的路由信息
//     */
//    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Route.class.getCanonicalName());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
//        log = Log.newLog(processingEnvironment.getMessager());
//        filerUtils = processingEnvironment.getFiler();
//        typeUtile = processingEnvironment.getTypeUtils();
//        elementsUtils = processingEnvironment.getElementUtils();
    }

    /**
     * 相当于main函数，正式处理注解
     *
     * @param set              使用了指定注解的节点的集合
     * @param roundEnvironment 表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true 表示后续处理器不会再处理(已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("gxd..." + set.size());
        if (!Utils.isEmpty(set)) {
            //获取所有被Route注解的元素集合
//            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Route.class);
//            //处理Route注解
//            if (!Utils.isEmpty(elementsAnnotatedWith)) {
//                log.i("Route Class: ===" + elementsAnnotatedWith.size());
//                parseRoutes(elementsAnnotatedWith);
//            }
            return true;
        }
        return false;
    }

//    private void parseRoutes(Set<? extends Element> anotatedWith) {
//        //elementsUtils 通过节点工具，传入全类名，生成节点 （activity）
//        TypeElement typeElement = elementsUtils.getTypeElement(Consts.ACTIVITY);
//        //节点的描述信息
//        TypeMirror type_activity = typeElement.asType();
//
//        for (Element element : anotatedWith) {
//            RouteMeta routeMeta = null;
//            //使用Route注解的类信息（acvitiy）
//            TypeMirror tm = element.asType();
//
//            Route route = element.getAnnotation(Route.class);
//
//            //判断注解使用什么类上面
//            if (typeUtile.isSubtype(tm, type_activity)) {
//                routeMeta = new RouteMeta(RouteMeta.Type.ACTIVITY, route, element);
//            } else {
//                throw new RuntimeException("[Just Support Activity/IService Route]:" + element);
//            }
//
//            /**
//             * 分组
//             */
//            categories(routeMeta);
//        }
//
//        //生成类需要实现的接口
//        TypeElement iRouteCroup = elementsUtils.getTypeElement(Consts.IROUTE_GROUP);
//
//        /**
//         *生成Group类
//         */
//        generatedGroup(iRouteCroup);
//
//    }
//
//    private void generatedGroup(TypeElement iRouteCroup) {
//        //创建参数类型  Map<MapString, RouteMeta>
//        ParameterizedTypeName atlas = ParameterizedTypeName.get(
//                ClassName.get(Map.class),
//                ClassName.get(String.class),
//                ClassName.get(RouteMeta.class));
//
//        //创建参数名字  Map<MapString, RouteMeta> atlas
//        ParameterSpec spec = ParameterSpec.builder(atlas, "atlas").build();
//
//        //遍历分组， 每一个分组创建一个   ...$$$$Group$$ 类
//        for (Map.Entry<String, List<RouteMeta>> stringListEntry : groupMap.entrySet()) {
//
//            //方法   public void loadInto(Map<MapString, RouteMeta> atlas) {}
//            MethodSpec.Builder builder = MethodSpec.methodBuilder(Consts.METHOD_LOAD_INTO)
//                    .addAnnotation(Override.class)
//                    .addModifiers(Modifier.PUBLIC)
//                    .returns(TypeName.VOID)
//                    .addParameter(spec);
//
//            String groupName = stringListEntry.getKey();
//            List<RouteMeta> groupData = stringListEntry.getValue();
//
//            for (RouteMeta routeMeta : groupData) {
//                /**
//                 * atlas.put("/main/test", RouteMeta.build(
//                 * RouteMeta.Type.ACTIVITY,SecondActivity.class, "/main/test", "main"));
//                 */
//                builder.addStatement("atlas.put($S,$T.build($T.$L,$T.class,$S, $S)",
//                        routeMeta.getPath(),
//                        ClassName.get(RouteMeta.class),
//                        ClassName.get(RouteMeta.Type.class),
//                        routeMeta.getType(),
//                        ClassName.get((TypeElement) routeMeta.getElement()),
//                        routeMeta.getPath().toLowerCase(),
//                        routeMeta.getGroup().toLowerCase());
//            }
//
//            //DNRouter$$Group$$main
//            String groupClassName = Consts.NAME_OF_GROUP + groupName;
//            try {
//                JavaFile.builder(Consts.PACKAGE_OF_GENERATE_FILE,
//                        TypeSpec.classBuilder(groupClassName)
//                                .addSuperinterface(ClassName.get(iRouteCroup))
//                                .addModifiers(Modifier.PUBLIC)
//                                .addMethod(builder.build()).build()).build().writeTo(filerUtils);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    private void categories(RouteMeta routeMeta) {
//        if (routeVerify(routeMeta)) {
//            List<RouteMeta> routeMetas = groupMap.get(routeMeta.getGroup());
//            //如果为记录分组则创建
//            if (Utils.isEmpty(routeMetas)) {
//                List<RouteMeta> routeMetaList = new ArrayList<>();
//                routeMetaList.add(routeMeta);
//                groupMap.put(routeMeta.getGroup(), routeMetaList);
//            } else {
//                routeMetas.add(routeMeta);
//            }
//        } else {
//            log.i("Group Info Error : " + routeMeta.getPath());
//        }
//
//    }
//
//    private boolean routeVerify(RouteMeta routeMeta) {
//        String path = routeMeta.getPath();
//        String group = routeMeta.getGroup();
//        if (Utils.isEmpty(path) || !path.startsWith("/")) {
//            return false;
//        }
//        if (Utils.isEmpty(group)) {
//            String substring = path.substring(1, path.indexOf("/", 1));
//            if (Utils.isEmpty(substring)) {
//                return false;
//            }
//            routeMeta.setGroup(substring);
//            return true;
//        }
//        return true;
//    }
}
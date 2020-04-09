package zy.pointer.j2easy.startup.init;

import afu.org.checkerframework.checker.oigj.qual.O;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.business.system.service.IPermissionService;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTree;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTreeNode;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限构建初始器
 *
 *  先解释一下 权限 结构是怎么样的.
 *    一般在页面中,我们会看到这样的结构 :
 *    后台管理:
 *              系统设置 , 资产管理 , ... ,
 *    对此我们抽象为:
 *    api           :   类似 root
 *      bm          :   后台管理( 后台对应的接口. )
 *      public      :   公共服务( 公共的一些请求参数的处理 )
 *      apps        :   应用服务( 前台服务,移动端 等相关的服务 )
 *
 *    对于后台接口中
 *      bm                      后台管理
 *          system      :       系统设置
 *              Account :       账户管理
 *
 *    当我们需要做一个人账户管理的时候,我们都会先新建包 : system , 统一后台管理模块的内容
 *    然后在 system 包下,再加一个 controller 包,这里面放上 AccountController 作为接口定义
 *    所以在一个 zy.pointer.j2easy.api.bm.system.controller.AccountController 这一个类定义中,
 *    我们就可以完全读出     bm                                      : 域( 用于区分 bm | public | apps 临时瞎起的一个概念)
 *                          bm.system                               : 后台管理内的 系统设置模块
 *                          bm.system.controller.AccountController  : 系统模块下的账户管理
 *    至此,通过 AccountController 就可以把整个菜单结构解析出来了.
 *
 *    所以对于一个 @RequestMapping 标记的 Controller 对应的方法中 ,
 *    HandleMethod  为 功能权限
 *    XXXController 为 菜单权限
 *    api.[xx.xx.xx].XXXController 中的 xx.xx.xx 也是菜单权限
 *
 *    1.> 查询数据库加载当前已经完成录入的所有权限,并通过 PathTree-DB 来保存 数据库中的菜单功能权限数据
 *    2.> 遍历 @RequestMapping , 同时往另一个 PathTree-MEM 中来保存当前得到菜单功能权限数据
 *    3.> 从 PathTree-MEM 的上级开始遍历,找出 PathTree-DB 中不存在的节点,按顺序添加到一个List中.
 *    4.> 遍历 List , 从 PathTree-DB 中访问 对应的父节点获取对应的ID 作为当前节点的 P_ID,插入到数据库,并将其更新到 PathTree-DB 中
 */
@Component
@Slf4j
public class PermissionBuildInitializer implements InitializingBean , ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    IPermissionService permissionService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 扫描 RequestMapping , GetMapping , PostMapping 等标识的 Controller.METHOD , 基于此构加载权限
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        permissionService.initRealmsPermission();

        // Base - Api : zy.pointer.j2easy.api
        // Realm      : bm(后台管理) , apps(应用服务) , public(公共接口)
        // Fixed PackageName : controller , Fixed Suffix : Controller : {entity}Controller (xxxController)
        String basePath = "zy.pointer.j2easy.api";
        String FIXED_PACKAGE_NAME = "controller";
        String FIXED_CONTROLLER_SUFFIX = "Controller";

        // 获取Spring容器检索到的所有 @RequestMapping : 这里会包括那些 @GetMapping , @PostMapping ... 等等
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        final PathTree<Permission> dbTree = new PathTree<>();
        permissionService.list().forEach( permission -> dbTree.put( new PathTreeNode( permission.getPath() , permission.getName() , permission ) ) );

        PathTree<Permission> memTree = new PathTree<>();

        // 遍历所有的 RequestMapping , 过滤出 功能性的菜单,并在过滤的过程中,将数据拼入到 memTree 结构中
        map.entrySet().stream()
                // 过滤只有我们定义下的包才进行权限结构构建,以防止其他包混进来瞎搞
                .filter( entry -> entry.getValue().getBeanType().getName().startsWith( basePath ))
                .forEach(entry -> {

            HandlerMethod handlerMethod = entry.getValue();
            Class  controllerClazz = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();

            // 关于它的父节点 : zy.pointer.j2easy.api.bm.system.controller.AccountController
            String className = controllerClazz.getName();
            /*
             * zy.pointer.j2easy.api.bm.system.controller.AccountController
             * _basePath_.{realm}.{modules}.controller.{entity}Controller
             *
             * eg1:
             *  value: api.bm.system.account
             *  path: /api/bm/system/account
             *
             * 解析逻辑:先去掉头,尾 取出 {realm}.{modules}.controller
             *          如果这个截取值中以 controller 结尾 , 则 去掉 controller 变成 {realm}.{modules}
             * 然后处理第一截取的 AccountController , 取出 entity -> Account 然后转小写 account
             */
            // --> bm.system.controller.AccountController
            String realm_modules_CONTROLLER_entityController = className.substring( basePath.length() + 1 );
            String entityController = realm_modules_CONTROLLER_entityController.substring( realm_modules_CONTROLLER_entityController.lastIndexOf(".") + 1 );
            String realm_modules_CONTROLLER = realm_modules_CONTROLLER_entityController.substring( 0 , realm_modules_CONTROLLER_entityController.length() - entityController.length() - 1 );
            String entity = entityController.replace( FIXED_CONTROLLER_SUFFIX , "" );
            // 注意 Entity 类中的首字母转一下小写
            entity = entity.substring( 0 , 1 ).toLowerCase() + entity.substring(1);
            String realm_modules = realm_modules_CONTROLLER;
            if ( realm_modules_CONTROLLER.contains(FIXED_PACKAGE_NAME) ){
                realm_modules = realm_modules_CONTROLLER.substring( 0 , realm_modules_CONTROLLER.length() - FIXED_PACKAGE_NAME.length() - 1 );
            }
            String parentValue = "api." + realm_modules;
            Permission parentPermission = null;
            if ( realm_modules.contains(".") ){   // 说明存在 modules 而不是直接建立在 bm 包下的直系 Controller
                parentValue = "api." + realm_modules + "." + entity;
                String parentPath  = "/" + parentValue.replaceAll("\\." , "/");
                String parentName = entity;
                Api api = controllerClazz.getDeclaredAnnotation( Api.class ) == null ? null : (Api)  controllerClazz.getDeclaredAnnotation( Api.class );
                parentName = api.value() != null ? api.value() : parentName;
                parentPermission = permissionService.buildMenuTypePermission( parentPath , parentName );
            }else{
                String parentPath  = "/" + parentValue.replaceAll("\\." , "/");
                parentPermission = (Permission) dbTree.get( parentPath ).getPayload();
            }

            String methodName = method.getName();
            String path = parentPermission.getPath() + "/" + methodName;
            ApiOperation apiOperation = method.getDeclaredAnnotation( ApiOperation.class );
            String name = apiOperation != null ? apiOperation.value() : methodName;
            Permission permission = permissionService.buildFuncTypePermission(path , name);

            memTree.put( new PathTreeNode( parentPermission.getPath() , parentPermission.getName() , parentPermission ) );
            memTree.put( new PathTreeNode( permission.getPath() , permission.getName() , permission ) );
        } );



        PathTreeNode parent = memTree.getRoot();
        parent.foreachHandle( ( pathTreeNode )->{
            String path = pathTreeNode.getPath();
            if ( dbTree.get( path ) == null ){
                String parentPath = path.substring(0 , path.lastIndexOf("/"));

                // 首先从 DB-TREE 中尝试找父节点,如果父节点存在,则直接去 id 作为当前要添加的Permission的PID来使用
                Permission parentPermission = (Permission) dbTree.get( parentPath ).getPayload();
                if ( parentPermission == null ){
                    // 如果不存在,则从 MEM-TREE 中找.
                    parentPermission = (Permission) memTree.get( parentPath ).getPayload();
                }
                // 以上,不用担心找不到的原因是因为,我们从设计上是从上外内遍历的,所以任何一个权限的父节点要么在DB中存在,要么在上一级递归中存储到了MEM-TREE中去了.
                Permission permission = null;
                if ( pathTreeNode.getPayload() == null ){
                    /** 从 MEM-TREE 中发现 pathTreeNode 没有挂载 payload , 则它就一定是一个通过包路径模拟出来的虚拟节点 */
                    permission = permissionService.buildMenuTypePermission( pathTreeNode.getPath() , pathTreeNode.getPath() );
                }else{
                    permission = (Permission) pathTreeNode.getPayload();
                }
                permission.setPId( parentPermission.getId() );
                permissionService.save( permission );
                pathTreeNode.setPayload( permission );

            }
        });
    }

}

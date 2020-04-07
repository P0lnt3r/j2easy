package zy.pointer.j2easy.startup.init;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import zy.pointer.j2easy.business.system.entity.Permission;
import zy.pointer.j2easy.business.system.service.IPermissionService;

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
 *    root      :   根
 *      bm          :   后台管理( 后台对应的接口. )
 *      public      :   公共服务( 公共的一些请求参数的处理 )
 *      apps        :   应用服务( 前台服务,移动端 等相关的服务 )
 *
 *    对于后台接口中
 *      bm
 *          system  :   系统设置
 *              Account:    账户管理
 *
 *    当我们需要做一个人账户管理的时候,我们都会先新建包 : system , 统一后台管理模块的内容
 *    然后在 system 包下,再加一个 controller 包,这里面放上 AccountController 作为接口定义
 *    所以在一个 zy.pointer.j2easy.api.bm.system.controller.AccountController 这一个类定义中,
 *    我们就可以完全读出 bm : 域( 用于区分 bm | public | apps 临时瞎起的一个概念)
 *                       bm.system : 后台管理内的 系统设置模块
 *                       bm.system.controller.AccountController: 系统模块下的账户管理
 *    至此,通过 AccountController 就可以把整个菜单结构解析出来了.
 *
 *    下一步还有一个点就是 通过 SpringBoot 读取所有的 @RequestMapping , 将具体的接口找到,
 *    这一类的权限被定义为 功能权限
 */
// @Component
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

        // Base - Api : zy.pointer.j2easy.api
        // Realm      : bm(后台管理) , apps(应用服务) , public(公共接口)
        // Fixed PackageName : controller , Fixed Suffix : Controller : {entity}Controller (xxxController)
        String basePath = "zy.pointer.j2easy.api";

        String FIXED_PACKAGE_NAME = "controller";
        String FIXED_CONTROLLER_SUFFIX = "Controller";

        // 获取Spring容器检索到的所有 @RequestMapping
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        // 在构建菜单级别的权限的时候,每拿到一个 @RequestMapping 都有可能重复得到它对应的 包结构,所有这里用Set , 并重写了 Permission 的 equals 方法
        Set<Permission> menuPermissionSet = new HashSet<>();
        // 遍历所有的 RequestMapping , 过滤出 功能性的菜单,并在过滤的过程中,构建叶子级别菜单
        Set<Permission> funcPermissionSet = map.entrySet().stream()
                // 过滤只有我们定义下的包才进行权限结构构建,以防其他库进来瞎搞
                .filter( entry -> entry.getValue().getBeanType().getName().startsWith( basePath ))
                .map(entry -> {

            HandlerMethod handlerMethod = entry.getValue();
            Class  controllerClazz = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();
            String className = controllerClazz.getName();
            // zy.pointer.j2easy.api.bm.system.controller.AccountController
            // 取 _basePath_        .{realm}.{modules}.controller.{entity}Controller
            String realm_modules_FIX_PACKAGE_Entity_SuffixController = className.substring( basePath.length() + 1);
            String[] array = realm_modules_FIX_PACKAGE_Entity_SuffixController.split("\\.");
            String realm = array[0];
            String entityController = array [ array.length - 1 ];
            String entity = entityController.replace( FIXED_CONTROLLER_SUFFIX , "" );
            // 注意 Entity 类中的首字母转一下小写
            entity = entity.substring( 0 , 1 ).toLowerCase() + entity.substring(1);
            String packages = realm_modules_FIX_PACKAGE_Entity_SuffixController.substring(
                     realm.length() + 1
                    ,realm_modules_FIX_PACKAGE_Entity_SuffixController.length() - ( FIXED_CONTROLLER_SUFFIX + "." + entityController ).length() - 1
            );
            // 包级别的为 叶结构菜单 , 包级别 + Entity 构成 叶结构末端菜单
            /*
             * 比如一个 zy.pointer.j2easy.api.bm.system.controller.AccountController.query()
             * bm(realm) : 后台管理
             *  system:    系统设置      叶结构菜单权限        bm.system
             *    Account: 用户管理      叶结构菜单末端权限    bm.system.Account
             *      query: 用户信息查询  功能权限              bm.system.Account:query
             *
             *    asset  : 资产管理      叶结构菜单权限        bm.system.asset
             *      Deposit: 充值管理    末端权限              bm.system.asset.Deposit
             *        get  : 充值记录查询                      bm.system.asset.Deposit:get
             */
            String[] packageArr = packages.split("\\.");
            if ( packageArr.length > 1 ){
                for( int i = 0 ;i<packageArr.length ;i++ ){
                    String value = "";
                    for( int j = 0 ; j<i;j++ ){
                        value += packageArr[ j ] + ".";
                    }
                    value += packageArr[i];
                    menuPermissionSet.add( permissionService.wrapByValue( realm + "." + value , value ) );
                }
            }else{
                menuPermissionSet.add( permissionService.wrapByValue( realm + "." + packages , packages ) );
            }
            // 末端菜单
            /*
               末端菜单的名称由 Controller 中的 Swagger2.@Api 注解来获取
             */
            String value = realm + "." + packages + "." + entity;
            Api api = ( Api ) controllerClazz.getDeclaredAnnotation( Api.class );
            String name = entity.toUpperCase();
            if ( api != null ){
                name = api.value();
            }
            Permission packageEndPermission = permissionService.wrapByValue(value , name);
            menuPermissionSet.add( packageEndPermission );
            // 当前功能菜单
            /*
               功能菜单的名称由 对应方法上的 Swagger2.@ApiOperation 注解来获取
             */
            String funcPermissionValue = realm + "." + packages + "." + entity + ":" + method.getName();
            String funcPermissionName  = method.getName();
            ApiOperation apiOperation = method.getDeclaredAnnotation( ApiOperation.class );
            if ( apiOperation != null ){
                funcPermissionName = apiOperation.value();
            }
            return permissionService.wrapByValue( funcPermissionValue , funcPermissionName );

        } ).collect( Collectors.toSet() );

        // 构建 菜单权限表中 root , /bm , /public , /app 四个的DB初始化,并加载到 root 中.
        permissionService.initRealmsPermission();

        /*

         */
        List<Permission> allDbMenuPermissionList = permissionService.getAllMenuPermissions();
        List<Permission> allDbFuncPermissionList = permissionService.getAllFuncPermissions();
        List<Permission> newMenuPermissionList = menuPermissionSet
                .stream()
                .filter( perm -> ! allDbMenuPermissionList.contains(perm) )
                .sorted( Comparator.comparing( Permission::getLevel ) )
                .collect(Collectors.toList());
        List<Permission> newFuncPermissionList = funcPermissionSet
                .stream()
                .filter( perm -> ! allDbFuncPermissionList.contains(perm) )
                .sorted( Comparator.comparing( Permission::getLevel ) )
                .collect(Collectors.toList());

        allDbMenuPermissionList.forEach( permissionService::add );
        allDbFuncPermissionList.forEach( permissionService::add );
        newMenuPermissionList.forEach( permissionService::addNew );
        newFuncPermissionList.forEach( permissionService::addNew );

    }



}

package zy.pointer.j2easy.framework.generate;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import zy.pointer.j2easy.framework.business.AbsBusinessService;
import zy.pointer.j2easy.framework.business.BusinessService;
import zy.pointer.j2easy.framework.repository.BaseEntity;
import zy.pointer.j2easy.framework.repository.RepositoryMapper;

import java.util.ArrayList;
import java.util.List;


public class CodeGenerator {

    /** 代码生成执行器 */
    static AutoGenerator generator;

    /*************************************** 数据源配置 ******************************************************/
    static String DATASOUCE_URL , DATASOURCE_DRIVER_NAME , DATASOURCE_USERNAME , DATASOURCE_PASSWORD ;
    static {
        DATASOUCE_URL = "jdbc:mysql://localhost:3306/blockchainedu?serverTimezone=UTC&useSSL=false&characterEncoding=utf8";
        DATASOURCE_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
        DATASOURCE_USERNAME = "root";
        DATASOURCE_PASSWORD = "123456";
    }
    /*********************************************************************************************************/

    /*************************************** 全局配置 ******************************************************/
    static String PROJECT_PATH , OUTPUT_DIR , AUTHOR;                                // 代码的输出位置 PROJECT_PATH / main / java / xxx
    static {
        PROJECT_PATH = System.getProperty("user.dir")  + "/j2easy-business/"         // 如果是 Maven 项目,则需要指定到具体的项目目录中
                                                        ;
        OUTPUT_DIR   = "src/main/java"                  ;                            //
        AUTHOR       = "zhouyang"                       ;                            // <作者> 生成文件中包含的注释信息
    }
    /*********************************************************************************************************/

    /*************************************** 映射策略配置 ******************************************************/
    static String TABLE_PREFIX ;
    static {
        TABLE_PREFIX = "TB_";                                                       // 表级别的标准前缀, 表名向Entity类名变形的需要剔除的部分,详情可以看一下策略设置的相关方法,不是很难
    }
    /*********************************************************************************************************/

    public static void main(String[] args) {

        generator = new AutoGenerator();
        System.out.println("实例化代码生成器.");

        setDataSourceConfig();
        System.out.println("完成数据源配置   >>" + generator.getDataSource());

        setTemplateEngine();
        System.out.println("完成设置模板引擎 >>" + generator.getTemplateEngine());

        setGlobalConfig();
        System.out.println("完成设置全局配置 >>" + generator.getGlobalConfig());

        setPackageConfig();
        System.out.println("完成设置包配置   >>" + generator.getPackageInfo());

        setCustomConfig();
        System.out.println("完成自定义配置   >>" + generator.getConfig());

        setStrategyConfig();
        System.out.println("完成生成策略配置 >>" + generator.getStrategy());

        generator.execute();
    }

    private static void setCustomConfig(){
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };
        String templatePath = "/templates/mapper.xml.btl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return PROJECT_PATH + "/src/main/resources/mapper/" + generator.getPackageInfo().getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);

        // 配置模板:以下设置相当于屏蔽了 在 保存 mapper 的目录中同级生成 XML ,进而采用上面的输出位置生成 mapper.xml
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        generator.setTemplate(templateConfig);

        generator.setCfg( cfg );
    }

    private static void setStrategyConfig(){
        StrategyConfig strategy = new StrategyConfig();

        // 基于 表名 映射 Entity 类名策略方案
        /*
         * 方案上提供了两种,一种是完全一致,另一种则会适当变形
         * 我们必然是需要使用适当变形来映射过来
         *
         * TB_SYSTEM_USER , 这种风格的命名, 可以看出 "TB_" 为标准前准 , "SYSTEM_" 为 模块前缀
         * 所以 表名映射为类名 时的 前缀为  "TB_" + ${ moduleName } + "_"
         */
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setTablePrefix( TABLE_PREFIX + generator.getPackageInfo().getModuleName() + "_" );

        // 基于列名 映射 Entity.Fields 属性的策略方案
        /*
         *  USERNAME -> username , USER_NAME -> userName
         */
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);

        // 设置 Lombck 插件
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        // 统一设置物理主键
        strategy.setInclude(scanner("请输入表名").split(","));
        // Entity 的 父类定义的公共列

        strategy.setSuperEntityClass(BaseEntity.class);
        strategy.setSuperEntityColumns( "_ID" , "_CREATE_TIME" , "_UPDATE_TIME" , "_UPDATE_TRACE" , "_DB_STATE" );

        strategy.setSuperMapperClass(RepositoryMapper.class.toString().split(" ")[1]);
        strategy.setSuperServiceClass(BusinessService.class.toString().split(" ")[1]);
        strategy.setSuperServiceImplClass(AbsBusinessService.class.toString().split(" ")[1]);

        generator.setStrategy(strategy);
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        return ScanUtil.scanner(tip);
    }

    /**
     * 获取数据源配置
     * @return
     */
    private static void setDataSourceConfig(){
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DATASOUCE_URL);
        dsc.setDriverName(DATASOURCE_DRIVER_NAME);
        dsc.setUsername(DATASOURCE_USERNAME);
        dsc.setPassword(DATASOURCE_PASSWORD);
        generator.setDataSource(dsc);
    }

    /**
     * 设置模板引擎
     *  支持引擎为: beetl ,freemarker , velocity
     *  在 mybaits-plus-generator 包下 的 templates 预置了三种模板引擎对应的默认模板.
     *  如果希望替换,则在当前项目的 resources 文件 > templates 文件夹中新建同名文件替换即可.
     */
    private static void setTemplateEngine(){
//        generator.setTemplateEngine( new VelocityTemplateEngine());
//        generator.setTemplateEngine(new FreemarkerTemplateEngine());
//        generator.setTemplateEngine(new BeetlTemplateEngine());
        /*
           不设置,则默认使用 velocity 模板引擎
         */
        generator.setTemplateEngine(new BeetlTemplateEngine());
    }

    private static void setGlobalConfig(){
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir( PROJECT_PATH + OUTPUT_DIR);
        gc.setAuthor(AUTHOR);
        gc.setOpen(false);
        gc.setSwagger2(true);
        // 设置在 maper.xml 文件中是否生成一个 ResultMap
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        generator.setGlobalConfig(gc);

    }

    private static void setPackageConfig(){
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        // 设置生成文件的父包
        pc.setParent("zy.pointer.j2easy.business");
        generator.setPackageInfo(pc);
    }

}

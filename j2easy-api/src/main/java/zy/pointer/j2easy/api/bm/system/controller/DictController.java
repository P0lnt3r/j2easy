package zy.pointer.j2easy.api.bm.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.system.dto.DictQueryDTO;
import zy.pointer.j2easy.api.bm.system.dto.DictSaveDTO;
import zy.pointer.j2easy.api.bm.system.vo.DictVo;
import zy.pointer.j2easy.api.bm.system.vo.TreeDictVo;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.business.system.service.IDictService;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTree;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTreeNode;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/system/dict")
@Api( "字典管理" )
public class DictController {

    @Autowired
    IDictService dictService;

    @GetMapping("/catalog")
    @ApiOperation( "获取字典目录结构树" )
    public List<TreeDictVo> getDictCatalog() throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Dict> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( Dict::getType , "2" /* 字典目录类型 */ );
        List<Dict> dictList = dictService.list( queryWrapper );
        PathTree<Dict> tree = new PathTree<>();
        dictList.forEach( dict -> tree.put( new PathTreeNode<Dict>( dict.getPath() , dict.getName() , dict )) );
        TreeDictVo root = tree.getRoot().convert( TreeDictVo.class , (pathTreeNode , dictVo ) -> {
            Dict dict = (Dict) pathTreeNode.getPayload();
            dictVo.setTitle( dict.getName() );
            dictVo.setKey( dict.getUniq() );
            dictVo.setId( dict.getId() );
            dictVo.setUniq( dict.getUniq() );
            return dictVo;
        } );
        return root.getChildren();
    }

    @GetMapping("/query")
    @ApiOperation( "分页查询字典" )
    public PageVo<DictVo , Dict> query( @Valid DictQueryDTO dto){
        IPage<Dict> iPage = dictService.selectByMapForPage( dto.convert()  , BeanUtil.beanToMap( dto ));
        return new PageVo().from( iPage , DictVo.class );
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation( "保存|更新 字典" )
    public boolean saveOrUpdate(@Valid DictSaveDTO dto){
        Dict dict = dto.convert();
        dict.setPath( "/" + dict.getUniq().replaceAll("\\." , "/") );
        return dictService.saveOrUpdate(dict);
    }

    @GetMapping( "/checkUniqExists" )
    @ApiOperation("检查 UNIQ 是否在全局已存在")
    @ApiImplicitParams({
            @ApiImplicitParam( paramType="query", name="uniq", dataType="String", required=true, value="字典全局唯一值" ),
            @ApiImplicitParam( paramType="query", name="id", dataType="Long", value="字典主键" )
    })
    public int checkUniqExists( @NotEmpty String uniq , Long id ){
        return dictService.checkUniqueExists( uniq , id ) ? 1 : 0;
    }

    @GetMapping( "/checkValExistsInSiblings" )
    @ApiOperation("检查 VAL 是否在同辈字典中已存在")
    @ApiImplicitParams({
            @ApiImplicitParam( paramType="query", name="pId", dataType="Long", value="父节点字典主键" , required = true),
            @ApiImplicitParam( paramType="query", name="val", dataType="String", required=true, value="字典值" ),
            @ApiImplicitParam( paramType="query", name="id", dataType="Long", value="字典主键" )
    })
    public int checkValExistsInSiblings( @NotEmpty Long pId , @NotEmpty String val  , Long id ){
        return dictService.checkValExistsInSiblings( pId, val, id) ? 1 :0 ;
    }

    @PostMapping( "/remove" )
    @ApiOperation( "删除字典" )
    @ApiImplicitParams({
            @ApiImplicitParam( paramType="query", name="id", dataType="Long", value="字典主键" , required = true),
    })
    public int remove( @NotEmpty Long id ){
        return dictService.removeById(id) ? 1 : 0;
    }


}

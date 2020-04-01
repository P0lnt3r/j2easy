package zy.pointer.j2easy.api.bm.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
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
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/dict")
@Api( "字典管理" )
public class DictController {

    @Autowired
    IDictService dictService;

    @GetMapping("/catalog")
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
            return dictVo;
        } );
        return root.getChildren();
    }

    @GetMapping("/query")
    public PageVo<DictVo , Dict> query( @Valid DictQueryDTO dto){
        IPage<Dict> iPage = dictService.selectByMapForPage( dto.convert()  , BeanUtil.beanToMap( dto ));
        return new PageVo().from( iPage , DictVo.class );
    }

    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@Valid DictSaveDTO dto){
        Dict dict = dto.convert();
        dict.setPath( "/" + dict.getUniq().replaceAll("\\." , "/") );
        return dictService.saveOrUpdate(dict);
    }


}

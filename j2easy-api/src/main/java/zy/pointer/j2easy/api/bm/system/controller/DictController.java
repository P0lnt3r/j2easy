package zy.pointer.j2easy.api.bm.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.system.vo.DictVo;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.business.system.service.IDictService;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTree;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTreeNode;

import java.util.List;

@RestController
@RequestMapping("/system/dict")
@Api( "字典管理" )
public class DictController {

    @Autowired
    IDictService dictService;

    @GetMapping("/catalog")
    public List<DictVo> getDictCatalog() throws InstantiationException, IllegalAccessException {
        LambdaQueryWrapper<Dict> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( Dict::getType , "2" /* 字典目录类型 */ );
        List<Dict> dictList = dictService.list( queryWrapper );
        PathTree<Dict> tree = new PathTree<>();
        dictList.forEach( dict -> tree.put( new PathTreeNode<Dict>( dict.getPath() , dict.getName() , dict )) );
        DictVo root = tree.getRoot().convert( DictVo.class , ( pathTreeNode , dictVo ) -> {
            Dict dict = (Dict) pathTreeNode.getPayload();
            dictVo.setTitle( dict.getName() );
            dictVo.setKey( dict.getUniq() );
            return dictVo;
        } );
        return root.getChildren();
    }

}

package zy.pointer.j2easy.test.business.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.api.bm.system.vo.DictVO;
import zy.pointer.j2easy.api.bm.system.vo.TreeNodeDictVO;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.business.system.service.IDictService;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTree;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTreeNode;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;
import zy.pointer.j2easy.test.SpringTestCase;

import java.util.HashMap;
import java.util.Map;

public class DictServiceTest extends SpringTestCase {

    @Autowired
    IDictService dictService;

    @Test
    public void test() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        PathTree<Dict> tree = new PathTree<>();
        dictService.list().forEach( dict -> tree.put( new PathTreeNode( dict.getPath() , dict.getName() , dict )) );
        System.out.println( tree );
        ObjectMapper mapper = new ObjectMapper();
        System.out.println( mapper.writeValueAsString(tree) );

        TreeNodeDictVO newRoot = tree.getRoot().convert(TreeNodeDictVO.class , (pathTreeNode , dictVo ) -> {
            Dict dict = (Dict)pathTreeNode.getPayload();
            dictVo.setKey( dict.getUniq() );
            dictVo.setTitle( dict.getName() );
            return dictVo;
        });
        System.out.println( mapper.writeValueAsString(newRoot) );
    }

    @Test
    public void testPage(){
        Page<Dict> page = new Page<>();
        page.setCurrent(1);
        page.setSize(5);
        Map<String,Object> params = new HashMap<>();
        params.put("type" , 1);
        IPage<Dict> _page =  dictService.selectByMapForPage(page , params);

        PageVo pageVo = new PageVo();
        pageVo = pageVo.from( _page , DictVO.class);
        System.out.println(pageVo);
    }


}

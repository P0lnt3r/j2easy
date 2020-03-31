package zy.pointer.j2easy.test.business.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zy.pointer.j2easy.api.bm.system.vo.DictVo;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.business.system.service.IDictService;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTree;
import zy.pointer.j2easy.framework.datastructuers.pathtree.PathTreeNode;
import zy.pointer.j2easy.test.SpringTestCase;

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

        DictVo newRoot = tree.getRoot().convert(DictVo.class , ( pathTreeNode , dictVo ) -> {
            Dict dict = (Dict)pathTreeNode.getPayload();
            dictVo.setKey( dict.getUniq() );
            dictVo.setTitle( dict.getName() );
            return dictVo;
        });
        System.out.println( mapper.writeValueAsString(newRoot) );
    }



}

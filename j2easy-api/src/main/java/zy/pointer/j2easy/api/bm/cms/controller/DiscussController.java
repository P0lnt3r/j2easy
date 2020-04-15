package zy.pointer.j2easy.api.bm.cms.controller;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.cms.vo.DiscussVO;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.business.cms.service.IDiscussService;
import zy.pointer.j2easy.framework.web.model.dto.PageQueryDTO;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

@RestController
@RequestMapping("/api/bm/discuss")
public class DiscussController {

    @Autowired
    IDiscussService discussService;

    @GetMapping("/query")
    public PageVo<DiscussVO , Discuss> query( PageQueryDTO<Discuss> DTO ){
        return new PageVo<DiscussVO , Discuss>().from(
                discussService.selectByMapForPage( DTO.convert() , BeanUtil.beanToMap( DTO )) ,
                DiscussVO.class
        );
    }

}

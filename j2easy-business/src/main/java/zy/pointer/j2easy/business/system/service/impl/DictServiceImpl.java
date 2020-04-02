package zy.pointer.j2easy.business.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.business.system.mapper.DictMapper;
import zy.pointer.j2easy.business.system.service.IDictService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;

@Transactional
@Primary
@Service
public class DictServiceImpl extends AbsBusinessService<DictMapper , Dict> implements IDictService {

    @Override
    @Transactional( readOnly = true)
    public boolean checkUniqueExists(String uniq, Long id) {
        LambdaQueryWrapper<Dict> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( Dict::getUniq , uniq );
        if ( id != null && id > 0 ){
            queryWrapper.ne( Dict::getId , id );
        }
        return getBaseMapper().selectCount( queryWrapper ) > 0;
    }

    @Override
    @Transactional( readOnly = true)
    public boolean checkValExistsInSiblings(Long pId, String val, Long id) {
        LambdaQueryWrapper<Dict> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq( Dict::getPId , pId )
                    .eq( Dict::getVal , val );
        if ( id != null && id > 0 ){
            queryWrapper.ne( Dict::getId , id );
        }
        return getBaseMapper().selectCount( queryWrapper ) > 0;
    }

}

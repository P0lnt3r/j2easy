package zy.pointer.j2easy.framework.datastructuers.pathtree;

import java.util.List;

/**
 *
 * @param <T> 实现类 填写自己的类即可,完成自关联.需要泛型申明,主要时防止擦拭丢失
 */
public abstract class SelfRefChildrenListObject<T extends SelfRefChildrenListObject> {

    public abstract void setChildren( List<T> children );

}

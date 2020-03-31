package zy.pointer.j2easy.framework.datastructuers.pathtree;

import java.util.List;

public abstract class SelfRefChildrenListObject<T extends SelfRefChildrenListObject> {

    public abstract void setChildren( List<T> children );

}

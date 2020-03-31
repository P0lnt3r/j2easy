package zy.pointer.j2easy.framework.datastructuers.pathtree;

public interface PathTreeNodeConvertHandler <Obj extends SelfRefChildrenListObject> {

     Obj convert( PathTreeNode node , Obj obj );

}

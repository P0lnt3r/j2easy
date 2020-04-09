package zy.pointer.j2easy.framework.datastructuers.pathtree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathTree <T> implements Serializable {

    private PathTreeNode<T> root;

    public PathTree(){
        root = new PathTreeNode<>( "/" , "root" , null );
    }



    public static void main(String[] args) {

        PathTree tree = new PathTree();
        PathTreeNode node = new PathTreeNode( "/system/account/add" , "添加用户" , null );
        PathTreeNode node2 = new PathTreeNode( "/system/account/update" , "更新用户" , null );
        PathTreeNode node3 = new PathTreeNode( "/api/v1/addUser" , "XXXX" , 100 );
        tree.put( node );
        tree.put( node2 );
        tree.put( node3 );
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(tree);
            System.out.println( json );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        PathTreeNode node4 = new PathTreeNode( "/api/v1/addUser" , "XXXX" , "我i才" );
        tree.put(node4);
        System.out.println( tree.get( "/api/v1/addUser" ).getPayload() );

    }

    public PathTreeNode<T> get( String path ){
        if ( "/".equals(path) ){
            return root;
        }
        PathTreeNode parent = getParent( path );
        return getPathTreeNode( parent , path );
    }

    public void put ( PathTreeNode node ){
        if ( node.getPath().equals( root.getPath() ) ){
            root.setPayload( (T) node.getPayload() );
            return;
        }
        PathTreeNode parent = getParent( node.getPath() );
        if ( parent.getChildren() == null ){
            parent.setChildren( new ArrayList< PathTreeNode >() );
        }

        PathTreeNode existsNode = getPathTreeNode( parent , node.getPath() )  ;
        if ( existsNode != null ){
            existsNode.setPayload( node.getPayload() );
        }else{
            parent.getChildren().add( node );
        }

    }

    /**
     * 从给的路径中获取上一级的父节点,如果父节点不存在,则会虚拟构建父节点
     * @param path
     * @return
     */
    private PathTreeNode getParent( String path ){
        String[] pathArr = path.split("/");
        PathTreeNode parent = root;
        String parentPath = "";
        for ( int i = 1; i<pathArr.length - 1; i++ ){
            parentPath = parentPath + "/" + pathArr[i];
            parent = getIgnoreNotExists( parent , parentPath );
        }
        return parent;
    }

    /**
     * 从父节点中获取子节点
     * @param parent
     * @param path
     * @return
     */
    private PathTreeNode getPathTreeNode( PathTreeNode parent , String path ){
        if ( parent.getChildren() == null ){
            parent.setChildren( new ArrayList<>() );
        }
        List<PathTreeNode> children = parent.getChildren();
        Optional<PathTreeNode> _target = children.stream()
                .filter( node -> path.equals( node.getPath() ) )
                .findFirst();
        return _target.isPresent() ? _target.get() : null;
    }

    /**
     * 从父节点中获取子节点,如果不存在子节点,则虚拟构建一个子节点并返回
     * @param parent
     * @param path
     * @return
     */
    private PathTreeNode getIgnoreNotExists( PathTreeNode parent , String path ){
        PathTreeNode target = getPathTreeNode( parent , path );
        if ( target == null ){
            target = new PathTreeNode( path , null , null );
            parent.getChildren().add( target );
        }
        return target;
    }


    public PathTreeNode<T> getRoot() {
        return root;
    }
    public void setRoot(PathTreeNode<T> root) {
        this.root = root;
    }
}

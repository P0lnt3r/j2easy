package zy.pointer.j2easy.framework.commons.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {

    public static Class getGenericsClass( Class clazz , int index ){
        Type _type = clazz.getGenericSuperclass();
        if ( _type instanceof ParameterizedType){
            ParameterizedType type = (ParameterizedType)_type;
            // 获取子类申明的泛型组
            Type[] typeArr = type.getActualTypeArguments();
            if ( typeArr != null && typeArr.length > index ){
                return (Class)typeArr[index];
            }
         }
        return null;
    }

}

package zy.pointer.j2easy.framework.log.annos;

import java.lang.annotation.*;

@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( value = { ElementType.METHOD } )
@Inherited
public @interface LogMethod {

    String name() default "";

    boolean logIn() default true;

    boolean logOut() default true;

}

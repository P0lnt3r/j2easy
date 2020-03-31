package zy.pointer.j2easy.framework.web.model.vo;

public interface IConvertHandler< Entity , ValueObject > {

    ValueObject handle( Entity entity , ValueObject vo );

}

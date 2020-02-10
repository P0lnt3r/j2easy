package zy.pointer.j2easy.framework.web.model.vo;

public interface IConvertHandler< Entity , ValueObject > {

    ValueObject hanlde( Entity entity , ValueObject vo );

}

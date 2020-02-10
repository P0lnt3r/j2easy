package zy.pointer.j2easy.framework.web.model.dto;

public interface IConvertHandler<DataTransferObject , Entity> {

    Entity handle( DataTransferObject dto , Entity entity);

}

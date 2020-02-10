package zy.pointer.j2easy.framework.web.model.dto;

public interface DataTransferObject<Entity> {

    Entity convert();

    <DataTransferObject> Entity convert( Class<DataTransferObject> clazz , IConvertHandler< DataTransferObject , Entity> handler );

}

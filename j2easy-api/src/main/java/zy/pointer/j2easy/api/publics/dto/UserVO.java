package zy.pointer.j2easy.api.publics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String name;

    private Integer type;

    private String registTime;

}

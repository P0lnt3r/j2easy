package zy.pointer.j2easy.api.bm.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestVO {

    private Long id ;

    private Boolean checked;

    public static void main(String[] args) throws JsonProcessingException {

        TestVO VO = new TestVO();
        VO.setId(1L);
        VO.setChecked(null);

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(VO));

    }

}

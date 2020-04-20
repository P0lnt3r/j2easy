package zy.pointer.j2easy.api.publics.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(
        ignoreUnknown = true	// 忽略 JSON 与 Bean 中没有一一对应的字段
)
public class TZApiResponse {

    private Object result;

    private Object targetUrl;

    private Boolean success;

    private Object error;




}

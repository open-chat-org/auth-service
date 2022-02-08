package openchat.authservice.dto;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import openchat.authservice.constant.ResponseMessage;

@Data
@AllArgsConstructor
public class ResponseModel {

    private Integer code;
    private ResponseMessage message;
    private HashMap<String, Object> response;
}

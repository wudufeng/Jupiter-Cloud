package com.ueb.framework.channel.pojo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    /** 响应码 */
    private String retCode;
    /** 响应信息 */
    private String retMsg;

    /** 响应参数 */
    private Map<String, Object> body;


    public MessageResponse(String retCode, String retMsg) {
        super();
        this.retCode = retCode;
        this.retMsg = retMsg;
    }


    public boolean isSuccess() {
        return "0000".equals(retCode);
    }
}

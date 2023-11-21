package com.sometool.dto.request;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

@Data
public class QywxMessageRequest extends QywxRequest {
    private String message;

    private List<String> toUserList;
    private String msgtype = "text";
    private Integer safe = 0;
    private Integer enable_id_trans = 0;
    private Integer enable_duplicate_check = 0;
    private Integer duplicate_check_interval = 0;

    private Content text;
    private String touser;

    public QywxMessageRequest getParamReq() {
        String to = "";
        StringBuffer sb = new StringBuffer();
        if (!ObjectUtils.isEmpty(this.toUserList)) {
            this.toUserList.forEach(x -> {
                sb.append(x + "|");
            });
        }
        to = sb.toString();
        if (to.endsWith("|")) {
            to = to.substring(0, to.lastIndexOf("|"));
        }
        this.touser = to;

        Content content = new Content();
        content.setContent(this.message);
        this.text = content;

        return this;
    }

    @Data
    class Content {
        private String content;
    }
}

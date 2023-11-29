package com.sometool.dto.request;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

@Data
public class QywxMessageRequest extends QywxRequest {
    private String message;

    /**
     * 指定接收消息的成员，成员ID列表（多个接收者用‘|’分隔，最多支持1000个）。
     * 特殊情况：指定为"@all"，则向该企业应用的全部成员发送
     * <p>
     * 最后会转成xxx|xxxx放到touser字段里
     */
    private List<String> toUserList;

    /**
     * 指定接收消息的部门，部门ID列表，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为"@all"时忽略本参数
     * <p>
     * 最后会转成xxx|xxxx放到 toparty 字段里
     */
    private List<String> toPartyList;

    /**
     * 指定接收消息的标签，标签ID列表，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为"@all"时忽略本参数
     * <p>
     * 最后会转成xxx|xxxx放到 totag 字段里
     */
    private List<String> toTagList;

    /**
     * 消息类型.
     * text：文本消息
     * textcard：文本卡片消息
     */
    private String msgtype = "text";

    /**
     * 表示是否是保密消息，0表示可对外分享，1表示不能分享且内容显示水印，默认为0
     */
    private Integer safe = 0;

    /**
     * 表示是否开启id转译，0表示否，1表示是，默认0。仅第三方应用需要用到，企业自建应用可以忽略。
     */
    private Integer enable_id_trans = 0;

    /**
     * 表示是否开启重复消息检查，0表示否，1表示是，默认0
     */
    private Integer enable_duplicate_check = 0;

    /**
     * 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时
     */
    private Integer duplicate_check_interval = 0;

    /**
     * 消息内容，最长不超过2048个字节，超过将截断（支持id转译）
     */
    private Content text;

    /**
     * 指定接收消息的成员，成员ID列表（多个接收者用‘|’分隔，最多支持1000个）。
     * 特殊情况：指定为"@all"，则向该企业应用的全部成员发送
     */
    private String touser;

    /**
     * 指定接收消息的部门，部门ID列表，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为"@all"时忽略本参数
     */
    private String toparty;

    /**
     * 指定接收消息的标签，标签ID列表，多个接收者用‘|’分隔，最多支持100个。
     * 当touser为"@all"时忽略本参数
     */
    private String totag;

    /**
     * 文本卡片消息。msgtype=textcard时用
     */
    private TextCard textcard;

    public QywxMessageRequest() {

    }

    public QywxMessageRequest(String agentid, String corpId, String corpSecret) {
        super(agentid, corpId, corpSecret);
    }

    public QywxMessageRequest getParamReq() {

        if (!ObjectUtils.isEmpty(this.toUserList)) {
            String to = "";
            StringBuffer sb = new StringBuffer();
            this.toUserList.forEach(x -> {
                sb.append(x + "|");
            });

            to = sb.toString();
            if (to.endsWith("|")) {
                to = to.substring(0, to.lastIndexOf("|"));
            }
            this.touser = to;
        }

        if (!ObjectUtils.isEmpty(this.toPartyList)) {
            String to = "";
            StringBuffer sb = new StringBuffer();
            this.toPartyList.forEach(x -> {
                sb.append(x + "|");
            });

            to = sb.toString();
            if (to.endsWith("|")) {
                to = to.substring(0, to.lastIndexOf("|"));
            }
            this.toparty = to;
        }

        if (!ObjectUtils.isEmpty(this.toTagList)) {
            String to = "";
            StringBuffer sb = new StringBuffer();
            this.toTagList.forEach(x -> {
                sb.append(x + "|");
            });

            to = sb.toString();
            if (to.endsWith("|")) {
                to = to.substring(0, to.lastIndexOf("|"));
            }
            this.totag = to;
        }

        if (ObjectUtils.isEmpty(this.message) || "text".endsWith(this.msgtype)) {
            Content content = new Content();
            content.setContent(this.message);
            this.text = content;
        }


        return this;
    }

    @Data
    public static class Content {
        private String content;
    }

    @Data
    public static class TextCard {
        /**
         * 标题，不超过128个字节，超过会自动截断（支持id转译）
         */
        private String title;

        /**
         * 描述，不超过512个字节，超过会自动截断（支持id转译）
         */
        private String description;

        /**
         * 点击后跳转的链接。最长2048字节，请确保包含了协议头(http/https)
         */
        private String url;

        /**
         * 按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。
         */
        private String btntxt;

        public TextCard() {
        }

        public TextCard(String title, String description, String url, String btntxt) {
            this.title = title;
            this.description = description;
            this.url = url;
            this.btntxt = btntxt;
        }
    }


}


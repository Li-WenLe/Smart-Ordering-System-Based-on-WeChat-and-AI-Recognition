package com.wxprogrem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String sender;
    private MessageType type;
    private String content;
    private Date timestamp;
    private String toUserId;
    private String fromUserId;
}
package com.wxprogrem.controller.common;

import com.wxprogrem.service.RAGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIChatController {
    @Autowired
    private RAGService ragService;
    @PostMapping
    public String sendMsg(String question) {
        String resp= ragService.response(question);
        return resp;
    }
}

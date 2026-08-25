package com.wxprogrem.service;

import org.nd4j.common.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;


public interface RAGService {
    String response(String question);

}

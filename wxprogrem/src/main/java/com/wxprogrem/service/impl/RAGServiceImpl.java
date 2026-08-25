package com.wxprogrem.service.impl;

import com.wxprogrem.service.RAGService;
import org.nd4j.common.io.ClassPathResource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.zhipuai.ZhiPuAiEmbeddingModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class RAGServiceImpl implements RAGService {

    private final ZhiPuAiEmbeddingModel zhiPuAiEmbeddingModel;
    private final ChatClient chatClient;

    private final List<float[]> vectors = new ArrayList<>();
    private final List<String> docs = new ArrayList<>();
    private volatile boolean initialized = false;

    public RAGServiceImpl(ZhiPuAiEmbeddingModel zhiPuAiEmbeddingModel, ChatClient.Builder chatClientBuilder) {
        this.zhiPuAiEmbeddingModel = zhiPuAiEmbeddingModel;
        this.chatClient = chatClientBuilder.build();
    }

    private static double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    private synchronized void initKnowledgeBaseIfNeeded() {
        if (initialized) {
            return;
        }
        ClassPathResource resource = new ClassPathResource("com/wxprogrem/知识库/知识库.txt");
        try {
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String[] words = content.split(" ");
            for (String word : words) {
                if (word == null || word.isBlank()) {
                    continue;
                }
                docs.add(word);
                vectors.add(zhiPuAiEmbeddingModel.embed(word));
            }
            initialized = true;
        } catch (IOException e) {
            throw new RuntimeException("加载知识库失败", e);
        }
    }

    @Override
    public String response(String question) {
        initKnowledgeBaseIfNeeded();

        float[] embed = zhiPuAiEmbeddingModel.embed(question);
        double top1 = -1, top2 = -1;
        int index1 = -1, index2 = -1;
        for (int i = 0; i < vectors.size(); i++) {
            double sim = cosineSimilarity(embed, vectors.get(i));
            if (sim > top1) {
                top2 = top1;
                top1 = sim;
                index2 = index1;
                index1 = i;
            } else if (sim > top2) {
                top2 = sim;
                index2 = i;
            }
        }

        StringBuilder ctxBuilder = new StringBuilder();
        if (index1 >= 0) {
            ctxBuilder.append(docs.get(index1));
        }
        if (index2 >= 0 && index2 != index1) {
            ctxBuilder.append("\n").append(docs.get(index2));
        }
        String ctx = ctxBuilder.toString();
        String prompt = "以下是知识库内容：\n" + ctx + "\n\n请结合以上内容回答用户的问题。";
        return chatClient.prompt(prompt)
                .system("你是知识助手，可以结合提供的知识库内容准确回答问题。")
                .user(question)
                .call()
                .content();
    }
}

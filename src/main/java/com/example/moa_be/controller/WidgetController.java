package com.example.moa_be.controller;

import com.example.moa_be.repository.ProjectRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/payments")
public class WidgetController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${tosspayments.secretKey}")
    private String widgetSecretKey;

    private final ProjectRepository projectRepository;

    // 생성자 주입 (Spring 4.3+는 @Autowired 없어도 자동 주입)
    public WidgetController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostMapping("/confirm")
    public ResponseEntity<JsonNode> confirmPayment(@RequestBody JsonNode paymentRequest) {
        try {
            String paymentKey = paymentRequest.get("paymentKey").asText();
            String orderId = paymentRequest.get("orderId").asText();
            String amountStr = paymentRequest.get("amount").asText();
            long amount = Long.parseLong(amountStr);

            logger.info("결제 확인 요청 - paymentKey: {}, orderId: {}, amount: {}", paymentKey, orderId, amount);

            String auth = Base64.getEncoder().encodeToString((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
            String authorizationHeader = "Basic " + auth;

            String jsonPayload = paymentRequest.toString();

            URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", authorizationHeader);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            InputStream responseStream = (responseCode == 200) ? connection.getInputStream() : connection.getErrorStream();

            JsonNode responseJson = objectMapper.readTree(responseStream);
            responseStream.close();

            logger.info("토스 결제 확인 응답: {}", responseJson.toString());

            if (responseCode == 200) {
                // 결제 성공 시 프로젝트 currentAmount 갱신
                String[] orderParts = orderId.split("_");
                if (orderParts.length < 3) {
                    throw new IllegalArgumentException("Invalid orderId format: " + orderId);
                }
                Long projectId = Long.parseLong(orderParts[2]);
                projectRepository.findById(projectId).ifPresent(project -> {
                    project.setCurrentAmount(project.getCurrentAmount() + amount);
                    projectRepository.save(project);
                    logger.info("프로젝트 id={}의 currentAmount가 {}만큼 증가했습니다. 현재값={}", projectId, amount, project.getCurrentAmount());
                });
            }

            return ResponseEntity.status(responseCode).body(responseJson);

        } catch (Exception e) {
            logger.error("결제 확인 중 오류 발생", e);
            return ResponseEntity.status(500).body(objectMapper.createObjectNode().put("error", "결제 확인 중 오류가 발생했습니다."));
        }
    }
}

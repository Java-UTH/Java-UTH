package com.example.SP26SE025.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {

    // Đường dẫn lưu trữ ảnh tạm thời
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping("/customer/home")
    public String customerHome() {
        return "customer/home";
    }

    @GetMapping("/customer/reports/analysis")
    public String showAnalysisPage(Model model) {
        model.addAttribute("historyList", new ArrayList<AnalysisHistory>());
        return "customer/analysis_report";
    }

    @PostMapping("/customer/reports/upload")
    public String handleAiAnalysis(@RequestParam("file") MultipartFile file, Model model) {
        List<AnalysisHistory> history = new ArrayList<>();
        
        if (file.isEmpty()) {
            model.addAttribute("historyList", history);
            return "customer/analysis_report";
        }

        try {
            // 1. Lưu file ảnh và tạo đường dẫn tuyệt đối
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            String absolutePath = filePath.toAbsolutePath().toString();
            
            // 2. Cấu hình lệnh gọi Python
            // Lưu ý: Sử dụng đường dẫn python từ log hệ thống của bạn
            String pythonExe = "C:\\Users\\ASUS PC\\AppData\\Local\\Microsoft\\WindowsApps\\python.exe";
            String scriptPath = "ai-service/src/test_model.py";

            ProcessBuilder pb = new ProcessBuilder(pythonExe, scriptPath, "--image", absolutePath);
            pb.redirectErrorStream(true); // Gộp lỗi và output làm một để dễ bắt lỗi
            Process process = pb.start();

            // 3. Đọc dữ liệu trả về từ Python
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder fullLog = new StringBuilder();
            String aiResult = "Lỗi: Không nhận được phản hồi từ script AI.";
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println("DEBUG PYTHON: " + line);
                fullLog.append(line).append("\n");
                
                // Tìm dòng kết quả chuẩn từ Python
                if (line.contains("Kết quả AI:")) {
                    aiResult = line.substring(line.indexOf("Kết quả AI:")).trim();
                }
            }

            // Nếu không tìm thấy kết quả AI, hiển thị dòng lỗi cuối cùng từ Python để debug
            if (aiResult.startsWith("Lỗi:") && fullLog.length() > 0) {
                aiResult = "Lỗi Python: " + (fullLog.length() > 100 ? fullLog.substring(0, 100) + "..." : fullLog.toString());
            }

            // 4. Cập nhật lịch sử hiển thị lên giao diện
            history.add(new AnalysisHistory(LocalDateTime.now(), "/uploads/" + fileName, aiResult));

        } catch (IOException e) {
            e.printStackTrace();
            history.add(new AnalysisHistory(LocalDateTime.now(), "error", "Lỗi hệ thống: " + e.getMessage()));
        }

        model.addAttribute("historyList", history);
        return "customer/analysis_report";
    }

    // Inner class để lưu trữ thông tin phân tích
    public static class AnalysisHistory {
        private LocalDateTime createdAt;
        private String imageUrl;
        private String aiResult;

        public AnalysisHistory(LocalDateTime createdAt, String imageUrl, String aiResult) {
            this.createdAt = createdAt;
            this.imageUrl = imageUrl;
            this.aiResult = aiResult;
        }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public String getImageUrl() { return imageUrl; }
        public String getAiResult() { return aiResult; }
    }
}
package com.example.SP26SE025.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import com.example.SP26SE025.security.CustomUserDetails;
import com.example.SP26SE025.security.JwtUtil;
import com.example.SP26SE025.service.CustomUserDetailsService;
import com.example.SP26SE025.dtos.ReportSimpleDTO;
import com.example.SP26SE025.entity.DiagnosisResult;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.entity.Role;
import com.example.SP26SE025.repository.DiagnosisRepository;
import com.example.SP26SE025.repository.UserRepository;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;

@Controller
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // --- 2. THÊM AUTOWIRED REPOSITORY MỚI ---
    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private UserRepository userRepository;
    // ----------------------------------------

    @GetMapping("/loginAdmin")
    public String loginPage() {
        return "login-view-admin";
    }

    @GetMapping("/admin/home")
    public String home() {
        return "admin/dashboard";
    }
    @GetMapping("/admin/users1")
    public String users(Model model) {
        model.addAttribute("title", "Users Management");
        model.addAttribute("content", "admin/users :: content");
        model.addAttribute("pageCss", "/css/users.css");
        return "admin/users";
    }
    @GetMapping("/admin/clinics")
    public String clinics(Model model) {
        model.addAttribute("title", "Clinics Management");
        model.addAttribute("content", "admin/clinics :: content");
        model.addAttribute("pageCss", "/css/clinics.css");
        return "admin/clinics";
    }

    // ================= DOCTORS =================
    @GetMapping("/admin/doctors")
    public String doctors(Model model) {
        model.addAttribute("title", "Doctors Management");
        model.addAttribute("content", "admin/doctors :: content");
        model.addAttribute("pageCss", "/css/doctors.css");
        return "admin/doctors";
    }

    // ================= PACKAGES =================
    @GetMapping("/admin/packages")
    public String packages(Model model) {
        model.addAttribute("title", "Service Packages");
        model.addAttribute("content", "admin/packages :: content");
        model.addAttribute("pageCss", "/css/packages.css");
        return "admin/packages";
    }

    // ================= AI MANAGEMENT =================
    @GetMapping("/admin/ai")
    public String ai(Model model) {
        model.addAttribute("title", "AI Management");
        model.addAttribute("content", "admin/ai :: content");
        model.addAttribute("pageCss", "/css/ai.css");
        return "admin/ai-management";
    }

    // ================= REPORTS (ĐÃ CẬP NHẬT LOGIC MỚI) =================
    // Thay thế hàm cũ bằng hàm này để có dữ liệu thật
    @GetMapping("/admin/reports")
    public String reports(Model model, 
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Long doctorId) {
        
        // 1. Lấy danh sách bác sĩ để đổ vào dropdown
        List<User> doctors = userRepository.findByRole(Role.DOCTOR);
        model.addAttribute("doctorsList", doctors);

        // 2. Tìm kiếm báo cáo từ DB
        List<DiagnosisResult> results = diagnosisRepository.searchReports(keyword, doctorId);

        // 3. Chuyển sang DTO đơn giản
        List<ReportSimpleDTO> reportList = new ArrayList<>();
        for (DiagnosisResult r : results) {
            String pName = r.getPatient() != null ? r.getPatient().getFullName() : "N/A";
            Long pId = r.getPatient() != null ? r.getPatient().getId() : 0L;
            String dName = r.getDoctor() != null ? r.getDoctor().getFullName() : "AI Tự động";

            reportList.add(new ReportSimpleDTO(
                r.getId(), pId, pName, r.getAnalysisDate(), dName
            ));
        }

        // 4. Gửi dữ liệu ra View
        model.addAttribute("reports", reportList);
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("selectedDoctorId", doctorId);

        model.addAttribute("title", "Reports");
        model.addAttribute("content", "admin/reports :: content");
        model.addAttribute("pageCss", "/css/reports.css");
        
        return "admin/reports";
    }
    
    // --- 3. THÊM HÀM XUẤT CSV ---
    @GetMapping("/reports/export/csv") 
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=baocao_benhnhan.xlsx");

        // 1. Tạo Workbook (File Excel) và Sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Báo cáo");

            // 2. Tạo Style cho Header (In đậm, Phông to hơn)
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            
            // Căn giữa tiêu đề (Optional)
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // 3. Tạo dòng tiêu đề (Header Row)
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã BN", "Tên Bệnh Nhân", "Ngày Phân Tích", "Bác Sĩ Phụ Trách"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // 4. Đổ dữ liệu từ Database vào
            List<DiagnosisResult> listReports = diagnosisRepository.findAll();
            int rowNum = 1;

            for (DiagnosisResult r : listReports) {
                Row row = sheet.createRow(rowNum++);

                // Cột 0: Mã BN
                String pId = r.getPatient() != null ? "BN-" + r.getPatient().getId() : "N/A";
                row.createCell(0).setCellValue(pId);

                // Cột 1: Tên BN
                String pName = r.getPatient() != null ? r.getPatient().getFullName() : "N/A";
                row.createCell(1).setCellValue(pName);

                // Cột 2: Ngày phân tích (Format lại cho đẹp nếu cần)
                // Lưu ý: Excel cần format date riêng, ở đây ta xuất chuỗi cho đơn giản
                String dateStr = r.getAnalysisDate() != null ? r.getAnalysisDate().toString() : "";
                row.createCell(2).setCellValue(dateStr);

                // Cột 3: Bác sĩ
                String dName = r.getDoctor() != null ? r.getDoctor().getFullName() : "AI Tự động";
                row.createCell(3).setCellValue(dName);
            }

            // 5. Tự động giãn cột cho vừa nội dung (Auto-size columns)
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 6. Ghi ra luồng output
            workbook.write(response.getOutputStream());
        }
    }
    // ---------------------------------------

    @PostMapping("/authenticateAdmin")
    public String authenticate(@RequestParam String username,
                               @RequestParam String password,
                               HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtil.generateToken(username);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "").trim().toUpperCase();

            return switch (role) {
                case "ADMIN" -> "redirect:/admin/home";
                // case "CLINIC" -> "redirect:/clinic/home";
                // case "DOCTOR" -> "redirect:/doctor/dashboard";
                default -> "redirect:/loginAdmin?error=true";
            };
        } catch (Exception e) {
            e.printStackTrace(); // Thêm dòng này để in lỗi ra console
            return "redirect:/loginAdmin?error=true";
        }
    }
}
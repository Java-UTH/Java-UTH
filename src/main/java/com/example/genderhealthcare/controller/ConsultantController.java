package com.example.genderhealthcare.controller;

import com.example.genderhealthcare.entity.ConsultationAppointment;
import com.example.genderhealthcare.entity.Question;
import com.example.genderhealthcare.service.ConsultationAppointmentService;
import com.example.genderhealthcare.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ✅ IMPORT ĐÚNG Ở ĐÂY
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ConsultantController {

    @Autowired
    private ConsultationAppointmentService consultationAppointmentService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/consultant/home")
    public String home() {
        return "consultant/home";
    }

    @GetMapping("/consultant/list-view")
    public String list() {
        return "consultant/list_consultation";
    }

    @GetMapping("/consultant/list")
    public String viewAppointments(Model model) {
        List<ConsultationAppointment> appointments = consultationAppointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "consultant/list_consultation";
    }

    // ✅ Hiển thị danh sách câu hỏi từ khách hàng
    @GetMapping("/consultant/questions")
    public String viewQuestions(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        return "consultant/question_list";
    }

    // ✅ Hiển thị form trả lời câu hỏi
    @GetMapping("/consultant/questions/answer/{id}")
    public String answerForm(@PathVariable Long id, Model model) {
        Question question = questionService.getById(id);
        model.addAttribute("question", question);
        return "consultant/answer_form";
    }

    // ✅ Lưu câu trả lời
    @PostMapping("/consultant/questions/answer")
    public String submitAnswer(@ModelAttribute Question question) {
        Question existing = questionService.getById(question.getId());
        existing.setAnswer(question.getAnswer());
        questionService.save(existing);
        return "redirect:/consultant/questions";
    }
}

package com.example.genderhealthcare.dtos;

public class MonthlyCountDTO {
    private String month; // định dạng "yyyy-MM"
    private Long count;

    public MonthlyCountDTO(String month, Long count) {
        this.month = month;
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

package com.au.expense.tracker.controller;

import com.au.expense.tracker.entity.ExpenseTrackerEntity;
import com.au.expense.tracker.service.ExpenseTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("expenses")
public class ExpenseTrackerController {

    @Autowired
    private ExpenseTrackerService expenseTrackerService;

    @GetMapping("getPayment")
    public List<ExpenseTrackerEntity> getPaymentDetails() throws IOException {
        return expenseTrackerService.getPaymentDetails();
    }
    @PostMapping("setPayment")
    public String setPaymentDetails(@RequestBody ExpenseTrackerEntity exp) throws IOException {
        return expenseTrackerService.setPaymentDetails(exp);
    }
    @PutMapping("updatePayment/{id}")
    public String updatePaymentDetails(@RequestBody ExpenseTrackerEntity exp,@PathVariable(value = "id") int id) throws IOException {
        return expenseTrackerService.updatePaymentDetails(exp,id);
    }
    @DeleteMapping("deletePayment/{id}")
    public String deletePaymentDetails(@PathVariable(value = "id") int id) throws IOException {
        return expenseTrackerService.deletePaymentDetails(id);
    }
}

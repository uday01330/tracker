package com.au.expense.tracker.service;

import com.au.expense.tracker.entity.ExpenseTrackerEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseTrackerService {

    private final ResourceLoader resourceLoader;

    public ExpenseTrackerService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    public List<ExpenseTrackerEntity> getPaymentDetails() throws IOException {
        String cellValue = null;
        Resource resource = resourceLoader.getResource("classpath:" + "Expense_Tracker.xlsx");
        File file = resource.getFile();
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows();
        List<ExpenseTrackerEntity> expenseTrackerEntityList = new ArrayList<>();
        for(int i = 1 ; i< rowCount ; i++){
            XSSFRow row = sheet.getRow(i);
            int cellCount = row.getPhysicalNumberOfCells();
            String[] arr = new String[cellCount];
            for (int j = 0; j < cellCount; j++){
                XSSFCell cell = row.getCell(j);
                cellValue = getCellValue(cell);
                arr[j] = cellValue;
                if(j == cellCount -1 ){
                    expenseTrackerEntityList.add(setExpensesList(arr));
                }
                System.out.println("||"+cellValue);
            }
        }
        workbook.close();
        fis.close();
        return expenseTrackerEntityList;
    }
    public String setPaymentDetails(ExpenseTrackerEntity exp) throws IOException {
        String cellValue = null;
        Resource resource = resourceLoader.getResource("classpath:" + "Expense_Tracker.xlsx");
        File file = resource.getFile();
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int num = sheet.getPhysicalNumberOfRows();
        Row row = sheet.createRow(num);
        row.createCell(0).setCellValue(num);
        row.createCell(1).setCellValue(exp.getAmount());
        row.createCell(2).setCellValue(exp.getPaymentType());
        row.createCell(3).setCellValue(exp.getPerson());
        row.createCell(4).setCellValue(exp.getReason());
        row.createCell(5).setCellValue(exp.getDateOfPayment());
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        return "Successfully Added";
    }
    public String updatePaymentDetails(ExpenseTrackerEntity exp,int rowNum) throws IOException {
        String cellValue = null;
        Resource resource = resourceLoader.getResource("classpath:" + "Expense_Tracker.xlsx");
        File file = resource.getFile();
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(rowNum);
        row.createCell(0).setCellValue(rowNum);
        row.createCell(1).setCellValue(exp.getAmount());
        row.createCell(2).setCellValue(exp.getPaymentType());
        row.createCell(3).setCellValue(exp.getPerson());
        row.createCell(4).setCellValue(exp.getReason());
        row.createCell(5).setCellValue(exp.getDateOfPayment());
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        return "Successfully Updated";
    }

    public String deletePaymentDetails(int rowNum) throws IOException {
        String cellValue = null;
        Resource resource = resourceLoader.getResource("classpath:" + "Expense_Tracker.xlsx");
        File file = resource.getFile();
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        deleteRow(sheet, rowNum);
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
        return "Successfully Removed";
    }
    private static void deleteRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }
    public ExpenseTrackerEntity setExpensesList(String arr[]){
        ExpenseTrackerEntity expenseTrackerEntity = new ExpenseTrackerEntity();
        for(int i =0; i<arr.length; i++){
            switch (i){
                case 0:
                    expenseTrackerEntity.setId(arr[i]);
                    break;
                case 1:
                    expenseTrackerEntity.setAmount(arr[i]);
                    break;
                case 2:
                    expenseTrackerEntity.setPaymentType(arr[i]);
                    break;
                case 3:
                    expenseTrackerEntity.setPerson(arr[i]);
                    break;
                case 4:
                    expenseTrackerEntity.setReason(arr[i]);
                    break;
                case 5:
                    expenseTrackerEntity.setDateOfPayment(arr[i]);
                    break;
                default:
                    break;
            }
        }
        return expenseTrackerEntity;
    }

    public String getCellValue(XSSFCell cell){
        switch (cell.getCellType()){
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            default:
                return cell.getStringCellValue();
        }
    }

//    public String DateTimeModifier(String dateTime){
//        LocalDateTime dateTime1 = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
//        LocalDateTime localDateTime = dateTime1.atOffset(ZoneOffset.UTC).toLocalDateTime();
//        // Define a custom formatter to remove the "T18:30" part
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        // Format the LocalDateTime object using the custom formatter
//        return localDateTime.format(formatter);
//    }
}

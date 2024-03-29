package com.iamthene.driverassistant.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;


import com.iamthene.driverassistant.activity.OilActivity;
import com.iamthene.driverassistant.model.Oil;

import com.iamthene.driverassistant.model.Refuel;
import com.iamthene.driverassistant.model.Repair;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExportFile {
    public void exportRepair(Context context, String name, List<Repair> repairs) {
        Workbook wb2003 = new HSSFWorkbook();
        String safeName = WorkbookUtil.createSafeSheetName(name);
        Sheet sheet = wb2003.createSheet(safeName);

        // Khỏi tạo Row, Cell đầu tiên
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Thời gian");
        cell = row.createCell(1);
        cell.setCellValue("Tên phương tiện");
        cell = row.createCell(2);
        cell.setCellValue("Phụ tùng");
        cell = row.createCell(3);
        cell.setCellValue("Chi phí");

        // Đổ dữ liệu
        int numRow = 1;
        for (Repair r : repairs) {
            row = sheet.createRow(numRow);
            cell = row.createCell(0);
            cell.setCellValue(r.getDate().concat(" ").concat(r.getTime()));
            cell = row.createCell(1);
            cell.setCellValue(r.getCarId());
            cell = row.createCell(2);
            cell.setCellValue(r.getPart());
            cell = row.createCell(3);
            cell.setCellValue(r.getPrice());
            numRow++;
        }

        // Xuất file
        File filePath = new File(Environment.getExternalStorageDirectory() + "/Thống kê sửa xe.xls");
        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            wb2003.write(fos);
            Toast.makeText(context, "Xuất file thành công!", Toast.LENGTH_SHORT).show();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            // Android 11 sẽ bị lỗi - Nên dùng thiết bị từ Android 10 đổ xuống
            Toast.makeText(context, "Xuất file thất bại!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void exportRefuel(Context context, String name, List<Refuel> refuels) {
        Workbook wb2003 = new HSSFWorkbook();
        String safeName = WorkbookUtil.createSafeSheetName(name);
        Sheet sheet = wb2003.createSheet(safeName);

        // Khỏi tạo Row, Cell đầu tiên
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Thời gian");
        cell = row.createCell(1);
        cell.setCellValue("Tên phương tiện");
        cell = row.createCell(2);
        cell.setCellValue("Loại xăng");
        cell = row.createCell(3);
        cell.setCellValue("Chi phí");
        cell = row.createCell(4);
        cell.setCellValue("Nơi đổ xăng");
        cell = row.createCell(5);
        cell.setCellValue("Dự kiến đi được");

        // Đổ dữ liệu
        int numRow = 1;
        for (Refuel r : refuels) {
            row = sheet.createRow(numRow);
            cell = row.createCell(0);
            cell.setCellValue(r.getTime());
            cell = row.createCell(1);
            cell.setCellValue(r.getCarNo());
            cell = row.createCell(2);
            cell.setCellValue(r.getFuel());
            cell = row.createCell(3);
            cell.setCellValue(r.getFee());
            cell = row.createCell(4);
            cell.setCellValue(r.getPlace());
            cell = row.createCell(5);
            cell.setCellValue(r.getExpDistance());
            numRow++;
        }

        // Xuất file
        File filePath = new File(Environment.getExternalStorageDirectory() + "/Thống kê đổ xăng.xls");
        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            wb2003.write(fos);
            Toast.makeText(context, "Xuất file thành công!", Toast.LENGTH_SHORT).show();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            // Android 11 sẽ bị lỗi - Nên dùng thiết bị từ Android 10 đổ xuống
            Toast.makeText(context, "Xuất file thất bại!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

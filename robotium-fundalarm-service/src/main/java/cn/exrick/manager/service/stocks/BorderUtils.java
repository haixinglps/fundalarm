package cn.exrick.manager.service.stocks;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class BorderUtils {
	public static void addBorderToSheet(Sheet sheet, Workbook workbook) {
//		CellStyle borderStyle = workbook.createCellStyle();
//		borderStyle.setBorderTop(BorderStyle.THIN);
//		borderStyle.setBorderBottom(BorderStyle.THIN);
//		borderStyle.setBorderLeft(BorderStyle.THIN);
//		borderStyle.setBorderRight(BorderStyle.THIN);

		// 创建橙色颜色对象
		byte[] orangeRGB = new byte[] { (byte) 255, (byte) 165, 0 };
		XSSFColor orangeColor = new XSSFColor(orangeRGB, null);

		// 创建带橙色边框的样式
		CellStyle orangeBorderStyle = workbook.createCellStyle();
		orangeBorderStyle.setBorderTop(BorderStyle.THIN);
		orangeBorderStyle.setTopBorderColor(orangeColor.getIndexed());
		orangeBorderStyle.setBorderBottom(BorderStyle.THIN);
		orangeBorderStyle.setBottomBorderColor(orangeColor.getIndexed());
		orangeBorderStyle.setBorderLeft(BorderStyle.THIN);
		orangeBorderStyle.setLeftBorderColor(orangeColor.getIndexed());
		orangeBorderStyle.setBorderRight(BorderStyle.THIN);
		orangeBorderStyle.setRightBorderColor(orangeColor.getIndexed());

		// 应用样式
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row == null)
				continue;

//			  for(int i=0; i < row.getLastCellNum(); i++) {
//		            
//		        }

			for (int j = 0; j < row.getLastCellNum(); j++) {

				// 可选：设置最大列宽限制（单位1/256字符宽度）
				// sheet.setColumnWidth(j, Math.min(50 * 256, sheet.getColumnWidth(j)));

				Cell cell = row.getCell(j);
				if (cell == null)
					continue;
				cell.setCellStyle(orangeBorderStyle);
			}
		}
	}
}

// 调用方式
//BorderUtils.addBorderToSheet(sheet, workbook);

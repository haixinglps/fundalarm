package cn.exrick.manager.service.stocks;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.exrick.manager.pojo.Category;
import cn.exrick.manager.pojo.Stock;
import cn.exrick.manager.pojo.StockDataHistory;
import cn.exrick.manager.pojo.StockDataWithBLOBs;
import cn.exrick.manager.service.StockService;
import cn.hutool.json.JSONArray;

//class Category {
//	String name;
//	List<Category> subCategories;
//	List<String> stocks;
//
//	public Category(String name) {
//		this.name = name;
//		this.subCategories = new ArrayList<>();
//		this.stocks = new ArrayList<>();
//	}
//}

public class DynamicExcelFupan {

	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
	static StockService service = context.getBean(StockService.class);

	public static void main(String[] args) throws Exception {
		// 测试数据构建

		List<String> lines = new ArrayList<String>();

		Path groupPath = Paths.get("d:/download/goodgroup.txt");
		try {
			lines = Files.readAllLines(groupPath);
//            for (String line : lines) {
//                System.out.println(line);
//            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < lines.size(); i++) {

			String tsname = lines.get(i).split("\t")[0];

			String sidString = "9052045";
//		String nameString = "军工-明星装备";
//		String tsname = "2月龙头";
//			if (tsname.indexOf("电力加") == -1) {
//				continue;
//			}

			String filePath = "d:/ts/" + tsname + ".txt";
			Workbook workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet(tsname);
			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String currentUrl;
				int j = 0;
				while ((currentUrl = br.readLine()) != null) {
					String sid = currentUrl.split("\t")[0];// .replace("userId�", "userId");
//				nameString = currentUrl.split("\t")[1];
					System.out.println(j + "\t" + sid);
					java.util.List<StockDataHistory> root = null;
					try {
						root = buildTestData(sid);
					} catch (Exception e) {

						e.printStackTrace();
						// TODO: handle exception
					}

//				System.out.println(root);

					if (root == null || root.size() == 0) {
						System.out.println("无数据：" + currentUrl);
						j++;
						continue;
					}

					// 创建工作簿

//				setupColumnWidths(sheet, new int[] { 20, 15, 15, 30 });

					// 创建表头
//				createHeaderRow(sheet, new String[] { "概念", "类型", "子分类", "股票" });

					// 填充数据
					fillData(j, sheet, root, workbook);

					// 保存文件

					j++;

				}
//			BorderUtils.addBorderToSheet(sheet, workbook);

				saveWorkbook(workbook, "d:\\download\\" + tsname + ".xlsx");
			} catch (IOException e) {
				System.err.println("监控文件出错: " + e.getMessage());
			}

		}

	}

	private static java.util.List<StockDataHistory> buildTestData(String sid) {
//		Category root = new Category("宇树科技");
//
//		Category type1 = new Category("参股");
//		type1.subCategories.add(new Category("A类"));
//		type1.subCategories.get(0).stocks.addAll(Arrays.asList("首开股份", "金发科技"));
//		type1.subCategories.add(new Category("B类"));
//
//		Category type11 = new Category("B类子类");
//		type11.stocks.addAll(Arrays.asList("中信证券", "卧龙电驱"));
//
//		type1.subCategories.get(1).subCategories.add(type11);
//
//		root.subCategories.add(type1);
//
//		Category type2 = new Category("合作");
//		type2.stocks.addAll(Arrays.asList("长盛轴承", "三一重工"));
//		root.subCategories.add(type2);

		// 读取数据库数据：
		// ApplicationContext context = new
		// ClassPathXmlApplicationContext("applicationContext-*.xml");
		SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("开始查询：" + sdfDateFormat.format(new Date()));
		java.util.List<StockDataHistory> category = service.getHistory(34, sid);
		System.out.println("查询完毕：" + sdfDateFormat.format(new Date()));

		return category;
	}

	private static void setupColumnWidths(Sheet sheet, int[] widths) {
		for (int i = 0; i < widths.length; i++) {
			sheet.setColumnWidth(i, widths[i] * 256);
		}
	}

	private static void createHeaderRow(Sheet sheet, String[] headers) {
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(i).setCellValue(headers[i]);
		}
	}

	private static int fillData(Sheet sheet, Category category, int startRow, int level) {
		int currentRow = startRow;
		int stockCount = countStocks(category);

		// 填充当前分类名称
		for (int i = 0; i < stockCount; i++) {
			Row row = sheet.getRow(currentRow + i) != null ? sheet.getRow(currentRow + i)
					: sheet.createRow(currentRow + i);
			row.createCell(level).setCellValue(category.getName());
		}

		// 合并单元格（需检查是否已合并）
		if (stockCount > 1) {
			CellRangeAddress newRegion = new CellRangeAddress(currentRow, currentRow + stockCount - 1, level, level);

			if (isRegionAvailable(sheet, newRegion)) {
				sheet.addMergedRegion(newRegion);
			}
		}

		// 处理子分类
		if (!category.getChildren().isEmpty()) {
			for (Category subCat : category.getChildren()) {
				int subStartRow = currentRow;
				currentRow = fillData(sheet, subCat, currentRow, level + 1);

				// 子分类合并
				if (currentRow - subStartRow > 1) {
					CellRangeAddress subRegion = new CellRangeAddress(subStartRow, currentRow - 1, level + 1,
							level + 1);

					if (isRegionAvailable(sheet, subRegion)) {
						sheet.addMergedRegion(subRegion);
					}
				}
			}
		}
		// 处理股票数据
		else if (!category.getStocks().isEmpty()) {
			for (Stock stock : category.getStocks()) {
				Row row = sheet.getRow(currentRow);
				row.createCell(level + 1).setCellValue(stock.getName());
				row.createCell(level + 2).setCellValue(stock.getReason());
				row.createCell(level + 3).setCellValue(stock.getRemark());
				row.createCell(level + 4).setCellValue(stock.getStockId());
//				sheet.autoSizeColumn(level + 1);
//				sheet.autoSizeColumn(level + 1);
				sheet.autoSizeColumn(level + 2);
				sheet.autoSizeColumn(level + 3);
				sheet.autoSizeColumn(level + 4);

				currentRow++;
			}
		}

		return currentRow;
	}

	private static int fillData(Integer index, Sheet sheet, java.util.List<StockDataHistory> stocks,
			Workbook workbook) {

		if (stocks.size() == 0)
			return 0;
		StockDataWithBLOBs baseInfo = service.getStockData(stocks.get(0).getStocknumber());
		// 提取历史题材：sysou
		System.out.println(stocks.get(0).getStocknumber());
		String subjes = baseInfo.getSubjectcachevos();
		JSONArray jArray = new JSONArray(subjes);
		String subjectString = "";
		for (int i = 0; i < jArray.size(); i++) {
			if (i < jArray.size() - 1) {
				subjectString += jArray.getJSONObject(i).getStr("name") + "+";
			} else {
				subjectString += jArray.getJSONObject(i).getStr("name");
			}
		}

		int currentRow = 9 * index;
//		int stockCount = countStocks(category);

		// 填充当前分类名称
		for (int i = 0; i < 7; i++) {
			Row row = sheet.getRow(currentRow + i) != null ? sheet.getRow(currentRow + i)
					: sheet.createRow(currentRow + i);
			row.createCell(1).setCellValue(baseInfo.getName());
			row.createCell(0).setCellValue(index + 1);
			if (i == 0)
				row.createCell(2).setCellValue("概念");

			else if (i == 2)
				row.createCell(2).setCellValue("星期");
			else if (i == 3)
				row.createCell(2).setCellValue("日期");
			else if (i == 4)
				row.createCell(2).setCellValue("量能");
			else if (i == 5)
				row.createCell(2).setCellValue("K线");
			else if (i == 6)
				row.createCell(2).setCellValue("涨跌幅");

		}

		// 合并单元格（需检查是否已合并）

		CellRangeAddress newRegion = new CellRangeAddress(currentRow, currentRow + 6, 0, 0);

		if (isRegionAvailable(sheet, newRegion)) {
			sheet.addMergedRegion(newRegion);
		}

		CellRangeAddress newRegion2 = new CellRangeAddress(currentRow, currentRow + 6, 1, 1);

		if (isRegionAvailable(sheet, newRegion2)) {
			sheet.addMergedRegion(newRegion2);
		}

		CellRangeAddress newRegion3 = new CellRangeAddress(currentRow, currentRow + 1, 3, 1 + stocks.size());

		if (isRegionAvailable(sheet, newRegion3)) {
			sheet.addMergedRegion(newRegion3);
		}

		CellRangeAddress newRegion4 = new CellRangeAddress(currentRow, currentRow + 1, 2, 2);
		if (isRegionAvailable(sheet, newRegion4)) {
			sheet.addMergedRegion(newRegion4);
		}

		// 处理子分类
//		if (!category.getChildren().isEmpty()) {
//			for (Category subCat : category.getChildren()) {
//				int subStartRow = currentRow;
//				currentRow = fillData(sheet, subCat, currentRow, level + 1);
//
//				// 子分类合并
//				if (currentRow - subStartRow > 1) {
//					CellRangeAddress subRegion = new CellRangeAddress(subStartRow, currentRow - 1, level + 1,
//							level + 1);
//
//					if (isRegionAvailable(sheet, subRegion)) {
//						sheet.addMergedRegion(subRegion);
//					}
//				}
//			}
//		}
		// 处理股票数据

		for (int j = 1; j < stocks.size(); j++) {
			StockDataHistory stock = stocks.get(j);
			Date tm = stock.getTradeDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(tm);
			int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if (week == 0)
				week = 7;
			SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
			String dtStr = sdf.format(tm);

			BigDecimal last = stocks.get(j - 1).getAmount();
			CellStyle style2 = workbook.createCellStyle();
			XSSFColor customRed = new XSSFColor(new java.awt.Color(254, 84, 73));
			((XSSFCellStyle) style2).setFillForegroundColor(customRed);
			style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			CellStyle style3 = workbook.createCellStyle();
			XSSFColor customGree = new XSSFColor(new java.awt.Color(144, 238, 144));
			((XSSFCellStyle) style3).setFillForegroundColor(customGree);
			style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			CellStyle style4 = workbook.createCellStyle();
			style4.setAlignment(HorizontalAlignment.CENTER);
			style4.setVerticalAlignment(VerticalAlignment.CENTER);

			for (int i = 0; i <= 6; i++) {
				Row row = sheet.getRow(currentRow + i);
				if (i == 0) {

					Cell cell = row.createCell(2 + j);
					cell.setCellValue(subjectString);
					cell.setCellStyle(style4);

				} else if (i == 2)
					row.createCell(2 + j).setCellValue("周" + (week == 7 ? "日" : week));
				else if (i == 3)
					row.createCell(2 + j).setCellValue(dtStr);
				else if (i == 4)
					row.createCell(2 + j).setCellValue(stock.getAmount().compareTo(last) > 0 ? "放" : "缩");
				else if (i == 5) {
					Cell cell = row.createCell(2 + j);
					cell.setCellValue(stock.getClose().compareTo(stock.getOpen()) >= 0 ? "红" : "绿");
					if (stock.getClose().compareTo(stock.getOpen()) >= 0) {
						// 方法1：使用预定义红色
//						CellStyle style1 = workbook.createCellStyle();
//						style1.setFillForegroundColor(IndexedColors.RED1.getIndex());
//						style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						cell.setCellStyle(style2);

					} else {
						// 方法1：使用预定义红色
//						CellStyle style1 = workbook.createCellStyle();
//						style1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//						style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						cell.setCellStyle(style3);
					}

				} else if (i == 6) {
					Cell cell = row.createCell(2 + j);
					cell.setCellValue(stock.getPctChg() + "%");

					if (stock.getPctChg().compareTo(new BigDecimal("0")) >= 0) {
						// 方法1：使用预定义红色
//						CellStyle style1 = workbook.createCellStyle();
//						style1.setFillForegroundColor(IndexedColors.RED1.getIndex());
//						style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						cell.setCellStyle(style2);

					} else {
						// 方法1：使用预定义红色
//						CellStyle style1 = workbook.createCellStyle();
//						style1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//						style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						cell.setCellStyle(style3);
					}

				}

//				sheet.autoSizeColumn(2 + j);
//				sheet.autoSizeColumn(level + 3);
//				sheet.autoSizeColumn(level + 4);
			}

		}

		return currentRow;
	}

	private static boolean isRegionAvailable(Sheet sheet, CellRangeAddress newRegion) {
		return sheet.getMergedRegions().stream()
				.noneMatch(region -> region.getFirstRow() == newRegion.getFirstRow()
						&& region.getLastRow() == newRegion.getLastRow()
						&& region.getFirstColumn() == newRegion.getFirstColumn());
	}

	private static int countStocks(Category category) {
		if (!category.getStocks().isEmpty()) {
			return category.getStocks().size();
		}
		return category.getChildren().stream().mapToInt(DynamicExcelFupan::countStocks).sum();
	}

	private static void saveWorkbook(Workbook workbook, String filename) throws Exception {
		try (FileOutputStream fos = new FileOutputStream(filename)) {
			workbook.write(fos);

		}
		workbook.close();
	}
}

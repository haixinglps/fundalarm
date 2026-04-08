package cn.exrick.manager.service.stocks;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.exrick.manager.pojo.Category;
import cn.exrick.manager.pojo.Stock;
import cn.exrick.manager.service.StockService;

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

public class DynamicExcelGenerator {

	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");

	public static void main(String[] args) throws Exception {
		// 测试数据构建

		String sidString = "9047098";
		String nameString = "可回收火箭";

		String filePath = "d:/download/linyuanall.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String currentUrl;
			while ((currentUrl = br.readLine()) != null) {
				String sid = currentUrl.split("\t")[0];// .replace("userId�", "userId");
				nameString = currentUrl.split("\t")[1];
				System.out.println(nameString + "\t" + sid);
				Category root = null;
				try {
					root = buildTestData(sid);
				} catch (Exception e) {

					e.printStackTrace();
					// TODO: handle exception
				}

				System.out.println(root);

				if (root == null)
					continue;

				// 创建工作簿
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("多级分类表");
				setupColumnWidths(sheet, new int[] { 20, 15, 15, 30 });

				// 创建表头
				createHeaderRow(sheet, new String[] { "概念", "类型", "子分类", "股票" });

				// 填充数据
				fillData(sheet, root, 1, 0);
				BorderUtils.addBorderToSheet(sheet, workbook);

				// 保存文件
				saveWorkbook(workbook, "d:\\download\\" + nameString + ".xlsx");

			}
		} catch (IOException e) {
			System.err.println("监控文件出错: " + e.getMessage());
		}

	}

	private static Category buildTestData(String sid) {
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

		StockService service = context.getBean(StockService.class);

		Category category = service.getCategory(sid);

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
		return category.getChildren().stream().mapToInt(DynamicExcelGenerator::countStocks).sum();
	}

	private static void saveWorkbook(Workbook workbook, String filename) throws Exception {
		try (FileOutputStream fos = new FileOutputStream(filename)) {
			workbook.write(fos);

		}
		workbook.close();
	}
}

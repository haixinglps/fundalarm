package cn.exrick.manager.service.stocks;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 优化版股票历史数据抓取 - 多线程并发
 */
public class StockHistoryFast {
    
    private static final String TOKEN = loadToken();
    
    private static String loadToken() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("/home/www/stock/stock_config.properties"));
            return props.getProperty("api.token", "");
        } catch (IOException e) {
            System.err.println("加载配置文件失败: " + e.getMessage());
            return "";
        }
    }
    private static final String URL_TEMPLATE = "https://app.txcfgl.com/api/app/data/one-stock-daily?stockId=%s&days=650&platform=app&searchStockType=one";
    private static final int THREAD_COUNT = 30;  // 并发线程数
    private static final int TIMEOUT_MS = 30000; // 超时时间
    
    private final String inputFile;
    private final String outputFile;
    private final String errorFile;
    private final int limitDays;
    
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failCount = new AtomicInteger(0);
    private final AtomicLong startTime = new AtomicLong(0);
    
    public StockHistoryFast(String inputFile, String outputFile, String errorFile, int limitDays) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.errorFile = errorFile;
        this.limitDays = limitDays;
    }
    
    public void run() throws Exception {
        List<String> stockCodes = Files.readAllLines(Paths.get(inputFile));
        System.out.println("共需处理 " + stockCodes.size() + " 只股票");
        System.out.println("并发线程数: " + THREAD_COUNT);
        System.out.println("开始时间: " + java.time.LocalDateTime.now());
        System.out.println("========================================");
        
        startTime.set(System.currentTimeMillis());
        
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);
        
        // 提交任务
        for (String stockCode : stockCodes) {
            final String code = stockCode.trim();
            if (code.isEmpty()) continue;
            
            completionService.submit(() -> {
                fetchStockHistory(code);
                return null;
            });
        }
        
        // 等待所有任务完成并显示进度
        int total = stockCodes.size();
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile, true));
        PrintWriter errorWriter = new PrintWriter(new FileWriter(errorFile, true));
        
        try {
            for (int i = 0; i < total; i++) {
                try {
                    Future<Void> future = completionService.poll(60, TimeUnit.SECONDS);
                    if (future != null) {
                        future.get();
                    }
                } catch (Exception e) {
                    System.err.println("任务执行异常: " + e.getMessage());
                }
                
                // 每50个显示一次进度
                if ((i + 1) % 50 == 0 || i == total - 1) {
                    showProgress(i + 1, total);
                }
            }
        } finally {
            outputWriter.close();
            errorWriter.close();
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);
        }
        
        long elapsed = System.currentTimeMillis() - startTime.get();
        System.out.println("\n========================================");
        System.out.println("完成时间: " + java.time.LocalDateTime.now());
        System.out.println("总耗时: " + formatTime(elapsed));
        System.out.println("成功: " + successCount.get() + ", 失败: " + failCount.get());
        System.out.println("平均速度: " + String.format("%.2f", total * 1000.0 / elapsed) + " 只/秒");
    }
    
    private void fetchStockHistory(String stockCode) {
        String url = String.format(URL_TEMPLATE, stockCode);
        int retryCount = 0;
        int maxRetries = 2;
        
        while (retryCount <= maxRetries) {
            try {
                String docJson = Jsoup.connect(url)
                        .userAgent("Dart/3.4 (dart:io)")
                        .header("appplatformbrand", "xiaomi")
                        .header("appversion", "10403")
                        .header("appplatform", "ANDROID")
                        .header("accept-encoding", "gzip")
                        .header("host", "app.txcfgl.com")
                        .header("authorization", TOKEN)
                        .timeout(TIMEOUT_MS)
                        .ignoreContentType(true)
                        .execute()
                        .body();
                
                JSONObject object = new JSONObject(docJson);
                JSONObject data = object.getJSONObject("data");
                if (data == null) {
                    System.err.println(stockCode + " - 无数据");
                    writeError(stockCode);
                    return;
                }
                
                JSONArray links = data.getJSONArray("items");
                if (links == null || links.isEmpty()) {
                    System.err.println(stockCode + " - 空数据");
                    writeError(stockCode);
                    return;
                }
                
                // 写入数据
                synchronized (this) {
                    writeHistoryData(links, stockCode);
                }
                
                successCount.incrementAndGet();
                return; // 成功，跳出重试循环
                
            } catch (Exception e) {
                retryCount++;
                if (retryCount > maxRetries) {
                    System.err.println(stockCode + " - 失败: " + e.getMessage());
                    writeError(stockCode);
                    failCount.incrementAndGet();
                } else {
                    // 重试前等待一下
                    try {
                        Thread.sleep(100 * retryCount);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }
    
    private void writeHistoryData(JSONArray items, String stockCode) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
            int limit = limitDays > 0 ? Math.min(limitDays, items.size()) : items.size();
            
            for (int i = 0; i < limit; i++) {
                JSONArray item = items.getJSONArray(i);
                StringBuilder line = new StringBuilder();
                
                for (int j = 0; j < item.size(); j++) {
                    if (j > 0) line.append("\t");
                    line.append(item.get(j));
                }
                line.append("\t1");
                
                writer.write(line.toString());
                writer.newLine();
            }
        }
    }
    
    private void writeError(String stockCode) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(errorFile, true))) {
            writer.println(stockCode);
        } catch (IOException e) {
            System.err.println("写入错误文件失败: " + e.getMessage());
        }
    }
    
    private void showProgress(int current, int total) {
        long elapsed = System.currentTimeMillis() - startTime.get();
        double percent = current * 100.0 / total;
        double speed = current * 1000.0 / elapsed; // 只/秒
        int remaining = (int) ((total - current) / speed); // 预估剩余秒数
        
        System.out.printf("\r进度: %d/%d (%.1f%%) | 速度: %.1f只/秒 | 剩余: %s | 成功:%d 失败:%d",
                current, total, percent, speed, formatTime(remaining * 1000L), 
                successCount.get(), failCount.get());
        System.out.flush();
    }
    
    private String formatTime(long ms) {
        long seconds = ms / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%d时%02d分%02d秒", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%d分%02d秒", minutes, seconds % 60);
        } else {
            return String.format("%d秒", seconds);
        }
    }
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("用法: java StockHistoryFast <days> [input_file] [output_file] [error_file]");
            System.out.println("  days: 获取多少天的数据 (0表示全部)");
            System.out.println("  input_file: 默认 /home/www/stock/ts/err.txt");
            System.out.println("  output_file: 默认 /home/www/stock/allstockhistory.txt");
            System.out.println("  error_file: 默认 /home/www/stock/err.txt");
            System.exit(1);
        }
        
        int days = Integer.parseInt(args[0]);
        String inputFile = args.length > 1 ? args[1] : "/home/www/stock/ts/err.txt";
        String outputFile = args.length > 2 ? args[2] : "/home/www/stock/allstockhistory.txt";
        String errorFile = args.length > 3 ? args[3] : "/home/www/stock/err.txt";
        
        try {
            // 清理旧文件
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(outputFile));
            
            StockHistoryFast fetcher = new StockHistoryFast(inputFile, outputFile, errorFile, days);
            fetcher.run();
        } catch (Exception e) {
            System.err.println("执行失败: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

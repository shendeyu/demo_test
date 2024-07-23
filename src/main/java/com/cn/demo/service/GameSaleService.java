package com.cn.demo.service;

import com.cn.demo.dao.GameSalesRepository;
import com.cn.demo.dto.GameSale;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class GameSaleService {

    @Autowired
    private GameSalesRepository gameSalesRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = Logger.getLogger(GameSaleService.class.getName());

    @Async
    public void importCSV(MultipartFile file) throws IOException {
        // 将文件保存到临时目录
        Path tempFilePath = Files.createTempFile(null, ".tmp");
        Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

        String fileName = file.getOriginalFilename();
        long importId = createImportRecord(fileName);

        BufferedReader fileReader = Files.newBufferedReader(tempFilePath);
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        List<GameSale> gameSalesList = new ArrayList<>();
        int batchSize = 1000;
        int totalRows = 0;
        int successRows = 0;
        int errorRows = 0;

        try {
            for (CSVRecord record : csvParser) {
                totalRows++;
                try {
                    //参数校验
                    if (!validate(record)) {
                        throw new Exception("error parameter");
                    }
                    GameSale gameSales = new GameSale();
                    gameSales.setGameNo(Integer.parseInt(record.get("game_no")));
                    gameSales.setGameName(record.get("game_name"));
                    gameSales.setGameCode(record.get("game_code"));
                    gameSales.setType(Integer.parseInt(record.get("type")));
                    gameSales.setCostPrice(new BigDecimal(record.get("cost_price")));
                    gameSales.setTax(new BigDecimal(record.get("tax")));
                    gameSales.setSalePrice(gameSales.getCostPrice().add(gameSales.getCostPrice().multiply(gameSales.getTax())));
                    // 解析毫秒级别的时间戳
                    long timestamp = Long.parseLong(record.get("date_of_sale"));
                    LocalDateTime dateOfSale = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
                    gameSales.setDateOfSale(dateOfSale);
                    gameSalesList.add(gameSales);
                    if (gameSalesList.size() == batchSize) {
                        batchInsert(gameSalesList);
                        successRows += gameSalesList.size();
                        gameSalesList.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorRows++;
                    logImportError(importId, totalRows, e.getMessage());
                }
            }
            if (!gameSalesList.isEmpty()) {
                batchInsert(gameSalesList);
                successRows += gameSalesList.size();
            }
            updateImportRecord(importId, "COMPLETED", totalRows, successRows, errorRows);
        } catch (Exception e) {
            e.printStackTrace();
            updateImportRecord(importId, "FAILED", totalRows, successRows, errorRows);
            throw new RuntimeException("Failed to import CSV file: " + fileName, e);
        } finally {
            csvParser.close();
            fileReader.close();
            // 删除临时文件
            Files.deleteIfExists(tempFilePath);
        }

    }

    private void batchInsert(List<GameSale> gameSalesList) {
        String sql = "INSERT INTO game_sales (game_no, game_name, game_code, type, cost_price, tax, sale_price, date_of_sale) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (GameSale gameSale : gameSalesList) {
            Object[] values = {
                    gameSale.getGameNo(),
                    gameSale.getGameName(),
                    gameSale.getGameCode(),
                    gameSale.getType(),
                    gameSale.getCostPrice(),
                    gameSale.getTax(),
                    gameSale.getSalePrice(),
                    Timestamp.valueOf(gameSale.getDateOfSale())
            };
            batchArgs.add(values);
        }
        LOGGER.info("Executing SQL: " + sql);
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private long createImportRecord(String fileName) {
        String sql = "INSERT INTO csv_imports (file_name, status) VALUES (?, 'IN_PROGRESS')";
        jdbcTemplate.update(sql, fileName);
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    private void updateImportRecord(long importId, String status, int totalRows, int successRows, int errorRows) {
        String sql = "UPDATE csv_imports SET status = ?, total_rows = ?, success_rows = ?, error_rows = ?, end_time = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, status, totalRows, successRows, errorRows, importId);
    }

    private void logImportError(long importId, int rowNumber, String errorMessage) {
        String sql = "INSERT INTO csv_import_errors (import_id, row_number, error_message) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, importId, rowNumber, errorMessage);
    }

    private boolean validate(CSVRecord record) {
        int gameNo = Integer.parseInt(record.get("game_no"));
        String gameName = record.get("game_name");
        String gameCode = record.get("game_code");
        int type = Integer.parseInt(record.get("type"));
        BigDecimal costPrice = new BigDecimal(record.get("cost_price"));
        if (gameNo < 1 || gameNo > 100) return false;
        if (gameName.length() > 20) return false;
        if (gameCode.length() > 5) return false;
        if (type != 1 && type != 2) return false;
        if (costPrice.compareTo(new BigDecimal(100)) > 0) {
            return false;
        }
        return true;
    }


    public List<GameSale> getGameSales(LocalDateTime startDate, LocalDateTime endDate,
                                        BigDecimal minSalePrice, BigDecimal maxSalePrice,
                                        int page, int size) {
        int offset = page * size;
        return gameSalesRepository.findGameSales(startDate, endDate, minSalePrice, maxSalePrice, offset, size);
    }

    public Map<String, Object> getTotalSales(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> map = new HashMap<>();
        int totalGamesSold = gameSalesRepository.countGamesSold(startDate, endDate);
        BigDecimal totalSales = gameSalesRepository.calculateTotalSales(startDate, endDate);
        map.put("totalGamesSold", totalGamesSold);
        map.put("totalSales", totalSales);
        return map;
    }

    public BigDecimal getTotalSalesByGameNo(LocalDateTime startDate, LocalDateTime endDate, Integer gameNo) {
        return gameSalesRepository.calculateTotalSalesByGameNo(startDate, endDate, gameNo);
    }


}

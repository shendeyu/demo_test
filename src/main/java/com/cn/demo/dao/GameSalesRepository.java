package com.cn.demo.dao;


import com.cn.demo.dto.GameSale;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GameSalesRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameSalesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<GameSale> ROW_MAPPER = (rs, rowNum) -> {
        GameSale gameSales = new GameSale();
        gameSales.setId(rs.getLong("id"));
        gameSales.setGameNo(rs.getInt("game_no"));
        gameSales.setGameName(rs.getString("game_name"));
        gameSales.setGameCode(rs.getString("game_code"));
        gameSales.setType(rs.getInt("type"));
        gameSales.setCostPrice(rs.getBigDecimal("cost_price"));
        gameSales.setTax(rs.getBigDecimal("tax"));
        gameSales.setSalePrice(rs.getBigDecimal("sale_price"));
        gameSales.setDateOfSale(rs.getTimestamp("date_of_sale").toLocalDateTime());
        return gameSales;
    };

    public List<GameSale> findGameSales(LocalDateTime startDate, LocalDateTime endDate,
                                        BigDecimal minSalePrice, BigDecimal maxSalePrice,
                                        int offset, int limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM game_sales WHERE 1=1");
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        if (startDate != null) {
            sql.append(" AND date_of_sale >= ?");
            map.put("startDate", Timestamp.valueOf(startDate));
        }
        if (endDate != null) {
            sql.append(" AND date_of_sale <= ?");
            map.put("endDate", Timestamp.valueOf(endDate));
        }
        if (minSalePrice != null) {
            sql.append(" AND sale_price < ?");
            map.put("minSalePrice", minSalePrice);
        }
        if (maxSalePrice != null) {
            sql.append(" AND sale_price > ?");
            map.put("maxSalePrice", maxSalePrice);
        }

        sql.append(" ORDER BY date_of_sale DESC LIMIT ? OFFSET ?");
        map.put("LIMIT", limit);
        map.put("OFFSET", offset);
        Object[] parm = new Object[map.size()];
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        int i=0;
        while(entries.hasNext()){
            Map.Entry<String, Object> entry = entries.next();
            parm[i] = entry.getValue();
            i++;
        }

        return jdbcTemplate.query(sql.toString(), ROW_MAPPER, parm);
    }


    public int countGamesSold(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM game_sales WHERE date_of_sale BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
    }

    public BigDecimal calculateTotalSales(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT SUM(sale_price) FROM game_sales WHERE date_of_sale BETWEEN ? AND ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
    }

    public BigDecimal calculateTotalSalesByGameNo(LocalDateTime startDate, LocalDateTime endDate, Integer gameNo) {
        String sql = "SELECT SUM(sale_price) FROM game_sales WHERE date_of_sale BETWEEN ? AND ? AND game_no = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), gameNo);
    }



}

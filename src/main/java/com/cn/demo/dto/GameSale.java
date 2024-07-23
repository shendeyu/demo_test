package com.cn.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GameSale {
    private Long id;
    private int gameNo;
    private String gameName;
    private String gameCode;
    private int type;
    private BigDecimal costPrice;
    private BigDecimal tax;
    private BigDecimal salePrice;
    private LocalDateTime dateOfSale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGameNo() {
        return gameNo;
    }

    public void setGameNo(int gameNo) {
        this.gameNo = gameNo;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public LocalDateTime getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(LocalDateTime dateOfSale) {
        this.dateOfSale = dateOfSale;
    }
}

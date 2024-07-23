package com.cn.demo.controller;

import com.cn.demo.dto.GameSale;
import com.cn.demo.dto.RespMsg;
import com.cn.demo.service.GameSaleService;
import com.cn.demo.utils.DateUtils;
import com.cn.demo.utils.ReturnCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RestController
public class DemoController {
    @Autowired
    private GameSaleService gameSaleService;


    @RequestMapping("/import")
    public RespMsg<?> importCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return RespMsg.error(ReturnCodeEnum.RETURN_ERROR_TYPE.getCode(), ReturnCodeEnum.RETURN_ERROR_TYPE.getMsg());
        }
        try {
            gameSaleService.importCSV(file);
            return RespMsg.success(ReturnCodeEnum.RETURN_SUCCESS.getMsg());
        } catch (IOException e) {
            return RespMsg.error(ReturnCodeEnum.RETURN_ERROR_UPLOAD.getCode(), ReturnCodeEnum.RETURN_ERROR_UPLOAD.getMsg());
        } catch (Exception e) {
            return RespMsg.error(ReturnCodeEnum.RETURN_OTHER_ERROR.getCode(), ReturnCodeEnum.RETURN_OTHER_ERROR.getMsg());
        }
    }

    @GetMapping("/getGameSales")
    public RespMsg<List<GameSale>> getGameSales(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) BigDecimal minSalePrice,
            @RequestParam(required = false) BigDecimal maxSalePrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        LocalDateTime start = DateUtils.getLocalDateTime(startDate);
        LocalDateTime end = DateUtils.getLocalDateTime(endDate);

        return new RespMsg<List<GameSale>>().success(ReturnCodeEnum.RETURN_SUCCESS.getMsg(),
                gameSaleService.getGameSales(start, end, minSalePrice, maxSalePrice, page, size));
    }

    @GetMapping("/getTotalSales")
    public RespMsg<?> getTotalSales(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer gameNo) {
        LocalDateTime start = DateUtils.getLocalDateTime(startDate);
        LocalDateTime end = DateUtils.getLocalDateTime(endDate);

        if (gameNo != null) {
            return new RespMsg<>().success(ReturnCodeEnum.RETURN_SUCCESS.getMsg(), gameSaleService.getTotalSalesByGameNo(start, end, gameNo));
        } else if (startDate != null && endDate != null) {
            return new RespMsg<>().success(ReturnCodeEnum.RETURN_SUCCESS.getMsg(), gameSaleService.getTotalSales(start, end));
        } else {
            return RespMsg.error(ReturnCodeEnum.RETURN_INVALID_PARAM.getCode(), ReturnCodeEnum.RETURN_INVALID_PARAM.getMsg());
        }
    }









}

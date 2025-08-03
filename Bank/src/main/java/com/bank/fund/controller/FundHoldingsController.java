package com.bank.fund.controller;

import com.bank.fund.entity.FundAccount;
import com.bank.fund.entity.FundHoldings;
import com.bank.fund.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fund-holdings")
public class FundHoldingsController {

    @Autowired
    private FundHoldingsService fundHoldingsService;

    @PostMapping
    public boolean insert(@RequestBody FundHoldings bean) {
        return fundHoldingsService.insert(bean);
    }

    @PutMapping
    public boolean update(@RequestBody FundHoldings bean) {
        return fundHoldingsService.update(bean);
    }

    @DeleteMapping
    public boolean delete(@RequestBody FundHoldings id) {
        return fundHoldingsService.delete(id);
    }
}

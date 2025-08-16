package com.bank.fund.controller;

import com.bank.fund.entity.FundHoldings;
import com.bank.fund.service.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/fund-holdings")
public class FundHoldingsController {

    @Autowired
    private FundHoldingsService fundHoldingsService;
    
    @GetMapping
    public ResponseEntity<List<FundHoldings>> getFundHoldingsByFundAccId(@RequestParam Integer fundAccId){
    	return ResponseEntity.ok(fundHoldingsService.getByFundAccId(fundAccId));
    }

    @PostMapping
    public FundHoldings insert(@RequestBody FundHoldings fundHoldings) {
        return fundHoldingsService.create(fundHoldings);
    }

    @PutMapping("/{id}")
    public FundHoldings update(@PathVariable Integer id, @RequestBody FundHoldings fundHoldings) {
        return fundHoldingsService.update(id, fundHoldings);
    }

}

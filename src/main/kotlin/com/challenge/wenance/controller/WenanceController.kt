package com.challenge.wenance.controller

import com.challenge.wenance.core.WenanceService
import com.challenge.wenance.dto.SearchRequest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/btcars")
class WenanceController(private var wenanceService: WenanceService) {

    @RequestMapping(value = ["/getPrice/{date}"], method = [(RequestMethod.GET)])
    fun getPrice(
        @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") date: Date
    ) = wenanceService.getBtcPrice(date) ?:
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "There are no stored prices")

    @RequestMapping(value = ["/searchPrices"], method = [(RequestMethod.POST)])
    fun searchPrices(
        @RequestBody searchRequest: SearchRequest
    ) = wenanceService.getBtcPrices(searchRequest)

    @RequestMapping(value = ["/averagePrice"], method = [(RequestMethod.POST)])
    fun averagePrice(
        @RequestBody searchRequest: SearchRequest
    ) = wenanceService.getPriceAverage(searchRequest)?:
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "There are no stored prices in giving dates")

}
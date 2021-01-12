package com.challenge.wenance.core

import com.challenge.wenance.dto.SearchRequest
import com.challenge.wenance.dto.SearchResponse
import com.challenge.wenance.model.Price
import java.util.*

interface WenanceService {
    fun saveMarketData()
    fun getBtcPrice(date: Date): Price?
    fun getBtcPrices(searchRequest: SearchRequest): SearchResponse
    fun getPriceAverage(searchRequest: SearchRequest): Price?
}
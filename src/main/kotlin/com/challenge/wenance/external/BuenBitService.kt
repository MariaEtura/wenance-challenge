package com.challenge.wenance.external

import com.challenge.wenance.model.MarketTicker

interface BuenBitService {
    fun getMarketTickers(): MarketTicker?
}
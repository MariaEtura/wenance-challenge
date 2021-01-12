package com.challenge.wenance.external

import com.challenge.wenance.model.MarketTicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class BuenBitServiceImpl: BuenBitService {

    @Autowired
    lateinit var restTemplate: RestTemplate

    override fun getMarketTickers(): MarketTicker? {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        headers.add("user-agent", "Application")
        val entity = HttpEntity<String>(headers)
        return restTemplate.exchange("https://be.buenbit.com/api/market/tickers/", HttpMethod.GET, entity, MarketTicker::class.java).body
    }
}
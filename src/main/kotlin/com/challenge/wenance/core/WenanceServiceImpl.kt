package com.challenge.wenance.core

import com.challenge.wenance.dto.SearchRequest
import com.challenge.wenance.dto.SearchResponse
import com.challenge.wenance.external.BuenBitService
import com.challenge.wenance.model.Price
import com.challenge.wenance.model.PriceRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Component
class WenanceServiceImpl(private val buenBitService: BuenBitService,
                         private val repository: PriceRepository) : WenanceService {

    private val sizeDefault = 10
    private val pageDefault = 0

    override fun saveMarketData() {
        val response = buenBitService.getMarketTickers()
        response.let {
            val btcars = response!!.data.btcars
            val price = repository.findByPurchasePriceAndSellingPrice(btcars.purchasePrice, btcars.sellingPrice)
            if(!price.isPresent){
                repository.save(btcars)
            }
        }
    }

    override fun getBtcPrice(date: Date): Price? {
        val response = repository.findFirstByCreatedDateLessThanOrderByCreatedDateDesc(date)
        return if(response.isPresent){
            response.get()
        } else {
            null
        }
    }

    override fun getBtcPrices(searchRequest: SearchRequest): SearchResponse {
        var size = sizeDefault
        var page = pageDefault

        searchRequest.page?.let { page = searchRequest.page }
        searchRequest.size?.let { size = searchRequest.size }
        val pageable: PageRequest
        try {
            pageable = PageRequest.of(page, size)
        } catch (e: IllegalArgumentException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

        val response: Page<Price> = if(searchRequest.initDate != null && searchRequest.endDate != null)
            repository.findByCreatedDateBetween(searchRequest.initDate, searchRequest.endDate, pageable)
        else if (searchRequest.initDate != null)
            repository.findByCreatedDateGreaterThanEqual(searchRequest.initDate, pageable)
        else if(searchRequest.endDate != null)
            repository.findByCreatedDateLessThanEqual(searchRequest.endDate, pageable)
        else
            repository.findAll(pageable)

        return SearchResponse(response.number, response.numberOfElements, response.totalPages, response.content)
    }

    override fun getPriceAverage(searchRequest: SearchRequest): Price? {
        if (searchRequest.initDate == null || searchRequest.endDate == null)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter two dates to filter")
        return average(repository.findByCreatedDateBetween(searchRequest.initDate, searchRequest.endDate))
    }

    fun average(prices: List<Price>): Price? {
        var aveSelling = 0.0
        var avePurchase = 0.0
        prices.forEach{ price ->
            aveSelling = aveSelling.plus(price.sellingPrice)
            avePurchase = avePurchase.plus(price.purchasePrice)
        }

        return if(prices.isNotEmpty()){
            avePurchase = avePurchase.div(prices.size)
            aveSelling = aveSelling.div(prices.size)
            Price(avePurchase, aveSelling)
        } else {
            null
        }
    }

}
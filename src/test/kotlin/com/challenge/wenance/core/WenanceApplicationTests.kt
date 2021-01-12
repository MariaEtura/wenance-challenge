package com.challenge.wenance.core

import com.challenge.wenance.dto.SearchRequest
import com.challenge.wenance.external.BuenBitService
import com.challenge.wenance.model.MarketTicker
import com.challenge.wenance.model.MarketTickerData
import com.challenge.wenance.model.Price
import com.challenge.wenance.model.PriceRepository
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import java.time.Month
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class WenanceApplicationTests {

    @Autowired
    lateinit var wenanceService: WenanceService

    @LocalServerPort
    private val port = 0

    @Autowired
    lateinit var priceRepository: PriceRepository

    @MockBean
    lateinit var buenBitService: BuenBitService

    @Before
    fun setUp(){
        priceRepository.deleteAll()
        val testPrice1 = Price(null, "AR$", "btc", "ars", 5419600.0, 5609300.0, "btcars", LocalDateTime.of(2021, Month.JANUARY, 6, 21, 15))
        val testPrice2 = Price(null, "AR$", "btc", "ars", 5752800.0, 5954200.0, "btcars", LocalDateTime.of(2021, Month.JANUARY, 6, 22, 41))
        val testPrice3 = Price(null, "AR$", "btc", "ars", 6046000.0, 6257600.0, "btcars", LocalDateTime.of(2021, Month.JANUARY, 7, 16, 24))
        val testPrice4 = Price(null, "AR$", "btc", "ars", 6862768.44, 6929768.44, "btcars", LocalDateTime.of(2021, Month.JANUARY, 8, 13, 40))
        priceRepository.save(testPrice1)
        priceRepository.save(testPrice2)
        priceRepository.save(testPrice3)
        priceRepository.save(testPrice4)
    }

    @Test
    fun testGetBtcPriceInDate() {
        given().port(port).`when`().get("/btcars/getPrice/2021-01-06 22:00:00").then().statusCode(200).body(
            "purchase_price", equalTo(5419600.0f),
            "selling_price", equalTo(5609300.0f),
            "id", nullValue(),
            "createdDate", equalTo("2021-01-06 21:15:00")
        )

        given().port(port).`when`().get("/btcars/getPrice/2021-01-07 18:00:00").then().statusCode(200).body(
            "purchase_price", equalTo(6046000.0f),
            "selling_price", equalTo(6257600.0f),
            "id", nullValue(),
            "createdDate", equalTo("2021-01-07 16:24:00")
        )
    }

    @Test
    fun testGetBtcPriceInDateNotFound(){
        given().port(port).`when`().get("/btcars/getPrice/2021-01-05 22:00:00").then().statusCode(404)
    }

    @Test
    fun testSearchPricesWithPageNullAndSizeNull(){
        val searchRequest = SearchRequest(GregorianCalendar(2021,0,6, 0, 0).time, GregorianCalendar(2021,0,8, 20, 0).time, null, null)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/searchPrices").then().statusCode(200).body(
            "currentPage", equalTo(0),
            "totalPages", equalTo(1),
            "totalItems", equalTo(4),
            "prices.size", equalTo(4)
        )
    }

    @Test
    fun testSearchPricesWithPageNull(){
        val searchRequest = SearchRequest(GregorianCalendar(2021,0,6, 0, 0).time, GregorianCalendar(2021,0,8, 20, 0).time, null, 2)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/searchPrices").then().statusCode(200).body(
            "currentPage", equalTo(0),
            "totalPages", equalTo(2),
            "totalItems", equalTo(2),
            "prices.size", equalTo(2)
        )
    }

    @Test
    fun testSearchPricesWithSizeNull(){
        val searchRequest = SearchRequest(GregorianCalendar(2021,0,6, 0, 0).time, GregorianCalendar(2021,0,8, 20, 0).time, 1, null)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/searchPrices").then().statusCode(200).body(
            "currentPage", equalTo(1),
            "totalPages", equalTo(1),
            "totalItems", equalTo(0),
            "prices.size", equalTo(0)
        )
    }

    @Test
    fun testSearchPrices(){
        val searchRequest = SearchRequest(GregorianCalendar(2021,0,6, 0, 0).time, GregorianCalendar(2021,0,8, 20, 0).time, 2, 1)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/searchPrices").then().statusCode(200).body(
            "currentPage", equalTo(2),
            "totalPages", equalTo(4),
            "totalItems", equalTo(1),
            "prices.size", equalTo(1)
        )
    }

    @Test
    fun testSearchPricesWithPageLessThanOne(){
        val searchRequest = SearchRequest(null, null, 0, 0)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/searchPrices").then()
            .statusCode(400)
    }

    @Test
    fun testSearchPricesWithoutInitDate(){
        val searchRequest = SearchRequest(null, GregorianCalendar(2021,0,7, 17, 0).time, null, null)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/searchPrices").then().statusCode(200).body(
            "currentPage", equalTo(0),
            "totalPages", equalTo(1),
            "totalItems", equalTo(3),
            "prices.size", equalTo(3)
        )
    }

    @Test
    fun testSearchPricesWithoutEndDate(){
        val searchRequest = SearchRequest(GregorianCalendar(2021,0,7, 23, 0).time, null, null, null)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/searchPrices").then().statusCode(200).body(
            "currentPage", equalTo(0),
            "totalPages", equalTo(1),
            "totalItems", equalTo(1),
            "prices.size", equalTo(1)
        )
    }

    @Test
    fun testSearchPricesWithoutDates(){
        val searchRequest = SearchRequest(null, null, null, null)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/searchPrices").then().statusCode(200).body(
            "currentPage", equalTo(0),
            "totalPages", equalTo(1),
            "totalItems", equalTo(4),
            "prices.size", equalTo(4)
        )
    }

    @Test
    fun testPriceAverage(){
        val searchRequest = SearchRequest(GregorianCalendar(2021,0,6, 22, 0).time, GregorianCalendar(2021,0,8, 20, 0).time, null, null)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/averagePrice").then().statusCode(200).body(
            "purchase_price", equalTo(6454384.220000001f),
            "selling_price", equalTo(6593684.220000001f)
        )
    }

    @Test
    fun testPriceAverageErrorBadRequest(){
        val searchRequest = SearchRequest(null, null, null, null)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/averagePrice").then()
            .statusCode(400)
    }

    @Test
    fun testPriceAverageErrorNotFound(){
        val searchRequest = SearchRequest(GregorianCalendar(2022,0,7, 0, 0).time, GregorianCalendar(2022,0,8, 23, 59).time, null, null)

        given().port(port).body(searchRequest).contentType(ContentType.JSON).`when`().post("/btcars/averagePrice").then()
            .statusCode(404)

    }

    @Test
    fun testXScheduleSavePriceWhenPriceAlreadyExist(){
        Mockito.`when`(buenBitService.getMarketTickers()).thenReturn(getMarketTickers(6862768.44, 6929768.44))
        val originalSize = priceRepository.findAll().size
        wenanceService.saveMarketData()
        assertEquals(originalSize, priceRepository.findAll().size)
    }


    @Test
    fun testXScheduleSavePriceWhenPriceIsNew(){
        Mockito.`when`(buenBitService.getMarketTickers()).thenReturn(getMarketTickers(7862768.59, 7929768.59))
        val originalSize = priceRepository.findAll().size
        wenanceService.saveMarketData()
        assertEquals(originalSize + 1, priceRepository.findAll().size)
    }

    private fun getMarketTickers(purchasePrice: Double, sellingPrice: Double): MarketTicker {
        val price = Price(purchasePrice, sellingPrice)
        val marketTickerData = MarketTickerData(null, null, price)
        return MarketTicker(marketTickerData)
    }

}

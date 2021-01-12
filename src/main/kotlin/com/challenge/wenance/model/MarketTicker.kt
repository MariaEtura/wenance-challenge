package com.challenge.wenance.model

import com.fasterxml.jackson.annotation.*
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class MarketTicker(
    @JsonProperty("object")
    var data : MarketTickerData
)

data class MarketTickerData(
    var daiars : Price?,
    var daiusd : Price?,
    var btcars : Price
)

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document
data class Price(
    @Id
    @JsonIgnore
    val id: ObjectId? = ObjectId.get(),
    @JsonProperty("currency")
    var currency: String?,
    @JsonProperty("bid_currency")
    var bidCurrency: String?,
    @JsonProperty("ask_currency")
    var askCurrency: String?,
    @JsonProperty("purchase_price")
    var purchasePrice: Double,
    @JsonProperty("selling_price")
    var sellingPrice: Double,
    @JsonProperty("market_identifier")
    var marketIdentifier: String?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdDate: LocalDateTime? = LocalDateTime.now()){

    constructor(purchasePrice: Double,
                sellingPrice: Double) : this(null, null, null, null, purchasePrice, sellingPrice, null,  null) {
    }

}


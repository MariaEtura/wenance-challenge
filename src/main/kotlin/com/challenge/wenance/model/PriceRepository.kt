package com.challenge.wenance.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

import java.util.*

interface PriceRepository: MongoRepository<Price, String> {

    fun findByPurchasePriceAndSellingPrice(purchasePrice: Double, sellingPrice: Double): Optional<Price>

    fun findFirstByCreatedDateLessThanOrderByCreatedDateDesc(createdDate: Date): Optional<Price>

    fun findByCreatedDateBetween(initDate: Date, endDate: Date, pageable: Pageable): Page<Price>

    fun findByCreatedDateBetween(initDate: Date, endDate: Date): List<Price>

    fun findByCreatedDateLessThanEqual(endDate: Date, pageable: Pageable): Page<Price>

    fun findByCreatedDateGreaterThanEqual(initDate: Date, pageable: Pageable): Page<Price>
}
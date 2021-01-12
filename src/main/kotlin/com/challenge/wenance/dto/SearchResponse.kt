package com.challenge.wenance.dto

import com.challenge.wenance.model.Price

data class SearchResponse(
    val currentPage: Int?,
    val totalItems: Int?,
    val totalPages: Int?,
    val prices: List<Price>?)

package com.challenge.wenance.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class SearchRequest(
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")val initDate: Date?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")val endDate: Date?,
    val page: Int?, val size: Int?)

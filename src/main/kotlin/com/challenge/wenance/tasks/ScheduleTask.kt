package com.challenge.wenance.tasks

import com.challenge.wenance.core.WenanceService
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduleTask (private val wenanceService: WenanceService,
                    @Value("\${cron.enable}") private val enabled: Boolean) {


    @Scheduled(fixedRate = 10000)
    fun saveMarketTickers(){
        if(enabled){
            wenanceService.saveMarketData()
        }
    }

}
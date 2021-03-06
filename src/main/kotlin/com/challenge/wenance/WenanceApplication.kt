package com.challenge.wenance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class WenanceApplication

fun main(args: Array<String>) {
    runApplication<WenanceApplication>(*args)
}

package com.challenge.wenance.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.util.*

@Configuration
class Config {
    @Bean
    @Primary
    fun restTemplate(): RestTemplate? {
        val restTemplate = RestTemplate()
        val mappingJackson2HttpMessageConverter = MappingJackson2HttpMessageConverter()
        mappingJackson2HttpMessageConverter.supportedMediaTypes =
            Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML)
        restTemplate.messageConverters.add(mappingJackson2HttpMessageConverter)
        return restTemplate
    }

}
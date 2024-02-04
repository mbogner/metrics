package dev.mbo.metrics.api

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class IllegalArgumentExceptionAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler
    fun handle(exc: IllegalArgumentException): ResponseEntity<String> {
        log.error("exception caught", exc)
        return ResponseEntity.badRequest().body("bad request!!")
    }

}
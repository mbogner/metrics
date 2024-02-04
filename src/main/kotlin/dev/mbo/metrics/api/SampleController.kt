package dev.mbo.metrics.api

import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/sample"])
class SampleController {

    private val log = LoggerFactory.getLogger(javaClass)

    @Timed(value = "SampleController_getSomething")
    @GetMapping(
        path = ["", "/"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getSomething(@RequestParam(name = "delay", required = false) delay: Long = 0): ResponseEntity<Sample> {
        log.info("getSomething(delay={})", delay)
        sleep(delay)
        return ResponseEntity.ok(Sample("getSomething working"))
    }

    @Timed(value = "SampleController_postSomething")
    @PostMapping(
        path = ["", "/"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun postSomething(
        @RequestParam(name = "delay", required = false) delay: Long = 0,
        @RequestBody body: String
    ): ResponseEntity<Sample> {
        log.info("postSomething(delay={})", delay)
        sleep(delay)
        return ResponseEntity.ok(Sample("postSomething working"))
    }

    private fun sleep(delay: Long) {
        if (delay < 0 || delay > 10_000) throw IllegalArgumentException("delay out of bounds")
        Thread.sleep(delay)
    }

}
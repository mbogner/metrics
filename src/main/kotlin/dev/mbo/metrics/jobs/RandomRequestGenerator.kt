package dev.mbo.metrics.jobs

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class RandomRequestGenerator(
    @Value("\${server.port}")
    private val port: Int,
) {

    private val rnd = SecureRandom.getInstanceStrong()
    private val selfClient = RestClient.create("http://localhost:$port")
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 50, timeUnit = TimeUnit.MILLISECONDS)
    fun requestSampleGet() {
        val delay = nextDelay()
        log.debug("delay={}", delay)

        val response = selfClient.get()
            .uri("/sample?delay={delay}", delay)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus({ status -> !status.is2xxSuccessful },
                { rq, rs -> log.error("Error executing get request. rq={}, rs={}", rq, rs) })
            .body(String::class.java)

        log.info("response from get: {}", response)
    }

    @Scheduled(fixedDelay = 100, timeUnit = TimeUnit.MILLISECONDS)
    fun requestSamplePost() {
        val delay = nextDelay()
        log.debug("delay={}", delay)

        val response = selfClient.post()
            .uri("/sample?delay={delay}", delay)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(UUID.randomUUID().toString())
            .retrieve()
            .onStatus({ status -> !status.is2xxSuccessful },
                { rq, rs -> log.error("Error executing post request. rq={}, rs={}", rq, rs) })
            .body(String::class.java)

        log.info("response from post: {}", response)
    }

    @Scheduled(fixedDelay = 3000, timeUnit = TimeUnit.MILLISECONDS)
    fun badRequest() {
        val response = selfClient.get()
            .uri("/sample?delay={delay}", -1)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus({ status -> !status.is2xxSuccessful },
                { rq, rs -> log.error("intentional bad request. rq={}, rs={}", rq, rs) })
            .body(String::class.java)

        log.info("response from bad request: {}", response)
    }

    private fun nextDelay(): Int {
        return rnd.nextInt(0, 5_000)
    }

}
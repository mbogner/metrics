# Metrics

This is a test project to play with OTEL collector and metrics collection of a JVM application.

The docker compose file contains configurations for:

- postgres: admin:s3cr3t@127.0.0.1:5432/db
- postgres-exporter (for prometheus): http://127.0.0.1:9187
- mail (mailhog for alertmanager): http://127.0.0.1:8025
- alertmanager (for prometheus notifications): http://127.0.0.1:9093
- prometheus: http://127.0.0.1:9090
- jaeger: http://127.0.0.1:16686
- loki (log receiver): http://127.0.0.1:3100
- otel collector: http://localhost:55679/debug/tracez
- grafana: http://127.0.0.1:3000 (admin:s3cr3t)

All services in the compose file have a healthcheck configured.

For the instrumentation of applications the `otel.env` file can be used.
The env variables in it instruct the application to be instrumented by the otel java agent and to log to the configured
otel collector.
The required jar file `opentelemetry-javaagent.jar` in the root of this project is NOT included in this repository
and needs to be downloaded from https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases.

The sample under src is a simple Spring Boot 3.2 application defining a very simple API that gets accessed via a job
that sends randomly timed requests - including a bad request to also see how this behaves in the systems.
It further shows how to add custom `@Timed` metrics and how to integrate hibernate statistics into micrometer.

This is a basic setup to get all tools up and running. First tests also showed that they communicate with each other.
The usefulness has to be verified yet and some parts need to be tweaked.
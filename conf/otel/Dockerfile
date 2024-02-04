# https://github.com/open-telemetry/opentelemetry-collector-releases/blob/main/distributions/otelcol/Dockerfile
FROM otel/opentelemetry-collector-contrib:latest as base

# -------------------------------
# let's use a alpine base image and just take the required content from the original scratch image
FROM alpine:latest

# install nc for healthcheck
RUN apk add --no-cache netcat-openbsd

# same as in original -------------
ARG USER_UID=10001
USER ${USER_UID}

COPY --from=base /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/ca-certificates.crt
COPY --from=base --chmod=755 /otelcol-contrib /otelcol-contrib
COPY --from=base /etc/otelcol-contrib/config.yaml /etc/otelcol-contrib/config.yaml

ENTRYPOINT ["/otelcol-contrib"]
CMD ["--config", "/etc/otelcol-contrib/config.yaml"]
EXPOSE 4317 55678 55679
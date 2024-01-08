# docker build -t ayan/shutdown-hook:latest .
# docker run -e TIME_TO_CLEAN_SECONDS=3 ayan/shutdown-hook
FROM azul/zulu-openjdk-alpine:11 as packager
RUN { \
        java --version ; \
        echo "jlink version:" && \
        jlink --version ; \
    }
ENV JAVA_MINIMAL=/opt/jre
RUN jlink \
    --verbose \
    --add-modules \
        java.base \
    --compress 2 \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --output "$JAVA_MINIMAL"

FROM alpine
ENV JAVA_MINIMAL=/opt/jre
ENV PATH="$PATH:$JAVA_MINIMAL/bin"
COPY --from=packager "$JAVA_MINIMAL" "$JAVA_MINIMAL"
COPY "ShutdownHook.jar" "/app.jar"
ENV TIME_TO_CLEAN_SECONDS 7
CMD [ "-jar", "/app.jar" ]
ENTRYPOINT [ "java" ]

FROM gradle:8.1.1-jdk17 AS builder
COPY --chown=gradle:gradle dockerfilehelper/build.gradle.kts.docker /home/gradle/src/build.gradle.kts
COPY --chown=gradle:gradle gradle/ /home/gradle/src/gradle/
COPY --chown=gradle:gradle dockerfilehelper/src /home/gradle/src/src/
WORKDIR /home/gradle/src
RUN apt-get update
RUN apt-get install libcurl4-openssl-dev -y
RUN apt-get install libpq-dev -y
RUN gradle linkReleaseExecutableNative --no-daemon # to download deps

FROM builder AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle linkReleaseExecutableNative --no-daemon

FROM ubuntu
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/bin/native/releaseExecutable/native.kexe /app/native.kexe
RUN apt-get update
RUN apt-get install libcurl4-openssl-dev -y
RUN apt-get install libpq-dev -y
ENTRYPOINT ["/app/native.kexe"]
# TODO: Why can't I stop a kexe application?
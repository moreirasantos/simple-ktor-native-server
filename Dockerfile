FROM ubuntu
EXPOSE 8080:8080
RUN mkdir /app
COPY /build/bin/native/releaseExecutable/native.kexe /app/native.kexe
RUN apt-get update
RUN apt-get install libcurl4-openssl-dev -y
RUN apt-get install libpq-dev -y
ENTRYPOINT ["/app/native.kexe"]
# TODO: Why can't I stop a kexe application?
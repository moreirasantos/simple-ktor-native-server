# ktor-native

## Build and Deploy
I need to document what needs to be installed to build this, but in the end, a Dockerfile should be able to build this app.

## TODO
* Need to properly handle a graceful shutdown

Commands
```
docker kill knative
docker rm knative
gradle clean linkReleaseExecutableNative
docker build -t knative .
docker run -d --name knative -p 8080:8080 knative
docker logs -f knative
```


# Docker File helper
A skeleton Kotlin/Native project to force the [Dockerfile](Dockerfile) to download Konan, the Kotlin/Native compiler.

This way, you won't have to download every `docker build`. Now currently most of the time of building the container is to bootup `gradle`.

You don't need to do anything here, running the build command will automatically use this.

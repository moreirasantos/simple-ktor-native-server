val ktorVersion = "2.3.1"
val psql_driver_version = "0.0.7"
val kotlinx_serialization_version = "1.4.0"

plugins {
    val kotlinVersion = "1.8.21"
    val ktorVersion = "2.3.1"
    application
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("io.ktor.plugin") version ktorVersion
    id("app.cash.sqldelight") version "2.0.0-alpha04"
}

group = "me.miguel"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

sqldelight {
    database("NativePostgres") {
        dialect("app.softwork:postgres-native-sqldelight-dialect:$psql_driver_version")
        packageName = "com.nativeserver.sqldelight"
    }
    linkSqlite = false
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")
                implementation("io.ktor:ktor-server-cio:$ktorVersion")

                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

                implementation("app.softwork:postgres-native-sqldelight-driver:$psql_driver_version")

                // Fix for kotlinx serialization version sync bug
                // https://github.com/hfhbd/postgres-native-sqldelight/issues/100
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }

        }
        val nativeTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
            }
        }
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("mainkt") // + "Kt" // if main not inside class/object
}


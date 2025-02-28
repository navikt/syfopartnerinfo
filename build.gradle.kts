group = "no.nav.syfo"
version = "1.0.0"

val hikari = "5.1.0"
val jacksonDataType = "2.17.2"
val kluent = "1.73"
val ktor = "2.3.12"
val logback = "1.5.8"
val logstashEncoder = "7.4"
val micrometerRegistry = "1.12.8"
val mockk = "1.13.12"
val nimbusjosejwt = "9.41.1"
val ojdbc8 = "19.3.0.0"
val spek = "2.0.19"

plugins {
    kotlin("jvm") version "2.0.20"
    id("com.gradleup.shadow") version "8.3.2"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation("io.ktor:ktor-serialization-jackson:$ktor")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor")
    implementation("io.ktor:ktor-server-call-id:$ktor")
    implementation("io.ktor:ktor-server-netty:$ktor")
    implementation("io.ktor:ktor-server-status-pages:$ktor")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoder")

    // Metrics
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktor")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerRegistry")

    // (De-)serialization
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonDataType")

    // Database
    implementation("com.oracle.ojdbc:ojdbc8:$ojdbc8")
    implementation("com.zaxxer:HikariCP:$hikari")

    testImplementation("com.nimbusds:nimbus-jose-jwt:$nimbusjosejwt")
    testImplementation("io.ktor:ktor-server-tests:$ktor")
    testImplementation("io.mockk:mockk:$mockk")
    testImplementation("org.amshove.kluent:kluent:$kluent")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    jar {
        manifest.attributes["Main-Class"] = "no.nav.syfo.AppKt"
    }

    create("printVersion") {

        doLast {
            println(project.version)
        }
    }

    shadowJar {
        archiveBaseName.set("app")
        archiveClassifier.set("")
        archiveVersion.set("")
    }

    test {
        useJUnitPlatform {
            includeEngines("spek2")
        }
        testLogging {
            showStandardStreams = true
        }
    }
}

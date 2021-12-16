import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "no.nav.syfo"
version = "1.0.0"

object Versions {
    const val coroutines = "1.4.2"
    const val hikari = "3.4.5"
    const val jackson = "2.11.3"
    const val kluent = "1.68"
    const val ktor = "1.4.0"
    const val logback = "1.2.3"
    const val logstashEncoder = "6.3"
    const val mockk = "1.10.5"
    const val nimbusjosejwt = "9.15.2"
    const val ojdbc8 = "19.3.0.0"
    const val prometheus = "0.9.0"
    const val spek = "2.0.15"
    const val spekjunit = "1.1.5"

    const val javaxAnnotationApi = "1.3.2"
    const val javaxActivation = "1.1.1"
    const val javaxJaxwsApi = "2.2.1"
    const val jaxbApi = "2.4.0-b180830.0359"
    const val jaxbBasicAnt = "1.11.1"
    const val jaxbRuntime = "2.4.0-b180830.0438"
    const val jaxwsTools = "2.3.1"
}

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.jmailen.kotlinter") version "2.1.1"
    id("com.diffplug.gradle.spotless") version "3.24.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
    mavenCentral()
    maven(url = "https://dl.bintray.com/kotlin/ktor")
    maven(url = "https://dl.bintray.com/spekframework/spek-dev")
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "https://repo1.maven.org/maven2/")
    maven(url = "https://oss.sonatype.org/content/groups/staging/")

}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:${Versions.coroutines}")

    implementation ("io.ktor:ktor-auth:${Versions.ktor}")
    implementation ("io.ktor:ktor-auth-jwt:${Versions.ktor}")
    implementation ("io.ktor:ktor-jackson:${Versions.ktor}")
    implementation ("io.ktor:ktor-server-netty:${Versions.ktor}")

    // Logging
    implementation ("ch.qos.logback:logback-classic:${Versions.logback}")
    implementation ("net.logstash.logback:logstash-logback-encoder:${Versions.logstashEncoder}")

    // Metrics
    implementation("io.prometheus:simpleclient_hotspot:${Versions.prometheus}")
    implementation("io.prometheus:simpleclient_common:${Versions.prometheus}")

    // (De-)serialization
    implementation ("com.fasterxml.jackson.module:jackson-module-jaxb-annotations:${Versions.jackson}")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}")
    implementation ("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${Versions.jackson}")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}")

    // Database
    implementation ("com.oracle.ojdbc:ojdbc8:${Versions.ojdbc8}")
    implementation ("com.zaxxer:HikariCP:${Versions.hikari}")

    implementation("javax.activation:activation:${Versions.javaxActivation}")
    implementation("javax.annotation:javax.annotation-api:${Versions.javaxAnnotationApi}")
    implementation("javax.xml.ws:jaxws-api:${Versions.javaxJaxwsApi}")
    implementation("javax.xml.bind:jaxb-api:${Versions.jaxbApi}")
    implementation("org.glassfish.jaxb:jaxb-runtime:${Versions.jaxbRuntime}")
    implementation("com.sun.xml.ws:jaxws-tools:${Versions.jaxwsTools}") {
        exclude(group = "com.sun.xml.ws", module = "policy}")
    }

    testImplementation ("com.nimbusds:nimbus-jose-jwt:${Versions.nimbusjosejwt}")
    testImplementation ("io.ktor:ktor-server-test-host:${Versions.ktor}")
    testImplementation ("io.mockk:mockk:${Versions.mockk}")
    testImplementation ("org.amshove.kluent:kluent:${Versions.kluent}")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly ("org.spekframework.spek2:spek-runner-junit5:${Versions.spek}") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly ("org.jetbrains.spek:spek-junit-platform-engine:${Versions.spekjunit}")
}


tasks {
    withType<Jar> {
        manifest.attributes["Main-Class"] = "no.nav.syfo.BootstrapKt"
    }

    create("printVersion") {

        doLast {
            println(project.version)
        }
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
        testLogging {
            showStandardStreams = true
        }
    }

    "check" {
        dependsOn("formatKotlin")
    }
}

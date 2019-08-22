import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.ServiceFileTransformer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "no.nav.syfo"
version = "1.0.0"

val coroutinesVersion = "1.1.1"
val jacksonVersion = "2.9.7"
val kluentVersion = "1.39"
val ktorVersion = "1.2.2"
val logbackVersion = "1.2.3"
val logstashEncoderVersion = "5.1"
val prometheusVersion = "0.5.0"
val spekVersion = "2.0.5"
val commonsTextVersion = "1.4"
val smCommonVersion = "2019.08.08-03-52-c78281e2409af36f3ef07df4369fa29b0ea81a46"
val micrometerRegistryPrometheusVersion = "1.1.5"
val nimbusjosejwtVersion = "7.5.1"
val spekjunitVersion = "1.1.5"
val ojdbc8Version = "12.2.0.1"

plugins {
    java
    kotlin("jvm") version "1.3.41"
    id("org.jmailen.kotlinter") version "2.1.0"
    id("com.diffplug.gradle.spotless") version "3.24.0"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

repositories {
    maven(url = "https://dl.bintray.com/kotlin/ktor")
    maven(url = "https://dl.bintray.com/spekframework/spek-dev")
    maven(url = "https://kotlin.bintray.com/kotlinx")
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation ("io.ktor:ktor-metrics-micrometer:$ktorVersion")
    implementation ("io.micrometer:micrometer-registry-prometheus:$micrometerRegistryPrometheusVersion")

    implementation ("io.ktor:ktor-server-netty:$ktorVersion")
    implementation ("io.ktor:ktor-jackson:$ktorVersion")
    implementation ("io.ktor:ktor-auth:$ktorVersion")
    implementation ("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$ktorVersion")

    implementation ("ch.qos.logback:logback-classic:$logbackVersion")
    implementation ("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")

    implementation ("com.fasterxml.jackson.module:jackson-module-jaxb-annotations:$jacksonVersion")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation ("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation ("no.nav.syfo.sm:syfosm-common-ws:$smCommonVersion")
    implementation ("no.nav.syfo.sm:syfosm-common-networking:$smCommonVersion")

    implementation ("org.apache.commons:commons-text:$commonsTextVersion")

    runtime ("com.oracle:ojdbc8:$ojdbc8Version")

    testImplementation ("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation ("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testImplementation ("com.nimbusds:nimbus-jose-jwt:$nimbusjosejwtVersion")
    testImplementation ("io.ktor:ktor-server-test-host:$ktorVersion")

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly ("org.spekframework.spek2:spek-runner-junit5:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly ("org.jetbrains.spek:spek-junit-platform-engine:$spekjunitVersion")
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
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<ShadowJar> {
        transform(ServiceFileTransformer::class.java) {
            setPath("META-INF/cxf")
            include("bus-extensions.txt")
        }
    }

    withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
        testLogging {
            showStandardStreams = true
        }
    }
}

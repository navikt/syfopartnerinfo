package no.nav.syfo

import com.auth0.jwk.JwkProviderBuilder
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.nav.syfo.application.ApplicationState
import no.nav.syfo.application.apiModule
import no.nav.syfo.db.Database
import no.nav.syfo.services.PartnerInformasjonService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.concurrent.TimeUnit

val log: Logger = LoggerFactory.getLogger("no.nav.syfo.syfopartnerinfo")

fun main() {
    val environment = Environment()

    val jwkProvider = JwkProviderBuilder(URL(environment.azureOpenIdConfigJwksUri))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    val applicationState = ApplicationState()

    val database = Database(environment)

    val partnerInformasjonService = PartnerInformasjonService(database)

    val applicationEngineEnvironment = applicationEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        config = HoconApplicationConfig(ConfigFactory.load())
    }

    val server = embeddedServer(
        Netty,
        environment = applicationEngineEnvironment,
        configure = {
            connector {
                port = environment.applicationPort
            }
            connectionGroupSize = 8
            workerGroupSize = 8
            callGroupSize = 16
        },
        module = {
            apiModule(
                environment = environment,
                applicationState = applicationState,
                jwkProvider = jwkProvider,
                partnerInformasjonService = partnerInformasjonService
            )
            monitor.subscribe(ApplicationStarted) { application ->
                applicationState.ready = true
                application.environment.log.info("Application is ready, running Java VM ${Runtime.version()}")
                try {
                    listOf("151295").forEach {
                        val partnerIds = partnerInformasjonService.finnPartnerInformasjon(it)
                        application.environment.log.info(
                            "Partnerids for herId $it is ${partnerIds.map { it.partnerId }.joinToString(", ")}"
                        )
                    }
                } catch (exc: Exception) {
                    application.environment.log.error("Caught exception", exc)
                }
            }
        }
    )

    Runtime.getRuntime().addShutdownHook(
        Thread {
            server.stop(10, 10, TimeUnit.SECONDS)
        }
    )
    server.start(wait = true)
}

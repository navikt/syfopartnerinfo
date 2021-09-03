package no.nav.syfo

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import no.nav.syfo.application.*
import no.nav.syfo.db.Database
import no.nav.syfo.services.PartnerInformasjonService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.concurrent.TimeUnit

val log: Logger = LoggerFactory.getLogger("no.nav.syfo.syfopartnerinfo")

@KtorExperimentalAPI
fun main() {
    val environment = Environment()

    val jwkProviderV1 = JwkProviderBuilder(URL(environment.jwkKeysUrl))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    val jwkProviderV2 = JwkProviderBuilder(URL(environment.azureOpenIdConfigJwksUri))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    val applicationState = ApplicationState()

    val database = Database(environment)

    val partnerInformasjonService = PartnerInformasjonService(database, environment.databasePrefix)

    val applicationEngine = embeddedServer(Netty, environment.applicationPort) {
        apiModule(
            environment = environment,
            applicationState = applicationState,
            jwkProviderV1 = jwkProviderV1,
            jwkProviderV2 = jwkProviderV2,
            partnerInformasjonService = partnerInformasjonService
        )
    }
    val applicationServer = ApplicationServer(applicationEngine, applicationState)

    applicationServer.start()

    applicationState.ready = true
}

package no.nav.syfo

import com.auth0.jwk.JwkProviderBuilder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.util.KtorExperimentalAPI
import java.net.URL
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import no.nav.syfo.application.ApplicationServer
import no.nav.syfo.application.ApplicationState
import no.nav.syfo.application.createApplicationEngine
import no.nav.syfo.db.Database
import no.nav.syfo.services.PartnerInformasjonService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val objectMapper: ObjectMapper = ObjectMapper().apply {
    registerKotlinModule()
    registerModule(JavaTimeModule())
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
}

val log: Logger = LoggerFactory.getLogger("no.nav.syfo.behandlerElektroniskKommunikasjon")

@KtorExperimentalAPI
fun main() {
    val environment = Environment()

    val jwkProvider = JwkProviderBuilder(URL(environment.jwkKeysUrl))
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()

    val applicationState = ApplicationState()

    val database = Database(environment)

    val partnerInformasjonService = PartnerInformasjonService(database, environment.databasePrefix)

    val applicationEngine = createApplicationEngine(environment, applicationState, jwkProvider, partnerInformasjonService)
    val applicationServer = ApplicationServer(applicationEngine, applicationState)

    applicationServer.start()

    applicationState.ready = true
}

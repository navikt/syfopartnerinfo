package no.nav.syfo.application

import com.auth0.jwk.JwkProvider
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.routing.*
import no.nav.syfo.Environment
import no.nav.syfo.aksessering.api.registerBehandlerApi
import no.nav.syfo.application.api.*
import no.nav.syfo.application.authentication.JwtIssuerType
import no.nav.syfo.application.authentication.installJwtAuthenticationV1
import no.nav.syfo.services.PartnerInformasjonService
import org.slf4j.event.Level

const val API_BASE_PATH = "/api"

fun Application.apiModule(
    environment: Environment,
    applicationState: ApplicationState,
    jwkProviderV1: JwkProvider,
    partnerInformasjonService: PartnerInformasjonService
) {
    installJwtAuthenticationV1(
        environment = environment,
        jwkProvider = jwkProviderV1,
        jwtIssuerType = JwtIssuerType.INTERNAL_AZUREAD_VEILEDER_V1
    )

    installContentNegotiation()
    installCallId()
    installStatusPages()
    install(CallLogging) {
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith("/api") }
    }

    routing {
        registerNaisApi(applicationState)
        route(API_BASE_PATH) {
            authenticate(JwtIssuerType.INTERNAL_AZUREAD_VEILEDER_V1.name) {
                registerBehandlerApi(partnerInformasjonService)
            }
        }
    }
}

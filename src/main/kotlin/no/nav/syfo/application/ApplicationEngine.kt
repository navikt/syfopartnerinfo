package no.nav.syfo.application

import com.auth0.jwk.JwkProvider
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.routing.*
import no.nav.syfo.Environment
import no.nav.syfo.aksessering.api.registerBehandlerApiV2
import no.nav.syfo.application.api.*
import no.nav.syfo.application.authentication.JwtIssuerType
import no.nav.syfo.application.authentication.installJwtAuthentication
import no.nav.syfo.services.PartnerInformasjonService
import org.slf4j.event.Level

const val API_BASE_PATH = "/api"

fun Application.apiModule(
    environment: Environment,
    applicationState: ApplicationState,
    jwkProvider: JwkProvider,
    partnerInformasjonService: PartnerInformasjonService
) {
    installJwtAuthentication(
        acceptedAudienceList = listOf(environment.azureAppClientId),
        jwtIssuer = environment.azureOpenIdConfigIssuer,
        jwkProvider = jwkProvider,
    )
    installContentNegotiation()
    installCallId()
    installStatusPages()
    install(CallLogging) {
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith(API_BASE_PATH) }
    }

    routing {
        registerNaisApi(applicationState)
        route(API_BASE_PATH) {
            authenticate(JwtIssuerType.INTERNAL_AZUREAD_VEILEDER_V2.name) {
                registerBehandlerApiV2(partnerInformasjonService)
            }
        }
    }
}

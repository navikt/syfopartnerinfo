package no.nav.syfo.application

import com.auth0.jwk.JwkProvider
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.nav.syfo.Environment
import no.nav.syfo.aksessering.api.registerBehandlerApi
import no.nav.syfo.application.api.*
import no.nav.syfo.application.authentication.installJwtAuthentication
import no.nav.syfo.services.PartnerInformasjonService
import org.slf4j.event.Level

fun createApplicationEngine(
    env: Environment,
    applicationState: ApplicationState,
    jwkProvider: JwkProvider,
    partnerInformasjonService: PartnerInformasjonService
): ApplicationEngine =
    embeddedServer(Netty, env.applicationPort) {
        installJwtAuthentication(env, jwkProvider)

        installContentNegotiation()
        installCallId()
        installStatusPages()
        install(CallLogging) {
            level = Level.DEBUG
            filter { call -> call.request.path().startsWith("/api") }
        }

        routing {
            registerNaisApi(applicationState)
            route("/api") {
                authenticate {
                    registerBehandlerApi(partnerInformasjonService)
                }
            }
        }
    }

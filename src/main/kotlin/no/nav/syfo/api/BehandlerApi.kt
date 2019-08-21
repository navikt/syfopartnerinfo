package no.nav.syfo.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.syfo.log
import no.nav.syfo.services.BehandlerService

fun Route.registerBehandlerApi(behandlerService: BehandlerService) {
    get("/behandler") {
        val herid = call.request.header("herid") ?: run {
            call.respond(HttpStatusCode.BadRequest, "Mangler header `herid` med herid")
            log.warn("Mottatt kall som mangler header herid")
            return@get
        }

                call.respond("partnerInformasjon")
            }
}

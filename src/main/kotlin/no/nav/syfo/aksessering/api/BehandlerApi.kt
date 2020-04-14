package no.nav.syfo.aksessering.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import no.nav.syfo.log
import no.nav.syfo.services.PartnerInformasjon
import no.nav.syfo.services.PartnerInformasjonService

fun Route.registerBehandlerApi(partnerInformasjonService: PartnerInformasjonService) {
    route("/v1") {
        get("/behandler") {
            log.info("Recived call to /api/v1/behandler")
            val herid = call.request.queryParameters["herid"]

            if (herid.isNullOrEmpty()) {
                log.info("Mangler query parameters: herid")
                call.respond(HttpStatusCode.BadRequest)
            } else if (partnerInformasjonService.finnPartnerInformasjon(herid).isEmpty()) {
                log.info("Fant ingen partnerInformasjon for akutell herid")
                call.respond(emptyList<PartnerInformasjon>())
            } else {
                call.respond(partnerInformasjonService.finnPartnerInformasjon(herid))
            }
        }
    }
}

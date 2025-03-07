package no.nav.syfo.aksessering.api

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.syfo.log
import no.nav.syfo.services.PartnerInformasjon
import no.nav.syfo.services.PartnerInformasjonService

const val v2BasePath = "/v2"
const val behandlerV2BasePath = "/behandler"
const val behandlerV2QueryParamHerid = "herid"

fun Route.registerBehandlerApiV2(
    partnerInformasjonService: PartnerInformasjonService,
) {
    route(v2BasePath) {
        get(behandlerV2BasePath) {
            val herid = call.request.queryParameters[behandlerV2QueryParamHerid]
            if (herid.isNullOrEmpty()) {
                log.info("Mangler query parameters: herid")
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val partnerInfo = partnerInformasjonService.finnPartnerInformasjon(herid)
                if (partnerInfo.isEmpty()) {
                    log.info("Fant ingen partnerInformasjon for aktuell herid: $herid")
                    call.respond(emptyList<PartnerInformasjon>())
                } else {
                    call.respond(partnerInfo)
                }
            }
        }
    }
}

package no.nav.syfo.aksessering.api

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.syfo.log
import no.nav.syfo.services.PartnerInformasjon
import no.nav.syfo.services.PartnerInformasjonService

const val v1BasePath = "/v1"
const val behandlerV1BasePath = "/behandler"
const val behandlerV1QueryParamHerid = "herid"

fun Route.registerBehandlerApi(partnerInformasjonService: PartnerInformasjonService) {
    route(v1BasePath) {
        get(behandlerV1BasePath) {
            val herid = call.request.queryParameters[behandlerV1QueryParamHerid]

            when {
                herid.isNullOrEmpty() -> {
                    log.info("Mangler query parameters: herid")
                    call.respond(HttpStatusCode.BadRequest)
                }
                partnerInformasjonService.finnPartnerInformasjon(herid).isEmpty() -> {
                    log.info("Fant ingen partnerInformasjon for akutell herid")
                    call.respond(emptyList<PartnerInformasjon>())
                }
                else -> {
                    call.respond(partnerInformasjonService.finnPartnerInformasjon(herid))
                }
            }
        }
    }
}

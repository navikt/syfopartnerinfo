package no.nav.syfo.aksessering.api

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import no.nav.syfo.log
import no.nav.syfo.services.ElektroniskAbonoment
import no.nav.syfo.services.ElektroniskAbonomentService

fun Routing.registerBehandlerApi(elektroniskAbonomentService: ElektroniskAbonomentService) {
    route("/api/v1") {
        get("/behandler") {
            log.info("Recived call to /api/v1/behandler")
            val herid = call.request.queryParameters["herid"]

            if (herid.isNullOrEmpty()) {
                log.info("Mangler query parameters: fnr")
                call.respond(HttpStatusCode.BadRequest)
            } else if (elektroniskAbonomentService.finnParnterInformasjon(herid).isEmpty()) {
                log.info("Fant ingen elektroniskAbonoment for akutell herid")
                call.respond(emptyList<ElektroniskAbonoment>())
            } else {
                call.respond(elektroniskAbonomentService.finnParnterInformasjon(herid))
            }
        }
    }
}

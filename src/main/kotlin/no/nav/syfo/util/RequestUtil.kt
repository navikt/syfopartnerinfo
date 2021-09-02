package no.nav.syfo.util

import io.ktor.application.*
import io.ktor.util.pipeline.*

const val NAV_CALL_ID_HEADER = "Nav-Call-Id"
fun PipelineContext<out Unit, ApplicationCall>.getCallId(): String {
    return this.call.request.headers[NAV_CALL_ID_HEADER].toString()
}

const val NAV_CONSUMER_ID_HEADER = "Nav-Consumer-Id"
fun PipelineContext<out Unit, ApplicationCall>.getConsumerId(): String {
    return this.call.request.headers[NAV_CONSUMER_ID_HEADER].toString()
}

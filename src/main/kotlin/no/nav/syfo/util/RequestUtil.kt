package no.nav.syfo.util

import io.ktor.server.application.*

const val NAV_CALL_ID_HEADER = "Nav-Call-Id"
fun ApplicationCall.getCallId(): String {
    return this.request.headers[NAV_CALL_ID_HEADER].toString()
}

const val NAV_CONSUMER_ID_HEADER = "Nav-Consumer-Id"
fun ApplicationCall.getConsumerId(): String {
    return this.request.headers[NAV_CONSUMER_ID_HEADER].toString()
}

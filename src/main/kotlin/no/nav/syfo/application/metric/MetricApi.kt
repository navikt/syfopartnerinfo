package no.nav.syfo.application.metric

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

const val podMetricsPath = "/prometheus"

fun Routing.registerMetricApi() {
    get(podMetricsPath) {
        call.respondText(METRICS_REGISTRY.scrape())
    }
}

package no.nav.syfo.services

import kotlinx.coroutines.GlobalScope
import net.logstash.logback.argument.StructuredArguments
import no.nav.syfo.LoggingMeta
import no.nav.syfo.log

class BehandlerService(
    private val herid: String
) {
    fun finnParnterInformasjon(): String = with(GlobalScope) {
        val loggingMeta = LoggingMeta(
                mottakId = "",
                orgNr = "",
                msgId = "",
                sykmeldingId = ""
        )
        log.info("Received a SM2013, going to rules, {}", StructuredArguments.fields(loggingMeta))
        return "resultat"
    }
}

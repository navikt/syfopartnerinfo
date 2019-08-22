package no.nav.syfo.services

import kotlinx.coroutines.GlobalScope
import net.logstash.logback.argument.StructuredArguments
import no.nav.syfo.LoggingMeta
import no.nav.syfo.log

class ElektroniskAbonomentService(
    private val herid: String
) {
    fun finnParnterInformasjon(): ElektroniskAbonoment = with(GlobalScope) {
        val loggingMeta = LoggingMeta(
                mottakId = "",
                orgNr = "",
                msgId = "",
                sykmeldingId = ""
        )
        // TODO run sql query
        // select distinct p.partner_id
        // from partner p, ABONNEMENT a
        // where p.PARTNER_ID = a.PARTNER_ID
        // and a.tjeneste_id = '3'
        // and (a.SLUTT_DATO > sysdate or a.SLUTT_DATO is NULL)
        // and p.her_id = 2581

        log.info("Received a SM2013, going to rules, {}", StructuredArguments.fields(loggingMeta))
        return ElektroniskAbonoment(1324)
    }
}

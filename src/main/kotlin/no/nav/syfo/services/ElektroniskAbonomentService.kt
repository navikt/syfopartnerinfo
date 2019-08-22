package no.nav.syfo.services

import kotlinx.coroutines.GlobalScope
import no.nav.syfo.LoggingMeta
import no.nav.syfo.db.DatabaseInterface
import no.nav.syfo.db.hentElektroniskAbonoment

class ElektroniskAbonomentService(
    private val database: DatabaseInterface
) {
    fun finnParnterInformasjon(herid: String): ElektroniskAbonoment = with(GlobalScope) {
        val loggingMeta = LoggingMeta(
                mottakId = "",
                orgNr = "",
                msgId = "",
                sykmeldingId = ""
        )

        return database.hentElektroniskAbonoment(herid).first()
    }
}

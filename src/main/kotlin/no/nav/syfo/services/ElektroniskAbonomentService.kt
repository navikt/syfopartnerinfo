package no.nav.syfo.services

import no.nav.syfo.aksessering.db.hentElektroniskAbonoment
import no.nav.syfo.db.DatabaseInterface

class ElektroniskAbonomentService(
    private val database: DatabaseInterface
) {
    fun finnParnterInformasjon(herid: String): List<ElektroniskAbonoment> = database.hentElektroniskAbonoment(herid)
}

package no.nav.syfo.services

import no.nav.syfo.aksessering.db.hentElektroniskAbonoment
import no.nav.syfo.db.DatabaseInterface

class ElektroniskAbonomentService(
    private val database: DatabaseInterface,
    private val databasePrefix: String
) {
    fun finnParnterInformasjon(herid: String): List<ElektroniskAbonoment> = database.hentElektroniskAbonoment(herid, databasePrefix)
}

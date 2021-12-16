package no.nav.syfo.services

import no.nav.syfo.aksessering.db.hentPartnerInformasjon
import no.nav.syfo.db.DatabaseInterface

class PartnerInformasjonService(
    private val database: DatabaseInterface,
    private val databasePrefix: String,
) {
    fun finnPartnerInformasjon(
        herid: String,
    ): List<PartnerInformasjon> =
        database.hentPartnerInformasjon(herid, databasePrefix)
}

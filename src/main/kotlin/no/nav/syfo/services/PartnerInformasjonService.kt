package no.nav.syfo.services

import no.nav.syfo.aksessering.db.hentPartnerInformasjon
import no.nav.syfo.db.DatabaseInterface

class PartnerInformasjonService(
    private val database: DatabaseInterface,
) {
    fun finnPartnerInformasjon(
        herid: String,
    ): List<PartnerInformasjon> =
        database.hentPartnerInformasjon(herid)
}

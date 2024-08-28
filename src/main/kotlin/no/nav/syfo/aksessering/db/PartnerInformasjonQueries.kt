package no.nav.syfo.aksessering.db

import no.nav.syfo.db.DatabaseInterface
import no.nav.syfo.db.toList
import no.nav.syfo.services.PartnerInformasjon
import java.sql.ResultSet

fun DatabaseInterface.hentPartnerInformasjon(
    herid: String,
    databasePrefix: String
): List<PartnerInformasjon> =
    connection.use { connection ->
        connection.prepareStatement(
            """
                SELECT partner.partner_id
                FROM PARTNER partner
                WHERE TRIM(partner.her_id)=?
                """
        ).use {
            it.setString(1, herid)
            it.executeQuery().toList { toPartnerInformasjon() }
        }
    }

fun ResultSet.toPartnerInformasjon(): PartnerInformasjon =
    PartnerInformasjon(
        getBigDecimal("partner_id").toInt()
    )

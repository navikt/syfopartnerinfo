package no.nav.syfo.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.Properties
import no.nav.syfo.Environment
import no.nav.syfo.VaultCredentials

class Database(private val env: Environment, private val vaultCredentialService: VaultCredentials) : DatabaseInterface {

    private var connectionProps = Properties().apply {
        put("user", vaultCredentialService.databaseUsername)
        put("password", vaultCredentialService.databasePassword)
    }

    private val connection1: Connection = DriverManager.getConnection(env.databaseUrl, connectionProps)

    override val connection: Connection
        get() = connection1
}

fun <T> ResultSet.toList(mapper: ResultSet.() -> T) = mutableListOf<T>().apply {
    while (next()) {
        add(mapper())
    }
}

interface DatabaseInterface {
    val connection: Connection
}

package no.nav.syfo.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import no.nav.syfo.Environment
import java.sql.Connection
import java.sql.ResultSet

class Database(private val env: Environment) : DatabaseInterface {

    private val dataSource: HikariDataSource

    override val connection: Connection
        get() = dataSource.connection

    init {
        dataSource = HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = env.databaseUrl
                username = env.databaseUsername
                password = env.databasePassword
                maximumPoolSize = 3
                isAutoCommit = false
                driverClassName = "oracle.jdbc.OracleDriver"
                validate()
            }
        )
    }
}

fun <T> ResultSet.toList(mapper: ResultSet.() -> T) = mutableListOf<T>().apply {
    while (next()) {
        add(mapper())
    }
}

interface DatabaseInterface {
    val connection: Connection
}

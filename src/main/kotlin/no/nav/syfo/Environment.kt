package no.nav.syfo

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists

data class Environment(
    val applicationPort: Int = getEnvVar("APPLICATION_PORT", "8080").toInt(),
    val applicationThreads: Int = getEnvVar("APPLICATION_THREADS", "1").toInt(),
    val applicationName: String = getEnvVar("NAIS_APP_NAME", "syfopartnerinfo"),

    val azureAppClientId: String = getEnvVar("AZURE_APP_CLIENT_ID"),
    val azureOpenIdConfigJwksUri: String = getEnvVar("AZURE_OPENID_CONFIG_JWKS_URI"),
    val azureOpenIdConfigIssuer: String = getEnvVar("AZURE_OPENID_CONFIG_ISSUER"),

    val databaseUrl: String = readDBConfig("jdbc_url"),
    val databaseUsername: String = readDBCred("username"),
    val databasePassword: String = readDBCred("password"),
)

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")

fun readDBConfig(envVar: String) =
    Paths.get("/secrets/oracle/config/$envVar")
        .takeIf { it.exists() }
        ?.let { Files.readString(it) }
        ?: "not found"

fun readDBCred(envVar: String) =
    Paths.get("/secrets/oracle/creds/$envVar")
        .takeIf { it.exists() }
        ?.let { Files.readString(it) }
        ?: "not found"

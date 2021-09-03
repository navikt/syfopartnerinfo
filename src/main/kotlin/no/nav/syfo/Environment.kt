package no.nav.syfo

data class Environment(
    val applicationPort: Int = getEnvVar("APPLICATION_PORT", "8080").toInt(),
    val applicationThreads: Int = getEnvVar("APPLICATION_THREADS", "1").toInt(),
    val applicationName: String = getEnvVar("NAIS_APP_NAME", "syfopartnerinfo"),

    val azureAppClientId: String = getEnvVar("AZURE_APP_CLIENT_ID"),
    val azureOpenIdConfigJwksUri: String = getEnvVar("AZURE_OPENID_CONFIG_JWKS_URI"),
    val azureOpenIdConfigIssuer: String = getEnvVar("AZURE_OPENID_CONFIG_ISSUER"),

    val databaseUrl: String = getEnvVar("EMOTTAK_JDBC_URL"),
    val databasePrefix: String = getEnvVar("DATABASE_PREFIX"),
    val databaseUsername: String = getEnvVar("EMOTTAK_USERNAME"),
    val databasePassword: String = getEnvVar("EMOTTAK_PASSWORD"),
)

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")

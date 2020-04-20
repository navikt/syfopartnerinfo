package no.nav.syfo

data class Environment(
    val applicationPort: Int = getEnvVar("APPLICATION_PORT", "8080").toInt(),
    val applicationThreads: Int = getEnvVar("APPLICATION_THREADS", "1").toInt(),
    val applicationName: String = getEnvVar("NAIS_APP_NAME", "syfopartnerinfo"),
    val aadAccessTokenUrl: String = getEnvVar("AADACCESSTOKEN_URL"),
    val aadDiscoveryUrl: String = getEnvVar("AADDISCOVERY_URL"),
    val jwkKeysUrl: String = getEnvVar("JWKKEYS_URL", "https://login.microsoftonline.com/common/discovery/keys"),
    val jwtIssuer: String = getEnvVar("JWT_ISSUER"),
    val clientId: String = getEnvVar("CLIENT_ID"),
    val databaseUrl: String = getEnvVar("EMOTTAK_JDBC_URL"),
    val databasePrefix: String = getEnvVar("DATABASE_PREFIX"),
    val databaseUsername: String = getEnvVar("EMOTTAK_USERNAME"),
    val databasePassword: String = getEnvVar("EMOTTAK_PASSWORD"),
    val appIds: List<String> = getEnvVar("ALLOWED_APP_IDS", "")
            .split(",")
            .map { it.trim() }
)

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")

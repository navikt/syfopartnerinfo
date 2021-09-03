package testhelper

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import no.nav.syfo.Environment
import no.nav.syfo.application.ApplicationState
import java.nio.file.Paths

fun testEnvironment() = Environment(8080,
    jwtIssuer = "https://sts.issuer.net/myid",
    appIds = "2,3".split(","),
    clientId = "1",
    aadAccessTokenUrl = "",
    aadDiscoveryUrl = "",
    databaseUrl = "",
    databasePrefix = "",
    databaseUsername = "",
    databasePassword = ""
)

fun testApplicationState() = ApplicationState(
    alive = true,
    ready = true
)

fun testJwkProviderV1(): JwkProvider {
    val path = "src/test/resources/jwkset.json"
    val uri = Paths.get(path).toUri().toURL()
    return JwkProviderBuilder(uri).build()
}

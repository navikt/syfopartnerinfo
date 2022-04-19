package no.nav.syfo.application.authentication

import com.auth0.jwk.JwkProvider
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("no.nav.syfo.application.authentication")

fun Application.installJwtAuthentication(
    acceptedAudienceList: List<String>,
    jwtIssuer: String,
    jwkProvider: JwkProvider,
) {
    install(Authentication) {
        configureJwt(
            acceptedAudienceList = acceptedAudienceList,
            jwtIssuer = jwtIssuer,
            jwkProvider = jwkProvider,
        )
    }
}

fun AuthenticationConfig.configureJwt(
    acceptedAudienceList: List<String>,
    jwtIssuer: String,
    jwkProvider: JwkProvider,
) {
    jwt(name = JwtIssuerType.INTERNAL_AZUREAD_VEILEDER_V2.name) {
        verifier(jwkProvider, jwtIssuer)
        validate { credential ->
            if (hasExpectedAudience(credential, acceptedAudienceList)) {
                JWTPrincipal(credential.payload)
            } else {
                log.info(
                    "Auth: Unexpected audience for jwt {}, {}",
                    StructuredArguments.keyValue("issuer", credential.payload.issuer),
                    StructuredArguments.keyValue("audience", credential.payload.audience)
                )
                null
            }
        }
    }
}

fun hasExpectedAudience(credentials: JWTCredential, expectedAudience: List<String>): Boolean {
    return expectedAudience.any { credentials.payload.audience.contains(it) }
}

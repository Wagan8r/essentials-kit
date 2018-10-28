# essentials-kit

Essentials Kit is an opinionated implementation of [Spring Security](https://github.com/spring-projects/spring-security)
that uses [JTWs](https://github.com/jwtk/jjwt) for securing resources. Essentials Kit was built with the philosophy that
companies that manage credentials on a large scale are much more well suited to safely handle those credentials than
your own platform. As such, Essentials Kit provides no support for directly managing usernames and passwords. Instead, hooks
have been created to allow for authenticating via third parties by supplying an authentication token from one of the
supported identity providers.

## Supported identity providers
- [Google](https://github.com/googleapis/google-api-java-client) - Implemented in
[GoogleTokenVerifier](https://github.com/Wagan8r/essentials-kit/blob/master/src/main/java/com/bts/essentials/verification/GoogleTokenVerifier.java)

## Authenticating
A request must first be submitted to a resource of your own choosing, but `/login` is a typical choice. The payload of
the request must contain the token from one of the supported identity providers. Simply `@Autowired` the
[`LoginService`](https://github.com/Wagan8r/essentials-kit/blob/master/src/main/java/com/bts/essentials/service/LoginService.java)
and call the `login()` method with the token.

The `login()` method tests the token by iterating over all `@Coponent`
[`TokenVerifier`](https://github.com/Wagan8r/essentials-kit/blob/master/src/main/java/com/bts/essentials/verification/TokenVerifier.java)
implementations in the classpath (thereby allowing custom verifiers to be written and automatically added in). Upon
successful token verification, a JWT will be generated that contains the user's basic information. Additionally, the
principal will be set in the current context of the Spring `SecurityContextHolder`.

Upon exiting any `@Controller` resource method, the current security context's principal will be inspected and a newly
generated JWT will be placed within the response's `Authorization` header.

Requests to secured endpoints must contain an `Authorization` header using the `Bearer` scheme e.g. `"Authorization":
"Bearer <jwt>"`. Clients making requests to resources secured by the essentials-kit should continually update their
stored JWT after each service request in order to prevent the user's authentication from becoming stale

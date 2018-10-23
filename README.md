# essentials-kit

A library that has an opinionated implementation of [Spring Security](https://github.com/spring-projects/spring-security) that uses [JTWs](https://github.com/jwtk/jjwt) for authenticating users. There is no support for directly managing usernames and passwords. Instead, hooks have been created to allow for authenticating via third parties by supplying an authentication token from one of the supported identity providers.

## Supported identity verifiers
- [Google](https://github.com/googleapis/google-api-java-client)

## Authenticating
A request must first be submitted to a resource of your own choosing such as `/login`. The payload of the request must contain the token from one of the supported identity providers. Upon successful verification, a JWT will be generated that contains the user's basic information. Additionally, the principal will be set in the current context of the Spring `SecurityContextHolder`.

Upon exiting any `@Controller` resource method, the current security context's principal will be inspected and a newly generated JWT will be placed within the response's `Authorization` header.

Requests to secured endpoints must contain an `Authorization` header using the `Bearer` scheme e.g. `"Authorization": "Bearer <jwt>"`. Clients making requests to resources secured by the essentials-kit should continually update their stored JWT after each service request in order to prevent the user's authentication from going stale

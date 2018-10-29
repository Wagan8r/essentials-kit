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

## Configuration
### AbstractSecurityConfigurerAdapter
The first step is to create an implementation of `AbstractSecurityConfigurerAdapter`. `AbstractSecurityConfigurerAdapter`
configures the built-in JWT handling beans as well as the [`HttpSecurity`]() and the [`WebSecurity`]() from Spring. Hooks
are provided to customize the configuration via the `configureAuthorizeRequests()` and `ignoreRequests()` methods to
configure what the authorization requirements are placed on requests and what requests for which to ignore security
respectively.

```
@Configuration
@EnableWebSecurity
@EnableWebMvc
public class MySecurityConfigurerAdapter extends AbstractSecurityConfigurerAdapter {
  @Override
    protected ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry configureAuthorizeRequests(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        return expressionInterceptUrlRegistry;
    }

    @Override
    protected void ignoreRequests(WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer) {
        ignoredRequestConfigurer.antMatchers("/login");
    }
}
```
In the above example, no additional request requirements are added and the `/login` resource has been configured to
be ignored by security so that unauthenticated users may call it.

### UsersService
An implementation of `UsersService` is also required. After verifying a token, the `TokenVerifier`s return a `BasicUser`,
which contains a common set of user information. However, it is desireable to have a platform-specific identifier, so
Essentials Kit also provides a `User` object that contains a UUID `id` field. The `UsersService` interface declares a
`getOrCreateUser()` method as a hook to store and retrieve users in whatever manner is appropriate for the implementing
projects.

## Authenticating
A request must first be submitted to a resource of your own choosing, but `/login` is a typical choice. The payload of
the request must contain the token from one of the supported identity providers. Simply `@Autowired` the
[`LoginService`](https://github.com/Wagan8r/essentials-kit/blob/master/src/main/java/com/bts/essentials/service/LoginService.java)
and call the `login()` method with the token.

```
@Controller
public class LoginController {
  @Autowired
  private LoginService loginService;
  
  @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<User> login(@RequestBody AuthenticationRequest authenticationRequest) {
        User user = loginService.login(authenticationRequest.getToken());
        return ResponseEntity.ok(user);
    }
}
```

The `login()` method tests the token by iterating over all `@Coponent`
[`TokenVerifier`](https://github.com/Wagan8r/essentials-kit/blob/master/src/main/java/com/bts/essentials/verification/TokenVerifier.java)
implementations in the classpath (thereby allowing custom verifiers to be written and automatically added in). Upon
successful token verification, a `User` object will be created that contains the user's basic information. Additionally, the
user will be set as the principal in the current context of the Spring `SecurityContextHolder`.

Upon exiting any `@Controller` resource method, the current security context's principal will be inspected and a newly
generated JWT will be placed within the response's `Authorization` header.

Requests to secured endpoints must contain an `Authorization` header using the `Bearer` scheme e.g. `"Authorization":
"Bearer <jwt>"`. Clients making requests to resources secured by the essentials-kit should continually update their
stored JWT after each service request in order to prevent the user's authentication from becoming stale

# OIDC Frontend + Backend Integration with Keycloak
*Secure Full-Stack Authentication & Authorization using OAuth2 and JWT*

---

# High-Level Architecture

```java
Browser → Frontend (OIDC Client)
        ↓ Access Token
Calendar Backend (Resource Server)
        ↓ JWKS / Token Validation
         Keycloak (OIDC Provider)
```    

---

# Authentication Flow (Frontend)

- User calls frontend http://localhost:8080/
- Redirect to Keycloak login page
- Keycloak authenticates user
- Keycloak returns authorization code
- Frontend exchanges code for tokens
- User becomes authenticated in Spring Security

---

# Token Propagation to Backend

- WebClient is configured with OAuth2 filter
- Filter uses `OAuth2AuthorizedClientManager`
- Access token automatically injected into requests
- Backend receives `Authorization: Bearer <token>`

### WebClient Configuration

```java
ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
        new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
oauth2.setDefaultOAuth2AuthorizedClient(true);

return WebClient.builder()
        .apply(oauth2.oauth2Configuration())
        .build();

```
ServletOAuth2AuthorizedClientExchangeFilterFunction attaches user’s Keycloak token to outgoing requests and 
authorizedClientManager to fetch, cache, or refresh the token.

---

# Logout Flow

- Spring clears session

- Calls Keycloak logout endpoint

- Passes id_token_hint

- Keycloak terminates SSO session

- Redirects back to frontend


---

# Backend

* Enables JWT decoding

* Disables sessions (stateless)

* Enforces endpoint-level security

* Accepts only signed Keycloak tokens

# Use of JwtAuthenticationConverter

Keycloak Role Format:

```json

"realm_access": {
    "roles": ["my-role", "admin"]
}

```
Spring Security Role Format:

```vbnet

authorities: ["my-role"]

```

Converter for:

* Extract Keycloak roles

* Convert to GrantedAuthority objects


```java

final var realmAccess = Optional.ofNullable((Map<String, Object>) jwt.getClaimAsMap("realm_access"));
final var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.getOrDefault("roles", List.of())));
return roles.map(List::stream).
orElse(Stream.empty())
        .map(SimpleGrantedAuthority::new)
.map(GrantedAuthority.class::cast).toList();

```
# Use of Stateless Session:

```java
  .sessionManagement(httpSecuritySessionManagementConfigurer -> {
        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
```

# Endpoint Security Configuration

```java

.requestMatchers("/api/v1/calendar").authenticated()
                        .anyRequest().denyAll()

```

# References

* https://www.keycloak.org/
* https://www.baeldung.com/spring-boot-keycloak
* https://medium.com/@mohammad.h.zbib/solving-jwt-role-mapping-issues-in-spring-boot-with-keycloak-3f40db57216e
* https://medium.com/@ramanamuttana/export-and-import-of-realm-from-keycloak-131deb118b72

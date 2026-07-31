package io.quarkus.vault.client.auth;

public record VaultJwtCredentials(String cacheKey, String jwt) {
};

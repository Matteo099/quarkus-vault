package io.quarkus.vault.runtime.config;

import static io.quarkus.vault.runtime.config.VaultRuntimeConfig.DEFAULT_JWT_AUTH_MOUNT_PATH;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.smallrye.config.WithDefault;

@ConfigGroup
public interface VaultJwtAuthenticationConfig {

    /**
     * Jwt authentication role that has been created in Vault to associate Vault
     * policies.
     * This property is required when selecting the Jwt authentication type.
     */
    Optional<String> role();

    /**
     * Allows configure Jwt authentication mount path.
     */
    @WithDefault(DEFAULT_JWT_AUTH_MOUNT_PATH)
    String mountPath();
}

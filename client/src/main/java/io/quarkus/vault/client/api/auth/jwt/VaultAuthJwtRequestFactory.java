package io.quarkus.vault.client.api.auth.jwt;

import io.quarkus.vault.client.api.auth.kubernetes.VaultAuthKubernetesLoginParams;
import io.quarkus.vault.client.api.auth.kubernetes.VaultAuthKubernetesLoginResult;
import io.quarkus.vault.client.api.common.VaultRequestFactory;
import io.quarkus.vault.client.common.VaultLeasedResultExtractor;
import io.quarkus.vault.client.common.VaultRequest;

public class VaultAuthJwtRequestFactory extends VaultRequestFactory {
    public static final VaultAuthJwtRequestFactory INSTANCE = new VaultAuthJwtRequestFactory();

    public VaultAuthJwtRequestFactory() {
        super("[AUTH (jwt)]");
    }

    public VaultRequest<VaultAuthKubernetesLoginResult> login(String mountPath, String role, String jwt) {
        return VaultRequest.post(getTraceOpName("Login"))
                .path("auth", mountPath, "login")
                .noToken()
                .body(new VaultAuthKubernetesLoginParams()
                        .setRole(role)
                        .setJwt(jwt))
                .expectOkStatus()
                .build(VaultLeasedResultExtractor.of(VaultAuthKubernetesLoginResult.class));
    }
}

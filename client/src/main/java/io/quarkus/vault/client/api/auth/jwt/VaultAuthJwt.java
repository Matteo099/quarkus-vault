package io.quarkus.vault.client.api.auth.jwt;

import java.util.concurrent.CompletionStage;

import io.quarkus.vault.client.api.auth.kubernetes.VaultAuthKubernetesLoginAuthResult;
import io.quarkus.vault.client.api.common.VaultLeasedResult;
import io.quarkus.vault.client.api.common.VaultMountableAPI;
import io.quarkus.vault.client.common.VaultRequestExecutor;
import io.quarkus.vault.client.common.VaultResponse;

public class VaultAuthJwt extends VaultMountableAPI<VaultAuthJwtRequestFactory> {
    public static VaultAuthJwtRequestFactory FACTORY = VaultAuthJwtRequestFactory.INSTANCE;

    public VaultAuthJwt(VaultRequestExecutor executor, String mountPath,
            VaultAuthJwtRequestFactory factory) {
        super(executor, factory, mountPath);
    }

    public VaultAuthJwt(VaultRequestExecutor executor, String mountPath) {
        this(executor, mountPath, FACTORY);
    }

    public CompletionStage<VaultAuthKubernetesLoginAuthResult> login(String role, String jwt) {
        return executor.execute(factory.login(mountPath, role, jwt))
                .thenApply(VaultResponse::getResult).thenApply(VaultLeasedResult::getAuth);
    }
}

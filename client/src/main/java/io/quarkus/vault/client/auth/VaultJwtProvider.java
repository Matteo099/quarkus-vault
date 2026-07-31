package io.quarkus.vault.client.auth;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public interface VaultJwtProvider extends Supplier<CompletionStage<VaultJwtCredentials>> {
}

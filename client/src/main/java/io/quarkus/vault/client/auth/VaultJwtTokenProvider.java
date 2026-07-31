package io.quarkus.vault.client.auth;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Logger;

import io.quarkus.vault.client.VaultException;
import io.quarkus.vault.client.api.auth.jwt.VaultAuthJwt;
import io.quarkus.vault.client.common.VaultRequestExecutor;
import io.quarkus.vault.client.common.VaultResponse;

public class VaultJwtTokenProvider implements VaultTokenProvider {

    private static final Logger log = Logger.getLogger(VaultJwtTokenProvider.class.getName());

    private final String mountPath;
    private final String role;
    private final Supplier<CompletionStage<VaultJwtCredentials>> jwtProvider;
    private final ConcurrentHashMap<String, VaultToken> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletableFuture<VaultToken>> loginInProgress = new ConcurrentHashMap<>();

    private Duration renewGracePeriod = Duration.ofSeconds(30);

    public VaultJwtTokenProvider(String mountPath, String role,
            Supplier<CompletionStage<VaultJwtCredentials>> jwtProvider) {
        this.mountPath = mountPath;
        this.role = role;
        this.jwtProvider = jwtProvider;
    }

    public VaultJwtTokenProvider(VaultJwtAuthOptions options) {
        this(options.mountPath, options.role, options.jwtProvider);
    }

    @Override
    public VaultTokenProvider caching(Duration renewGracePeriod) {
        this.renewGracePeriod = renewGracePeriod != null ? renewGracePeriod : Duration.ofSeconds(30);
        return this;
    }

    @Override
    public void invalidateCache() {
        cache.clear();
        loginInProgress.clear();
    }

    @Override
    public CompletionStage<VaultToken> apply(VaultAuthRequest authRequest) {
        var executor = authRequest.getExecutor();
        if (executor == null) {
            return CompletableFuture.failedStage(new VaultException("No executor available to perform JWT login"));
        }

        return jwtProvider.get().thenCompose(credentials -> {
            if (credentials == null || credentials.jwt() == null || credentials.jwt().isBlank()) {
                return CompletableFuture.completedStage(null);
            }

            String key = credentials.cacheKey();
            VaultToken cached = cache.get(key);

            if (cached != null && cached.isValid() && !cached.shouldExtend(renewGracePeriod)) {
                log.fine(() -> "Using cached Vault token for " + key);
                return CompletableFuture.completedStage(cached.cached());
            }

            CompletableFuture<VaultToken> inFlight = loginInProgress.computeIfAbsent(key, k -> {
                CompletableFuture<VaultToken> future = new CompletableFuture<>();

                login(executor, authRequest, credentials).whenComplete((token, error) -> {
                    loginInProgress.remove(k);

                    if (error != null) {
                        future.completeExceptionally(error);
                        return;
                    }

                    if (token != null) {
                        cache.put(k, token.cached());
                    } else {
                        cache.remove(k);
                    }

                    future.complete(token);
                });

                return future;
            });

            return inFlight.thenApply(token -> token == null ? null : token.cached());
        });
    }

    private CompletionStage<VaultToken> login(
            VaultRequestExecutor executor,
            VaultAuthRequest authRequest,
            VaultJwtCredentials credentials) {

        var loginRequest = VaultAuthJwt.FACTORY.login(
                mountPath,
                role,
                credentials.jwt());

        return executor.execute(loginRequest)
                .thenApply(VaultResponse::getResult)
                .thenApply(res -> {

                    var auth = res.getAuth();

                    if (auth == null) {
                        return null;
                    }

                    return VaultToken.from(
                            auth.getClientToken(),
                            auth.isRenewable(),
                            auth.getLeaseDuration(),
                            auth.getNumUses(),
                            authRequest.getInstantSource());
                });
    }
}

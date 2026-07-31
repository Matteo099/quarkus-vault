package io.quarkus.vault.client.auth;

import static io.quarkus.vault.client.auth.VaultCachingTokenProvider.DEFAULT_RENEW_GRACE_PERIOD;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public class VaultJwtAuthOptions extends VaultAuthOptions {

    public static class Builder {
        private String mountPath;
        private String role;
        private Supplier<CompletionStage<VaultJwtCredentials>> jwtProvider;
        private Duration cachingRenewGracePeriod = DEFAULT_RENEW_GRACE_PERIOD;

        public Builder mountPath(String mountPath) {
            this.mountPath = mountPath;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder jwtProvider(Supplier<CompletionStage<VaultJwtCredentials>> jwtProvider) {
            this.jwtProvider = jwtProvider;
            return this;
        }

        public Builder caching(Duration cachingRenewGracePeriod) {
            this.cachingRenewGracePeriod = cachingRenewGracePeriod;
            return this;
        }

        public Builder noCaching() {
            this.cachingRenewGracePeriod = Duration.ZERO;
            return this;
        }

        public VaultJwtAuthOptions build() {
            return new VaultJwtAuthOptions(this);
        }
    }

    public final String mountPath;
    public final String role;
    public final Supplier<CompletionStage<VaultJwtCredentials>> jwtProvider;

    private VaultJwtAuthOptions(Builder builder) {
        super(builder.cachingRenewGracePeriod);
        this.mountPath = Objects.requireNonNull(builder.mountPath);
        this.role = Objects.requireNonNull(builder.role);
        this.jwtProvider = Objects.requireNonNull(builder.jwtProvider);
    }

    public static Builder builder() {
        return new Builder();
    }

}

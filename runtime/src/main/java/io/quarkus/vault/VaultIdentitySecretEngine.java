package io.quarkus.vault;

import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityAliasCreateResult;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityAliasData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityIdAliasesResult;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityGroupAliasData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityGroupData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityIdNameResult;

/**
 * Blocking wrapper around {@link VaultIdentitySecretReactiveEngine}.
 * Converts Uni-based reactive calls into synchronous operations.
 */
@ApplicationScoped
public class VaultIdentitySecretEngine {

    private final VaultIdentitySecretReactiveEngine engine;

    @Inject
    public VaultIdentitySecretEngine(VaultIdentitySecretReactiveEngine engine) {
        this.engine = engine;
    }

    // ============================================================
    // ENTITY
    // ============================================================

    public List<String> listEntities() {
        return engine.listEntities().await().indefinitely();
    }

    public List<String> listEntitiesByName() {
        return engine.listEntitiesByName().await().indefinitely();
    }

    public VaultSecretsIdentityEntityData readEntityById(String id) {
        return engine.readEntity(id).await().indefinitely();
    }

    public VaultSecretsIdentityEntityData readEntityByName(String name) {
        return engine.readEntityByName(name).await().indefinitely();
    }

    public VaultSecretsIdentityEntityIdAliasesResult createOrUpdateEntityByName(String name,
            List<String> policies, boolean disabled, Map<String, String> metadata) {
        return engine.createOrUpdateEntityByName(name, policies, disabled, metadata).await().indefinitely();
    }

    public void updateEntityById(String id,
            List<String> policies, boolean disabled, Map<String, String> metadata) {
        engine.updateEntity(id, policies, disabled, metadata).await().indefinitely();
    }

    public void deleteEntity(String id) {
        engine.deleteEntity(id).await().indefinitely();
    }

    // ============================================================
    // ENTITY ALIAS
    // ============================================================

    public VaultSecretsIdentityAliasCreateResult upsertEntityAlias(String name,
            String canonicalId,
            String mountAccessor) {
        return engine.upsertEntityAlias(name, canonicalId, mountAccessor).await().indefinitely();
    }

    public VaultSecretsIdentityEntityAliasData readEntityAlias(String id) {
        return engine.readEntityAlias(id).await().indefinitely();
    }

    public void deleteEntityAlias(String id) {
        engine.deleteEntityAlias(id).await().indefinitely();
    }

    // ============================================================
    // GROUP
    // ============================================================

    public List<String> listGroups() {
        return engine.listGroups().await().indefinitely();
    }

    public List<String> listGroupsByName() {
        return engine.listGroupsByName().await().indefinitely();
    }

    public VaultSecretsIdentityGroupData readGroup(String id) {
        return engine.readGroup(id).await().indefinitely();
    }

    public VaultSecretsIdentityGroupData readGroupByName(String name) {
        return engine.readGroupByName(name).await().indefinitely();
    }

    public VaultSecretsIdentityIdNameResult createOrUpdateGroupByName(String name, String type, List<String> policies,
            List<String> memberEntityIds, List<String> memberGroupIds,
            Map<String, String> metadata) {
        return engine.createOrUpdateGroupByName(name, type, policies, memberEntityIds, memberGroupIds, metadata).await()
                .indefinitely();
    }

    public void updateGroupById(String id, String type, List<String> policies,
            List<String> memberEntityIds, List<String> memberGroupIds,
            Map<String, String> metadata) {
        engine.updateGroup(id, type, policies, memberEntityIds, memberGroupIds, metadata).await().indefinitely();
    }

    public void deleteGroup(String id) {
        engine.deleteGroup(id).await().indefinitely();
    }

    // ============================================================
    // GROUP ALIAS
    // ============================================================

    public VaultSecretsIdentityAliasCreateResult upsertGroupAlias(String name,
            String canonicalId,
            String mountAccessor) {
        return engine.upsertGroupAlias(name, canonicalId, mountAccessor).await().indefinitely();
    }

    public VaultSecretsIdentityGroupAliasData readGroupAlias(String id) {
        return engine.readGroupAlias(id).await().indefinitely();
    }

    public void deleteGroupAlias(String id) {
        engine.deleteGroupAlias(id).await().indefinitely();
    }

    // ============================================================
    // LOOKUP ENTITY
    // ============================================================

    public VaultSecretsIdentityEntityData lookupEntityByName(String name) {
        return engine.lookupEntityByName(name).await().indefinitely();
    }

    public VaultSecretsIdentityEntityData lookupEntityById(String id) {
        return engine.lookupEntityById(id).await().indefinitely();
    }

    public VaultSecretsIdentityEntityData lookupEntityByAliasName(String name) {
        return engine.lookupEntityByAliasName(name).await().indefinitely();
    }

    public VaultSecretsIdentityEntityData lookupEntityByAliasId(String id) {
        return engine.lookupEntityByAliasId(id).await().indefinitely();
    }

    public VaultSecretsIdentityEntityData lookupEntityByAliasNameAndMountAccessor(String name,
            String aliasMountAccessor) {
        return engine.lookupEntityByAliasNameAndMountAccessor(name, aliasMountAccessor).await().indefinitely();
    }

    // ============================================================
    // LOOKUP GROUP
    // ============================================================

    public VaultSecretsIdentityGroupData lookupGroupByName(String name) {
        return engine.lookupGroupByName(name).await().indefinitely();
    }

    public VaultSecretsIdentityGroupData lookupGroupById(String id) {
        return engine.lookupGroupById(id).await().indefinitely();
    }

    public VaultSecretsIdentityGroupData lookupGroupByAliasName(String name) {
        return engine.lookupGroupByAliasName(name).await().indefinitely();
    }

    public VaultSecretsIdentityGroupData lookupGroupByAliasId(String id) {
        return engine.lookupGroupByAliasId(id).await().indefinitely();
    }

    public VaultSecretsIdentityGroupData lookupGroupByAliasNameAndMountAccessor(String name,
            String aliasMountAccessor) {
        return engine.lookupGroupByAliasNameAndMountAccessor(name, aliasMountAccessor).await().indefinitely();
    }

}

package io.quarkus.vault.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.quarkus.vault.VaultIdentitySecretReactiveEngine;
import io.quarkus.vault.client.VaultClient;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentity;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityAliasCreateResult;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityAliasData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityIdAliasesResult;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityGroupAliasData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityGroupData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityIdNameResult;
import io.smallrye.mutiny.Uni;

/**
 * Implementation of {@link VaultIdentitySecretReactiveEngine} backed by
 * {@link VaultSecretsIdentity}.
 * <p>
 * This class wraps the async CompletionStage calls into Mutiny {@link Uni} for
 * reactive use.
 * It supports managing:
 * <ul>
 * <li>Entities (users / service identities)</li>
 * <li>Entity Aliases (external IdP identities)</li>
 * <li>Groups</li>
 * <li>Group Aliases</li>
 * </ul>
 */
@ApplicationScoped
public class VaultIdentityManager implements VaultIdentitySecretReactiveEngine {

    private final VaultSecretsIdentity identity;

    @Inject
    public VaultIdentityManager(VaultClient client) {
        this.identity = client.secrets().identity();
    }

    // ============================================================
    // ENTITY
    // ============================================================

    @Override
    public Uni<List<String>> listEntities() {
        return Uni.createFrom().completionStage(identity.listEntities(true));
    }

    @Override
    public Uni<List<String>> listEntitiesByName() {
        return Uni.createFrom().completionStage(identity.listEntitiesByName(true));
    }

    @Override
    public Uni<VaultSecretsIdentityEntityData> readEntity(String id) {
        return Uni.createFrom().completionStage(identity.readEntity(id));
    }

    @Override
    public Uni<VaultSecretsIdentityEntityData> readEntityByName(String name) {
        return Uni.createFrom().completionStage(identity.readEntityByName(name));
    }

    @Override
    public Uni<VaultSecretsIdentityEntityIdAliasesResult> createOrUpdateEntityByName(String name, List<String> policies,
            boolean disabled,
            Map<String, String> metadata) {
        Map<String, Object> map = new HashMap<>();
        map.put("disabled", disabled);
        map.put("policies", policies);
        map.put("metadata", metadata);
        return Uni.createFrom().completionStage(identity.createOrUpdateEntityByName(name, map));
    }

    @Override
    public Uni<Void> updateEntity(String id, List<String> policies,
            boolean disabled, Map<String, String> metadata) {
        Map<String, Object> map = new HashMap<>();
        map.put("disabled", disabled);
        map.put("policies", policies);
        map.put("metadata", metadata);
        return Uni.createFrom().completionStage(identity.updateEntityById(id, map));
    }

    @Override
    public Uni<Void> deleteEntity(String id) {
        return Uni.createFrom().completionStage(identity.deleteEntity(id));
    }

    // ============================================================
    // ENTITY ALIAS
    // ============================================================

    @Override
    public Uni<VaultSecretsIdentityAliasCreateResult> upsertEntityAlias(String name, String canonicalId,
            String mountAccessor) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("canonical_id", canonicalId);
        map.put("mount_accessor", mountAccessor);
        return Uni.createFrom().completionStage(identity.createEntityAlias(map));
    }

    @Override
    public Uni<VaultSecretsIdentityEntityAliasData> readEntityAlias(String id) {
        return Uni.createFrom().completionStage(identity.readEntityAliasById(id));
    }

    @Override
    public Uni<Void> deleteEntityAlias(String id) {
        return Uni.createFrom().completionStage(identity.deleteEntityAliasById(id));
    }

    // ============================================================
    // GROUP
    // ============================================================

    @Override
    public Uni<List<String>> listGroups() {
        return Uni.createFrom().completionStage(identity.listGroupsById(true));
    }

    @Override
    public Uni<List<String>> listGroupsByName() {
        return Uni.createFrom().completionStage(identity.listGroupsByName(true));
    }

    @Override
    public Uni<VaultSecretsIdentityGroupData> readGroup(String id) {
        return Uni.createFrom().completionStage(identity.readGroupById(id));
    }

    @Override
    public Uni<VaultSecretsIdentityGroupData> readGroupByName(String name) {
        return Uni.createFrom().completionStage(identity.readGroupByName(name));
    }

    @Override
    public Uni<VaultSecretsIdentityIdNameResult> createOrUpdateGroupByName(String name, String type,
            List<String> policies, List<String> memberEntityIds, List<String> memberGroupIds,
            Map<String, String> metadata) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("policies", policies);
        map.put("member_entity_ids", memberEntityIds);
        map.put("member_group_ids", memberGroupIds);
        map.put("metadata", metadata);
        return Uni.createFrom().completionStage(identity.createOrUpdateGroupByName(name, map));
    }

    @Override
    public Uni<Void> updateGroup(String id, String type,
            List<String> policies, List<String> memberEntityIds, List<String> memberGroupIds,
            Map<String, String> metadata) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("policies", policies);
        map.put("member_entity_ids", memberEntityIds);
        map.put("member_group_ids", memberGroupIds);
        map.put("metadata", metadata);
        return Uni.createFrom().completionStage(identity.updateGroupById(id, map));
    }

    @Override
    public Uni<Void> deleteGroup(String id) {
        return Uni.createFrom().completionStage(identity.deleteGroupById(id));
    }

    // ============================================================
    // GROUP ALIAS
    // ============================================================

    @Override
    public Uni<VaultSecretsIdentityAliasCreateResult> upsertGroupAlias(String name, String canonicalId,
            String mountAccessor) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("canonical_id", canonicalId);
        map.put("mount_accessor", mountAccessor);
        return Uni.createFrom().completionStage(identity.createOrUpdateGroupAlias(map));
    }

    @Override
    public Uni<VaultSecretsIdentityGroupAliasData> readGroupAlias(String id) {
        return Uni.createFrom().completionStage(identity.readGroupAliasById(id));
    }

    @Override
    public Uni<Void> deleteGroupAlias(String id) {
        return Uni.createFrom().completionStage(identity.deleteGroupAliasById(id));
    }

    // ============================================================
    // LOOKUP ENTITY
    // ============================================================

    @Override
    public Uni<VaultSecretsIdentityEntityData> lookupEntityByName(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return Uni.createFrom().completionStage(identity.lookupEntity(params));
    }

    @Override
    public Uni<VaultSecretsIdentityEntityData> lookupEntityById(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return Uni.createFrom().completionStage(identity.lookupEntity(params));
    }

    @Override
    public Uni<VaultSecretsIdentityEntityData> lookupEntityByAliasName(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("alias_name", name);
        return Uni.createFrom().completionStage(identity.lookupEntity(params));
    }

    @Override
    public Uni<VaultSecretsIdentityEntityData> lookupEntityByAliasId(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("alias_id", id);
        return Uni.createFrom().completionStage(identity.lookupEntity(params));
    }

    @Override
    public Uni<VaultSecretsIdentityEntityData> lookupEntityByAliasNameAndMountAccessor(String name,
            String aliasMountAccessor) {
        Map<String, String> params = new HashMap<>();
        params.put("alias_name", name);
        params.put("alias_mount_accessor", aliasMountAccessor);
        return Uni.createFrom().completionStage(identity.lookupEntity(params));
    }

    // ============================================================
    // LOOKUP GROUP
    // ============================================================

    @Override
    public Uni<VaultSecretsIdentityGroupData> lookupGroupByName(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return Uni.createFrom().completionStage(identity.lookupGroup(params));
    }

    @Override
    public Uni<VaultSecretsIdentityGroupData> lookupGroupById(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return Uni.createFrom().completionStage(identity.lookupGroup(params));
    }

    @Override
    public Uni<VaultSecretsIdentityGroupData> lookupGroupByAliasName(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("alias_name", name);
        return Uni.createFrom().completionStage(identity.lookupGroup(params));
    }

    @Override
    public Uni<VaultSecretsIdentityGroupData> lookupGroupByAliasId(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("alias_id", id);
        return Uni.createFrom().completionStage(identity.lookupGroup(params));
    }

    @Override
    public Uni<VaultSecretsIdentityGroupData> lookupGroupByAliasNameAndMountAccessor(String name,
            String aliasMountAccessor) {
        Map<String, String> params = new HashMap<>();
        params.put("alias_name", name);
        params.put("alias_mount_accessor", aliasMountAccessor);
        return Uni.createFrom().completionStage(identity.lookupGroup(params));
    }
}

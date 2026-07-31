package io.quarkus.vault;

import java.util.List;
import java.util.Map;

import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityAliasCreateResult;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityAliasData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityEntityIdAliasesResult;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityGroupAliasData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityGroupData;
import io.quarkus.vault.client.api.secrets.identity.VaultSecretsIdentityIdNameResult;
import io.smallrye.mutiny.Uni;

/**
 * Reactive API for interacting with Vault/OpenBao Identity secret engine.
 * <p>
 * Supports:
 * <ul>
 * <li>Entities (users / service identities)</li>
 * <li>Entity Aliases (external IdP identities like Keycloak users)</li>
 * <li>Groups</li>
 * <li>Group Aliases (external IdP groups)</li>
 * </ul>
 * Identity objects represent the authorization graph used by Vault/OpenBao
 * to resolve effective permissions.
 */
public interface VaultIdentitySecretReactiveEngine {

    // ============================================================
    // ENTITY
    // ============================================================

    /**
     * List all entity IDs registered in the identity engine.
     *
     * @return a reactive {@link Uni} emitting the list of entity IDs
     */
    Uni<List<String>> listEntities();

    /**
     * List all entity names registered in the identity engine.
     *
     * @return a reactive {@link Uni} emitting the list of entity names
     */
    Uni<List<String>> listEntitiesByName();

    /**
     * Read an entity by its ID.
     *
     * @param id the entity identifier
     * @return a reactive {@link Uni} emitting the entity data
     */
    Uni<VaultSecretsIdentityEntityData> readEntity(String id);

    /**
     * Read an entity by its name.
     *
     * @param name the entity name
     * @return a reactive {@link Uni} emitting the entity data
     */
    Uni<VaultSecretsIdentityEntityData> readEntityByName(String name);

    /**
     * Create or update an entity.
     *
     * @param name the entity name
     * @param policies list of policies attached to the entity
     * @param metadata optional metadata
     * @return a reactive {@link Uni} emitting the created/updated entity as a map
     */
    public Uni<VaultSecretsIdentityEntityIdAliasesResult> createOrUpdateEntityByName(String name,
            List<String> policies,
            boolean disabled, Map<String, String> metadata);

    public Uni<Void> updateEntity(String id, List<String> policies,
            boolean disabled, Map<String, String> metadata);

    /**
     * Delete an entity by its ID.
     *
     * @param id the entity ID
     * @return a reactive {@link Uni} emitting completion when the deletion is done
     */
    Uni<Void> deleteEntity(String id);

    // ============================================================
    // ENTITY ALIAS
    // ============================================================

    /**
     * Create or update an entity alias linking an external identity provider
     * (Keycloak, OIDC, LDAP) to an entity.
     *
     * @param name alias name (external username)
     * @param canonicalId the entity ID
     * @param mountAccessor the auth backend accessor
     * @return a reactive {@link Uni} emitting the alias data
     */
    Uni<VaultSecretsIdentityAliasCreateResult> upsertEntityAlias(String name, String canonicalId,
            String mountAccessor);

    /**
     * Read an entity alias by ID.
     *
     * @param id alias ID
     * @return a reactive {@link Uni} emitting the alias data
     */
    Uni<VaultSecretsIdentityEntityAliasData> readEntityAlias(String id);

    /**
     * Delete an entity alias by ID.
     *
     * @param id alias ID
     * @return a reactive {@link Uni} emitting completion when the deletion is done
     */
    Uni<Void> deleteEntityAlias(String id);

    // ============================================================
    // GROUP
    // ============================================================

    /**
     * List all group IDs.
     *
     * @return a reactive {@link Uni} emitting the list of group IDs
     */
    Uni<List<String>> listGroups();

    /**
     * List all group names.
     *
     * @return a reactive {@link Uni} emitting the list of group names
     */
    Uni<List<String>> listGroupsByName();

    /**
     * Read a group by ID.
     *
     * @param id group identifier
     * @return a reactive {@link Uni} emitting the group data
     */
    Uni<VaultSecretsIdentityGroupData> readGroup(String id);

    /**
     * Read a group by name.
     *
     * @param name group name
     * @return a reactive {@link Uni} emitting the group data
     */
    Uni<VaultSecretsIdentityGroupData> readGroupByName(String id);

    /**
     * Create or update a group.
     *
     * @param name the group name
     * @param policies list of policies attached
     * @param memberEntityIds member entity IDs
     * @param metadata optional metadata
     * @return a reactive {@link Uni} emitting the created/updated group as a map
     */
    Uni<VaultSecretsIdentityIdNameResult> createOrUpdateGroupByName(String name, String type, List<String> policies,
            List<String> memberEntityIds, List<String> memberGroupIds,
            Map<String, String> metadata);

    Uni<Void> updateGroup(String id, String type,
            List<String> policies, List<String> memberEntityIds, List<String> memberGroupIds,
            Map<String, String> metadata);

    /**
     * Delete a group by ID.
     *
     * @param id group ID
     * @return a reactive {@link Uni} emitting completion when the deletion is done
     */
    Uni<Void> deleteGroup(String id);

    // ============================================================
    // GROUP ALIAS
    // ============================================================

    /**
     * Create or update a group alias linking external groups (Keycloak roles / LDAP
     * groups)
     * to identity groups.
     *
     * @param name alias name
     * @param canonicalId group ID
     * @param mountAccessor auth backend accessor
     * @return a reactive {@link Uni} emitting the alias data
     */
    Uni<VaultSecretsIdentityAliasCreateResult> upsertGroupAlias(String name, String canonicalId,
            String mountAccessor);

    /**
     * Read a group alias by ID.
     *
     * @param id alias ID
     * @return a reactive {@link Uni} emitting the alias data
     */
    Uni<VaultSecretsIdentityGroupAliasData> readGroupAlias(String id);

    /**
     * Delete a group alias by ID.
     *
     * @param id alias ID
     * @return a reactive {@link Uni} emitting completion when the deletion is done
     */
    Uni<Void> deleteGroupAlias(String id);

    // ============================================================

    Uni<VaultSecretsIdentityEntityData> lookupEntityByName(String name);

    Uni<VaultSecretsIdentityEntityData> lookupEntityById(String id);

    Uni<VaultSecretsIdentityEntityData> lookupEntityByAliasName(String name);

    Uni<VaultSecretsIdentityEntityData> lookupEntityByAliasId(String id);

    Uni<VaultSecretsIdentityEntityData> lookupEntityByAliasNameAndMountAccessor(String name,
            String aliasMountAccessor);

    // ============================================================

    Uni<VaultSecretsIdentityGroupData> lookupGroupByName(String name);

    Uni<VaultSecretsIdentityGroupData> lookupGroupById(String id);

    Uni<VaultSecretsIdentityGroupData> lookupGroupByAliasName(String name);

    Uni<VaultSecretsIdentityGroupData> lookupGroupByAliasId(String id);

    Uni<VaultSecretsIdentityGroupData> lookupGroupByAliasNameAndMountAccessor(String name,
            String aliasMountAccessor);
}

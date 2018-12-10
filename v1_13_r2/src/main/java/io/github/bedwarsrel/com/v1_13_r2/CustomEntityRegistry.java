package io.github.bedwarsrel.com.v1_13_r2;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Function;
import org.bukkit.entity.EntityType;

@SuppressWarnings("rawtypes")
public class CustomEntityRegistry extends RegistryMaterials {

  // Use this registry-hack
  // https://github.com/BillyGalbreath/Ridables/blob/master/src/main/java/net/pl3x/bukkit/ridables/util/RegistryHax.java

  // Or this
  // https://www.spigotmc.org/threads/how-to-register-custom-entities-in-1-13.329942/page-2
  // As tried below. But didnt work xD

  private static CustomEntityRegistry instance = null;

  private final BiMap<MinecraftKey, Class<? extends Entity>> customEntities = HashBiMap.create();
  private final BiMap<Class<? extends Entity>, MinecraftKey> customEntityClasses =
      this.customEntities.inverse();
  private final Map<Class<? extends Entity>, Integer> customEntityIds = new HashMap<>();

  private final IRegistry wrapped;

  private CustomEntityRegistry(IRegistry original) {
    this.wrapped = original;
  }

  public static void addCustomEntity(int entityId, String entityName,
      Class<? extends Entity> entityClass) {
    getInstance().putCustomEntity(entityId, entityName, entityClass);
  }

  public static CustomEntityRegistry getInstance() {
    if (instance != null) {
      return instance;
    }

    instance = new CustomEntityRegistry(IRegistry.ENTITY_TYPE);

    /*
    try {
      // TODO: Update name on version change (RegistryMaterials)
      Field registryMaterialsField = EntityTypes.class.getDeclaredField("b");
      registryMaterialsField.setAccessible(true);

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(registryMaterialsField,
          registryMaterialsField.getModifiers() & ~Modifier.FINAL);

      registryMaterialsField.set(null, instance);
    } catch (Exception e) {
      instance = null;

      throw new RuntimeException("Unable to override the old entity RegistryMaterials", e);
    }

    return instance;
    */



    /*

    // ok, first thing we do is make a new EntityTypes for the custom entity
    EntityTypes.a<TNTSheep> type = EntityTypes.a.a(TNTSheep.class, (Function<? super World, ? extends TNTSheep>) TNTSheep::new);
    EntityTypes<TNTSheep> types = type.a("dolphin");


    // now we manually add this EntityTypes to the REGISTRY
    // this replaces the EntityTypes.REGISTRY.a(key, EntityTypes) call
    MinecraftKey key = new MinecraftKey("dolphin");
    */

    /*
    try {
      // this is only present on Paper servers, so we surround in a try/catch so we dont crash Spigot servers
      EntityTypes.clsToKeyMap.put(TNTSheep.class, key);
      EntityTypes.clsToTypeMap.put(TNTSheep.class, EntityType.fromName("dolphin"));
    } catch (NoSuchFieldError ignore) {
    }
    */


    /*
    // now we have to use a lot of reflection to hack our way through the actual registry
    // The registry has 3 arrays that it uses to lookup EntityTypes from ID and ID from EntityTypes.
    // The "b" field holds the EntityTypes in a specific index depending on the hash of the object.
    // The "c" field holds the ID for "d" field using the same index as "b" (confused yet?)
    // The "d" field holds the EntityType at a "squished" index (all null objects moved to the end)
    // See https://pastebin.com/mBp8f9uz for an example of these fields and what I mean.
    try {
      // the "a" field is the RegistryID containing all the EntityTypes in memory at indexes based on the object's hash
      Field registryMaterials_fieldA = RegistryMaterials.class.getDeclaredField("a");
      registryMaterials_fieldA.setAccessible(true);
      RegistryID<EntityTypes<?>> registryID = (RegistryID<EntityTypes<?>>) registryMaterials_fieldA.get(IRegistry.ENTITY_TYPE);

      // this is the internal ID for the current dolphin for the "d" field, lets keep new one in the same spot
      int originalID = registryID.getId(EntityTypes.DOLPHIN);

      // Calculate the new id for "b" and "c" using the object's hash
      Method registryID_methodD = RegistryID.class.getDeclaredMethod("d", Object.class);
      registryID_methodD.setAccessible(true);
      Method registryID_methodE = RegistryID.class.getDeclaredMethod("e", int.class);
      registryID_methodE.setAccessible(true);
      int newIndex = (int) registryID_methodE.invoke(registryID, registryID_methodD.invoke(registryID, types));

      // null out the original EntityType and add in the new one in the correct spot
      Field registryID_fieldB = RegistryID.class.getDeclaredField("b");
      registryID_fieldB.setAccessible(true);
      Object[] arrB = (Object[]) registryID_fieldB.get(registryID);
      arrB[newIndex] = types;
      int oldIndex = -1;
      for (int i = 0; i < arrB.length; i++) {
        if (arrB[i] == EntityTypes.DOLPHIN) {
          arrB[i] = null;
          oldIndex = i;
          break;
        }
      }
      registryID_fieldB.set(registryID, arrB);

      // zero out the old "b" location and set the new one
      Field registryID_fieldC = RegistryID.class.getDeclaredField("c");
      registryID_fieldC.setAccessible(true);
      int[] arrC = (int[]) registryID_fieldC.get(registryID);
      arrC[oldIndex] = 0;
      arrC[newIndex] = originalID;
      registryID_fieldC.set(registryID, arrC);

      // update "d" field with the new EntityType
      Field registryID_fieldD = RegistryID.class.getDeclaredField("d");
      registryID_fieldD.setAccessible(true);
      Object[] arrD = (Object[]) registryID_fieldD.get(registryID);
      arrD[originalID] = types;
      registryID_fieldD.set(registryID, arrD);

      // set the 3 arrays back to the "a" field
      registryMaterials_fieldA.set(IRegistry.ENTITY_TYPE, registryID);

      // this is a simple inversed (backwards) map for EntityTypes by MinecraftKey
      Field registryId_b = RegistryMaterials.class.getDeclaredField("b");
      registryId_b.setAccessible(true);
      Map<EntityTypes<?>, MinecraftKey> mapB_original = (Map<EntityTypes<?>, MinecraftKey>) registryId_b.get(IRegistry.ENTITY_TYPE);
      Map<EntityTypes<?>, MinecraftKey> mapB_replacement = HashBiMap.create();
      for (Map.Entry<EntityTypes<?>, MinecraftKey> entry : mapB_original.entrySet()) {
        if (entry.getKey() != EntityTypes.DOLPHIN) {
          mapB_replacement.put(entry.getKey(), entry.getValue());
        } else {
          mapB_replacement.put(types, key);
        }
      }
      registryId_b.set(IRegistry.ENTITY_TYPE, mapB_replacement);

      // this is a simple map for EntityTypes by MinecraftKey
      Field registrySimple_fieldC = RegistryMaterials.class.getDeclaredField("c");
      registrySimple_fieldC.setAccessible(true);
      Map<MinecraftKey, EntityTypes<?>> mapC = (Map<MinecraftKey, EntityTypes<?>>) registrySimple_fieldC.get(IRegistry.ENTITY_TYPE);
      mapC.put(key, types);
      registrySimple_fieldC.set(IRegistry.ENTITY_TYPE, mapC);

      // This replaces the constant field in EntityTypes with the custom one
      Field entityTypes_fieldDOLPHIN = EntityTypes.class.getField("DOLPHIN");
      entityTypes_fieldDOLPHIN.setAccessible(true);
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(entityTypes_fieldDOLPHIN, entityTypes_fieldDOLPHIN.getModifiers() & ~Modifier.FINAL);
      entityTypes_fieldDOLPHIN.set(null, types);
    } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
    }

    /*
    // this fixes the spawner egg to point to the correct EntityTypes.
    Item dolphinSpawnEgg = CraftItemStack.asNMSCopy(new ItemStack(Material.DOLPHIN_SPAWN_EGG)).getItem();
    try {
      Field field_d = ItemMonsterEgg.class.getDeclaredField("d");
      field_d.setAccessible(true);
      field_d.set(dolphinSpawnEgg, EntityTypes.DOLPHIN);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    */






    return instance;



  }

  /*

  @SuppressWarnings("unchecked")
  @Override
  public int a(Object key) { // TODO: Update name on version change (getId)
    if (this.customEntityIds.containsKey(key)) {
      return this.customEntityIds.get(key);
    }

    return this.wrapped.a(key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public MinecraftKey b(Object value) { // TODO: Update name on version change (getKey)
    if (this.customEntityClasses.containsKey(value)) {
      return this.customEntityClasses.get(value);
    }

    return (MinecraftKey) wrapped.b(value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class<? extends Entity> get(Object key) {
    if (this.customEntities.containsKey(key)) {
      return this.customEntities.get(key);
    }

    return (Class<? extends Entity>) wrapped.get(key);
  }

  */

  public void putCustomEntity(int entityId, String entityName,
      Class<? extends Entity> entityClass) {
    MinecraftKey minecraftKey = new MinecraftKey(entityName);

    this.customEntities.put(minecraftKey, entityClass);
    this.customEntityIds.put(entityClass, entityId);
  }
}

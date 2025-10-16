package com.urfour.artemis.infos;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinecraftInfos {
    private PlayerInfos Player;
    private WorldInfos World;
    private GUIInfos Gui;
    private static final Logger LOGGER = LogManager.getLogger("MinecraftInfos");

    public void update() {
        // Lazy initialization - only create these when we actually need them
        // This ensures Minecraft classes are loaded only after the game is ready
        if (Player == null) {
            Player = new PlayerInfos();
            World = new WorldInfos();
            Gui = new GUIInfos();
        }
        Player.getInfos();
        World.getInfos();
        Gui.getInfos();
        // Log only occasionally to avoid spam (every 50 updates = ~5 seconds)
        // if (System.currentTimeMillis() % 5000 < 100) {
        //     LOGGER.info("MinecraftInfos updated - Biome: " + World.Biome);
        // }
    }

    private static class PlayerInfos {
        private boolean InGame;
        private float Health;
        private float MaxHealth;
        private float Absorption;
        private boolean IsDead;
        private int ArmorPoints;
        private int ExperienceLevel;
        private float Experience;
        private int FoodLevel;
        private float SaturationLevel;
        private boolean IsSneaking;
        private boolean IsRidingHorse;
        private boolean IsBurning;
        private boolean IsInWater;
        private HashMap<String, Boolean> PlayerEffects = new HashMap<>();
        private static final HashMap<String, RegistryEntry<StatusEffect>> TARGET_EFFECTS;
        private HashMap<String, String> Armor = new HashMap<>();
        private int CurrentHotbarSlot;
        private String LeftHandItem;
        private String RightHandItem;

        static {
            TARGET_EFFECTS = new HashMap<>();
            TARGET_EFFECTS.put("MoveSpeed", StatusEffects.SPEED);
            TARGET_EFFECTS.put("MoveSlowdown", StatusEffects.SLOWNESS);
            TARGET_EFFECTS.put("Haste", StatusEffects.HASTE);
            TARGET_EFFECTS.put("MiningFatigue", StatusEffects.MINING_FATIGUE);
            TARGET_EFFECTS.put("Strength", StatusEffects.STRENGTH);
            TARGET_EFFECTS.put("InstantHealth", StatusEffects.INSTANT_HEALTH);
            TARGET_EFFECTS.put("InstantDamage", StatusEffects.INSTANT_DAMAGE);
            TARGET_EFFECTS.put("JumpBoost", StatusEffects.JUMP_BOOST);
            TARGET_EFFECTS.put("Confusion", StatusEffects.NAUSEA);
            TARGET_EFFECTS.put("Regeneration", StatusEffects.REGENERATION);
            TARGET_EFFECTS.put("Resistance", StatusEffects.RESISTANCE);
            TARGET_EFFECTS.put("FireResistance", StatusEffects.FIRE_RESISTANCE);
            TARGET_EFFECTS.put("WaterBreathing", StatusEffects.WATER_BREATHING);
            TARGET_EFFECTS.put("Invisibility", StatusEffects.INVISIBILITY);
            TARGET_EFFECTS.put("Blindness", StatusEffects.BLINDNESS);
            TARGET_EFFECTS.put("NightVision", StatusEffects.NIGHT_VISION);
            TARGET_EFFECTS.put("Hunger", StatusEffects.HUNGER);
            TARGET_EFFECTS.put("Weakness", StatusEffects.WEAKNESS);
            TARGET_EFFECTS.put("Poison", StatusEffects.POISON);
            TARGET_EFFECTS.put("Wither", StatusEffects.WITHER);
            TARGET_EFFECTS.put("HealthBoost", StatusEffects.HEALTH_BOOST);
            TARGET_EFFECTS.put("Absorption", StatusEffects.ABSORPTION);
            TARGET_EFFECTS.put("Saturation", StatusEffects.SATURATION);
            TARGET_EFFECTS.put("Glowing", StatusEffects.GLOWING);
            TARGET_EFFECTS.put("Levitation", StatusEffects.LEVITATION);
            TARGET_EFFECTS.put("Luck", StatusEffects.LUCK);
            TARGET_EFFECTS.put("BadLuck", StatusEffects.UNLUCK);
            TARGET_EFFECTS.put("SlowFalling", StatusEffects.SLOW_FALLING);
            TARGET_EFFECTS.put("ConduitPower", StatusEffects.CONDUIT_POWER);
            TARGET_EFFECTS.put("DolphinsGrace", StatusEffects.DOLPHINS_GRACE);
            TARGET_EFFECTS.put("Bad_omen", StatusEffects.BAD_OMEN);
            TARGET_EFFECTS.put("VillageHero", StatusEffects.HERO_OF_THE_VILLAGE);
        }
        private String testIfAir(ItemStack item) {
            if (item.isEmpty() || item.getItem() == Items.AIR) {
                return null;
            } else {
                Identifier id = Registries.ITEM.getId(item.getItem());
                return id != null ? id.toString().replace("minecraft:", "") : null;
            }
        }
        private void getInfos() {
            try {
                PlayerEntity player = MinecraftClient.getInstance().player;
                assert player != null;
                Health = player.getHealth();
                MaxHealth = player.getMaxHealth();
                Absorption = player.getAbsorptionAmount();
                IsDead = !player.isLiving();
                ArmorPoints = player.getArmor();
                ExperienceLevel = player.experienceLevel;
                Experience = player.experienceProgress;
                FoodLevel = player.getHungerManager().getFoodLevel();
                SaturationLevel = player.getHungerManager().getSaturationLevel();
                IsSneaking = player.isSneaking();
                IsRidingHorse = player.hasVehicle();
                IsBurning = player.isOnFire();
                IsInWater = player.isSubmergedInWater();
                for (Map.Entry<String, RegistryEntry<StatusEffect>> effect : TARGET_EFFECTS.entrySet())
                    PlayerEffects.put(effect.getKey(), player.hasStatusEffect(effect.getValue()));

                RightHandItem = testIfAir(player.getEquippedStack(EquipmentSlot.MAINHAND));
                LeftHandItem = testIfAir(player.getEquippedStack(EquipmentSlot.OFFHAND));
                CurrentHotbarSlot = player.getInventory().getSelectedSlot();
                Armor.put("boots", testIfAir(player.getEquippedStack(EquipmentSlot.FEET)));
                Armor.put("leggings", testIfAir(player.getEquippedStack(EquipmentSlot.LEGS)));
                Armor.put("chestplate", testIfAir(player.getEquippedStack(EquipmentSlot.CHEST)));
                Armor.put("helmet", testIfAir(player.getEquippedStack(EquipmentSlot.HEAD)));
                InGame = true;
            } catch (Exception ex) {
                InGame = false;
            }

        }
    }

    private static class WorldInfos {
        private long WorldTime;
        private boolean IsDayTime;
        private boolean IsRaining;
        private float RainStrength;
        private String Dimension;
        private String Biome;

        private void getInfos() {
            try {
                ClientWorld world = MinecraftClient.getInstance().world;
                assert world != null;
                WorldTime = world.getTimeOfDay();
                IsDayTime = world.isDay();
                RainStrength = world.getRainGradient(1);
                IsRaining = world.isRaining();
                Dimension = world.getRegistryKey().getValue().toString();
                Biome = world.getBiome(MinecraftClient.getInstance().player.getBlockPos()).getKey().get().getValue().toString();
            } catch (Exception ex) {

            }
        }
    }
    private static class GUIInfos {
        private static class KeyCode {
            public String Code;
            public String Context;

            public KeyCode(String code, String context) {
                this.Code = code;
                this.Context = context;
            }
        }
        private boolean OptionsGuiOpen;
        private boolean ControlsGuiOpen;
        private boolean ChatGuiOpen;
        private boolean KeybindsGuiOpen;
        private KeyCode[] keys;

        private void getInfos() {
            try {
                MinecraftClient client = MinecraftClient.getInstance();
                ChatGuiOpen = client.currentScreen instanceof ChatScreen;
                OptionsGuiOpen = client.currentScreen instanceof OptionsScreen;
                ControlsGuiOpen = client.currentScreen instanceof ControlsOptionsScreen;
                KeybindsGuiOpen = client.currentScreen instanceof KeybindsScreen;
                /*keys = null;
                if (ControlsGuiOpen) {
                    KeyBinding[] temp = client.options.;
                    List<KeyCode> tempList = new ArrayList<>();
                    for (KeyBinding key : temp) {
                        System.out.println(key.toString())
                        if (!key.getTranslationKey().contains("unknown") && key.getTranslationKey().contains("keyboard")) {
                            String context = key.getCategory().equals("key.categories.inventory") ? "GUI" : "UNIVERSAL";
                            tempList.add(new KeyCode(key.getTranslationKey(), null, context));
                        }
                    }
                    keys = new KeyCode[tempList.size()];
                    keys = tempList.toArray(keys);
                }*/
            } catch (Exception ignore) {

            }
        }
    }
}

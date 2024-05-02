package com.urfour.artemis.infos;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinecraftInfos {
    private PlayerInfos Player = new PlayerInfos();
    private WorldInfos World = new WorldInfos();
    private GUIInfos Gui = new GUIInfos();

    public void update() {
        Player.getInfos();
        World.getInfos();
        Gui.getInfos();
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
        private static final HashMap<String, StatusEffect> TARGET_EFFECTS;
        private HashMap<String, String> armor = new HashMap<>();
        private int CurrentHotbarSlot;
        private String LeftHandItem;
        private String RightHandItem;

        static {
            TARGET_EFFECTS = new HashMap<>();
            TARGET_EFFECTS.put("MoveSpeed", StatusEffect.byRawId(1));
            TARGET_EFFECTS.put("MoveSlowdown", StatusEffect.byRawId(2));
            TARGET_EFFECTS.put("Haste", StatusEffect.byRawId(3));
            TARGET_EFFECTS.put("MiningFatigue", StatusEffect.byRawId(4));
            TARGET_EFFECTS.put("Strength", StatusEffect.byRawId(5));
            TARGET_EFFECTS.put("InstantHealth", StatusEffect.byRawId(6));
            TARGET_EFFECTS.put("InstantDamage", StatusEffect.byRawId(7));
            TARGET_EFFECTS.put("JumpBoost", StatusEffect.byRawId(8));
            TARGET_EFFECTS.put("Confusion", StatusEffect.byRawId(9));
            TARGET_EFFECTS.put("Regeneration", StatusEffect.byRawId(10));
            TARGET_EFFECTS.put("Resistance", StatusEffect.byRawId(11));
            TARGET_EFFECTS.put("FireResistance", StatusEffect.byRawId(12));
            TARGET_EFFECTS.put("WaterBreathing", StatusEffect.byRawId(13));
            TARGET_EFFECTS.put("Invisibility", StatusEffect.byRawId(14));
            TARGET_EFFECTS.put("Blindness", StatusEffect.byRawId(15));
            TARGET_EFFECTS.put("NightVision", StatusEffect.byRawId(16));
            TARGET_EFFECTS.put("Hunger", StatusEffect.byRawId(17));
            TARGET_EFFECTS.put("Weakness", StatusEffect.byRawId(18));
            TARGET_EFFECTS.put("Poison", StatusEffect.byRawId(19));
            TARGET_EFFECTS.put("Wither", StatusEffect.byRawId(20));
            TARGET_EFFECTS.put("HealthBoost", StatusEffect.byRawId(21));
            TARGET_EFFECTS.put("Absorption", StatusEffect.byRawId(22));
            TARGET_EFFECTS.put("Saturation", StatusEffect.byRawId(23));
            TARGET_EFFECTS.put("Glowing", StatusEffect.byRawId(24));
            TARGET_EFFECTS.put("Levitation", StatusEffect.byRawId(25));
            TARGET_EFFECTS.put("Luck", StatusEffect.byRawId(26));
            TARGET_EFFECTS.put("BadLuck", StatusEffect.byRawId(27));
            TARGET_EFFECTS.put("SlowFalling", StatusEffect.byRawId(28));
            TARGET_EFFECTS.put("ConduitPower", StatusEffect.byRawId(29));
            TARGET_EFFECTS.put("DolphinsGrace", StatusEffect.byRawId(30));
            TARGET_EFFECTS.put("Bad_omen", StatusEffect.byRawId(31));
            TARGET_EFFECTS.put("VillageHero", StatusEffect.byRawId(32));
        }
        private String testIfAir(ItemStack item) {
            if (item.getTranslationKey().equals("block.minecraft.air")) {
                return null;
            }
            else {
                return item.getTranslationKey().replace("block.", "");
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
                for (Map.Entry<String, StatusEffect> effect : TARGET_EFFECTS.entrySet())
                    PlayerEffects.put(effect.getKey(), player.getStatusEffect(effect.getValue()) != null);
                ArrayList<String> handItems = new ArrayList<>();
                ArrayList<String> armorItems = new ArrayList<>();
                player.getHandItems().forEach(item -> handItems.add(testIfAir(item)));
                player.getArmorItems().forEach(item -> handItems.add(testIfAir(item)));
                RightHandItem = handItems.get(0);
                LeftHandItem = handItems.get(1);
                CurrentHotbarSlot = player.getInventory().selectedSlot;
                if (!armorItems.isEmpty()) {
                    armor.put("boots", armorItems.get(0));
                    armor.put("leggings", armorItems.get(1));
                    armor.put("chestplate", armorItems.get(2));
                    armor.put("helmet", armorItems.get(3));
                }
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
                PlayerEntity player = MinecraftClient.getInstance().player;
                assert player != null;
                WorldTime = world.getTimeOfDay();
                IsDayTime = world.isDay();
                RainStrength = world.getRainGradient(1);
                IsRaining = world.isRaining();
                Dimension = world.getRegistryKey().getValue().toString();
                Biome = world.getBiome(player.getBlockPos()).getKey().get().getValue().toString();
            } catch (Exception ex) {

            }
        }
    }
    private static class GUIInfos {
        private static class KeyCode {
            public String code;
            public String context;

            public KeyCode(String code, String context) {
                this.code = code;
                this.context = context;
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
                if (controlsGuiOpen) {
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

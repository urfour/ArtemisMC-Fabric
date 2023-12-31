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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinecraftInfos {
    private PlayerInfos player = new PlayerInfos();
    private WorldInfos world = new WorldInfos();
    private GUIInfos gui = new GUIInfos();

    public void update() {
        player.getInfos();
        world.getInfos();
        gui.getInfos();
    }

    private static class PlayerInfos {
        private boolean inGame;
        private float health;
        private float maxHealth;
        private float absorption;
        private boolean isDead;
        private int armorPoints;
        private int experienceLevel;
        private float experience;
        private int foodLevel;
        private float saturationLevel;
        private boolean isSneaking;
        private boolean isRidingHorse;
        private boolean isBurning;
        private boolean isInWater;
        private HashMap<String, Boolean> playerEffects = new HashMap<>();
        private static final HashMap<String, StatusEffect> TARGET_EFFECTS;
        private HashMap<String, String> armor = new HashMap<>();
        private int currentHotbarSlot;
        private String leftHandItem;
        private String rightHandItem;

        static {
            TARGET_EFFECTS = new HashMap<>();
            TARGET_EFFECTS.put("moveSpeed", StatusEffect.byRawId(1));
            TARGET_EFFECTS.put("moveSlowdown", StatusEffect.byRawId(2));
            TARGET_EFFECTS.put("haste", StatusEffect.byRawId(3));
            TARGET_EFFECTS.put("miningFatigue", StatusEffect.byRawId(4));
            TARGET_EFFECTS.put("strength", StatusEffect.byRawId(5));
            TARGET_EFFECTS.put("instantHealth", StatusEffect.byRawId(6));
            TARGET_EFFECTS.put("instantDamage", StatusEffect.byRawId(7));
            TARGET_EFFECTS.put("jumpBoost", StatusEffect.byRawId(8));
            TARGET_EFFECTS.put("confusion", StatusEffect.byRawId(9));
            TARGET_EFFECTS.put("regeneration", StatusEffect.byRawId(10));
            TARGET_EFFECTS.put("resistance", StatusEffect.byRawId(11));
            TARGET_EFFECTS.put("fireResistance", StatusEffect.byRawId(12));
            TARGET_EFFECTS.put("waterBreathing", StatusEffect.byRawId(13));
            TARGET_EFFECTS.put("invisibility", StatusEffect.byRawId(14));
            TARGET_EFFECTS.put("blindness", StatusEffect.byRawId(15));
            TARGET_EFFECTS.put("nightVision", StatusEffect.byRawId(16));
            TARGET_EFFECTS.put("hunger", StatusEffect.byRawId(17));
            TARGET_EFFECTS.put("weakness", StatusEffect.byRawId(18));
            TARGET_EFFECTS.put("poison", StatusEffect.byRawId(19));
            TARGET_EFFECTS.put("wither", StatusEffect.byRawId(20));
            TARGET_EFFECTS.put("healthBoost", StatusEffect.byRawId(21));
            TARGET_EFFECTS.put("absorption", StatusEffect.byRawId(22));
            TARGET_EFFECTS.put("saturation", StatusEffect.byRawId(23));
            TARGET_EFFECTS.put("glowing", StatusEffect.byRawId(24));
            TARGET_EFFECTS.put("levitation", StatusEffect.byRawId(25));
            TARGET_EFFECTS.put("luck", StatusEffect.byRawId(26));
            TARGET_EFFECTS.put("badLuck", StatusEffect.byRawId(27));
            TARGET_EFFECTS.put("slowFalling", StatusEffect.byRawId(28));
            TARGET_EFFECTS.put("conduitPower", StatusEffect.byRawId(29));
            TARGET_EFFECTS.put("dolphinsGrace", StatusEffect.byRawId(30));
            TARGET_EFFECTS.put("bad_omen", StatusEffect.byRawId(31));
            TARGET_EFFECTS.put("villageHero", StatusEffect.byRawId(32));
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
                health = player.getHealth();
                maxHealth = player.getMaxHealth();
                absorption = player.getAbsorptionAmount();
                isDead = !player.isLiving();
                armorPoints = player.getArmor();
                experienceLevel = player.experienceLevel;
                experience = player.experienceProgress;
                foodLevel = player.getHungerManager().getFoodLevel();
                saturationLevel = player.getHungerManager().getSaturationLevel();
                isSneaking = player.isSneaking();
                isRidingHorse = player.hasVehicle();
                isBurning = player.isOnFire();
                isInWater = player.isSubmergedInWater();
                for (Map.Entry<String, StatusEffect> effect : TARGET_EFFECTS.entrySet())
                    playerEffects.put(effect.getKey(), player.getStatusEffect(effect.getValue()) != null);
                ArrayList<String> handItems = new ArrayList<>();
                ArrayList<String> armorItems = new ArrayList<>();
                player.getHandItems().forEach(item -> handItems.add(testIfAir(item)));
                player.getArmorItems().forEach(item -> handItems.add(testIfAir(item)));
                rightHandItem = handItems.get(0);
                leftHandItem = handItems.get(1);
                currentHotbarSlot = player.getInventory().selectedSlot;
                if (!armorItems.isEmpty()) {
                    armor.put("boots", armorItems.get(0));
                    armor.put("leggings", armorItems.get(1));
                    armor.put("chestplate", armorItems.get(2));
                    armor.put("helmet", armorItems.get(3));
                }
                inGame = true;
            } catch (Exception ex) {
                inGame = false;
            }

        }
    }

    private static class WorldInfos {
        private long worldTime;
        private boolean isDayTime;
        private boolean isRaining;
        private float rainStrength;
        private String dimension;

        private void getInfos() {
            try {
                ClientWorld world = MinecraftClient.getInstance().world;
                assert world != null;
                worldTime = world.getTimeOfDay();
                isDayTime = world.isDay();
                rainStrength = world.getRainGradient(1);
                isRaining = world.isRaining();
                dimension = world.getRegistryKey().getValue().toString();
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
        private boolean optionsGuiOpen;
        private boolean controlsGuiOpen;
        private boolean chatGuiOpen;
        private boolean keybindsGuiOpen;
        private KeyCode[] keys;

        private void getInfos() {
            try {
                MinecraftClient client = MinecraftClient.getInstance();
                chatGuiOpen = client.currentScreen instanceof ChatScreen;
                optionsGuiOpen = client.currentScreen instanceof OptionsScreen;
                controlsGuiOpen = client.currentScreen instanceof ControlsOptionsScreen;
                keybindsGuiOpen = client.currentScreen instanceof KeybindsScreen;
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

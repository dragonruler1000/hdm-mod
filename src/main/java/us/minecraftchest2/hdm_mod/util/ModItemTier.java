package us.minecraftchest2.hdm_mod.util;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import us.minecraftchest2.hdm_mod.init.ItemInit;

import java.util.function.Supplier;

public enum ModItemTier implements IItemTier {
    MGTI(4, 30000, 10.0F, 20.0F, 5, () -> {
        return Ingredient.of(ItemInit.KNIFE.get());
    });

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyValue<Ingredient> repairIngredient;

    ModItemTier(int level, int durability, float miningSpeed, float damage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.level = level;
        this.uses = durability;
        this.speed = miningSpeed;
        this.damage = damage;
        this.enchantmentValue = enchantability;
        this.repairIngredient = new LazyValue<>(repairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
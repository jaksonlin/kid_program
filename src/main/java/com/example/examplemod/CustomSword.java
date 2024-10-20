package com.example.examplemod;

import org.joml.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;

import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class CustomSword extends SwordItem {
    private static final int CREEPER_COUNT = 5; // Number of creepers to spawn
    private static final double SPAWN_RADIUS = 3.0; // Radius around the target to spawn creepers

    public CustomSword() {
        super(Tiers.DIAMOND, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 30, -4.4F)));
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Set the target on fire for 5 seconds (100 ticks)
        target.setRemainingFireTicks(100);
        // Add lighting effect in a 9x9x9 area
        Level world = target.level();
        BlockPos centerPos = target.blockPosition();
         Random random = new Random();
         // Add lighting effect in a 9x9 area
         for (int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    BlockPos lightPos = centerPos.offset(x, y, z);
                    
                        world.setBlockAndUpdate(lightPos, Blocks.LIGHT.defaultBlockState());
                        
                        // Schedule the light to be removed after a short delay (e.g., 60 ticks or 3 seconds)
                        world.scheduleTick(lightPos, Blocks.LIGHT, 60);
                    
                }
            }
        }
        // Summon a swarm of creepers
        for (int i = 0; i < CREEPER_COUNT; i++) {
            double x = target.getX() + (random.nextFloat() - 0.5) * 2 * SPAWN_RADIUS;
            double y = target.getY();
            double z = target.getZ() + (random.nextFloat() - 0.5) * 2 * SPAWN_RADIUS;
            
            Creeper creeper = EntityType.CREEPER.create(world);
            if (creeper != null) {
                creeper.setPos(x, y, z);
                world.addFreshEntity(creeper);
            }
        }
        
        // Call the parent method to ensure normal sword behavior
        return super.hurtEnemy(stack, target, attacker);
    }
}

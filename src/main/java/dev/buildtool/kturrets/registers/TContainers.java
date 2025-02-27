package dev.buildtool.kturrets.registers;

import dev.buildtool.kturrets.KTurrets;
import dev.buildtool.kturrets.arrow.ArrowTurretContainer;
import dev.buildtool.kturrets.brick.BrickTurretContainer;
import dev.buildtool.kturrets.bullet.BulletTurretContainer;
import dev.buildtool.kturrets.cobble.CobbleTurretContainer;
import dev.buildtool.kturrets.firecharge.FireChargeTurretContainer;
import dev.buildtool.kturrets.gauss.GaussTurretContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TContainers {
    public static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, KTurrets.ID);
    static public MenuType<ArrowTurretContainer> ARROW_TURRET;
    public static MenuType<BulletTurretContainer> BULLET_TURRET;
    public static MenuType<FireChargeTurretContainer> FIRE_CHARGE_TURRET;
    public static MenuType<BrickTurretContainer> BRICK_TURRET;
    public static MenuType<GaussTurretContainer> GAUSS_TURRET;
    public static MenuType<CobbleTurretContainer> COBBLE_TURRET;

    static {
        ARROW_TURRET = IForgeMenuType.create(ArrowTurretContainer::new);
        CONTAINERS.register("arrow_turret", () -> ARROW_TURRET);
        BULLET_TURRET = IForgeMenuType.create(BulletTurretContainer::new);
        CONTAINERS.register("bullet_turret", () -> BULLET_TURRET);
        FIRE_CHARGE_TURRET = IForgeMenuType.create(FireChargeTurretContainer::new);
        CONTAINERS.register("fire_charge_turret", () -> FIRE_CHARGE_TURRET);
        BRICK_TURRET = IForgeMenuType.create(BrickTurretContainer::new);
        CONTAINERS.register("brick_turret", () -> BRICK_TURRET);
        GAUSS_TURRET = IForgeMenuType.create(GaussTurretContainer::new);
        CONTAINERS.register("gauss_turret", () -> GAUSS_TURRET);
        COBBLE_TURRET = IForgeMenuType.create(CobbleTurretContainer::new);
        CONTAINERS.register("cobble_turret", () -> COBBLE_TURRET);
    }
}

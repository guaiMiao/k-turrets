package dev.buildtool.kturrets.registers;

import dev.buildtool.kturrets.EntityRenderer2;
import dev.buildtool.kturrets.arrow.ArrowTurretContainer;
import dev.buildtool.kturrets.arrow.ArrowTurretModelv3;
import dev.buildtool.kturrets.arrow.ArrowTurretScreen;
import dev.buildtool.kturrets.brick.*;
import dev.buildtool.kturrets.bullet.*;
import dev.buildtool.kturrets.cobble.*;
import dev.buildtool.kturrets.firecharge.FireChargeScreen;
import dev.buildtool.kturrets.firecharge.FireChargeTurretContainer;
import dev.buildtool.kturrets.firecharge.FirechargeTurretModelv3;
import dev.buildtool.kturrets.gauss.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SuppressWarnings("RedundantCast")
    @SubscribeEvent
    public static void register(FMLClientSetupEvent clientSetupEvent) {
        MenuScreens.register(TContainers.ARROW_TURRET, (MenuScreens.ScreenConstructor<ArrowTurretContainer, ArrowTurretScreen>) (p_create_1_, p_create_2_, p_create_3_) -> new ArrowTurretScreen(p_create_1_, p_create_2_, p_create_3_, true));
        MenuScreens.register(TContainers.BULLET_TURRET, (MenuScreens.ScreenConstructor<BulletTurretContainer, BulletScreen>) (p1, p2, p3) -> new BulletScreen(p1, p2, p3, true));
        MenuScreens.register(TContainers.FIRE_CHARGE_TURRET, (MenuScreens.ScreenConstructor<FireChargeTurretContainer, FireChargeScreen>) (p1, p2, p3) -> new FireChargeScreen(p1, p2, p3, true));
        MenuScreens.register(TContainers.BRICK_TURRET, (MenuScreens.ScreenConstructor<BrickTurretContainer, BrickTurretScreen>) (p1, p2, p3) -> new BrickTurretScreen(p1, p2, p3, true));
        MenuScreens.register(TContainers.GAUSS_TURRET, (MenuScreens.ScreenConstructor<GaussTurretContainer, GaussTurretScreen>) (p1, p2, p3) -> new GaussTurretScreen(p1, p2, p3, true));
        MenuScreens.register(TContainers.COBBLE_TURRET, (MenuScreens.ScreenConstructor<CobbleTurretContainer, CobbleTurretScreen>) (p1, p2, p3) -> new CobbleTurretScreen(p1, p2, p3, true));
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions definitions) {
        definitions.registerLayerDefinition(ArrowTurretModelv3.LAYER_LOCATION, ArrowTurretModelv3::createBodyLayer);
        definitions.registerLayerDefinition(BrickTurretModelv2.LAYER_LOCATION, BrickTurretModelv2::createBodyLayer);
        definitions.registerLayerDefinition(BulletTurretModelv3.LAYER_LOCATION, BulletTurretModelv3::createBodyLayer);
        definitions.registerLayerDefinition(CobbleTurretModelv3.LAYER_LOCATION, CobbleTurretModelv3::createBodyLayer);
        definitions.registerLayerDefinition(FirechargeTurretModelv3.LAYER_LOCATION, FirechargeTurretModelv3::createBodyLayer);
        definitions.registerLayerDefinition(GaussTurretModelv2.LAYER_LOCATION, GaussTurretModelv2::createBodyLayer);
        definitions.registerLayerDefinition(BrickModel.LAYER_LOCATION, BrickModel::createBodyLayer);
        definitions.registerLayerDefinition(BulletModel.LAYER_LOCATION, BulletModel::createBodyLayer);
        definitions.registerLayerDefinition(CobblestoneModel.LAYER_LOCATION, CobblestoneModel::createBodyLayer);
        definitions.registerLayerDefinition(GaussBulletModel.LAYER_LOCATION, GaussBulletModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers renderers) {
        renderers.registerEntityRenderer(TEntities.ARROW_TURRET.get(), manager -> new EntityRenderer2<>(manager, new ArrowTurretModelv3<>(manager.bakeLayer(ArrowTurretModelv3.LAYER_LOCATION)), "arrow_turret2", false, 0.4f));
        renderers.registerEntityRenderer(TEntities.COBBLE_TURRET.get(), manager -> new EntityRenderer2<>(manager, new CobbleTurretModelv3<>(manager.bakeLayer(CobbleTurretModelv3.LAYER_LOCATION)), "cobble_turret2", false, 0.2f));
        renderers.registerEntityRenderer(TEntities.GAUSS_TURRET.get(), manager -> new EntityRenderer2<>(manager, new GaussTurretModelv2<>(manager.bakeLayer(GaussTurretModelv2.LAYER_LOCATION)), "gaussturret", false, 0.2f));
        renderers.registerEntityRenderer(TEntities.BRICK_TURRET.get(), manager -> new EntityRenderer2<>(manager, new BrickTurretModelv2<>(manager.bakeLayer(BrickTurretModelv2.LAYER_LOCATION)), "brick_turret", false, 0.4f));
        renderers.registerEntityRenderer(TEntities.FIRE_CHARGE_TURRET.get(), manager -> new EntityRenderer2<>(manager, new FirechargeTurretModelv3<>(manager.bakeLayer(FirechargeTurretModelv3.LAYER_LOCATION)), "firecharge_turret2", false, 0.3f));
        renderers.registerEntityRenderer(TEntities.BULLET_TURRET.get(), manager -> new EntityRenderer2<>(manager, new BulletTurretModelv3<>(manager.bakeLayer(BulletTurretModelv3.LAYER_LOCATION)), "bullet_turret2", false, 0.4f));

        renderers.registerEntityRenderer(TEntities.BRICK.get(), BrickRenderer::new);
        renderers.registerEntityRenderer(TEntities.GAUSS_BULLET.get(), GaussBulletRenderer::new);
        renderers.registerEntityRenderer(TEntities.COBBLESTONE.get(), CobblestoneRenderer::new);
        renderers.registerEntityRenderer(TEntities.BULLET.get(), BulletRenderer::new);
    }
}

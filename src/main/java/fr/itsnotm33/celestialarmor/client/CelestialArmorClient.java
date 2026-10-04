package fr.itsnotm33.celestialarmor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.itsnotm33.celestialarmor.CelestialArmorMod;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = CelestialArmorMod.MODID, value = Dist.CLIENT)
public final class CelestialArmorClient {
    private static final ResourceLocation WHITE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CelestialArmorMod.MODID, "textures/entity/equipment/white.png");

    private CelestialArmorClient() {}

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (var skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new CelestialArmorLayer(playerRenderer));
            }
        }
    }

    private static final class CelestialArmorLayer extends RenderLayer<PlayerRenderState, PlayerModel> {
        private CelestialArmorLayer(RenderLayerParent<PlayerRenderState, PlayerModel> parent) {
            super(parent);
        }

        @Override
        public void render(
                PoseStack poseStack,
                MultiBufferSource bufferSource,
                int packedLight,
                PlayerRenderState state,
                float yRot,
                float xRot
        ) {
            if (state.isInvisible) {
                return;
            }

            PlayerModel model = getParentModel();
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(WHITE_TEXTURE));

            if (is(state.headItem, CelestialArmorMod.CELESTIAL_HELMET.get())) {
                CelestialArmorModel.renderAttached(
                        poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        model.head, CelestialMeshData.HEAD
                );
            }

            if (is(state.chestItem, CelestialArmorMod.CELESTIAL_CHESTPLATE.get())) {
                CelestialArmorModel.renderAttached(
                        poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        model.body, CelestialMeshData.BODY
                );
                CelestialArmorModel.renderAttached(
                        poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        model.rightArm, CelestialMeshData.RIGHT_ARM
                );
                CelestialArmorModel.renderAttached(
                        poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        model.leftArm, CelestialMeshData.LEFT_ARM
                );
            }

            if (is(state.legsItem, CelestialArmorMod.CELESTIAL_LEGGINGS.get())) {
                CelestialArmorModel.renderAttached(
                        poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        model.rightLeg, CelestialMeshData.RIGHT_LEG
                );
                CelestialArmorModel.renderAttached(
                        poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        model.leftLeg, CelestialMeshData.LEFT_LEG
                );
            }

            if (is(state.feetItem, CelestialArmorMod.CELESTIAL_BOOTS.get())) {
                CelestialArmorModel.renderAttached(
                        poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        model.rightLeg, CelestialMeshData.RIGHT_BOOT
                );
                CelestialArmorModel.renderAttached(
                        poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        model.leftLeg, CelestialMeshData.LEFT_BOOT
                );
            }
        }

        private static boolean is(ItemStack stack, net.minecraft.world.item.Item item) {
            return stack != null && !stack.isEmpty() && stack.is(item);
        }
    }
}

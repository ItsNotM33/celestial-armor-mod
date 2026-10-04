package fr.itsnotm33.celestialarmor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.itsnotm33.celestialarmor.CelestialArmorMod;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;

final class CelestialArmorModel extends Model {
    private final HumanoidModel<?> base;
    private final Item armorItem;

    CelestialArmorModel(HumanoidModel<?> base, Item armorItem) {
        super(RenderType::entityCutoutNoCull);
        this.base = base;
        this.armorItem = armorItem;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
        if (armorItem == CelestialArmorMod.CELESTIAL_HELMET.get()) {
            renderAttached(poseStack, consumer, packedLight, packedOverlay, base.head, CelestialMeshData.HEAD);
        } else if (armorItem == CelestialArmorMod.CELESTIAL_CHESTPLATE.get()) {
            renderAttached(poseStack, consumer, packedLight, packedOverlay, base.body, CelestialMeshData.BODY);
            renderAttached(poseStack, consumer, packedLight, packedOverlay, base.rightArm, CelestialMeshData.RIGHT_ARM);
            renderAttached(poseStack, consumer, packedLight, packedOverlay, base.leftArm, CelestialMeshData.LEFT_ARM);
        } else if (armorItem == CelestialArmorMod.CELESTIAL_LEGGINGS.get()) {
            renderAttached(poseStack, consumer, packedLight, packedOverlay, base.rightLeg, CelestialMeshData.RIGHT_LEG);
            renderAttached(poseStack, consumer, packedLight, packedOverlay, base.leftLeg, CelestialMeshData.LEFT_LEG);
        } else if (armorItem == CelestialArmorMod.CELESTIAL_BOOTS.get()) {
            renderAttached(poseStack, consumer, packedLight, packedOverlay, base.rightLeg, CelestialMeshData.RIGHT_BOOT);
            renderAttached(poseStack, consumer, packedLight, packedOverlay, base.leftLeg, CelestialMeshData.LEFT_BOOT);
        }
    }

    private static void renderAttached(
            PoseStack poseStack,
            VertexConsumer consumer,
            int packedLight,
            int packedOverlay,
            ModelPart parent,
            int sectionId
    ) {
        CelestialMeshData.Section section = CelestialMeshData.section(sectionId);
        if (section == null) {
            return;
        }

        poseStack.pushPose();
        parent.translateAndRotate(poseStack);
        PoseStack.Pose pose = poseStack.last();

        float[] positions = section.positions();
        float[] normals = section.normals();
        int[] colors = section.colors();

        for (int vertexIndex : section.indices()) {
            int p = vertexIndex * 3;
            consumer.addVertex(pose, positions[p], positions[p + 1], positions[p + 2])
                    .setColor(colors[vertexIndex])
                    .setUv(0.5F, 0.5F)
                    .setOverlay(packedOverlay)
                    .setLight(packedLight)
                    .setNormal(pose, normals[p], normals[p + 1], normals[p + 2]);
        }

        poseStack.popPose();
    }
}

package fr.itsnotm33.celestialarmor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

final class CelestialArmorModel {
    private CelestialArmorModel() {}

    static void renderAttached(
            PoseStack poseStack,
            VertexConsumer consumer,
            int packedLight,
            int packedOverlay,
            ModelPart parent,
            int sectionId
    ) {
        CelestialMeshData.Section section = CelestialMeshData.section(sectionId);
        if (section == null || !parent.visible) {
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

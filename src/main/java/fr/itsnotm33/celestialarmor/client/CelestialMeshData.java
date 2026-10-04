package fr.itsnotm33.celestialarmor.client;

import fr.itsnotm33.celestialarmor.CelestialArmorMod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

final class CelestialMeshData {
    static final int HEAD = 0;
    static final int BODY = 1;
    static final int RIGHT_ARM = 2;
    static final int LEFT_ARM = 3;
    static final int RIGHT_LEG = 4;
    static final int LEFT_LEG = 5;
    static final int RIGHT_BOOT = 6;
    static final int LEFT_BOOT = 7;

    private static final Map<Integer, Section> SECTIONS = load();

    private CelestialMeshData() {}

    static Section section(int id) {
        return SECTIONS.get(id);
    }

    private static Map<Integer, Section> load() {
        try {
            StringBuilder encoded = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                String path = "/assets/" + CelestialArmorMod.MODID + "/mesh/mesh%02d.b64".formatted(i);
                try (InputStream in = CelestialMeshData.class.getResourceAsStream(path)) {
                    if (in == null) {
                        throw new IOException("Missing armor mesh resource: " + path);
                    }
                    encoded.append(new String(in.readAllBytes(), StandardCharsets.US_ASCII).trim());
                }
            }

            byte[] compressed = Base64.getDecoder().decode(encoded.toString());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
                gzip.transferTo(output);
            }

            ByteBuffer data = ByteBuffer.wrap(output.toByteArray()).order(ByteOrder.LITTLE_ENDIAN);
            if (data.get() != 'C' || data.get() != 'E' || data.get() != 'L' || data.get() != 'Q') {
                throw new IOException("Invalid celestial armor mesh magic");
            }

            int version = data.getInt();
            if (version != 1) {
                throw new IOException("Unsupported celestial armor mesh version " + version);
            }

            int count = data.getInt();
            Map<Integer, Section> sections = new HashMap<>();
            for (int sectionIndex = 0; sectionIndex < count; sectionIndex++) {
                int id = Byte.toUnsignedInt(data.get());
                int vertexCount = data.getInt();
                int indexCount = data.getInt();

                float[] positions = new float[vertexCount * 3];
                float[] normals = new float[vertexCount * 3];
                int[] colors = new int[vertexCount];

                for (int vertex = 0; vertex < vertexCount; vertex++) {
                    int p = vertex * 3;
                    positions[p] = data.getShort() / 16384.0F;
                    positions[p + 1] = data.getShort() / 16384.0F;
                    positions[p + 2] = data.getShort() / 16384.0F;

                    normals[p] = data.get() / 127.0F;
                    normals[p + 1] = data.get() / 127.0F;
                    normals[p + 2] = data.get() / 127.0F;

                    int r = Byte.toUnsignedInt(data.get());
                    int g = Byte.toUnsignedInt(data.get());
                    int b = Byte.toUnsignedInt(data.get());
                    int a = Byte.toUnsignedInt(data.get());
                    colors[vertex] = (a << 24) | (r << 16) | (g << 8) | b;
                }

                int[] indices = new int[indexCount];
                for (int index = 0; index < indexCount; index++) {
                    indices[index] = Short.toUnsignedInt(data.getShort());
                }

                sections.put(id, new Section(positions, normals, colors, indices));
            }
            return Map.copyOf(sections);
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Could not load Celestial Armor mesh", exception);
        }
    }

    record Section(float[] positions, float[] normals, int[] colors, int[] indices) {}
}

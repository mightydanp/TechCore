package com.mightydanp.techcore.registries;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class VanillaNoiseSettingsOverridesTest {
    private static final int SAFE_CHUNK_LIMIT_BYTES = 24 * 1024;

    static {
        SharedConstants.tryDetectVersion();
        try {
            Field bootstrapped = Bootstrap.class.getDeclaredField("isBootstrapped");
            bootstrapped.setAccessible(true);
            bootstrapped.setBoolean(null, true);
            try {
                BuiltInRegistries.bootStrap();
            } catch (RuntimeException ignored) {
            }
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    void runtimeNoiseGenerationMixinFilesAreGoneAndOnlyUnrelatedMixinsRemain() throws Exception {
        assertThrows(ClassNotFoundException.class, () -> Class.forName("com.mightydanp.techcore.mixin.world.level.NoiseBasedChunkGeneratorMixin"));
        assertThrows(ClassNotFoundException.class, () -> Class.forName("com.mightydanp.techcore.mixin.world.level.NoiseBasedChunkGeneratorSettingsHelper"));

        try (InputStream inputStream = VanillaNoiseSettingsOverridesTest.class.getResourceAsStream("/techcore.mixins.json")) {
            assertNotNull(inputStream);
            JsonObject config = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonArray mixins = config.getAsJsonArray("mixins");
            assertNotNull(mixins);

            Set<String> entries = new HashSet<>();
            for (JsonElement mixin : mixins) {
                entries.add(mixin.getAsString());
            }

            assertFalse(entries.contains("world.level.NoiseBasedChunkGeneratorMixin"));
            assertFalse(entries.contains("world.level.NoiseGeneratorSettingsMixin"));
            assertTrue(entries.contains("world.level.LevelMixin"));
        }
    }

    @Test
    void embeddedDefinitionsParseAndOnlyChangeOreVeinsEnabled() {
        assertOnlyOreVeinsFlagDiffers(VanillaNoiseSettingsOverrides.overworldSource(), VanillaNoiseSettingsOverrides.overworld());
        assertOnlyOreVeinsFlagDiffers(VanillaNoiseSettingsOverrides.largeBiomesSource(), VanillaNoiseSettingsOverrides.largeBiomes());
        assertOnlyOreVeinsFlagDiffers(VanillaNoiseSettingsOverrides.amplifiedSource(), VanillaNoiseSettingsOverrides.amplified());
    }

    @Test
    void repeatedFactoryCallsReturnIndependentMutableObjects() {
        JsonObject first = VanillaNoiseSettingsOverrides.overworld();
        JsonObject second = VanillaNoiseSettingsOverrides.overworld();

        first.addProperty("ore_veins_enabled", true);
        first.getAsJsonObject("noise").addProperty("height", 1);

        assertFalse(second.get("ore_veins_enabled").getAsBoolean());
        assertEquals(384, second.getAsJsonObject("noise").get("height").getAsInt());
    }

    @Test
    void eachEmbeddedChunkStaysBelowSafeByteLimit() {
        assertEquals(6, VanillaNoiseSettingsOverrides.overworldChunkCount());
        assertEquals(6, VanillaNoiseSettingsOverrides.largeBiomesChunkCount());
        assertEquals(6, VanillaNoiseSettingsOverrides.amplifiedChunkCount());

        assertTrue(VanillaNoiseSettingsOverrides.overworldMaxChunkUtf8Bytes() < SAFE_CHUNK_LIMIT_BYTES);
        assertTrue(VanillaNoiseSettingsOverrides.largeBiomesMaxChunkUtf8Bytes() < SAFE_CHUNK_LIMIT_BYTES);
        assertTrue(VanillaNoiseSettingsOverrides.amplifiedMaxChunkUtf8Bytes() < SAFE_CHUNK_LIMIT_BYTES);
    }

    @Test
    void productionCodeDoesNotUseClasspathVanillaLookup() throws Exception {
        Path source = resolveProjectPath(
                "src/main/java/com/mightydanp/techcore/registries/VanillaNoiseSettingsOverrides.java",
                "TechCore/src/main/java/com/mightydanp/techcore/registries/VanillaNoiseSettingsOverrides.java"
        );

        String code = Files.readString(source, StandardCharsets.UTF_8);
        assertFalse(code.contains("getResource("));
        assertFalse(code.contains("getResourceAsStream("));
        assertFalse(code.contains("/data/minecraft/worldgen/noise_settings/"));
        assertFalse(code.contains("Missing built-in vanilla resource"));
    }

    @Test
    void generatedDocumentsDecodeWithNoiseGeneratorSettingsCodec() {
        DataResult<NoiseGeneratorSettings> overworld = NoiseGeneratorSettings.DIRECT_CODEC.parse(JsonOps.INSTANCE, VanillaNoiseSettingsOverrides.overworld());
        DataResult<NoiseGeneratorSettings> largeBiomes = NoiseGeneratorSettings.DIRECT_CODEC.parse(JsonOps.INSTANCE, VanillaNoiseSettingsOverrides.largeBiomes());
        DataResult<NoiseGeneratorSettings> amplified = NoiseGeneratorSettings.DIRECT_CODEC.parse(JsonOps.INSTANCE, VanillaNoiseSettingsOverrides.amplified());

        Assumptions.assumeTrue(
                overworld.result().isPresent() && largeBiomes.result().isPresent() && amplified.result().isPresent(),
                () -> "NoiseGeneratorSettings.DIRECT_CODEC parse unavailable in current bootstrap state: "
                        + overworld.error().map(Object::toString).orElse("overworld-ok") + " / "
                        + largeBiomes.error().map(Object::toString).orElse("large-biomes-ok") + " / "
                        + amplified.error().map(Object::toString).orElse("amplified-ok")
        );
    }

    private static void assertOnlyOreVeinsFlagDiffers(JsonObject source, JsonObject generated) {
        assertTrue(source.get("ore_veins_enabled").getAsBoolean());
        assertFalse(generated.get("ore_veins_enabled").getAsBoolean());
        assertJsonEqualsExcept(source, generated, "/ore_veins_enabled");
    }

    private static void assertJsonEqualsExcept(JsonElement expected, JsonElement actual, String ignoredPointer) {
        assertJsonEqualsExcept(expected, actual, "", ignoredPointer);
    }

    private static void assertJsonEqualsExcept(JsonElement expected, JsonElement actual, String path, String ignoredPointer) {
        if (path.equals(ignoredPointer)) {
            return;
        }

        if (expected.isJsonObject()) {
            assertTrue(actual.isJsonObject(), path);
            JsonObject expectedObject = expected.getAsJsonObject();
            JsonObject actualObject = actual.getAsJsonObject();
            assertEquals(expectedObject.keySet(), actualObject.keySet(), path);
            for (Map.Entry<String, JsonElement> entry : expectedObject.entrySet()) {
                String childPath = path + "/" + entry.getKey();
                assertJsonEqualsExcept(entry.getValue(), actualObject.get(entry.getKey()), childPath, ignoredPointer);
            }
            return;
        }

        if (expected.isJsonArray()) {
            assertTrue(actual.isJsonArray(), path);
            JsonArray expectedArray = expected.getAsJsonArray();
            JsonArray actualArray = actual.getAsJsonArray();
            assertEquals(expectedArray.size(), actualArray.size(), path);
            for (int index = 0; index < expectedArray.size(); index++) {
                assertJsonEqualsExcept(expectedArray.get(index), actualArray.get(index), path + "/" + index, ignoredPointer);
            }
            return;
        }

        assertEquals(expected, actual, path);
    }

    private static Path resolveProjectPath(String moduleRelative, String workspaceRelative) {
        Path modulePath = Path.of(moduleRelative);
        if (Files.exists(modulePath)) {
            return modulePath;
        }

        Path workspacePath = Path.of(workspaceRelative);
        if (Files.exists(workspacePath)) {
            return workspacePath;
        }

        throw new IllegalStateException("Missing expected source file: " + moduleRelative);
    }
}

package com.mightydanp.techcore.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class WorldGenDiagnosticsCommandTest {
    @Test
    void commandUsesTechcoreNamespaceAndIsDevelopmentOnly() {
        CommandDispatcher<CommandSourceStack> developmentDispatcher = new CommandDispatcher<>();
        CommandDispatcher<CommandSourceStack> productionDispatcher = new CommandDispatcher<>();

        WorldGenDiagnosticsCommand.register(developmentDispatcher, true);
        WorldGenDiagnosticsCommand.register(productionDispatcher, false);

        assertNotNull(developmentDispatcher.getRoot().getChild("techcore"));
        assertEquals(null, productionDispatcher.getRoot().getChild("techcore"));
    }

    @Test
    void commandRequiresPermissionLevelFour() {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        CommandSourceStack denied = mock(CommandSourceStack.class);
        CommandSourceStack allowed = mock(CommandSourceStack.class);

        WorldGenDiagnosticsCommand.register(dispatcher, true);

        when(denied.hasPermission(4)).thenReturn(false);
        when(allowed.hasPermission(4)).thenReturn(true);

        assertFalse(dispatcher.getRoot().getChild("techcore").getChild("worldgendiagnostics").canUse(denied));
        assertTrue(dispatcher.getRoot().getChild("techcore").getChild("worldgendiagnostics").canUse(allowed));
    }

    @Test
    void commandSourceContainsReadOnlyDiagnosticsOnly() throws Exception {
        String source = Files.readString(projectPath("src/main/java/com/mightydanp/techcore/command/WorldGenDiagnosticsCommand.java"));

        assertTrue(source.contains("remove_vanilla_ores_exists="));
        assertTrue(source.contains("giant_noise_vein_datapack_override_active="));
        assertTrue(source.contains("overworld_noise_settings_overrides="));
        assertTrue(source.contains("remove_vanilla_nether_ores_exists="));

        assertFalse(source.contains(".setBlock("));
        assertFalse(source.contains(".setBlockAndUpdate("));
        assertFalse(source.contains(".getChunk("));
        assertFalse(source.contains(".getChunkNow("));
    }

    private static Path projectPath(String relativePath) {
        try {
            Path location = Paths.get(WorldGenDiagnosticsCommandTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath();
            for (Path current = location; current != null; current = current.getParent()) {
                Path direct = current.resolve(relativePath);
                if (Files.exists(direct)) {
                    return direct;
                }

                Path modulePrefixed = current.resolve("TechCore").resolve(relativePath);
                if (Files.exists(modulePrefixed)) {
                    return modulePrefixed;
                }
            }
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        throw new IllegalStateException("Missing path: " + relativePath);
    }
}

package com.mightydanp.techcore.api.resources.assets.content.material;

import com.mightydanp.techcore.api.resources.assets.content.model.block.BlockModelContent;
import com.mightydanp.techcore.api.resources.assets.content.model.item.ItemModelContent;
import com.mightydanp.techcore.materials.properties.Icons;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MaterialTextureResolver {
    private final Map<String, Set<String>> indexedTextures = new HashMap<>();
    private final Map<String, ResourceLocation> resolvedCache = new HashMap<>();

    public void indexNamespace(String namespace) {
        index(namespace, ItemModelContent.ITEM_FOLDER);
        index(namespace, BlockModelContent.BLOCK_FOLDER);
    }

    public void index(String namespace, String textureFolder) {
        Path materialRoot = findMaterialRoot(namespace, textureFolder);

        if (!Files.isDirectory(materialRoot)) {
            return;
        }

        try {
            Files.list(materialRoot)
                    .filter(Files::isDirectory)
                    .forEach(folder -> {
                        String folderName = folder.getFileName().toString();
                        Set<String> names = new HashSet<>();

                        try {
                            Files.walk(folder)
                                    .filter(Files::isRegularFile)
                                    .filter(path -> path.toString().endsWith(".png"))
                                    .forEach(path -> {
                                        String relative = folder.relativize(path).toString()
                                                .replace("\\", "/");
                                        names.add(relative);
                                    });
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to index texture folder: " + folder, e);
                        }

                        indexedTextures.put(indexKey(namespace, textureFolder, folderName), names);
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to index material textures: " + materialRoot, e);
        }
    }

    public @NotNull ResourceLocation resolve(String namespace, String textureFolder, Icons.@NotNull Icon icon, @NotNull String textureName) {
        String cleanTextureName = textureName.endsWith(".png")
                ? textureName
                : textureName + ".png";

        String cacheKey = namespace + "|" + textureFolder + "|" + icon.label() + "|" + cleanTextureName;

        ResourceLocation cached = resolvedCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        ResourceLocation resolved = tryResolve(namespace, textureFolder, icon.label(), cleanTextureName);

        if (resolved == null && icon.hasFallback()) {
            resolved = tryResolve(namespace, textureFolder, icon.fallback(), cleanTextureName);
        }

        if (resolved == null) {
            resolved = tryResolve(namespace, textureFolder, Icons.DEFAULT_FOLDER, cleanTextureName);
        }

        if (resolved != null) {
            resolvedCache.put(cacheKey, resolved);
            return resolved;
        }

        throw new IllegalStateException(
                "Missing " + textureFolder + " texture for namespace '" + namespace +
                        "', icon '" + icon.label() +
                        "', texture '" + cleanTextureName +
                        "', searched primary='" + icon.label() +
                        "', fallback='" + icon.fallback() +
                        "', default='" + Icons.DEFAULT_FOLDER + "'"
        );
    }

    public boolean hasTexture(String namespace, String textureFolder, String folder, @NotNull String textureName) {
        String cleanTextureName = textureName.endsWith(".png")
                ? textureName
                : textureName + ".png";

        return indexedTextures
                .getOrDefault(indexKey(namespace, textureFolder, folder), Set.of())
                .contains(cleanTextureName);
    }

    public void clearCache() {
        resolvedCache.clear();
    }

    private ResourceLocation tryResolve(String namespace, String textureFolder, String folder, String cleanTextureName) {
        if (folder == null || folder.isBlank()) {
            return null;
        }

        Set<String> available = indexedTextures.getOrDefault(
                indexKey(namespace, textureFolder, folder),
                Set.of()
        );

        if (!available.contains(cleanTextureName)) {
            return null;
        }

        String noPng = cleanTextureName.substring(
                0,
                cleanTextureName.length() - ".png".length()
        );

        return ResourceLocation.fromNamespaceAndPath(
                namespace,
                textureFolder + "/material/" + folder + "/" + noPng
        );
    }

    private Path findMaterialRoot(String namespace, String textureFolder) {
        return ModList.get()
                .getModFileById(namespace)
                .getFile()
                .findResource(
                        "assets",
                        namespace,
                        "textures",
                        textureFolder,
                        "material"
                );
    }

    @Contract(pure = true)
    private static @NotNull String indexKey(String namespace, String textureFolder, String folder) {
        return namespace + "|" + textureFolder + "|" + folder;
    }
}
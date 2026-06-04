package com.mightydanp.techcore.world.level;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public final class WasGenerated {
    private static final String DATA_ID = "techcore_changed_after_generation";

    private WasGenerated() {
    }

    public static boolean wasGenerated(ServerLevel level, BlockPos pos) {
        return !ChangedAfterGenerationData.get(level).wasChanged(pos);
    }

    public static void markChanged(ServerLevel level, BlockPos pos) {
        ChangedAfterGenerationData.get(level).markChanged(pos);
    }

    private static final class ChangedAfterGenerationData extends SavedData {
        private final Long2ObjectOpenHashMap<LongOpenHashSet> changedPositions = new Long2ObjectOpenHashMap<>();

        private static ChangedAfterGenerationData get(ServerLevel level) {
            return level.getDataStorage().computeIfAbsent(
                    ChangedAfterGenerationData::load,
                    ChangedAfterGenerationData::new,
                    DATA_ID
            );
        }

        private void markChanged(BlockPos pos) {
            changedPositions.computeIfAbsent(chunkKey(pos), key -> new LongOpenHashSet()).add(localKey(pos));
            setDirty();
        }

        private boolean wasChanged(BlockPos pos) {
            LongOpenHashSet positions = changedPositions.get(chunkKey(pos));
            return positions != null && positions.contains(localKey(pos));
        }

        private static ChangedAfterGenerationData load(CompoundTag tag) {
            ChangedAfterGenerationData data = new ChangedAfterGenerationData();
            ListTag chunks = tag.getList("chunks", Tag.TAG_COMPOUND);

            for (int i = 0; i < chunks.size(); i++) {
                CompoundTag chunkTag = chunks.getCompound(i);
                data.changedPositions.put(chunkTag.getLong("chunk"), new LongOpenHashSet(chunkTag.getLongArray("positions")));
            }

            return data;
        }

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
            ListTag chunks = new ListTag();

            for (Long2ObjectMap.Entry<LongOpenHashSet> entry : changedPositions.long2ObjectEntrySet()) {
                CompoundTag chunkTag = new CompoundTag();
                chunkTag.putLong("chunk", entry.getLongKey());
                chunkTag.putLongArray("positions", entry.getValue().toLongArray());
                chunks.add(chunkTag);
            }

            tag.put("chunks", chunks);
            return tag;
        }

        private static long chunkKey(BlockPos pos) {
            return ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4);
        }

        private static long localKey(BlockPos pos) {
            return BlockPos.asLong(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        }
    }
}

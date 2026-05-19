package com.mightydanp.techcore.world.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record ProcessedStage(ItemStack itemStack, ProcessedStages processedStage) {
    public static final String TAG = "processed_stage";

    public static final Codec<ProcessedStage> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("item_stack").forGetter(ProcessedStage::itemStack),
                    ProcessedStage.ProcessedStages.CODEC.fieldOf(TAG).forGetter(ProcessedStage::processedStage)
            ).apply(instance, ProcessedStage::new));

    public static boolean hasProcessedStage(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG);
    }

    public static String getProcessedStage(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG) ? tag.getString(TAG) : null;
    }

    public static String getProcessedStageOrDefault(ItemStack itemStack, String defaultProcessedStage) {
        if(hasProcessedStage(itemStack)) return getProcessedStage(itemStack);

        if(defaultProcessedStage == null) return null;

        setProcessedStage(itemStack, defaultProcessedStage);
        return defaultProcessedStage;
    }

    public static void setProcessedStage(ItemStack itemStack, String processedStage) {
        itemStack.getOrCreateTag().putString(TAG, processedStage);
    }

    public static ProcessedStage fromStack(ItemStack itemStack) {
        String processedStageName = getProcessedStage(itemStack);
        ProcessedStages processedStages = ProcessedStages.fromStage(processedStageName);

        return new ProcessedStage(itemStack, processedStages);
    }
    
    public enum ProcessedStages{
        CENTRIFUGED("centrifuged", 0.00F),
        CRUSHED("crushed", 0.01F),
        GROUND("ground", 0.02F),
        MACERATED("macerated", 0.03F),
        MAGNETIZED("magnetized", 0.04F),
        MIXED("mixed", 0.05F),
        PURIFIED("purified", 0.06F),
        NONE("purified", 1F);

        public static final Codec<ProcessedStages> CODEC = Codec.STRING.xmap(
                ProcessedStages::fromStage,
                ProcessedStages::getStage);

        private final String stage;
        private final float value;

        ProcessedStages(String stage, float value){
            this.stage = stage;
            this.value = value;
        }

        public String getStage() {
            return stage;
        }

        public float getValue() {
            return value;
        }

        public static ProcessedStages fromStage(String stage) {
            for (ProcessedStages processedStage : values()) {
                if (processedStage.stage.equals(stage)) {
                    return processedStage;
                }
            }

            return null;
            //throw new IllegalArgumentException("Unknown processed stage: " + stage);
        }

        public static float getProcessedStageLevel(ItemStack itemStack) {
            ProcessedStage stage = ProcessedStage.fromStack(itemStack);

            return stage.processedStage().getValue();
        }
    }
}

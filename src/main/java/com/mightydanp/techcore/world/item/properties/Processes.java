package com.mightydanp.techcore.world.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record Processes(ItemStack itemStack, List<Process> processes) {
    public static final String TAG = "processes";

    public static final Codec<Processes> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("item_stack").forGetter(Processes::itemStack),
                    Processes.Process.CODEC.listOf().fieldOf(TAG).forGetter(Processes::processes)
            ).apply(instance, Processes::new));

    public static boolean hasProcesses(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG);
    }

    public static List<String> getProcesses(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains(TAG)) return List.of();

        ListTag Processes = tag.getList(TAG, Tag.TAG_STRING);

        return Processes.stream().map(Tag::getAsString).toList();
    }

    public static List<String> getProcessesOrDefault(ItemStack itemStack, List<String> defaultProcesses) {
        if(hasProcesses(itemStack)) return getProcesses(itemStack);

        if(defaultProcesses.isEmpty()) return new ArrayList<>();

        setProcesses(itemStack, defaultProcesses);
        return defaultProcesses;
    }

    public static void setProcesses(ItemStack itemStack, List<String> Processes) {
        ListTag ProcessesTag = new ListTag();
        for (String stage : Processes) {
            ProcessesTag.add(StringTag.valueOf(stage));
            itemStack.getOrCreateTag().put(TAG, ProcessesTag);
        }
    }

    public static Processes fromStack(ItemStack itemStack) {
        List<Process> Processes = getProcesses(itemStack).stream()
                .map(Process::fromProcess)
                .toList();

        return new Processes(itemStack, Processes);
    }

    public enum Process{
        NONE("none"),
        CENTRIFUGE("centrifuge"),
        GRIND("grind"),
        MACERATE("macerate"),
        MAGNETIZE("magnetize"),
        MIX("mix"),
        PURIFY("purify");

        public static final Codec<Process> CODEC = Codec.STRING.xmap(
                Process::fromProcess,
                Process::getProcess);

        private final String process;

        Process(String process) {
            this.process = process;
        }

        public String getProcess() {
            return process;
        }

        public static Process fromProcess(String processName) {
            for (Process process : values()) {
                if (process.process.equals(processName)) {
                    return process;
                }
            }
            throw new IllegalArgumentException("Unknown processed process: " + processName);
        }
    }
}

package com.mightydanp.techcore.client.event;

public final class ItemRenderEventGuard {
    private static final ThreadLocal<Boolean> RENDERING_EXTRA_PASS =
            ThreadLocal.withInitial(() -> false);

    private ItemRenderEventGuard() {
    }

    public static boolean isRenderingExtraPass() {
        return RENDERING_EXTRA_PASS.get();
    }

    public static void startExtraPass() {
        RENDERING_EXTRA_PASS.set(true);
    }

    public static void endExtraPass() {
        RENDERING_EXTRA_PASS.remove();
    }
}
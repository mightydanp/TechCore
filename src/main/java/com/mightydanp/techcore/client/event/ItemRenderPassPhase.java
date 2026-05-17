package com.mightydanp.techcore.client.event;

public enum ItemRenderPassPhase {
    /**
     * Fired before the vanilla item model render call.
     */
    BEFORE_EFFECTS,

    /**
     * Reserved for hooks that run around the active item effect/model render path.
     * This phase does not guarantee priority over other mods by itself; subscribers
     * should still use Forge event priority when ordering matters.
     */
    EFFECTS,

    /**
     * Fired after vanilla item model rendering and effect hooks have finished.
     */
    AFTER_EFFECTS

}
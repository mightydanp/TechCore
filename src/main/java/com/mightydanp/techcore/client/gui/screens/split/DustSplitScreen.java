package com.mightydanp.techcore.client.gui.screens.split;

import com.mightydanp.techcore.client.ref.ScreenRef;
import com.mightydanp.techcore.materials.Item.DustItem;
import com.mightydanp.techcore.network.TCNetworkChannel;
import com.mightydanp.techcore.network.protocol.game.ServerBoundSplitDustPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.jetbrains.annotations.NotNull;

// Overlay screen shown when the player right-clicks an empty inventory slot while holding a DustItem.
// Renders on top of the parent screen (the inventory the player had open).
public class DustSplitScreen extends Screen {
    // The screen that was open before this one — rendered behind and returned to on close
    private final Screen parent;
    // The dust ItemStack currently held in the player's cursor
    private final ItemStack cursorStack;
    // The inventory slot index the player right-clicked — where the split dust will be placed
    private final int slotIndex;
    // The slider widget used to choose how much dust to split off
    private ForgeSlider slider;
    private Runnable pendingCloseAction = null;

    // title is the screen name shown in the title bar, looked up from the lang file
    public DustSplitScreen(Screen parent, ItemStack cursorStack, int slotIndex) {
        super(Component.translatable(ScreenRef.dust_split_screen));
        this.parent = parent;
        this.cursorStack = cursorStack;
        this.slotIndex = slotIndex;
    }

    // init() is called by Minecraft whenever the screen is (re)opened or the window is resized.
    // this.width and this.height are set before init() runs, so it's safe to use them for positioning.
    @Override
    protected void init() {
        // Safety check — only build widgets if the cursor item is actually a DustItem
        if (cursorStack.getItem() instanceof DustItem) {
            // Read how much dust is currently in the cursor stack — this is the slider's maximum
            int quantity = ((DustItem) cursorStack.getItem()).getQuantity(cursorStack);

            // Slider — lets the player choose how much dust to split off (1 to current quantity)
            // Centered horizontally, slightly above the middle of the screen
            slider = addRenderableWidget(new ForgeSlider(
                    this.width / 2 - 75, this.height / 2 - 10,  // x, y — centered
                    150, 20,                                      // width, height
                    Component.translatable(ScreenRef.dust_split_amount), Component.empty(), // prefix label, no suffix
                    1, quantity,   // min value, max value
                    quantity,      // starting value — defaults to the full current amount
                    1, 0, true     // step size 1 (whole units only), precision 0, draw the value as text
            ));

            // Confirm button — green checkmark, sends the split packet then closes the screen
            addRenderableWidget(Button.builder(
                    Component.literal("✔").withStyle(ChatFormatting.GREEN),
                    btn -> {
                        TCNetworkChannel.INSTANCE.sendToServer(
                                new ServerBoundSplitDustPacket(slotIndex, slider.getValueInt())
                        );
                        pendingCloseAction = this::onClose;
                    }).bounds(
                    this.width / 2 + 5,    // x — 5px right of center
                    this.height / 2 + 20,  // y — 20px below center, below the slider
                    20, 20                 // small square button
            ).build());

            // Cancel button — red X, discards the split and returns to parent without sending a packet
            addRenderableWidget(Button.builder(
                    Component.literal("✘").withStyle(ChatFormatting.RED),
                    btn -> pendingCloseAction = this::onClose).bounds(
                    this.width / 2 - 25,   // x — 25px left of center (20px button width + 5px gap)
                    this.height / 2 + 20,  // y — same row as the confirm button
                    20, 20                 // small square button
            ).build());
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Draws the parent screen (the one that was open before this one) underneath this
        // overlay. If you skip this, the screen behind goes black.
        //
        // Minecraft's GUI is technically 3D — every draw has an X, Y, and Z. Larger Z means
        // "closer to the camera" and renders on top of smaller Z. Inside parent.render() the
        // game places things at specific Z values:
        //   - Slot items at Z = 150  (GuiGraphics#renderItem)
        //   - Cursor item at Z = 232  (AbstractContainerScreen#renderFloatingItem)
        // A widget drawn without a Z translate sits at Z = 0, which is behind those items.
        parent.render(guiGraphics, mouseX, mouseY, partialTick);

        // Saves the current transform onto the pose stack. Anything drawn before the matching
        // popPose() uses the modified transform. If you don't pushPose() before translating,
        // the translate will leak into tooltips and any other rendering done this frame.
        guiGraphics.pose().pushPose();

        // Moves the coordinate system to Z = 400 — above slot items (150) and the cursor
        // item (232). If you don't do this, slot items and the cursor item will pop through
        // the slider and buttons because they sit at higher Z and win the depth test.
        // Any value comfortably above 232 works; 400 leaves headroom for tooltips.
        guiGraphics.pose().translate(0.0f, 0.0f, 400.0f);

        // Full-screen dim overlay. The color is ARGB — the top byte is alpha (opacity):
        //   0xFF000000 = fully opaque (parent hidden completely)
        //   0xC0000000 = ~75% dark
        //   0x80000000 = ~50% dark
        //   0x00000000 = fully transparent (parent fully visible — current setting)
        // If you don't draw this fill, the parent screen is visible at full clarity.
        guiGraphics.fill(0, 0, this.width, this.height, 0x00000000);

        // Calls Screen.render(), which iterates everything registered via addRenderableWidget()
        // in init() and draws them. The widgets inherit the Z = 400 transform from the pose
        // stack, so they render on top of items.
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Restores the transform pushed earlier. If you don't pop, the Z = 400 translate
        // leaks into anything drawn after render() returns, like tooltips.
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean result = super.mouseReleased(mouseX, mouseY, button);
        if (pendingCloseAction != null) {
            Runnable action = pendingCloseAction;
            pendingCloseAction = null;
            action.run();
        }
        return result;
    }

    // Return to the parent screen instead of closing to the game entirely
    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }
}
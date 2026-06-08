package com.mightydanp.techcore.client.gui.screens.split;

import com.mightydanp.techcore.client.ref.ScreenRef;
import com.mightydanp.techcore.network.TCNetworkChannel;
import com.mightydanp.techcore.network.protocol.game.ServerBoundSplitQuantityPacket;
import com.mightydanp.techcore.world.item.properties.Quantity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.jetbrains.annotations.NotNull;


public class QuantitySplitScreen extends Screen {
    private final Screen parent;

    private final ItemStack cursorStack;
    private final Slot hoveredSlot;

    private final int slotIndex;
    private final int clientSlotIndex;

    private ForgeSlider slider;
    private Runnable pendingCloseAction = null;

    public QuantitySplitScreen(Screen parent, ItemStack cursorStack, Slot hoverSlot, int slotIndex, int clientSlotIndex) {
        super(Component.translatable(ScreenRef.quantity_split_screen_translatable));
        this.parent = parent;
        this.cursorStack = cursorStack;
        this.hoveredSlot = hoverSlot;
        this.slotIndex = slotIndex;
        this.clientSlotIndex = clientSlotIndex;
    }

    @Override
    protected void init() {
        Quantity cursorQuantity = Quantity.stack(cursorStack).get();

        if (cursorQuantity == null) return;

        int quantity;
        if (!hoveredSlot.hasItem()) {
            quantity = cursorQuantity.quantity();
        } else if (canMergeQuantityStacks(cursorStack, hoveredSlot.getItem())) {
            Quantity slotQuantity = Quantity.stack(hoveredSlot.getItem()).get();

            if (slotQuantity == null) return;

            quantity = Math.min(cursorQuantity.quantity(), slotQuantity.maxQuantity() - slotQuantity.quantity());
        } else {
            return;
        }

        quantity = Math.min(quantity, cursorQuantity.maxQuantity());

        if (quantity < 1) return;

        slider = addRenderableWidget(new ForgeSlider(
                this.width / 2 - 75,
                this.height / 2 - 10,
                150,
                20,
                Component.translatable(ScreenRef.quantity_split_amount_translatable),
                Component.empty(),
                1,
                quantity,
                quantity,
                1,
                0,
                true
        ) {
            {
                if (Double.isNaN(value)) value = 1.0;
            }
        });

        // Confirm button — green checkmark, sends the split packet then closes the screen
        addRenderableWidget(Button.builder(
                Component.literal("✔").withStyle(ChatFormatting.GREEN),
                btn -> {
                    // Gets the amount selected by the slider.
                    int splitAmount = slider.getValueInt();

                    // Sends both indexes to the server:
                    // slotIndex is the real server container slot.
                    // clientSlotIndex is the open client menu slot that should be updated by the server response.
                    TCNetworkChannel.INSTANCE.sendToServer(
                            new ServerBoundSplitQuantityPacket(slotIndex, clientSlotIndex, splitAmount, this.cursorStack)
                    );

                    // Closes after mouse release. The client cursor/slot is not changed here;
                    // it waits for the server's accepted split result packet.
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
                20, 20// small square button
        ).build());
    }

    private static boolean canMergeQuantityStacks(ItemStack cursorStack, ItemStack slotStack) {
        if (!ItemStack.isSameItem(cursorStack, slotStack)) {
            return false;
        }

        return ItemStack.isSameItemSameTags(withoutQuantity(cursorStack), withoutQuantity(slotStack));
    }

    private static @NotNull ItemStack withoutQuantity(@NotNull ItemStack stack) {
        ItemStack copy = stack.copy();
        CompoundTag tag = copy.getTag();

        if (tag != null) {
            tag.remove(Quantity.TAG);
            tag.remove(Quantity.MAX_TAG);

            if (tag.isEmpty()) {
                copy.setTag(null);
            }
        }

        return copy;
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

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

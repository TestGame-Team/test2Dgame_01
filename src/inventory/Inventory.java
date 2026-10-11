package inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Small, UI-independent inventory model. Mutations are kept in one place. */
public final class Inventory {
    public static final class Stack {
        public final Item item;
        private int quantity;
        private boolean equipped;

        private Stack(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        public int getQuantity() { return quantity; }
        public boolean isEquipped() { return equipped; }
        private void setQuantity(int quantity) { this.quantity = quantity; }
        private void setEquipped(boolean equipped) { this.equipped = equipped; }
    }

    private final int capacity;
    private final List<Stack> stacks = new ArrayList<>();

    public Inventory(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be positive");
        this.capacity = capacity;
    }

    public List<Stack> getStacks() {
        return Collections.unmodifiableList(stacks);
    }

    public int getCapacity() { return capacity; }

    /** Adds as much as possible; returns the quantity that could not fit. */
    public int addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) return quantity;
        int remaining = quantity;

        if (item.stackable) {
            for (Stack stack : stacks) {
                if (stack.item.id.equals(item.id) && stack.quantity < item.maxStack) {
                    int moved = Math.min(remaining, item.maxStack - stack.quantity);
                    stack.setQuantity(stack.quantity + moved);
                    remaining -= moved;
                    if (remaining == 0) return 0;
                }
            }
        }

        while (remaining > 0 && stacks.size() < capacity) {
            int moved = item.stackable ? Math.min(remaining, item.maxStack) : 1;
            stacks.add(new Stack(item, moved));
            remaining -= moved;
            if (!item.stackable) {
                // Non-stackable items occupy separate slots.
                continue;
            }
        }
        return remaining;
    }

    public boolean toggleEquip(int index) {
        if (!isValid(index)) return false;
        Stack selected = stacks.get(index);
        if (!selected.item.equippable) return false;

        if (selected.equipped) {
            selected.setEquipped(false);
            return true;
        }

        for (Stack stack : stacks) {
            if (stack.item.equipmentSlot.equals(selected.item.equipmentSlot)) {
                stack.setEquipped(false);
            }
        }
        selected.setEquipped(true);
        return true;
    }

    /** Drops the entire selected stack and returns it; null means invalid selection. */
    public Stack drop(int index) {
        if (!isValid(index)) return null;
        Stack dropped = stacks.remove(index);
        dropped.setEquipped(false);
        return dropped;
    }

    private boolean isValid(int index) {
        return index >= 0 && index < stacks.size();
    }
}

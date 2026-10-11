package inventory;

/** Immutable item definition shared by every stack of the same item. */
public final class Item {
    public static final Item RUSTY_SWORD = new Item("rusty_sword", "Rusty Sword", false, 1, true, "WEAPON", 1);
    public static final Item POTION = new Item("potion", "Potion", true, 99, false, "", 0);
    public static final Item OLD_COIN = new Item("old_coin", "Old Coin", true, 999, false, "", 0);

    public final String id;
    public final String name;
    public final boolean stackable;
    public final int maxStack;
    public final boolean equippable;
    public final String equipmentSlot;
    public final int attackBonus;

    private Item(String id, String name, boolean stackable, int maxStack,
                 boolean equippable, String equipmentSlot, int attackBonus) {
        this.id = id;
        this.name = name;
        this.stackable = stackable;
        this.maxStack = maxStack;
        this.equippable = equippable;
        this.equipmentSlot = equipmentSlot;
        this.attackBonus = attackBonus;
    }
}

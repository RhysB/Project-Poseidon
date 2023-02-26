package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CraftingManager {

    private static final CraftingManager a = new CraftingManager();
    private List b = new ArrayList();

    public static final CraftingManager getInstance() {
        return a;
    }

    private CraftingManager() {
        (new RecipesTools()).a(this);
        (new RecipesWeapons()).a(this);
        (new RecipeIngots()).a(this);
        (new RecipesFood()).a(this);
        (new RecipesCrafting()).a(this);
        (new RecipesArmor()).a(this);
        (new RecipesDyes()).a(this);
        this.registerShapedRecipe(new ItemStack(Item.PAPER, 3), new Object[] { "###", '#', Item.SUGAR_CANE});
        this.registerShapedRecipe(new ItemStack(Item.BOOK, 1), new Object[] { "#", "#", "#", '#', Item.PAPER});
        this.registerShapedRecipe(new ItemStack(Block.FENCE, 2), new Object[] { "###", "###", '#', Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.JUKEBOX, 1), new Object[] { "###", "#X#", "###", '#', Block.WOOD, 'X', Item.DIAMOND});
        this.registerShapedRecipe(new ItemStack(Block.NOTE_BLOCK, 1), new Object[] { "###", "#X#", "###", '#', Block.WOOD, 'X', Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Block.BOOKSHELF, 1), new Object[] { "###", "XXX", "###", '#', Block.WOOD, 'X', Item.BOOK});
        this.registerShapedRecipe(new ItemStack(Block.SNOW_BLOCK, 1), new Object[] { "##", "##", '#', Item.SNOW_BALL});
        this.registerShapedRecipe(new ItemStack(Block.CLAY, 1), new Object[] { "##", "##", '#', Item.CLAY_BALL});
        this.registerShapedRecipe(new ItemStack(Block.BRICK, 1), new Object[] { "##", "##", '#', Item.CLAY_BRICK});
        this.registerShapedRecipe(new ItemStack(Block.GLOWSTONE, 1), new Object[] { "##", "##", '#', Item.GLOWSTONE_DUST});
        this.registerShapedRecipe(new ItemStack(Block.WOOL, 1), new Object[] { "##", "##", '#', Item.STRING});
        this.registerShapedRecipe(new ItemStack(Block.TNT, 1), new Object[] { "X#X", "#X#", "X#X", 'X', Item.SULPHUR, '#', Block.SAND});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 3, 3), new Object[] { "###", '#', Block.COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 3, 0), new Object[] { "###", '#', Block.STONE});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 3, 1), new Object[] { "###", '#', Block.SANDSTONE});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 3, 2), new Object[] { "###", '#', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.LADDER, 2), new Object[] { "# #", "###", "# #", '#', Item.STICK});
        this.registerShapedRecipe(new ItemStack(Item.WOOD_DOOR, 1), new Object[] { "##", "##", "##", '#', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.TRAP_DOOR, 2), new Object[] { "###", "###", '#', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Item.IRON_DOOR, 1), new Object[] { "##", "##", "##", '#', Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Item.SIGN, 1), new Object[] { "###", "###", " X ", '#', Block.WOOD, 'X', Item.STICK});
        this.registerShapedRecipe(new ItemStack(Item.CAKE, 1), new Object[] { "AAA", "BEB", "CCC", 'A', Item.MILK_BUCKET, 'B', Item.SUGAR, 'C', Item.WHEAT, 'E', Item.EGG});
        this.registerShapedRecipe(new ItemStack(Item.SUGAR, 1), new Object[] { "#", '#', Item.SUGAR_CANE});
        this.registerShapedRecipe(new ItemStack(Block.WOOD, 4), new Object[] { "#", '#', Block.LOG});
        this.registerShapedRecipe(new ItemStack(Item.STICK, 4), new Object[] { "#", "#", '#', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.TORCH, 4), new Object[] { "X", "#", 'X', Item.COAL, '#', Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.TORCH, 4), new Object[] { "X", "#", 'X', new ItemStack(Item.COAL, 1, 1), '#', Item.STICK});
        this.registerShapedRecipe(new ItemStack(Item.BOWL, 4), new Object[] { "# #", " # ", '#', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.RAILS, 16), new Object[] { "X X", "X#X", "X X", 'X', Item.IRON_INGOT, '#', Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.GOLDEN_RAIL, 6), new Object[] { "X X", "X#X", "XRX", 'X', Item.GOLD_INGOT, 'R', Item.REDSTONE, '#', Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.DETECTOR_RAIL, 6), new Object[] { "X X", "X#X", "XRX", 'X', Item.IRON_INGOT, 'R', Item.REDSTONE, '#', Block.STONE_PLATE});
        this.registerShapedRecipe(new ItemStack(Item.MINECART, 1), new Object[] { "# #", "###", '#', Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Block.JACK_O_LANTERN, 1), new Object[] { "A", "B", 'A', Block.PUMPKIN, 'B', Block.TORCH});
        this.registerShapedRecipe(new ItemStack(Item.STORAGE_MINECART, 1), new Object[] { "A", "B", 'A', Block.CHEST, 'B', Item.MINECART});
        this.registerShapedRecipe(new ItemStack(Item.POWERED_MINECART, 1), new Object[] { "A", "B", 'A', Block.FURNACE, 'B', Item.MINECART});
        this.registerShapedRecipe(new ItemStack(Item.BOAT, 1), new Object[] { "# #", "###", '#', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Item.BUCKET, 1), new Object[] { "# #", " # ", '#', Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Item.FLINT_AND_STEEL, 1), new Object[] { "A ", " B", 'A', Item.IRON_INGOT, 'B', Item.FLINT});
        this.registerShapedRecipe(new ItemStack(Item.BREAD, 1), new Object[] { "###", '#', Item.WHEAT});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", '#', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Item.FISHING_ROD, 1), new Object[] { "  #", " #X", "# X", '#', Item.STICK, 'X', Item.STRING});
        this.registerShapedRecipe(new ItemStack(Block.COBBLESTONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", '#', Block.COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Item.PAINTING, 1), new Object[] { "###", "#X#", "###", '#', Item.STICK, 'X', Block.WOOL});
        this.registerShapedRecipe(new ItemStack(Item.GOLDEN_APPLE, 1), new Object[] { "###", "#X#", "###", '#', Block.GOLD_BLOCK, 'X', Item.APPLE});
        this.registerShapedRecipe(new ItemStack(Block.LEVER, 1), new Object[] { "X", "#", '#', Block.COBBLESTONE, 'X', Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.REDSTONE_TORCH_ON, 1), new Object[] { "X", "#", '#', Item.STICK, 'X', Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Item.DIODE, 1), new Object[] { "#X#", "III", '#', Block.REDSTONE_TORCH_ON, 'X', Item.REDSTONE, 'I', Block.STONE});
        this.registerShapedRecipe(new ItemStack(Item.WATCH, 1), new Object[] { " # ", "#X#", " # ", '#', Item.GOLD_INGOT, 'X', Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Item.COMPASS, 1), new Object[] { " # ", "#X#", " # ", '#', Item.IRON_INGOT, 'X', Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Item.MAP, 1), new Object[] { "###", "#X#", "###", '#', Item.PAPER, 'X', Item.COMPASS});
        this.registerShapedRecipe(new ItemStack(Block.STONE_BUTTON, 1), new Object[] { "#", "#", '#', Block.STONE});
        this.registerShapedRecipe(new ItemStack(Block.STONE_PLATE, 1), new Object[] { "##", '#', Block.STONE});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_PLATE, 1), new Object[] { "##", '#', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.DISPENSER, 1), new Object[] { "###", "#X#", "#R#", '#', Block.COBBLESTONE, 'X', Item.BOW, 'R', Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Block.PISTON, 1), new Object[] { "TTT", "#X#", "#R#", '#', Block.COBBLESTONE, 'X', Item.IRON_INGOT, 'R', Item.REDSTONE, 'T', Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.PISTON_STICKY, 1), new Object[] { "S", "P", 'S', Item.SLIME_BALL, 'P', Block.PISTON});
        this.registerShapedRecipe(new ItemStack(Item.BED, 1), new Object[] { "###", "XXX", '#', Block.WOOL, 'X', Block.WOOD});
        Collections.sort(this.b, new RecipeSorter(this));
        System.out.println(this.b.size() + " recipes");
    }

    public void registerShapedRecipe(ItemStack itemstack, Object... aobject) { // CraftBukkit - default -> public
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (aobject[i] instanceof String[]) {
            String[] astring = (String[]) ((String[]) aobject[i++]);

            for (int l = 0; l < astring.length; ++l) {
                String s1 = astring[l];

                ++k;
                j = s1.length();
                s = s + s1;
            }
        } else {
            while (aobject[i] instanceof String) {
                String s2 = (String) aobject[i++];

                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < aobject.length; i += 2) {
            Character character = (Character) aobject[i];
            ItemStack itemstack1 = null;

            if (aobject[i + 1] instanceof Item) {
                itemstack1 = new ItemStack((Item) aobject[i + 1]);
            } else if (aobject[i + 1] instanceof Block) {
                itemstack1 = new ItemStack((Block) aobject[i + 1], 1, -1);
            } else if (aobject[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack) aobject[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(c0)) {
                aitemstack[i1] = ((ItemStack) hashmap.get(c0)).cloneItemStack();
            } else {
                aitemstack[i1] = null;
            }
        }

        this.b.add(new ShapedRecipes(j, k, aitemstack, itemstack));
    }

    public void registerShapelessRecipe(ItemStack itemstack, Object... aobject) { // CraftBukkit - default -> public
        ArrayList arraylist = new ArrayList();
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object instanceof ItemStack) {
                arraylist.add(((ItemStack) object).cloneItemStack());
            } else if (object instanceof Item) {
                arraylist.add(new ItemStack((Item) object));
            } else {
                if (!(object instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                arraylist.add(new ItemStack((Block) object));
            }
        }

        this.b.add(new ShapelessRecipes(itemstack, arraylist));
    }

    public ItemStack craft(InventoryCrafting inventorycrafting) {
        for (int i = 0; i < this.b.size(); ++i) {
            CraftingRecipe craftingrecipe = (CraftingRecipe) this.b.get(i);

            if (craftingrecipe.a(inventorycrafting)) {
                return craftingrecipe.b(inventorycrafting);
            }
        }

        return null;
    }

    public List b() {
        return this.b;
    }
}

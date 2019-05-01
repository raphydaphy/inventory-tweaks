package invtweaks;


import invtweaks.api.IItemTree;
import invtweaks.api.IItemTreeCategory;
import invtweaks.api.IItemTreeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.TagHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Contains the whole hierarchy of categories and items, as defined in the XML item tree. Is used to recognize keywords
 * and store item orders.
 *
 * @author Jimeo Wan
 */
public class InvTweaksItemTree implements IItemTree {
    public static final String UNKNOWN_ITEM = "unknown";

    private static final Logger log = InvTweaks.log;
    @Nullable
    private static List<IItemTreeItem> defaultItems = null;
    /**
     * All categories, stored by name
     */
    @NotNull
    private Map<String, IItemTreeCategory> categories = new HashMap<>();
    /**
     * Items stored by ID. A same ID can hold several names.
     */
    @NotNull
    private Map<String, List<IItemTreeItem>> itemsById = new HashMap<>(500);
    /**
     * Items stored by name. A same name can match several IDs.
     */
    @NotNull
    private Map<String, List<IItemTreeItem>> itemsByName = new HashMap<>(500);

    private String rootCategory;
    @NotNull
    private List<OreDictInfo> oresRegistered = new ArrayList<>();

    private int highestOrder = 0;

    public InvTweaksItemTree() {
        reset();
    }

    public void reset() {

        if(defaultItems == null) {
            defaultItems = new ArrayList<>();
            defaultItems.add(new InvTweaksItemTreeItem(UNKNOWN_ITEM, null, InvTweaksConst.DAMAGE_WILDCARD, null,
                    Integer.MAX_VALUE));
        }

        // Reset tree
        categories.clear();
        itemsByName.clear();
        itemsById.clear();

    }

    /**
     * Checks if given item ID matches a given keyword (either the item's name is the keyword, or it is in the keyword
     * category)
     */
    @Override
    public boolean matches(@Nullable List<IItemTreeItem> items, @NotNull String keyword) {

        if(items == null) {
            return false;
        }

        // The keyword is an item
        for(@NotNull IItemTreeItem item : items) {
            if(item.getName() != null && item.getName().equals(keyword)) {
                return true;
            }
        }

        // The keyword is a category
        IItemTreeCategory category = getCategory(keyword);
        if(category != null) {
            for(IItemTreeItem item : items) {
                if(category.contains(item)) {
                    return true;
                }
            }
        }

        // Everything is stuff
        return keyword.equals(rootCategory);

    }

    @Override
    public int getKeywordDepth(String keyword) {
        try {
            return getRootCategory().findKeywordDepth(keyword);
        } catch(NullPointerException e) {
            log.error("The root category is missing: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int getKeywordOrder(String keyword) {
        List<IItemTreeItem> items = getItems(keyword);
        if(items != null && items.size() != 0) {
            return items.get(0).getOrder();
        } else {
            try {
                return getRootCategory().findCategoryOrder(keyword);
            } catch(NullPointerException e) {
                log.error("The root category is missing: " + e.getMessage());
                return -1;
            }
        }
    }

    /**
     * Checks if the given keyword is valid (i.e. represents either a registered item or a registered category)
     */
    @Override
    public boolean isKeywordValid(String keyword) {

        // Is the keyword an item?
        if(containsItem(keyword)) {
            return true;
        }

        // Or maybe a category ?
        else {
            IItemTreeCategory category = getCategory(keyword);
            return category != null;
        }
    }

    /**
     * Returns a reference to all categories.
     */
    @NotNull
    @Override
    public Collection<IItemTreeCategory> getAllCategories() {
        return categories.values();
    }

    @Override
    public IItemTreeCategory getRootCategory() {
        return categories.get(rootCategory);
    }

    @Override
    public void setRootCategory(@NotNull IItemTreeCategory category) {
        rootCategory = category.getName();
        categories.put(rootCategory, category);
    }

    @Override
    public IItemTreeCategory getCategory(String keyword) {
        return categories.get(keyword);
    }

    @Override
    public boolean isItemUnknown(String id, int damage) {
        return itemsById.get(id) == null;
    }

    @NotNull
    @Override
    public List<IItemTreeItem> getItems(@Nullable String id, int damage, @Nullable CompoundTag extra) {
        if(id == null) {
            return new ArrayList<>();
        }

        List<IItemTreeItem> items = itemsById.get(id);
        @NotNull List<IItemTreeItem> filteredItems = new ArrayList<>();
        if(items != null) {
            filteredItems.addAll(items);
        }

        // Filter items of same ID, but different damage value
        if(items != null && !items.isEmpty()) {
            items.stream().filter(item -> item.getDamage() != InvTweaksConst.DAMAGE_WILDCARD && item.getDamage() != damage).forEach(filteredItems::remove);
        }

        items = filteredItems;
        filteredItems = new ArrayList<>(items);

        // Filter items that don't match extra data
        if(extra != null && !items.isEmpty()) {
            items.stream().filter(item -> !TagHelper.areTagsEqual(item.getExtraData(), extra, true)).forEach(filteredItems::remove);
        }

        // If there's no matching item, create new ones
        if(filteredItems.isEmpty()) {
            int newItemOrder = highestOrder + 1;
            @NotNull IItemTreeItem newItemId = new InvTweaksItemTreeItem(String.format("%s-%d", id, damage), id, damage, null,
                    newItemOrder);
            @NotNull IItemTreeItem newItemDamage = new InvTweaksItemTreeItem(id, id,
                    InvTweaksConst.DAMAGE_WILDCARD, null, newItemOrder);
            addItem(getRootCategory().getName(), newItemId);
            addItem(getRootCategory().getName(), newItemDamage);
            filteredItems.add(newItemId);
            filteredItems.add(newItemDamage);
        }

        filteredItems.removeIf(Objects::isNull);

        return filteredItems;

    }

    @NotNull
    @Override
    public List<IItemTreeItem> getItems(String id, int damage) {
        return getItems(id, damage, null);
    }

    @Override
    public List<IItemTreeItem> getItems(String name) {
        return itemsByName.get(name);
    }

    @NotNull
    @Override
    public IItemTreeItem getRandomItem(@NotNull Random r) {
        return (IItemTreeItem) itemsByName.values().toArray()[r.nextInt(itemsByName.size())];
    }

    @Override
    public boolean containsItem(String name) {
        return itemsByName.containsKey(name);
    }

    @Override
    public boolean containsCategory(String name) {
        return categories.containsKey(name);
    }

    @NotNull
    @Override
    public IItemTreeCategory addCategory(String parentCategory, String newCategory) throws NullPointerException {
        @NotNull IItemTreeCategory addedCategory = new InvTweaksItemTreeCategory(newCategory);
        addCategory(parentCategory, addedCategory);
        return addedCategory;
    }

    @NotNull
    @Override
    public IItemTreeItem addItem(String parentCategory, String name, String id, int damage, int order)
            throws NullPointerException {
        return addItem(parentCategory, name, id, damage, null, order);
    }

    @NotNull
    @Override
    public IItemTreeItem addItem(String parentCategory, String name, String id, int damage, CompoundTag extra, int order)
            throws NullPointerException {
        @NotNull InvTweaksItemTreeItem addedItem = new InvTweaksItemTreeItem(name, id, damage, extra, order);
        addItem(parentCategory, addedItem);
        return addedItem;
    }

    @Override
    public void addCategory(String parentCategory, @NotNull IItemTreeCategory newCategory) throws NullPointerException {
        // Build tree
        categories.get(parentCategory).addCategory(newCategory);

        // Register category
        categories.put(newCategory.getName(), newCategory);
    }

    @Override
    public void addItem(String parentCategory, @NotNull IItemTreeItem newItem) throws NullPointerException {
        highestOrder = Math.max(highestOrder, newItem.getOrder());

        // Build tree
        categories.get(parentCategory).addItem(newItem);

        // Register item
        if(itemsByName.containsKey(newItem.getName())) {
            itemsByName.get(newItem.getName()).add(newItem);
        } else {
            @NotNull List<IItemTreeItem> list = new ArrayList<>();
            list.add(newItem);
            itemsByName.put(newItem.getName(), list);
        }
        if(itemsById.containsKey(newItem.getId())) {
            itemsById.get(newItem.getId()).add(newItem);
        } else {
            @NotNull List<IItemTreeItem> list = new ArrayList<>();
            list.add(newItem);
            itemsById.put(newItem.getId(), list);
        }
    }

    public int getHighestOrder() {
        return highestOrder;
    }

    @Override
    public void registerOre(String category, String name, String oreName, int order) {
        for(@Nullable ItemStack i : OreDictionary.getOres(oreName)) {
            if(i != null) {
                // TODO: It looks like Mojang changed the internal name type to ResourceLocation. Evaluate how much of a pain that will be.
                addItem(category,
                        new InvTweaksItemTreeItem(name, i.getItem().getRegistryName().toString(), i.getItemDamage(), null, order));
            } else {
                log.warn(String.format("An OreDictionary entry for %s is null", oreName));
            }
        }
        oresRegistered.add(new OreDictInfo(category, name, oreName, order));
    }

    @SubscribeEvent
    public void oreRegistered(@NotNull OreDictionary.OreRegisterEvent ev) {
        // TODO: It looks like Mojang changed the internal name type to ResourceLocation. Evaluate how much of a pain that will be.
        oresRegistered.stream().filter(ore -> ore.oreName.equals(ev.getName())).forEach(ore -> {
            @NotNull ItemStack evOre = ev.getOre();
            if(!evOre.isEmpty()) {
                // TODO: It looks like Mojang changed the internal name type to ResourceLocation. Evaluate how much of a pain that will be.
                addItem(ore.category, new InvTweaksItemTreeItem(ore.name, evOre.getItem().getRegistryName().toString(),
                        evOre.getItemDamage(), null, ore.order));
            } else {
                log.warn(String.format("An OreDictionary entry for %s is null", ev.getName()));
            }
        });
    }

    private static class OreDictInfo {
        String category;
        String name;
        String oreName;
        int order;

        OreDictInfo(String category_, String name_, String oreName_, int order_) {
            category = category_;
            name = name_;
            oreName = oreName_;
            order = order_;
        }
    }
}

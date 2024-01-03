package town.kibty.enderrelay.recipe;

import net.minecraft.core.GlobalPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import town.kibty.enderrelay.EnderRelay;
import town.kibty.enderrelay.block.EnderRelayBlockEntity;

public class EnderRelayRecipe extends CustomRecipe {
    public static final Item[][] RECIPE = new Item[][] {
            {Items.OBSIDIAN, Items.POPPED_CHORUS_FRUIT, Items.OBSIDIAN},
            {Items.POPPED_CHORUS_FRUIT, Items.BARRIER, Items.POPPED_CHORUS_FRUIT},
            {Items.OBSIDIAN, Items.POPPED_CHORUS_FRUIT, Items.OBSIDIAN}
    };

    public static final SimpleCraftingRecipeSerializer<EnderRelayRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(EnderRelayRecipe::new);
    public EnderRelayRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int i = 0;
        for (Item[] row : RECIPE) {
            for (Item item : row) {
                ItemStack gotItem = container.getItem(i);
                i++;
                if (item == Items.BARRIER) {
                    if (!gotItem.is(Items.COMPASS)) return false;
                    if (!CompassItem.isLodestoneCompass(gotItem)) return false;
                    if (CompassItem.getLodestonePosition(gotItem.getTag()) == null) return false;
                    if (level.isClientSide) continue;
                    GlobalPos pos = CompassItem.getLodestonePosition(gotItem.getTag());
                    Level lodedstoneLevel = level.getServer().getLevel(pos.dimension());
                    if (lodedstoneLevel.dimensionTypeId() != BuiltinDimensionTypes.END) return false;

                    continue;
                }
                if (!gotItem.is(item)) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack compass = container.getItem(4);
        GlobalPos pos = CompassItem.getLodestonePosition(compass.getTag());
        ItemStack relay = new ItemStack(EnderRelay.ENDER_RELAY_ITEM, 1);
        CompoundTag blockTag = new CompoundTag();

        blockTag.putString(EnderRelayBlockEntity.DIMENSION_ID_KEY, pos.dimension().location().toString());
        blockTag.putIntArray(EnderRelayBlockEntity.POSITION_KEY, new int[] { pos.pos().getX(), pos.pos().getY(), pos.pos().getZ() });
        BlockItem.setBlockEntityData(relay, EnderRelay.ENDER_RELAY_BLOCK_ENTITY, blockTag);
        return relay;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x == 3 && y == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}

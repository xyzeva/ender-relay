package town.kibty.enderrelay.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import town.kibty.enderrelay.EnderRelay;

public class EnderRelayBlockEntity extends BlockEntity {
    public static final String DIMENSION_ID_KEY = "RelayDimensionId";
    public static final String POSITION_KEY = "RelayPosition";
    private String dimension;
    private Integer x;
    private Integer y;
    private Integer z;
    public EnderRelayBlockEntity(BlockPos pos, BlockState state) {
        super(EnderRelay.ENDER_RELAY_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        if (nbt.contains(DIMENSION_ID_KEY)) dimension = nbt.getString(DIMENSION_ID_KEY);
        if (nbt.contains(POSITION_KEY)) {
            int[] coordsArr = nbt.getIntArray(POSITION_KEY);
            x = coordsArr[0];
            y = coordsArr[1];
            z = coordsArr[2];
        }

        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        if (dimension != null) nbt.putString(DIMENSION_ID_KEY, dimension);
        if (x != null) nbt.putIntArray(POSITION_KEY, new int[] { x, y, z });

        super.saveAdditional(nbt);
    }

    public boolean hasNoLocation() {
        return getDimension() == null || getX() == null || getY() == null || getZ() == null;
    }

    public String getDimension() {
        return dimension;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }
}

package com.leclowndu93150.thaumaturge.content.eldritch.block;

import com.leclowndu93150.thaumaturge.content.eldritch.maze.MazeSavedData;
import com.leclowndu93150.thaumaturge.content.entity.EntityCultistCleric;
import com.leclowndu93150.thaumaturge.content.entity.EntityCultistKnight;
import com.leclowndu93150.thaumaturge.content.entity.EntityEldritchGuardian;
import com.leclowndu93150.thaumaturge.registry.TCBlockEntities;
import com.leclowndu93150.thaumaturge.registry.TCBlocks;
import com.leclowndu93150.thaumaturge.registry.TCEntities;
import com.leclowndu93150.thaumaturge.registry.TCSounds;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public final class BlockEntityEldritchAltar extends BlockEntity {
    public static final byte SPAWN_CULTIST = 0;
    public static final byte SPAWN_GUARDIAN = 1;

    private static final int WARMUP_TICKS = 80;
    private static final int SPAWN_INTERVAL = 40;
    private static final int MAX_KNIGHTS = 8;
    private static final int SCAN_RANGE = 24;

    private boolean spawner;
    private boolean mazeChecked;
    private boolean open;
    private boolean spawnedClerics;
    private byte spawnType;
    private byte eyes;
    private int counter;

    public BlockEntityEldritchAltar(BlockPos pos, BlockState state) {
        super(TCBlockEntities.ELDRITCH_ALTAR.get(), pos, state);
    }

    public void serverTick(Level level, BlockPos pos) {
        if (!mazeChecked) {
            mazeChecked = true;
            checkForMaze();
            setChanged();
        }
        if (!spawner || counter++ < WARMUP_TICKS || counter % SPAWN_INTERVAL != 0) {
            return;
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        switch (spawnType) {
            case SPAWN_CULTIST -> {
                if (!spawnedClerics) {
                    spawnClerics(serverLevel, pos);
                } else {
                    spawnGuards(serverLevel, pos);
                }
            }
            case SPAWN_GUARDIAN -> spawnGuardian(serverLevel, pos);
        }
    }

    private void spawnGuards(ServerLevel level, BlockPos pos) {
        List<EntityCultistCleric> clerics = level.getEntitiesOfClass(
                EntityCultistCleric.class, new AABB(pos).inflate(SCAN_RANGE, 16.0, SCAN_RANGE));
        if (clerics.isEmpty()) {
            setSpawner(false);
            return;
        }
        List<Mob> cultists = level.getEntitiesOfClass(
                Mob.class,
                new AABB(pos).inflate(SCAN_RANGE, 16.0, SCAN_RANGE),
                mob -> mob instanceof EntityCultistKnight || mob instanceof EntityCultistCleric);
        if (cultists.size() >= MAX_KNIGHTS) {
            return;
        }
        EntityCultistKnight knight = new EntityCultistKnight(TCEntities.CULTIST_KNIGHT.get(), level);
        int x = pos.getX()
                + Mth.randomBetweenInclusive(level.getRandom(), 4, 10)
                        * Mth.randomBetweenInclusive(level.getRandom(), -1, 1);
        int y = pos.getY()
                + Mth.randomBetweenInclusive(level.getRandom(), 0, 3)
                        * Mth.randomBetweenInclusive(level.getRandom(), -1, 1);
        int z = pos.getZ()
                + Mth.randomBetweenInclusive(level.getRandom(), 4, 10)
                        * Mth.randomBetweenInclusive(level.getRandom(), -1, 1);
        BlockPos spawnPos = new BlockPos(x, y, z);
        if (!level.getBlockState(spawnPos.below()).isSolidRender(level, spawnPos.below())) {
            return;
        }
        knight.moveTo(x + 0.5, y, z + 0.5, 0.0F, 0.0F);
        if (level.noCollision(knight) && !level.containsAnyLiquid(knight.getBoundingBox())) {
            knight.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.EVENT, null);
            knight.restrictTo(pos, 16);
            level.addFreshEntity(knight);
        }
    }

    private void spawnGuardian(ServerLevel level, BlockPos pos) {
        EntityEldritchGuardian guardian = new EntityEldritchGuardian(TCEntities.ELDRITCH_GUARDIAN.get(), level);
        int x = pos.getX()
                + Mth.randomBetweenInclusive(level.getRandom(), 4, 10)
                        * Mth.randomBetweenInclusive(level.getRandom(), -1, 1);
        int y = pos.getY()
                + Mth.randomBetweenInclusive(level.getRandom(), 0, 3)
                        * Mth.randomBetweenInclusive(level.getRandom(), -1, 1);
        int z = pos.getZ()
                + Mth.randomBetweenInclusive(level.getRandom(), 4, 10)
                        * Mth.randomBetweenInclusive(level.getRandom(), -1, 1);
        BlockPos spawnPos = new BlockPos(x, y, z);
        if (!level.getBlockState(spawnPos.below()).isSolidRender(level, spawnPos.below())) {
            return;
        }
        if (!level.getEntitiesOfClass(EntityEldritchGuardian.class, new AABB(spawnPos).inflate(32.0, 16.0, 32.0))
                .isEmpty()) {
            return;
        }
        guardian.moveTo(x + 0.5, y, z + 0.5, 0.0F, 0.0F);
        if (level.noCollision(guardian) && !level.containsAnyLiquid(guardian.getBoundingBox())) {
            guardian.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.EVENT, null);
            guardian.restrictTo(pos, 16);
            level.addFreshEntity(guardian);
        }
    }

    private void spawnClerics(ServerLevel level, BlockPos pos) {
        int success = 0;
        for (int a = 0; a < 4; a++) {
            int xx = a < 2 ? -2 : 2;
            int zz = a % 2 == 0 ? -2 : 2;
            if (!level.getBlockState(pos.offset(xx, -1, zz)).isSolidRender(level, pos.offset(xx, -1, zz))) {
                continue;
            }
            EntityCultistCleric cleric = new EntityCultistCleric(TCEntities.CULTIST_CLERIC.get(), level);
            cleric.moveTo(pos.getX() + 0.5 + xx, pos.getY(), pos.getZ() + 0.5 + zz, 0.0F, 0.0F);
            if (level.noCollision(cleric) && !level.containsAnyLiquid(cleric.getBoundingBox())) {
                cleric.restrictTo(pos, 8);
                cleric.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null);
                if (level.addFreshEntity(cleric)) {
                    success++;
                    cleric.setRitualist(true);
                }
            }
        }
        if (success > 2) {
            spawnedClerics = true;
            setChanged();
        }
    }

    public boolean checkForMaze() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        MazeSavedData maze = MazeSavedData.get(serverLevel);
        int w = 15 + serverLevel.getRandom().nextInt(8) * 2;
        int h = 15 + serverLevel.getRandom().nextInt(8) * 2;
        int chunkX = worldPosition.getX() >> 4;
        int chunkZ = worldPosition.getZ() >> 4;
        if (!maze.mazesInRange(chunkX, chunkZ, w, h)) {
            maze.generateMaze(chunkX, chunkZ, w, h, serverLevel.getRandom().nextLong());
            return false;
        }
        return true;
    }

    public void openPortal() {
        if (level == null || level.isClientSide()) {
            return;
        }
        open = true;
        BlockPos above = worldPosition.above();
        level.removeBlock(above, false);
        level.setBlock(above, TCBlocks.ELDRITCH_PORTAL.get().defaultBlockState(), 3);
        level.playSound(null, worldPosition, TCSounds.WAND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public boolean isSpawner() {
        return spawner;
    }

    public void setSpawner(boolean spawner) {
        this.spawner = spawner;
    }

    public byte getSpawnType() {
        return spawnType;
    }

    public void setSpawnType(byte spawnType) {
        this.spawnType = spawnType;
    }

    public byte getEyes() {
        return eyes;
    }

    public void setEyes(byte eyes) {
        this.eyes = eyes;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider registries) {
        super.loadAdditional(input, registries);
        eyes = input.getByte("eyes");
        mazeChecked = input.getBoolean("mazeChecked");
        open = input.getBoolean("open");
        spawnedClerics = input.getBoolean("spawnedClerics");
        spawner = input.getBoolean("spawner");
        spawnType = input.getByte("spawntype");
    }

    @Override
    protected void saveAdditional(CompoundTag output, HolderLookup.Provider registries) {
        super.saveAdditional(output, registries);
        output.putByte("eyes", eyes);
        output.putBoolean("mazeChecked", mazeChecked);
        output.putBoolean("open", open);
        output.putBoolean("spawnedClerics", spawnedClerics);
        output.putBoolean("spawner", spawner);
        output.putByte("spawntype", spawnType);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = super.getUpdateTag(registries);
        {
            CompoundTag output = new CompoundTag();
            saveAdditional(output, registries);
            nbt.merge(output);
        }
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}

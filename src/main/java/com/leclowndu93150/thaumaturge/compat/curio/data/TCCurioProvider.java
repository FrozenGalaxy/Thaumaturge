package com.leclowndu93150.thaumaturge.compat.curio.data;

import com.leclowndu93150.thaumaturge.TCIds;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

public class TCCurioProvider extends CuriosDataProvider {
    public TCCurioProvider(
            PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(TCIds.MODID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        createEntities("players").addPlayer().addSlots("head", "necklace", "ring", "belt", "charm");
    }
}

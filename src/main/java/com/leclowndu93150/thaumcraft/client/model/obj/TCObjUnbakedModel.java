package com.leclowndu93150.thaumcraft.client.model.obj;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

public final class TCObjUnbakedModel extends AbstractUnbakedModel {
    private final TCObjGeometry geometry;

    public TCObjUnbakedModel(StandardModelParameters parameters, ResourceLocation model, boolean flipV, boolean cornerSpace) {
        super(parameters);
        this.geometry = new TCObjGeometry(model, flipV, cornerSpace);
    }

    @Override
    public UnbakedGeometry geometry() {
        return geometry;
    }

    public static final class Loader implements UnbakedModelLoader<TCObjUnbakedModel> {
        public static final Loader INSTANCE = new Loader();

        private Loader() {}

        @Override
        public TCObjUnbakedModel read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
            StandardModelParameters parameters = StandardModelParameters.parse(jsonObject, context);
            ResourceLocation model = ResourceLocation.parse(GsonHelper.getAsString(jsonObject, "model"));
            boolean flipV = GsonHelper.getAsBoolean(jsonObject, "flip_v", false);
            boolean cornerSpace = GsonHelper.getAsBoolean(jsonObject, "corner_space", false);
            return new TCObjUnbakedModel(parameters, model, flipV, cornerSpace);
        }
    }
}

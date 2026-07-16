package com.leclowndu93150.thaumcraft.client.model.obj;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.NamedRenderTypeManager;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.IModelBuilder;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.joml.Matrix4f;

public final class TCObjGeometry implements IUnbakedGeometry<TCObjGeometry> {
    private static final float CENTER_OFFSET = 0.5F;
    private static final String PARTICLE_SLOT = "particle";
    private static final ResourceLocation DEFAULT_RENDER_TYPE = ResourceLocation.withDefaultNamespace("cutout");

    private final ResourceLocation model;
    private final boolean flipV;
    private final boolean cornerSpace;
    private final ResourceLocation renderType;

    public TCObjGeometry(ResourceLocation model, boolean flipV, boolean cornerSpace, ResourceLocation renderType) {
        this.model = model;
        this.flipV = flipV;
        this.cornerSpace = cornerSpace;
        this.renderType = renderType;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
                           Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState,
                           ItemOverrides overrides) {
        MeshModel mesh = loadMesh();
        Matrix4f transform = new Matrix4f()
                .translate(CENTER_OFFSET, CENTER_OFFSET, CENTER_OFFSET)
                .mul(modelState.getRotation().getMatrix());
        if (cornerSpace) {
            transform.translate(-CENTER_OFFSET, -CENTER_OFFSET, -CENTER_OFFSET);
        }

        RenderTypeGroup renderTypes = NamedRenderTypeManager.get(renderType);
        TextureAtlasSprite particle = spriteGetter.apply(context.getMaterial(PARTICLE_SLOT));
        IModelBuilder<?> builder = IModelBuilder.of(context.useAmbientOcclusion(), context.useBlockLight(),
                context.isGui3d(), context.getTransforms(), overrides, particle, renderTypes);

        List<BakedQuad> quads = new ArrayList<>();
        for (MeshPart part : mesh.parts()) {
            String slot = part.material() == null ? PARTICLE_SLOT : part.material().name();
            if (!context.hasMaterial(slot)) {
                slot = PARTICLE_SLOT;
            }
            TextureAtlasSprite sprite = spriteGetter.apply(context.getMaterial(slot));
            quads.clear();
            MeshQuadBaker.bakePart(mesh, part, sprite, part.tintIndex(), transform, flipV, quads);
            for (BakedQuad quad : quads) {
                builder.addUnculledFace(quad);
            }
        }
        return builder.build();
    }

    private MeshModel loadMesh() {
        try {
            return TCObjLoader.load(Minecraft.getInstance().getResourceManager(), model);
        } catch (IOException e) {
            throw new RuntimeException("Could not load OBJ model at " + model, e);
        }
    }

    public static final class Loader implements IGeometryLoader<TCObjGeometry> {
        public static final Loader INSTANCE = new Loader();

        private Loader() {}

        @Override
        public TCObjGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
            ResourceLocation model = ResourceLocation.parse(GsonHelper.getAsString(jsonObject, "model"));
            boolean flipV = GsonHelper.getAsBoolean(jsonObject, "flip_v", false);
            boolean cornerSpace = GsonHelper.getAsBoolean(jsonObject, "corner_space", false);
            ResourceLocation renderType = jsonObject.has("render_type")
                    ? ResourceLocation.parse(GsonHelper.getAsString(jsonObject, "render_type"))
                    : DEFAULT_RENDER_TYPE;
            return new TCObjGeometry(model, flipV, cornerSpace, renderType);
        }
    }
}

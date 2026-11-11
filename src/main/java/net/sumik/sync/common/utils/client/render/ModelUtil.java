package net.sumik.sync.common.utils.client.render;

import net.minecraft.client.model.geom.ModelPart;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sumik.sync.common.utils.math.Voxel;
import net.sumik.sync.common.utils.math.VoxelIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.lang.reflect.Field;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@OnlyIn(Dist.CLIENT)
public final class ModelUtil {
    private static final Field CUBES;
    private static final Field CHILDREN;

    static {
        try {
            CUBES = ModelPart.class.getDeclaredField("cubes");
            CUBES.setAccessible(true);
            CHILDREN = ModelPart.class.getDeclaredField("children");
            CHILDREN.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static ModelPart copy(ModelPart original) {
        ModelPart copy = new ModelPart(getCubes(original), getChildren(original));
        copy.copyFrom(original);
        return copy;
    }

    public static Stream<Voxel> asVoxels(ModelPart part) {
        return asVoxels(0, 0, 0, part);
    }

    public static Stream<Voxel> asVoxels(float x, float y, float z, ModelPart part) {
        final float pivotX = x + part.x;
        final float pivotY = y + part.y;
        final float pivotZ = z + part.z;
        return Stream.concat(
                getCubes(part).stream().flatMap(cube -> asVoxels(pivotX, pivotY, pivotZ, cube)),
                getChildren(part).values().stream().flatMap(p -> asVoxels(pivotX, pivotY, pivotZ, p))
        );
    }

    @SuppressWarnings("unchecked")
    private static List<ModelPart.Cube> getCubes(ModelPart part) {
        try {
            return (List<ModelPart.Cube>) CUBES.get(part);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, ModelPart> getChildren(ModelPart part) {
        try {
            return (Map<String, ModelPart>) CHILDREN.get(part);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Stream<Voxel> asVoxels(float x, float y, float z, ModelPart.Cube cube) {
        int sizeX = (int)(cube.maxX - cube.minX);
        int sizeY = (int)(cube.maxY - cube.minY);
        int sizeZ = (int)(cube.maxZ - cube.minZ);
        Iterator<Voxel> iterator = new VoxelIterator(x, y, z, sizeX, sizeY, sizeZ);
        Spliterator<Voxel> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
        return StreamSupport.stream(spliterator, false);
    }

    private ModelUtil() {
    }
}

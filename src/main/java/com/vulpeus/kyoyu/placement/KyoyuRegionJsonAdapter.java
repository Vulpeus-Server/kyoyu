package com.vulpeus.kyoyu.placement;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

import java.io.IOException;

public class KyoyuRegionJsonAdapter extends TypeAdapter<KyoyuRegion> {
    @Override
    public void write(JsonWriter out, KyoyuRegion value) throws IOException {
        out.beginObject();
        out.name("name").value(value.getName());
        out.name("pos").value(value.getPos().asLong());
        out.name("mirror").value(value.getMirror().name());
        out.name("rotation").value(value.getRotation().name());
        out.name("ignoreEntity").value(value.ignoreEntities());
        out.name("enable").value(value.isEnable());
        out.endObject();
    }

    @Override
    public KyoyuRegion read(JsonReader in) throws IOException {
        String name = "none";
        BlockPos pos = BlockPos.ZERO;
        Mirror mirror = Mirror.NONE;
        Rotation rotation = Rotation.NONE;
        boolean ignoreEntity = false;
        boolean enable = true;

        in.beginObject();
        while (in.hasNext()) {
            String key = in.nextName();
            switch (key) {
                case "name":
                    name = in.nextString();
                    break;
                case "pos":
                    pos = BlockPos.of(in.nextLong());
                    break;
                case "mirror":
                    mirror = Mirror.valueOf(in.nextString());
                    break;
                case "rotation":
                    rotation = Rotation.valueOf(in.nextString());
                    break;
                case "ignoreEntity":
                    ignoreEntity = in.nextBoolean();
                    break;
                case "enable":
                    enable = in.nextBoolean();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();

        return new KyoyuRegion(pos, mirror, rotation, name, ignoreEntity, enable);
    }
}

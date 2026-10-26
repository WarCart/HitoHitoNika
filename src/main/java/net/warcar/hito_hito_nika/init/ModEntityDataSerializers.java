package net.warcar.hito_hito_nika.init;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import xyz.pixelatedw.mineminenomi.api.WyHelper;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, HitoHitoNoMiNikaMod.MOD_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VECTOR_CODEC = new StreamCodec<>() {
        @Override
        public Vec3 decode(RegistryFriendlyByteBuf buffer) {
            double x = buffer.readDouble();
            double y = buffer.readDouble();
            double z = buffer.readDouble();
            return new Vec3(x, y, z);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, Vec3 vector) {
            buffer.writeDouble(vector.x);
            buffer.writeDouble(vector.y);
            buffer.writeDouble(vector.z);
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, ArrayList<Vec3>> TURNS_CODEC = new StreamCodec<>() {
        @Override
        public ArrayList<Vec3> decode(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readInt();
            ArrayList<Vec3> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                double x = buffer.readDouble();
                double y = buffer.readDouble();
                double z = buffer.readDouble();
                list.add(new Vec3(x, y, z));
            }
            return list;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, ArrayList<Vec3> list) {
            buffer.writeInt(list.size());
            for (Vec3 vec : list) {
                buffer.writeDouble(vec.x);
                buffer.writeDouble(vec.y);
                buffer.writeDouble(vec.z);
            }
        }
    };

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Vec3>> VECTOR_SERIALIZER = register("Vector", () -> new EntityDataSerializer<Vec3>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, Vec3> codec() {
            return VECTOR_CODEC;
        }

        @Override
        public Vec3 copy(Vec3 vector) {
            return new Vec3(vector.x, vector.y, vector.z);
        }
    });

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<ArrayList<Vec3>>> TURNS_SERIALIZER = register("Turns", () -> new EntityDataSerializer<>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ArrayList<Vec3>> codec() {
            return TURNS_CODEC;
        }

        @Override
        public ArrayList<Vec3> copy(ArrayList<Vec3> list) {
            return new ArrayList<>(list);
        }
    });

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }

    public static <T extends EntityDataSerializer<?>> DeferredHolder<EntityDataSerializer<?>, T> register(String name, Supplier<T> sup) {
        return SERIALIZERS.register(WyHelper.getResourceName(name), sup);
    }
}

package org.bukkit.craftbukkit.generator;

import net.minecraft.server.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class CustomChunkGenerator extends InternalChunkGenerator {
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final Random random;

    public CustomChunkGenerator(World world, long seed, ChunkGenerator generator) {
        this.world = (WorldServer) world;
        this.generator = generator;

        this.random = new Random(seed);
    }

    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    public Chunk getOrCreateChunk(int x, int z) {
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        byte[] types = generator.generate(world.getWorld(), random, x, z);

        Chunk chunk = new Chunk(world, types, x, z);

        chunk.initLighting();

        return chunk;
    }

    public void getChunkAt(IChunkProvider icp, int i, int i1) {
        // Nothing!
    }

    public boolean saveChunks(boolean bln, IProgressUpdate ipu) {
        return true;
    }

    public boolean unloadChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        return generator.generate(world, random, x, z);
    }

    public Chunk getChunkAt(int x, int z) {
        return getOrCreateChunk(x, z);
    }

    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return generator.canSpawn(world, x, z);
    }

    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return generator.getDefaultPopulators(world);
    }
}

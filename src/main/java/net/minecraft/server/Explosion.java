package net.minecraft.server;

import com.legacyminecraft.poseidon.PoseidonConfig;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// CraftBukkit start
// CraftBukkit end

public class Explosion {
    public boolean setFire = false;
    private final Random random = new Random();
    private final World world;
    public double posX;
    public double posY;
    public double posZ;
    public Entity source;
    public EntityDamageEvent.DamageCause customDamageCause = null; // Poseidon
    public float size;
    public Set<ChunkPosition> blocks = new HashSet<>(); // UberBukkit: Set -> Set<ChunkPosition>

    public boolean wasCanceled = false; // CraftBukkit

    public Explosion(World world, Entity entity, double d0, double d1, double d2, float f) {
        this.world = world;
        this.source = entity;
        this.size = f;
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
    }

    public void a() {
        float f = this.size;
        byte b0 = 16;

        int i;
        int j;
        int k;
        double d0;
        double d1;
        double d2;

        for (i = 0; i < b0; ++i) {
            for (j = 0; j < b0; ++j) {
                for (k = 0; k < b0; ++k) {
                    if (i == 0 || i == b0 - 1 || j == 0 || j == b0 - 1 || k == 0 || k == b0 - 1) {
                        double d3 = ((float) i / ((float) b0 - 1.0F) * 2.0F - 1.0F);
                        double d4 = ((float) j / ((float) b0 - 1.0F) * 2.0F - 1.0F);
                        double d5 = ((float) k / ((float) b0 - 1.0F) * 2.0F - 1.0F);
                        double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                        d3 /= d6;
                        d4 /= d6;
                        d5 /= d6;
                        float f1 = this.size * (0.7F + this.world.random.nextFloat() * 0.6F);

                        d0 = this.posX;
                        d1 = this.posY;
                        d2 = this.posZ;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                            int l = MathHelper.floor(d0);
                            int i1 = MathHelper.floor(d1);
                            int j1 = MathHelper.floor(d2);
                            int k1 = this.world.getTypeId(l, i1, j1);

                            if (k1 > 0) {
                                f1 -= (Block.byId[k1].a(this.source) + 0.3F) * f2;
                            }

                            if (f1 > 0.0F) {
                                this.blocks.add(new ChunkPosition(l, i1, j1));
                            }

                            d0 += d3 * (double) f2;
                            d1 += d4 * (double) f2;
                            d2 += d5 * (double) f2;
                        }
                    }
                }
            }
        }

        this.size *= 2.0F;
        i = MathHelper.floor(this.posX - (double) this.size - 1.0D);
        j = MathHelper.floor(this.posX + (double) this.size + 1.0D);
        k = MathHelper.floor(this.posY - (double) this.size - 1.0D);
        int l1 = MathHelper.floor(this.posY + (double) this.size + 1.0D);
        int i2 = MathHelper.floor(this.posZ - (double) this.size - 1.0D);
        int j2 = MathHelper.floor(this.posZ + (double) this.size + 1.0D);
        List list = this.world.b(this.source, AxisAlignedBB.b(i, k, i2, j, l1, j2));
        Vec3D vec3d = Vec3D.create(this.posX, this.posY, this.posZ);

        /*
         * Whether explosions should be optimized or not
         * A backport from PaperMC
         * Config option:
         * optimizedExplosions: false
         */
        boolean optimizeExplosions = (boolean) PoseidonConfig.getInstance().getProperty("world-settings.optimized-explosions");
        boolean sendMotion = (boolean) PoseidonConfig.getInstance().getProperty("world-settings.send-explosion-velocity");

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = (Entity) list.get(k2);
            double d7 = entity.f(this.posX, this.posY, this.posZ) / (double) this.size;

            if (d7 <= 1.0D) {
                d0 = entity.locX - this.posX;
                d1 = entity.locY - this.posY;
                d2 = entity.locZ - this.posZ;
                double d8 = MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2);

                d0 /= d8;
                d1 /= d8;
                d2 /= d8;
                double d9;
                if (optimizeExplosions) {
                    d9 = this.getBlockDensity(vec3d, entity); // Paper - Optimize explosions
                } else {
                    d9 = this.world.a(vec3d, entity.boundingBox);
                }
                double d10 = (1.0D - d7) * d9;

                // CraftBukkit start - explosion damage hook
                org.bukkit.Server server = this.world.getServer();
                org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                int damageDone = (int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) this.size + 1.0D);

                // Block explosion, damagee is not null
                if (damagee != null && (this.source == null || this.source instanceof EntityTNTPrimed)) {
                    // This event gets fired by tnt, exploding beds, and explosions created by plugins
                    // TODO: get the x/y/z of the tnt block?
                    EntityDamageByBlockEvent event = getEntityDamageByBlockEvent(damagee, damageDone);
                    server.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        entity.damageEntity(this.source, event.getDamage());
                        entity.motX += d0 * d10;
                        entity.motY += d1 * d10;
                        entity.motZ += d2 * d10;
                        if (sendMotion) { // Poseidon: fix explosion velocity
                            entity.velocityChanged = true;
                        }
                    }
                } else {
                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(this.source.getBukkitEntity(), damagee, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, damageDone);
                    server.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        entity.damageEntity(this.source, event.getDamage());
                        entity.motX += d0 * d10;
                        entity.motY += d1 * d10;
                        entity.motZ += d2 * d10;
                        if (sendMotion) { // Poseidon: fix explosion velocity
                            entity.velocityChanged = true;
                        }
                    }
                }
                // CraftBukkit end
            }
        }

        this.size = f;

        ArrayList<ChunkPosition> arraylist = new ArrayList<>();
        arraylist.addAll(this.blocks);

        if (this.setFire) {
            for (int l2 = arraylist.size() - 1; l2 >= 0; --l2) {
                ChunkPosition chunkposition = arraylist.get(l2);
                int i3 = chunkposition.x;
                int j3 = chunkposition.y;
                int k3 = chunkposition.z;
                int l3 = this.world.getTypeId(i3, j3, k3);
                int i4 = this.world.getTypeId(i3, j3 - 1, k3);

                if (l3 == 0 && Block.o[i4] && this.random.nextInt(3) == 0) {
                    this.world.setTypeId(i3, j3, k3, Block.FIRE.id);
                }
            }
        }
    }

    @NotNull
    private EntityDamageByBlockEvent getEntityDamageByBlockEvent(org.bukkit.entity.Entity damagee, int damageDone) {
        EntityDamageByBlockEvent event;
        if (this.customDamageCause != null) {
            event = new EntityDamageByBlockEvent(null, damagee, this.customDamageCause, damageDone);
        } else if (this.source instanceof EntityTNTPrimed) {
            event = new EntityDamageByBlockEvent(null, damagee, EntityDamageEvent.DamageCause.TNT_EXPLOSION, damageDone);
        } else {
            event = new EntityDamageByBlockEvent(null, damagee, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, damageDone);
        }
        return event;
    }

    public void a(boolean flag) {
        this.world.makeSound(this.posX, this.posY, this.posZ, "random.explode", 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);

        ArrayList<ChunkPosition> blocksCopy = new ArrayList<>(this.blocks);

        // CraftBukkit start
        org.bukkit.World bworld = this.world.getWorld();
        org.bukkit.entity.Entity explode = this.source == null ? null : this.source.getBukkitEntity();
        Location location = new Location(bworld, this.posX, this.posY, this.posZ);

        List<org.bukkit.block.Block> blockList = new ArrayList<>();
        for (int j = blocksCopy.size() - 1; j >= 0; j--) {
            ChunkPosition cpos = blocksCopy.get(j);
            // UberBukkit - No need to handle blocks that aren't in the world's boundaries
            if (cpos.y > 127 || cpos.y < 0) {
                blocksCopy.remove(j);
                continue;
            }

            org.bukkit.block.Block block = bworld.getBlockAt(cpos.x, cpos.y, cpos.z);
            if (block.getType() != org.bukkit.Material.AIR) {
                blockList.add(block);
            }
        }

        EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList);
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            this.wasCanceled = true;
            return;
        }

        // Project Poseidon Start
        // Backport from newer CraftBukkit
        blocksCopy.clear();
        this.blocks.clear();
        for (final org.bukkit.block.Block block2 : event.blockList()) {
            final ChunkPosition coords = new ChunkPosition(block2.getX(), block2.getY(), block2.getZ());
            blocksCopy.add(coords);
            this.blocks.add(coords);
        }
        // Project Poseidon End
        // CraftBukkit end

        for (int i = blocksCopy.size() - 1; i >= 0; --i) {
            ChunkPosition chunkposition = blocksCopy.get(i);
            int j = chunkposition.x;
            int k = chunkposition.y;
            int l = chunkposition.z;
            int i1 = this.world.getTypeId(j, k, l);

            if (flag) {
                double d0 = (float) j + this.world.random.nextFloat();
                double d1 = (float) k + this.world.random.nextFloat();
                double d2 = (float) l + this.world.random.nextFloat();
                double d3 = d0 - this.posX;
                double d4 = d1 - this.posY;
                double d5 = d2 - this.posZ;
                double d6 = MathHelper.a(d3 * d3 + d4 * d4 + d5 * d5);

                d3 /= d6;
                d4 /= d6;
                d5 /= d6;
                double d7 = 0.5D / (d6 / (double) this.size + 0.1D);

                d7 *= this.world.random.nextFloat() * this.world.random.nextFloat() + 0.3F;
                d3 *= d7;
                d4 *= d7;
                d5 *= d7;
                this.world.a("explode", (d0 + this.posX) / 2.0D, (d1 + this.posY) / 2.0D, (d2 + this.posZ) / 2.0D, d3, d4, d5);
                this.world.a("smoke", d0, d1, d2, d3, d4, d5);
            }

            // CraftBukkit - stop explosions from putting out fire
            if (i1 > 0 && i1 != Block.FIRE.id) {
                // CraftBukkit
                Block.byId[i1].dropNaturally(this.world, j, k, l, this.world.getData(j, k, l), event.getYield());
                this.world.setTypeId(j, k, l, 0);
                Block.byId[i1].d(this.world, j, k, l);
            }
        }
    }

    // Paper start - Optimize explosions
    private float getBlockDensity(Vec3D vec3d, Entity entity) {
        CacheKey key = new CacheKey(this, entity.boundingBox);
        Float blockDensity = this.world.explosionDensityCache.get(key);
        if (blockDensity == null) {
            blockDensity = this.world.a(vec3d, entity.boundingBox);
            this.world.explosionDensityCache.put(key, blockDensity);
        }

        return blockDensity;
    }

    static class CacheKey {
        private final World world;
        private final double posX, posY, posZ;
        private final double minX, minY, minZ;
        private final double maxX, maxY, maxZ;

        public CacheKey(Explosion explosion, AxisAlignedBB aabb) {
            this.world = explosion.world;
            this.posX = explosion.posX;
            this.posY = explosion.posY;
            this.posZ = explosion.posZ;
            this.minX = aabb.a;
            this.minY = aabb.b;
            this.minZ = aabb.c;
            this.maxX = aabb.d;
            this.maxY = aabb.e;
            this.maxZ = aabb.f;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (Double.compare(cacheKey.posX, posX) != 0) return false;
            if (Double.compare(cacheKey.posY, posY) != 0) return false;
            if (Double.compare(cacheKey.posZ, posZ) != 0) return false;
            if (Double.compare(cacheKey.minX, minX) != 0) return false;
            if (Double.compare(cacheKey.minY, minY) != 0) return false;
            if (Double.compare(cacheKey.minZ, minZ) != 0) return false;
            if (Double.compare(cacheKey.maxX, maxX) != 0) return false;
            if (Double.compare(cacheKey.maxY, maxY) != 0) return false;
            if (Double.compare(cacheKey.maxZ, maxZ) != 0) return false;
            return world.equals(cacheKey.world);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = world.hashCode();
            temp = Double.doubleToLongBits(posX);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(posY);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(posZ);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(minX);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(minY);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(minZ);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(maxX);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(maxY);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(maxZ);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
    // Paper end
}

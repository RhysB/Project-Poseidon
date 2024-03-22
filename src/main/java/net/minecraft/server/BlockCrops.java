package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BlockCrops extends BlockFlower {

    protected BlockCrops(int i, int j) {
        super(i, j);
        this.textureId = j;
        this.a(true);
        float f = 0.5F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    }

    protected boolean c(int i) {
        return i == Block.SOIL.id;
    }

    public void a(World world, int i, int j, int k, Random random) {
        super.a(world, i, j, k, random);
        if (world.getLightLevel(i, j + 1, k) >= 9) {
            int l = world.getData(i, j, k);

            if (l < 7) {
                float f = this.h(world, i, j, k);

                if (random.nextInt((int) (100.0F / f)) == 0) {
                    ++l;
                    world.setData(i, j, k, l);
                }
            }
        }
    }

    public void d_(World world, int i, int j, int k) {
        world.setData(i, j, k, 7);
    }

    private float h(World world, int i, int j, int k) {
        float f = 1.0F;
        int l = world.getTypeId(i, j, k - 1);
        int i1 = world.getTypeId(i, j, k + 1);
        int j1 = world.getTypeId(i - 1, j, k);
        int k1 = world.getTypeId(i + 1, j, k);
        int l1 = world.getTypeId(i - 1, j, k - 1);
        int i2 = world.getTypeId(i + 1, j, k - 1);
        int j2 = world.getTypeId(i + 1, j, k + 1);
        int k2 = world.getTypeId(i - 1, j, k + 1);
        boolean flag = j1 == this.id || k1 == this.id;
        boolean flag1 = l == this.id || i1 == this.id;
        boolean flag2 = l1 == this.id || i2 == this.id || j2 == this.id || k2 == this.id;

        for (int l2 = i - 1; l2 <= i + 1; ++l2) {
            for (int i3 = k - 1; i3 <= k + 1; ++i3) {
                int j3 = world.getTypeId(l2, j - 1, i3);
                float f1 = 0.0F;

                if (j3 == Block.SOIL.id) {
                    f1 = 1.0F;
                    if (world.getData(l2, j - 1, i3) > 0) {
                        f1 = 3.0F;
                    }
                }

                if (l2 != i || i3 != k) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1) {
            f /= 2.0F;
        }

        return f;
    }

    public int a(int i, int j) {
        if (j < 0) {
            j = 7;
        }

        return this.textureId + j;
    }

    public Optional<List<ItemStack>> getDrops(World world, int x, int y, int z, int data){
        Optional<List<ItemStack>> ret = super.getDrops(world, x, y, z, data);
        List<ItemStack> stacks = ret.orElse(new ArrayList<>(3));
        for(int i = 0; i < 3; i++){
            if(world.random.nextInt(15) <= 1){
                stacks.add(new ItemStack(Item.SEEDS));
            }
        }
        return Optional.of(stacks);
    }

    public int getDropId(int i, Random random) {
        return i == 7 ? Item.WHEAT.id : -1;
    }

    public int a(Random random) {
        return 1;
    }
}

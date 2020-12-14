package net.minecraft.server;

import com.legacyminecraft.poseidon.PoseidonConfig;

public class BlockFence extends Block {
    private boolean modernFencingBounding = false;

    public BlockFence(int i, int j) {
        super(i, j, Material.WOOD);
        modernFencingBounding = (boolean) PoseidonConfig.getInstance().getConfigOption("world-settings.use-modern-fence-bounding-boxes", false);
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.getTypeId(i, j - 1, k) == this.id ? true : (!world.getMaterial(i, j - 1, k).isBuildable() ? false : super.canPlace(world, i, j, k));
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        if(!modernFencingBounding) {
            return AxisAlignedBB.b((double) i, (double) j, (double) k, (double) (i + 1), (double) ((float) j + 1.5F), (double) (k + 1));
        }

        boolean flag = this.b(world, i, j, k - 1);
        boolean flag1 = this.b(world, i, j, k + 1);
        boolean flag2 = this.b(world, i - 1, j, k);
        boolean flag3 = this.b(world, i + 1, j, k);
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.375F;
        float f3 = 0.625F;

        if (flag) {
            f2 = 0.0F;
        }

        if (flag1) {
            f3 = 1.0F;
        }

        if (flag2) {
            f = 0.0F;
        }

        if (flag3) {
            f1 = 1.0F;
        }

        return AxisAlignedBB.b((double) ((float) i + f), (double) j, (double) ((float) k + f2), (double) ((float) i + f1), (double) ((float) j + 1.5F), (double) ((float) k + f3));
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getTypeId(i, j, k);

        if (l != this.id) {
            Block block = Block.byId[l];

            return block != null && block.material.h() && block.b() ? block.material != Material.PUMPKIN : false;
        } else {
            return true;
        }
    }


    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }
}

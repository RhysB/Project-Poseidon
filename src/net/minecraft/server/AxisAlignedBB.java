package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class AxisAlignedBB {

    private static List g = new ArrayList();
    private static int h = 0;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public static AxisAlignedBB a(double d0, double d1, double d2, double d3, double d4, double d5) {
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public static void a() {
        h = 0;
    }

    public static AxisAlignedBB b(double d0, double d1, double d2, double d3, double d4, double d5) {
        if (h >= g.size()) {
            g.add(a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D));
        }

        return ((AxisAlignedBB) g.get(h++)).c(d0, d1, d2, d3, d4, d5);
    }

    private AxisAlignedBB(double d0, double d1, double d2, double d3, double d4, double d5) {
        this.minX = d0;
        this.minY = d1;
        this.minZ = d2;
        this.maxX = d3;
        this.maxY = d4;
        this.maxZ = d5;
    }

    public AxisAlignedBB c(double d0, double d1, double d2, double d3, double d4, double d5) {
        this.minX = d0;
        this.minY = d1;
        this.minZ = d2;
        this.maxX = d3;
        this.maxY = d4;
        this.maxZ = d5;
        return this;
    }

    public AxisAlignedBB a(double d0, double d1, double d2) {
        double d3 = this.minX;
        double d4 = this.minY;
        double d5 = this.minZ;
        double d6 = this.maxX;
        double d7 = this.maxY;
        double d8 = this.maxZ;

        if (d0 < 0.0D) {
            d3 += d0;
        }

        if (d0 > 0.0D) {
            d6 += d0;
        }

        if (d1 < 0.0D) {
            d4 += d1;
        }

        if (d1 > 0.0D) {
            d7 += d1;
        }

        if (d2 < 0.0D) {
            d5 += d2;
        }

        if (d2 > 0.0D) {
            d8 += d2;
        }

        return b(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB b(double d0, double d1, double d2) {
        double d3 = this.minX - d0;
        double d4 = this.minY - d1;
        double d5 = this.minZ - d2;
        double d6 = this.maxX + d0;
        double d7 = this.maxY + d1;
        double d8 = this.maxZ + d2;

        return b(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB c(double d0, double d1, double d2) {
        return b(this.minX + d0, this.minY + d1, this.minZ + d2, this.maxX + d0, this.maxY + d1, this.maxZ + d2);
    }

    public double a(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.maxY > this.minY && axisalignedbb.minY < this.maxY) {
            if (axisalignedbb.maxZ > this.minZ && axisalignedbb.minZ < this.maxZ) {
                double d1;

                if (d0 > 0.0D && axisalignedbb.maxX <= this.minX) {
                    d1 = this.minX - axisalignedbb.maxX;
                    if (d1 < d0) {
                        d0 = d1;
                    }
                }

                if (d0 < 0.0D && axisalignedbb.minX >= this.maxX) {
                    d1 = this.maxX - axisalignedbb.minX;
                    if (d1 > d0) {
                        d0 = d1;
                    }
                }

                return d0;
            } else {
                return d0;
            }
        } else {
            return d0;
        }
    }

    public double b(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.maxX > this.minX && axisalignedbb.minX < this.maxX) {
            if (axisalignedbb.maxZ > this.minZ && axisalignedbb.minZ < this.maxZ) {
                double d1;

                if (d0 > 0.0D && axisalignedbb.maxY <= this.minY) {
                    d1 = this.minY - axisalignedbb.maxY;
                    if (d1 < d0) {
                        d0 = d1;
                    }
                }

                if (d0 < 0.0D && axisalignedbb.minY >= this.maxY) {
                    d1 = this.maxY - axisalignedbb.minY;
                    if (d1 > d0) {
                        d0 = d1;
                    }
                }

                return d0;
            } else {
                return d0;
            }
        } else {
            return d0;
        }
    }

    public double c(AxisAlignedBB axisalignedbb, double d0) {
        if (axisalignedbb.maxX > this.minX && axisalignedbb.minX < this.maxX) {
            if (axisalignedbb.maxY > this.minY && axisalignedbb.minY < this.maxY) {
                double d1;

                if (d0 > 0.0D && axisalignedbb.maxZ <= this.minZ) {
                    d1 = this.minZ - axisalignedbb.maxZ;
                    if (d1 < d0) {
                        d0 = d1;
                    }
                }

                if (d0 < 0.0D && axisalignedbb.minZ >= this.maxZ) {
                    d1 = this.maxZ - axisalignedbb.minZ;
                    if (d1 > d0) {
                        d0 = d1;
                    }
                }

                return d0;
            } else {
                return d0;
            }
        } else {
            return d0;
        }
    }

    public boolean a(AxisAlignedBB axisalignedbb) {
        return axisalignedbb.maxX > this.minX && axisalignedbb.minX < this.maxX ? (axisalignedbb.maxY > this.minY && axisalignedbb.minY < this.maxY ? axisalignedbb.maxZ > this.minZ && axisalignedbb.minZ < this.maxZ : false) : false;
    }

    public AxisAlignedBB d(double d0, double d1, double d2) {
        this.minX += d0;
        this.minY += d1;
        this.minZ += d2;
        this.maxX += d0;
        this.maxY += d1;
        this.maxZ += d2;
        return this;
    }

    public boolean a(Vec3D vec3d) {
        return vec3d.a > this.minX && vec3d.a < this.maxX ? (vec3d.b > this.minY && vec3d.b < this.maxY ? vec3d.c > this.minZ && vec3d.c < this.maxZ : false) : false;
    }

    public AxisAlignedBB shrink(double d0, double d1, double d2) {
        double d3 = this.minX + d0;
        double d4 = this.minY + d1;
        double d5 = this.minZ + d2;
        double d6 = this.maxX - d0;
        double d7 = this.maxY - d1;
        double d8 = this.maxZ - d2;

        return b(d3, d4, d5, d6, d7, d8);
    }

    public AxisAlignedBB clone() {
        return b(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public MovingObjectPosition a(Vec3D vec3d, Vec3D vec3d1) {
        Vec3D vec3d2 = vec3d.a(vec3d1, this.minX);
        Vec3D vec3d3 = vec3d.a(vec3d1, this.maxX);
        Vec3D vec3d4 = vec3d.b(vec3d1, this.minY);
        Vec3D vec3d5 = vec3d.b(vec3d1, this.maxY);
        Vec3D vec3d6 = vec3d.c(vec3d1, this.minZ);
        Vec3D vec3d7 = vec3d.c(vec3d1, this.maxZ);

        if (!this.b(vec3d2)) {
            vec3d2 = null;
        }

        if (!this.b(vec3d3)) {
            vec3d3 = null;
        }

        if (!this.c(vec3d4)) {
            vec3d4 = null;
        }

        if (!this.c(vec3d5)) {
            vec3d5 = null;
        }

        if (!this.d(vec3d6)) {
            vec3d6 = null;
        }

        if (!this.d(vec3d7)) {
            vec3d7 = null;
        }

        Vec3D vec3d8 = null;

        if (vec3d2 != null && (vec3d8 == null || vec3d.b(vec3d2) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d2;
        }

        if (vec3d3 != null && (vec3d8 == null || vec3d.b(vec3d3) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d3;
        }

        if (vec3d4 != null && (vec3d8 == null || vec3d.b(vec3d4) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d4;
        }

        if (vec3d5 != null && (vec3d8 == null || vec3d.b(vec3d5) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d5;
        }

        if (vec3d6 != null && (vec3d8 == null || vec3d.b(vec3d6) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d6;
        }

        if (vec3d7 != null && (vec3d8 == null || vec3d.b(vec3d7) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d7;
        }

        if (vec3d8 == null) {
            return null;
        } else {
            byte b0 = -1;

            if (vec3d8 == vec3d2) {
                b0 = 4;
            }

            if (vec3d8 == vec3d3) {
                b0 = 5;
            }

            if (vec3d8 == vec3d4) {
                b0 = 0;
            }

            if (vec3d8 == vec3d5) {
                b0 = 1;
            }

            if (vec3d8 == vec3d6) {
                b0 = 2;
            }

            if (vec3d8 == vec3d7) {
                b0 = 3;
            }

            return new MovingObjectPosition(0, 0, 0, b0, vec3d8);
        }
    }

    private boolean b(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.b >= this.minY && vec3d.b <= this.maxY && vec3d.c >= this.minZ && vec3d.c <= this.maxZ;
    }

    private boolean c(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.a >= this.minX && vec3d.a <= this.maxX && vec3d.c >= this.minZ && vec3d.c <= this.maxZ;
    }

    private boolean d(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.a >= this.minX && vec3d.a <= this.maxX && vec3d.b >= this.minY && vec3d.b <= this.maxY;
    }

    public void b(AxisAlignedBB axisalignedbb) {
        this.minX = axisalignedbb.minX;
        this.minY = axisalignedbb.minY;
        this.minZ = axisalignedbb.minZ;
        this.maxX = axisalignedbb.maxX;
        this.maxY = axisalignedbb.maxY;
        this.maxZ = axisalignedbb.maxZ;
    }

    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}

package net.minecraft.server;

import java.io.*;
import java.util.*;

public class WorldMapCollection {

    private IDataManager a;
    private Map b = new HashMap();
    private List c = new ArrayList();
    private Map d = new HashMap();

    public WorldMapCollection(IDataManager idatamanager) {
        this.a = idatamanager;
        this.b();
    }

    public WorldMapBase a(Class oclass, String s) {
        WorldMapBase worldmapbase = (WorldMapBase) this.b.get(s);

        if (worldmapbase != null) {
            return worldmapbase;
        } else {
            if (this.a != null) {
                try {
                    File file1 = this.a.b(s);

                    if (file1 != null && file1.exists()) {
                        try {
                            worldmapbase = (WorldMapBase) oclass.getConstructor(new Class[] { String.class}).newInstance(new Object[] { s});
                        } catch (Exception exception) {
                            throw new RuntimeException("Failed to instantiate " + oclass.toString(), exception);
                        }

                        FileInputStream fileinputstream = new FileInputStream(file1);
                        NBTTagCompound nbttagcompound = CompressedStreamTools.a((InputStream) fileinputstream);

                        fileinputstream.close();
                        worldmapbase.a(nbttagcompound.k("data"));
                    }
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                }
            }

            if (worldmapbase != null) {
                this.b.put(s, worldmapbase);
                this.c.add(worldmapbase);
            }

            return worldmapbase;
        }
    }

    public void a(String s, WorldMapBase worldmapbase) {
        if (worldmapbase == null) {
            throw new RuntimeException("Can\'t set null data");
        } else {
            if (this.b.containsKey(s)) {
                this.c.remove(this.b.remove(s));
            }

            this.b.put(s, worldmapbase);
            this.c.add(worldmapbase);
        }
    }

    public void a() {
        for (int i = 0; i < this.c.size(); ++i) {
            WorldMapBase worldmapbase = (WorldMapBase) this.c.get(i);

            if (worldmapbase.b()) {
                this.a(worldmapbase);
                worldmapbase.a(false);
            }
        }
    }

    private void a(WorldMapBase worldmapbase) {
        if (this.a != null) {
            try {
                File file1 = this.a.b(worldmapbase.a);

                if (file1 != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    worldmapbase.b(nbttagcompound);
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.a("data", nbttagcompound);
                    FileOutputStream fileoutputstream = new FileOutputStream(file1);

                    CompressedStreamTools.a(nbttagcompound1, (OutputStream) fileoutputstream);
                    fileoutputstream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void b() {
        try {
            this.d.clear();
            if (this.a == null) {
                return;
            }

            File file1 = this.a.b("idcounts");

            if (file1 != null && file1.exists()) {
                DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
                NBTTagCompound nbttagcompound = CompressedStreamTools.a((DataInput) datainputstream);

                datainputstream.close();
                Iterator iterator = nbttagcompound.c().iterator();

                while (iterator.hasNext()) {
                    NBTBase nbtbase = (NBTBase) iterator.next();

                    if (nbtbase instanceof NBTTagShort) {
                        NBTTagShort nbttagshort = (NBTTagShort) nbtbase;
                        String s = nbttagshort.b();
                        short short1 = nbttagshort.a;

                        this.d.put(s, short1);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int a(String s) {
        Short oshort = (Short) this.d.get(s);

        if (oshort == null) {
            oshort = (short) 0;
        } else {
            oshort = (short) (oshort.shortValue() + 1);
        }

        this.d.put(s, oshort);
        if (this.a == null) {
            return oshort.shortValue();
        } else {
            try {
                File file1 = this.a.b("idcounts");

                if (file1 != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    Iterator iterator = this.d.keySet().iterator();

                    while (iterator.hasNext()) {
                        String s1 = (String) iterator.next();
                        short short1 = ((Short) this.d.get(s1)).shortValue();

                        nbttagcompound.a(s1, short1);
                    }

                    DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));

                    CompressedStreamTools.a(nbttagcompound, (DataOutput) dataoutputstream);
                    dataoutputstream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return oshort.shortValue();
        }
    }
}

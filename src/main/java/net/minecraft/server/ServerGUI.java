package net.minecraft.server;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.logging.Logger;

public class ServerGUI extends JComponent implements ICommandListener {

    public static Logger a = Logger.getLogger("Minecraft");
    private MinecraftServer b;

    public static void a(MinecraftServer minecraftserver) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            ;
        }

        ServerGUI servergui = new ServerGUI(minecraftserver);
        JFrame jframe = new JFrame("Minecraft server");

        jframe.add(servergui);
        jframe.pack();
        jframe.setLocationRelativeTo((Component) null);
        jframe.setVisible(true);
        jframe.addWindowListener(new ServerWindowAdapter(minecraftserver));
    }

    public ServerGUI(MinecraftServer minecraftserver) {
        this.b = minecraftserver;
        this.setPreferredSize(new Dimension(854, 480));
        this.setLayout(new BorderLayout());

        try {
            this.add(this.c(), "Center");
            this.add(this.a(), "West");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private JComponent a() {
        JPanel jpanel = new JPanel(new BorderLayout());

        jpanel.add(new GuiStatsComponent(), "North");
        jpanel.add(this.b(), "Center");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return jpanel;
    }

    private JComponent b() {
        PlayerListBox playerlistbox = new PlayerListBox(this.b);
        JScrollPane jscrollpane = new JScrollPane(playerlistbox, 22, 30);

        jscrollpane.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return jscrollpane;
    }

    private JComponent c() {
        JPanel jpanel = new JPanel(new BorderLayout());
        JTextArea jtextarea = new JTextArea();

        a.addHandler(new GuiLogOutputHandler(jtextarea));
        JScrollPane jscrollpane = new JScrollPane(jtextarea, 22, 30);

        jtextarea.setEditable(false);
        JTextField jtextfield = new JTextField();

        jtextfield.addActionListener(new ServerGuiCommandListener(this, jtextfield));
        jtextarea.addFocusListener(new ServerGuiFocusAdapter(this));
        jpanel.add(jscrollpane, "Center");
        jpanel.add(jtextfield, "South");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
        return jpanel;
    }

    public void sendMessage(String s) {
        a.info(s);
    }

    public String getName() {
        return "CONSOLE";
    }

    static MinecraftServer a(ServerGUI servergui) {
        return servergui.b;
    }
}

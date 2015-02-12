package io.github.wayerr.ft.swing;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * an example of Focus-traverse usage
 * Created by wayerr on 10.02.15.
 */
public final class Example implements Runnable {
    private static final int GAP = 3;

    public static void main(String args[]) throws InvocationTargetException, InterruptedException {
        Example example = new Example();
        EventQueue.invokeAndWait(example);
    }

    @Override
    public void run() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());

        Container pane = frame.getContentPane();

        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        JComponent one = new JScrollPane(new JTextArea("1"));
        JComponent two = new JScrollPane(new JTextArea("2"));
        JComponent three = new JScrollPane(new JTextArea("3"));
        JComponent four = createPanel();

        gl.setVerticalGroup(gl.createParallelGroup()
            .addGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup()
                    .addComponent(one, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
                    .addComponent(two, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
                )
                .addGap(GAP)
                .addComponent(three, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
            )
            .addComponent(four, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
        );
        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addGroup(gl.createParallelGroup()
                .addGroup(gl.createSequentialGroup()
                    .addComponent(two, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
                    .addGap(GAP)
                    .addComponent(one, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
                )
                .addComponent(three, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
            )
            .addGap(GAP)
            .addComponent(four, 100, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        );


        //!!!
        TraverseFocusSupport tfs = new TraverseFocusSupport();
        tfs.install(frame);

        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        double x = ss.width / 3d;
        double y = ss.height / 3d;
        frame.setBounds((int) x, (int) y, (int) ((ss.width - x) / 2d), (int) ((ss.height - y) / 2d));
        frame.setVisible(true);
    }

    private JComponent createPanel() {
        JPanel panel = new JPanel();
        GroupLayout gl = new GroupLayout(panel);
        panel.setLayout(gl);

        JComponent one = new JCheckBox("checkbox 1");
        JComponent two = new JCheckBox("checkbox 2");
        JComponent three = new JButton("ok");
        JComponent four = new JButton("cancel");

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addComponent(one)
            .addGap(GAP)
            .addComponent(two)
            .addGap(GAP)
            .addGroup(gl.createParallelGroup()
                .addComponent(three)
                .addComponent(four)
            )
        );
        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(two, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
            .addComponent(one, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
            .addGroup(gl.createSequentialGroup()
                .addGap(0, 10, Integer.MAX_VALUE)
                .addComponent(three)
                .addGap(GAP)
                .addComponent(four)
                .addGap(GAP)
            )
        );
        gl.linkSize(three, four);
        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.add(createMenu("one", "one 1", "one 2", "one 3"));
        bar.add(createMenu("two", "two 1", "two 2", "two 3"));
        bar.add(createMenu("three", "three 1", "three 2", "three 3"));
        return bar;
    }

    private JMenu createMenu(String menuName, String... childs) {
        JMenu menu = new JMenu(menuName);
        for (String child : childs) {
            menu.add(child);
        }
        return menu;
    }
}

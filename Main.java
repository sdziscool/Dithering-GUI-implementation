//Dithering-GUI, a GUI for dithering algorithms
//Written and maintained by sdziscool
//contact: github.com/sdziscool

package sdziscode.ditheringGUI;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;


public class Main extends PApplet {
    private int algon = 2;
    private int div = 46; //divider
    private float[][][] dithmain = { // int[][][] dithmain = new int[5][5][16];
            { //stucki 0 (div 16)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 8, 4},
                    {2, 4, 8, 4, 2},
                    {1, 2, 4, 2, 1}
            },
            { //burkes 1 (div 32)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 8, 4},
                    {2, 4, 8, 4, 2},
                    {0, 0, 0, 0, 0}
            },
            { //JJN 2 (div 46)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 7, 5},
                    {3, 5, 7, 5, 3},
                    {0, 3, 5, 3, 0}
            },
            { //Floyd steinberg 3 (div 16)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 7, 0},
                    {0, 1, 5, 3, 0},
                    {0, 0, 0, 0, 0}
            },
            { //atkinson 4 (div 8)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 1, 1},
                    {0, 1, 1, 1, 0},
                    {0, 0, 1, 0, 0}
            },
            { //Sierra 5 (div 32)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 5, 3},
                    {2, 4, 5, 4, 2},
                    {0, 2, 3, 2, 0}
            },
            { //Sierra 2 row 6 (div 16)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 4, 3},
                    {1, 2, 3, 2, 1},
                    {0, 0, 0, 0, 0}
            },
            { //Sierra Lite 7 (div 4)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 2, 0},
                    {0, 1, 1, 0, 0},
                    {0, 0, 0, 0, 0}
            },
            { //Custom / S-Dither 8 (div ???)
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 1, 6},
                    {3, 4, 3, 2, 5},
                    {0, 1, 2, 3, 4}
            }
    };

    private PImage src;
    private PImage res;
    private int[][] matrix;

    // Bayer matrix 8x8
    int[][] matrix3x3 = {
            {1, 8, 4},
            {7, 6, 3},
            {5, 2, 9}
    };
    int[][] matrix4x4 = {
            {1, 9, 3, 11},
            {13, 5, 15, 7},
            {4, 12, 2, 10},
            {16, 8, 14, 6}
    };
    int[][] matrix2x2 = {
            {1, 3},
            {4, 2}
    };
    int[][] matrix8x8 = {
            {1, 49, 13, 61, 4, 52, 16, 64},
            {33, 17, 45, 29, 36, 20, 48, 32},
            {9, 57, 5, 53, 12, 60, 8, 56},
            {41, 25, 37, 21, 44, 28, 40, 24},
            {3, 51, 15, 63, 2, 50, 14, 62},
            {35, 19, 47, 31, 34, 18, 46, 30},
            {11, 59, 7, 55, 10, 58, 6, 54},
            {43, 27, 39, 23, 42, 26, 38, 22}
    };
    private int coln, coloram = 24, palsw = 1, loops = 1, s = 1, msize = 8, imam, scaleam = 6, hsbSwitch = 0, wij = 8, hoo = 8;;
    private float mratio = 10 / 18.5f, mfactor = 255.0f / 50, endscale = 2, scalar = 4, scale; //okay as is 1.0 / 18.5
    private Integer[] palette, altpal, ownpal, scalepal; //main palette, could later be implemented to be user defined
    private File[] selectedFile;

//GUI_start

    private JRadioButton impl1, impl2, impl3, impl4, impl5, impl6, impl7, impl8, impl9, impl10, impl11, pal1, pal2, pal3, pal4, pal5, pal6, pal7, hc, sc, bc; //DONE
    private JTextField tmratio, tmfactor, tcoloram, tloops, tscalar, tsteps, tendscale, tmsize, towncol, tscales, twij, thoog;
    private JButton start, filebutton, owncolset;
    private JCheckBox adv, linestep; //done

    private JTextArea info = new JTextArea("Please select one or multiple files to dither!"), fileinfo = new JTextArea("no files selected yet");
    private JFrame frame;
    private JPanel panel1, panel2, panel3, panel4, panel5, panel6, panel7, subpanel1, subpanel2, subpanel3;
    private JLabel loopl, coloraml, scalarl, stepsl, mratl, mfactl, endscalel, linestepl, msizel, wijl, hool;

    private JPanel[] c;
    //GUI_end
    public void setup() {
        scale = 1 / scalar;
        noLoop();
        surface.setVisible(false);
    }


    public void setunder() {
        scale = 1 / scalar;
        //println(scale);
        src = loadImage(selectedFile[imam].getAbsolutePath());
        res = createImage(src.width, src.height, RGB);
        src.resize((int) (scale * src.width), (int) (scale * src.height)); //resize
        surface.setResizable(true);
        surface.setSize(src.width, src.height);
    }

    public void draw() {

        Hardcode beginning = new Hardcode();
        beginning.buildit();
        //image(src, 0, 0);
    }

    public class Hardcode implements ActionListener {
        public Hardcode() {
        }

        public void buildit() {
            start = new JButton("Start");
            impl11 = new JRadioButton("Custom");
            impl10 = new JRadioButton("Sierra Lite");
            impl9 = new JRadioButton("Sierra-2");
            impl8 = new JRadioButton("Sierra");
            impl7 = new JRadioButton("JJN");
            impl6 = new JRadioButton("Burkes");
            impl5 = new JRadioButton("Stucki");
            impl4 = new JRadioButton("Atkinson");
            impl1 = new JRadioButton("Ordered", true);
            impl2 = new JRadioButton("Random");
            impl3 = new JRadioButton("Floyd Steinberg");

            pal4 = new JRadioButton("Black and White");
            pal1 = new JRadioButton("Random Colors", true);
            pal2 = new JRadioButton("Random Colors + Black");
            pal3 = new JRadioButton("3bit");
            pal5 = new JRadioButton("Own Colors");
            pal6 = new JRadioButton("Scaling of one or multuple colors");
            pal7 = new JRadioButton("boxes");

            hc = new JRadioButton("Hue");
            sc = new JRadioButton("Saturation");
            bc = new JRadioButton("Brightness", true);

            owncolset = new JButton("Reset and update color amount");
            tmsize = new JTextField("2", 3);
            msizel = new JLabel("size of bayermatrix: 2, 3, 4 or 8");
            tscalar = new JTextField("1", 3);
            scalarl = new JLabel("Scale of resulting pixels");
            tendscale = new JTextField("1", 3);
            endscalel = new JLabel("resizes without loss of quality");

            tscales = new JTextField("6", 3);
            twij = new JTextField("20", 3);
            thoog = new JTextField("20", 3);
            wijl = new JLabel("width of boxes");
            hool = new JLabel("height of boxes");

            //advanced options
            adv = new JCheckBox("Advanced Options", false);
            adv.addActionListener(this);
            tsteps = new JTextField("1", 3);
            stepsl = new JLabel("steps for the algorithm to take");
            linestep = new JCheckBox("liner");
            linestepl = new JLabel("Create horizontal lines in steps");
            tmratio = new JTextField("1", 6);
            mratl = new JLabel("mratio");
            tmfactor = new JTextField("17", 6);
            mfactl = new JLabel("mfactor");

            //
            tloops = new JTextField("1", 3);
            tcoloram = new JTextField("6", 3);
            loopl = new JLabel("Amount of loops");
            coloraml = new JLabel("Amount of (random) colors");
            towncol = new JTextField("6", 3);
            owncolset.addActionListener(this);
            owncolset.setActionCommand("set colam");
            frame = new JFrame();
            panel1 = new JPanel();
            panel2 = new JPanel();
            panel3 = new JPanel();
            panel4 = new JPanel();
            panel5 = new JPanel();
            panel6 = new JPanel();
            panel7 = new JPanel();
            subpanel1 = new JPanel();
            subpanel2 = new JPanel();
            subpanel3 = new JPanel();
            panel1.setLayout(new GridLayout(4, 1));
            panel2.setLayout(new GridLayout(4, 1));
            panel3.setLayout(new GridLayout(5, 2));
            panel4.setLayout(new GridLayout(2, 2));
            panel5.setLayout(new GridLayout(5, 2));
            tcoloram.setSize(panel3.getWidth() / 16, 20);
            tloops.setSize(panel3.getWidth() / 16, 20);
            ButtonGroup modulo = new ButtonGroup();
            modulo.add(impl1);
            modulo.add(impl2);
            modulo.add(impl3);
            modulo.add(impl4);
            modulo.add(impl5);
            modulo.add(impl6);
            modulo.add(impl7);
            modulo.add(impl8);
            modulo.add(impl9);
            modulo.add(impl10);
            modulo.add(impl11);

            panel1.add(impl1);
            panel1.add(impl2);
            panel1.add(impl3);
            panel1.add(impl4);
            panel1.add(impl5);
            panel1.add(impl6);
            panel1.add(impl7);
            panel1.add(impl8);
            panel1.add(impl9);
            panel1.add(impl10);
            panel1.add(impl11);

            pal1.addActionListener(this);
            pal2.addActionListener(this);
            pal3.addActionListener(this);
            pal4.addActionListener(this);
            pal5.addActionListener(this);
            pal6.addActionListener(this);
            pal7.addActionListener(this);
            pal1.setActionCommand("Random Colors");
            pal2.setActionCommand("Random Colors + Black");
            pal3.setActionCommand("3bit");
            pal4.setActionCommand("Black and White");
            pal5.setActionCommand("Random Colors");
            pal6.setActionCommand("Own Colors");
            pal7.setActionCommand("magic");
            adv.setActionCommand("Advanced Options");
            ButtonGroup modula = new ButtonGroup();
            modula.add(pal1);
            modula.add(pal2);
            modula.add(pal3);
            modula.add(pal4);
            modula.add(pal5);
            modula.add(pal6);
            modula.add(pal7);
            ButtonGroup moduless = new ButtonGroup();
            moduless.add(hc);
            moduless.add(bc);
            moduless.add(sc);
            panel2.add(pal1);
            panel2.add(pal2);
            panel2.add(pal3);
            panel2.add(pal4);
            panel2.add(pal5);
            panel2.add(pal6);
            panel2.add(pal7);
            panel3.add(tmsize);
            panel3.add(msizel);
            panel3.add(tloops);
            panel3.add(loopl);
            panel3.add(tcoloram);
            panel3.add(coloraml);
            panel4.add(tendscale);
            panel4.add(endscalel);
            panel4.add(tscalar);
            panel4.add(scalarl);

            subpanel3.add(twij);
            subpanel3.add(wijl);
            subpanel3.add(thoog);
            subpanel3.add(hool);

            panel1.setBorder(BorderFactory.createLineBorder(Color.black));
            panel2.setBorder(BorderFactory.createLineBorder(Color.black));
            panel3.setBorder(BorderFactory.createLineBorder(Color.black));
            panel7.setBorder(BorderFactory.createLineBorder(Color.black));
            info.setBorder(BorderFactory.createLineBorder(Color.black));
            fileinfo.setBorder(BorderFactory.createLineBorder(Color.black));

            panel5.add(adv);
            panel6.setLayout(new GridLayout(1, 2));
            panel7.setLayout(new GridLayout());
            panel6.add(towncol);
            panel6.add(owncolset);
            frame.setLayout(new GridLayout(7, 2));

            frame.setSize(800, 800);
            frame.add(panel1);
            frame.add(panel2);
            frame.add(panel3);
            frame.add(panel4);
            frame.add(panel5);
            frame.add(start);
            frame.add(panel6);
            frame.add(panel7);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            start.addActionListener(this);
            //FileCHooser
            filebutton = new JButton("Select File(s)");
            filebutton.addActionListener(this);

            //colorchoserGUI
            JButton colorpick = new JButton("Pick Color(s)");
            frame.add(colorpick);
            colorpick.addActionListener(this);
            //colorchooser end
            frame.add(filebutton);


            //TEXTTHINGS
            JScrollPane scro1 = new JScrollPane(fileinfo);
            JScrollPane scro2 = new JScrollPane(info);

            fileinfo.setLineWrap(true);
            info.setLineWrap(true);
            frame.add(scro2);
            frame.add(scro1);
            frame.add(subpanel3);
            JPanel copyright = new JPanel();
            copyright.setLayout(new GridLayout(1,4));
            copyright.add(new JTextArea("Written and maintained by sdziscool"+"\ncontact:\ngithub.com/sdziscool\nhave fun!\nVersion: 0.8"));
            frame.add(copyright);
            //info.append("\nwelcome"); //info.setText("The new text\n" + textArea.getText());


            //ENDTEXTTHINGS

            coloram = Integer.parseInt(towncol.getText());
            ownpal = new Integer[coloram];
            c = new JPanel[coloram];

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //
            frame.setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("Pick Color(s)")) {
                //JColorChooser picker = new JColorChooser(Color.BLACK);
                Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
                ownpal[coln] = newColor.getRGB();
                coln++;
                c[coln - 1] = new JPanel();
                c[coln - 1].setBackground(newColor);
                c[coln - 1].setOpaque(true);
                panel7.add(c[coln - 1]);
                panel7.revalidate();
                panel7.repaint();
            }

            if (e.getActionCommand().equals("set colam")) {
                coloram = Integer.parseInt(towncol.getText());
                ownpal = new Integer[coloram];
                c = new JPanel[coloram];
                coln = 0;
                panel7.setBackground(Color.BLACK);
                panel7.removeAll();
                panel7.repaint();
                info.setText(null);
                info.append("#Colors reset\nyou can now select new colors\n");
            }

            if ((e.getActionCommand().equals("Select File(s)"))) {
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(true);
                FileFilter filter = new FileNameExtensionFilter(
                        "Image File", "jpg", "png", "bmp", "tiff", "jpeg", "gif");
                fc.setFileFilter(filter);
                fc.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fc.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fc.getSelectedFiles();
                    //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    scale = 1 / scalar;
                    imam = selectedFile.length - 1;
                    fileinfo.setText("");
                    for (int i = 0; i <= imam; i++) {
                        fileinfo.append(selectedFile[i].getName() + "\n");                                                             ///fileinfo.append( selectedFile[i].getName() + "\n"); info.setText("The new text\n" + textArea.getText());
                    }
                    fileinfo.append("- ### -   - ### -   - ### -\nfile amount: " + (imam + 1));

                    //image embedding
                    src = loadImage(selectedFile[imam].getAbsolutePath());
                    res = createImage(src.width, src.height, RGB);
                    src.resize((int) (scale * src.width), (int) (scale * src.height)); //resize
                    surface.setResizable(true);
                    surface.setSize(src.width, src.height);
                }
            }


            //surface.setResizable(true);
            //surface.setSize(src.width, src.height);
            if ((e.getActionCommand().equals("Random Colors") || e.getActionCommand().equals("Random Colors + Black"))) {
                panel6.removeAll();
                panel6.setLayout(new GridLayout(1, 2));
                panel6.add(towncol);
                panel6.add(owncolset);

                panel3.removeAll();
                panel3.add(tmsize);
                panel3.add(msizel);
                panel3.add(tloops);
                panel3.add(loopl);
                panel3.add(tcoloram);
                panel3.add(coloraml);
                panel3.revalidate();
                panel3.repaint();
                panel6.revalidate();
                panel6.repaint();
            } else if (e.getActionCommand().equals("Black and White") || e.getActionCommand().equals("3bit")) {
                panel6.removeAll();
                panel6.setLayout(new GridLayout(1, 2));
                panel6.add(towncol);
                panel6.add(owncolset);

                panel3.removeAll();
                panel3.revalidate();
                panel3.repaint();
                panel6.revalidate();
                panel6.repaint();
            } else if (e.getActionCommand().equals("Own Colors")) {
                panel6.removeAll();
                panel6.setLayout(new GridLayout(5, 2));
                JLabel infohsb = new JLabel("Scaling based on brightness, saturation or hue?");
                panel6.add(infohsb);
                panel6.add(bc);
                panel6.add(sc);
                panel6.add(hc);
                panel6.add(tscales);
                panel6.add(towncol);
                panel6.add(owncolset);
                panel6.revalidate();
                panel6.repaint();
            }

            if ((e.getActionCommand().equals("Advanced Options") && adv.isSelected())) {
                panel5.add(subpanel1);
                panel5.add(tsteps);
                panel5.add(stepsl);
                panel5.add(linestep);
                panel5.add(linestepl);
                panel5.add(tmratio);
                panel5.add(mratl);
                panel5.add(tmfactor);
                panel5.add(mfactl);
                panel5.revalidate();
                panel5.repaint();
            } else if ((e.getActionCommand().equals("Advanced Options") && !(adv.isSelected()))) {
                panel5.removeAll();
                panel5.add(adv);
                panel5.repaint();
            }

            if (e.getActionCommand().equals("Start")) {
                imam = selectedFile.length - 1;

                //GUI
                loops = Integer.parseInt(tloops.getText());

                if (pal6.isSelected()) {
                    coloram = Integer.parseInt(towncol.getText());
                } else {
                    coloram = Integer.parseInt(tcoloram.getText()); //owncolam
                }

                s = Integer.parseInt(tsteps.getText());
                mratio = Float.parseFloat(tmratio.getText());
                mfactor = Float.parseFloat(tmfactor.getText());
                scalar = Float.parseFloat(tscalar.getText());
                endscale = Float.parseFloat(tendscale.getText());
                msize = Integer.parseInt(tmsize.getText());
                scaleam = Integer.parseInt(tscales.getText());


                wij = src.width;
                hoo = src.height;

                if(impl5.isSelected()){
                    div = 16;
                    algon = 0;
                } else if(impl6.isSelected()){
                    div = 32;
                    algon = 1;
                } else if(impl7.isSelected()){
                    div = 46;
                    algon = 2;
                } else if(impl8.isSelected()){
                    div = 32;
                    algon = 5;
                } else if(impl9.isSelected()){
                    div = 16;
                    algon = 6;
                } else if(impl10.isSelected()){
                    div = 4;
                    algon = 7;
                } else if(impl11.isSelected()){
                    //div = (int)(mfactor/mratio);
                    algon = 8;
                    div = 0;
                    for(int z = 0; z < 25; z++){
                        div += dithmain[algon][z/5][z%5];
                        println(div + " " + z);
                    }
                    println(div);

                }
                if(adv.isSelected()){
                    div = (int)(mfactor/mratio);
                }




                if (pal1.isSelected()) {
                    palsw = 0;
                } else if (pal2.isSelected()) {
                    palsw = 1;
                } else if (pal3.isSelected()) {
                    palsw = 2;
                } else if (pal4.isSelected()) {
                    palsw = 3;
                } else if (pal5.isSelected()) {
                    palsw = 4;
                } else if (pal6.isSelected()) {
                    palsw = 5;
                } else {
                    palsw = 6;
                    wij = (int) (Integer.parseInt(twij.getText()) / scalar);
                    hoo = (int) (Integer.parseInt(thoog.getText()) / scalar);
                }
                //GUI
                while (imam >= 0) {
                    setunder();


                    for (int i = 0; i < loops; i++) {
                        // Init canvas
                        background(0, 0, 0);

                        image(src, 0, 0);

                        // Define step
                        if (pal1.isSelected() || pal2.isSelected()) {
                            collorcollector(0, 0, coloram);
                        }


                        if (sc.isSelected()) {
                            hsbSwitch = 1;
                        } else if (hc.isSelected()) {
                            hsbSwitch = 0;
                        } else {
                            hsbSwitch = 2;
                        }

                        if (pal6.isSelected()) {
                            scaleCalculator();
                        }

                        //println("beep3");
                        background(0, 0, 0);

                        if (msize == 2) {
                            matrix = matrix2x2;
                        } else if (msize == 3) {
                            matrix = matrix3x3;
                        } else if (msize == 8) {
                            matrix = matrix8x8;
                        } else { //failsafe
                            matrix = matrix4x4;
                            msize = 4;
                        }


                        //println("beep2");
                        //newness
                        for (int y3 = 0; y3 <= src.height / hoo; y3++) {
                            for (int x3 = 0; x3 <= src.width / wij; x3++) {

                                if (pal7.isSelected()) {
                                    //int q = getAverageColor(src, x3 * wij, y3 * hoo);
                                    //int z = extractColorFromImage(src, x3 * wij, y3 * hoo);
                                    //colorMode(RGB);
                                    collorcollector(x3 * wij, y3 * hoo, coloram);

                                    //dubpal = new int[2];
                                    //dubpal = altpal;
                                }
                                //System.out.println(x3 + " " + y3 + "hello");

                                for (int y = y3 * hoo; y < y3 * hoo + hoo && y < src.height; y += s) {
                                    for (int x = x3 * wij; x < x3 * wij + wij && x < src.width; x += s) {
                                        //ENDnewness
                                        // Scan image
                                        //for (int x = 0; x < src.width; x += s) {
                                        //for (int y = 0; y < src.height; y += s) {


                                        int oldpixel = src.get(x, y); // Calculate pixel

                                        int newpixel;

                                        if (impl1.isSelected()) { //impl.equals("ord") impl.equals("ran")
                                            int value = color((oldpixel >> 16 & 0xFF) + (oldpixel >> 16 & 0xFF) * (mratio / mfactor * matrix[x % msize][y % msize]),
                                                    (oldpixel >> 8 & 0xFF) + (oldpixel >> 8 & 0xFF) * (mratio / mfactor * matrix[x % msize][y % msize]),
                                                    (oldpixel & 0xFF) + (oldpixel & 0xFF) * (mratio / mfactor * matrix[x % msize][y % msize]));
                                            //original: int value = color((oldpixel >> 16 & 0xFF) + (mratio * matrix[x % msize][y % msize] * mfactor), (oldpixel >> 8 & 0xFF) + (mratio * matrix[x % msize][y % msize] * mfactor), (oldpixel & 0xFF) + +(mratio * matrix[x % msize][y % msize] * mfactor));
                                            newpixel = findClosestColor(value);
                                            src.set(x, y, newpixel);
                                        } else if (impl2.isSelected()) {
                                            newpixel = findClosestColor(color(red(oldpixel) + random(-64, 64), green(oldpixel) + random(-64, 64), blue(oldpixel) + random(-64, 64)));
                                            src.set(x, y, newpixel);
                                        } else if (impl3.isSelected()) {
                                            newpixel = findClosestColor(oldpixel);
                                            int quant_error = color(red(oldpixel) - red(newpixel), green(oldpixel) - green(newpixel), blue(oldpixel) - blue(newpixel));
                                            src.set(x, y, newpixel);

                                            //Floys Steinberg
                                            int s1 = src.get(x + s, y);
                                            src.set(x + s, y, color(red(s1) + 7.0f / 16 * red(quant_error), green(s1) + 7.0f / 16 * green(quant_error), blue(s1) + 7.0f / 16 * blue(quant_error)));
                                            int s2 = src.get(x - s, y + s);
                                            src.set(x - s, y + s, color(red(s2) + 3.0f / 16 * red(quant_error), green(s2) + 3.0f / 16 * green(quant_error), blue(s2) + 3.0f / 16 * blue(quant_error)));
                                            int s3 = src.get(x, y + s);
                                            src.set(x, y + s, color(red(s3) + 5.0f / 16 * red(quant_error), green(s3) + 5.0f / 16 * green(quant_error), blue(s3) + 5.0f / 16 * blue(quant_error)));
                                            int s4 = src.get(x + s, y + s);
                                            src.set(x + s, y + s, color(red(s4) + 1.0f / 16 * red(quant_error), green(s4) + 1.0f / 16 * green(quant_error), blue(s4) + 1.0f / 16 * blue(quant_error)));
                                        } else if (impl4.isSelected()) { //if (impl.equals("atkin"))

                                            newpixel = findClosestColor(oldpixel);
                                            int quant_error = color(red(oldpixel) - red(newpixel), green(oldpixel) - green(newpixel), blue(oldpixel) - blue(newpixel));
                                            src.set(x, y, newpixel);

                                            // Atkinson algorithm http://verlagmartinkoch.at/software/dither/index.html
                                            int s1 = src.get(x + s, y);
                                            src.set(x + s, y, color(red(s1) + 1.0f / 8 * red(quant_error), green(s1) + 1.0f / 8 * green(quant_error), blue(s1) + 1.0f / 8 * blue(quant_error)));
                                            int s2 = src.get(x - s, y + s);
                                            src.set(x - s, y + s, color(red(s2) + 1.0f / 8 * red(quant_error), green(s2) + 1.0f / 8 * green(quant_error), blue(s2) + 1.0f / 8 * blue(quant_error)));
                                            int s3 = src.get(x, y + s);
                                            src.set(x, y + s, color(red(s3) + 1.0f / 8 * red(quant_error), green(s3) + 1.0f / 8 * green(quant_error), blue(s3) + 1.0f / 8 * blue(quant_error)));
                                            int s4 = src.get(x + s, y + s);
                                            src.set(x + s, y + s, color(red(s4) + 1.0f / 8 * red(quant_error), green(s4) + 1.0f / 8 * green(quant_error), blue(s4) + 1.0f / 8 * blue(quant_error)));
                                            int s5 = src.get(x + 2 * s, y);
                                            src.set(x + 2 * s, y, color(red(s5) + 1.0f / 8 * red(quant_error), green(s5) + 1.0f / 8 * green(quant_error), blue(s5) + 1.0f / 8 * blue(quant_error)));
                                            int s6 = src.get(x, y + 2 * s);
                                            src.set(x, y + 2 * s, color(red(s6) + 1.0f / 8 * red(quant_error), green(s6) + 1.0f / 8 * green(quant_error), blue(s6) + 1.0f / 8 * blue(quant_error)));
                                        } else { //test case
                                            newpixel = findClosestColor(oldpixel);
                                            int quant_error = color(red(oldpixel) - red(newpixel), green(oldpixel) - green(newpixel), blue(oldpixel) - blue(newpixel));
                                            src.set(x, y, newpixel);
                                            for (int dy = 2; dy < 5; dy++) {
                                                for (int dx = 2; dx < 5; dx++) {
                                                    if(dithmain[algon][dx][dy] != 0) {
                                                        int sall = src.get(x+dx-2,y+dy-2);
                                                        src.set(x + dx -2, y + dy -2, color(red(sall) + dithmain[algon][dx][dy] / div * red(quant_error),
                                                                green(sall) + dithmain[algon][dx][dy] / div * green(quant_error),
                                                                blue(sall) + dithmain[algon][dx][dy] / div * blue(quant_error))); //dithmain[algon][dx][dy]
                                                    }
                                                }

                                            }
                                            }

                                            // Draw
                                            stroke(newpixel);
                                            point(x, y);
                                            if (linestep.isSelected()) {
                                                //println("beep");
                                                line(x, y, x + s, y + s);
                                            }
                                        }
                                    }
                                    palette = null;
                                }
                            }

                            res = qs(scalar, src);
                            //println("beep");
                            if (!(endscale == 1)) {
                                res = qs(endscale, res);
                            }

                            image(res, 0, 0);
                            //String path = savePath(selectedFile[imam].getAbsolutePath() + "_result\\" + palsw + "_" + hour() + second() + millis() * 100 + "_" + i + "result.png");
                            String path = savePath("results\\" + selectedFile[imam].getName() + "_" + hour() + second() + millis() * 100 + "_" + i + "result.png");
                            res.save(path);

                            if (!(palsw == 0 || palsw == 1 || palsw == 6)) {
                                break;
                            }
                            src = loadImage(selectedFile[imam].getAbsolutePath());
                            src.resize((int) (scale * src.width), (int) (scale * src.height)); //resize
                        }
                        imam--;
                        //frame.dispose();
                        //draw();
                        palette = null;
                        clear();
                    }
                }
            }
        }

        // Find closest colors in palette
        public int findClosestColor(int in) {

            //Palette colors
            Integer[] pal3bit = { // 3bit color palette
                    color(0),
                    color(255),
                    color(255, 0, 0),
                    color(0, 255, 0),
                    color(0, 0, 255),
                    color(255, 255, 0),
                    color(0, 255, 255),
                    color(255, 0, 255),
                    color(0, 255, 255),
            };

            Integer[] monopal = {
                    color(0), color(255)
            }; //monochrome palette black and white

            if (palsw == 0 || palsw == 1) {
                palette = altpal;
            } else if (palsw == 2) {
                palette = pal3bit;
            } else if (palsw == 3) {
                palette = monopal;
            } else if (palsw == 4) {
                palette = ownpal;
            } else if (palsw == 5) {
                palette = scalepal;
            } else {
                palette = altpal;
            }
            //System.out.println(palsw);

            PVector[] vpalette = new PVector[palette.length];
            PVector vcolor = new PVector((in >> 16 & 0xFF), (in >> 8 & 0xFF), (in & 0xFF));
            int current = 0;
            float distance = 2000000000;  //vcolor.dist(new PVector(0, 0, 0)); <<complete bullshit

            for (int i = 0; i < palette.length; i++) {
                int r = (palette[i] >> 16 & 0xFF);
                int g = (palette[i] >> 8 & 0xFF);
                int b = (palette[i] & 0xFF);
                vpalette[i] = new PVector(r, g, b);
                float d = vcolor.dist(vpalette[i]);
                if (d < distance) {
                    distance = d;
                    current = i;
                }
            }
            return palette[current];
        }

        public void collorcollector(int r, int t, int ranam) { //extracts colors from original image at random, only takes new colors

            altpal = new Integer[ranam];
            int i = 0;
            if (palsw == 1) {
                altpal[0] = color(0);
                i++;
            }
            int rx;
            int ry;
            int c = 0;

            rx = PApplet.parseInt(random(wij - 1));
            ry = PApplet.parseInt(random(hoo - 1));

            while (rx + r >= src.width || ry + t >= src.height) {
                rx = PApplet.parseInt(random(wij - 1));
                ry = PApplet.parseInt(random(hoo - 1));
                c++;
                if (c % 2000 == 0 && c != 0) {
                    //System.out.println("beephelp");
                    r = 0;
                    t = 0;
                }
            }
            c = 0;
            for (int x = rx + r; x < src.width; x++) {
                for (int y = ry + t; y < src.height; y++) {
                    //i < altpal.length
                    int blahbab = color(src.get(x, y));
                    if (!Arrays.asList(altpal).contains(blahbab) && x < src.width && y < src.height) { //src.get(x,y) Arrays.stream(altpal).anyMatch(q -> altpal[q] == blahbab )
                        if (i == altpal.length) {
                            break;
                        }
                        //println(src.get(x, y));
                        altpal[i] = color(src.get(x, y));
                        i++;

                        x = r + PApplet.parseInt(random(wij - 1) / 2);
                        y = t + PApplet.parseInt(random(hoo - 1) / 2);


                        c = 0;
                    } else if (x >= src.width || y >= src.height) {
                        x = r;
                        y = t;
                        //println("beepreset");
                    } else if (c > 400) {
                        if (i == altpal.length) {
                            break;
                        }
                        altpal[i] = color(0);
                        c = 0;
                        x = PApplet.parseInt(random(wij - 1));
                        y = PApplet.parseInt(random(hoo - 1));
                        i++;
                        //println("beepevac" + (i - 1));
                    }
                    c++;
                }
            }
            while (i < altpal.length) {
                altpal[i] = color(0);
                i++;
            }

        }

        public void scaleCalculator() {

            // orig impl // scalepal = new Integerscaleam];

            //new impl w/ multiple colors

            scalepal = new Integer[scaleam * coloram];
            int k = 0;

            //end impl

            colorMode(HSB, 255);
            for (int j = 0; j < coloram; j++) {

                for (int i = 0; i < scaleam; i++) {
                    if (hsbSwitch == 2) {
                        scalepal[k] = color(hue(ownpal[j]), saturation(ownpal[j]), 255 - 255 * i / scaleam); //brightness(ownpal[ownpal.length])
                    } else if (hsbSwitch == 1) {
                        scalepal[k] = color(hue(ownpal[j]), 255 - 255 * i / scaleam, brightness(ownpal[j]));
                    } else {
                        scalepal[k] = color(255 - 255 * i / scaleam, saturation(ownpal[j]), brightness(ownpal[j]));
                    }
                    k++;
                }
            }
            colorMode(RGB, 255);
        }


        public PImage qs(float scalor, PImage orig) { //QuanteSize, non smooth image resizeing
            surface.setSize((int) (scalor * (orig.width)), (int) (scalor * (orig.height)));
            PImage result = new PImage((int) (scalor * (orig.width)), (int) (scalor * (orig.height)));
            for (int x = 0; x < orig.width; x++) {
                for (int y = 0; y < orig.height; y++) {
                    for (int a = 0; a < scalor; a++) {
                        for (int b = 0; b < scalor; b++) {
                            //println(x+ " "+ y  +" " + a + " " + b );

                            result.set((int) (scalor * x + a), (int) (scalor * y + b), orig.get(x, y));
                        }
                    }
                }
                //println(x);
            }
            return result;
        }

        public int extractColorFromImage(PImage img, int x, int y) {
            colorMode(HSB);
            img.loadPixels();
            //int numberOfPixels = size*size;
            int[] hues = new int[256];
            float[] saturations = new float[256];
            float[] brightnesses = new float[256];

            for (int i = x - wij; i < x + wij * 2 && i < img.width; i++) {
                for (int j = y - hoo; j < y + hoo * 2 && j < img.height; j++) {
                    int pixel = img.get(i, j);
                    int hue = Math.round(hue(pixel));
                    float saturation = saturation(pixel);
                    float brightness = brightness(pixel);
                    hues[hue]++;
                    saturations[hue] += saturation;
                    brightnesses[hue] += brightness;
                }
            }

            // Find the most common hue.
            int hueCount = hues[0];
            int hue = 0;
            for (int i = 1; i < hues.length; i++) {
                if (hues[i] > hueCount) {
                    hueCount = hues[i];
                    hue = i;
                }
            }

            // Set the vars for displaying the color.
            return (color(hue, saturations[hue] / hueCount, brightnesses[hue] / hueCount));
        }


        public int getAverageColor(PImage img, int x, int y) {
            img.loadPixels();
            int r = 0, g = 0, b = 0, count = 1;
            for (int i = x - wij; i < x + wij * 2 && i < img.width; i++) {
                for (int j = y - hoo; j < y + hoo * 2 && j < img.height; j++) {
                    int c = img.get(i, j);
                    r += c >> 16 & 0xFF;
                    g += c >> 8 & 0xFF;
                    b += c & 0xFF;
                    count++;
                }
            }
            r /= count;
            g /= count;
            b /= count;
            return color(r, g, b);
        }


        static public void main(String[] passedArgs) {
            String[] appletArgs = new String[]{"sdziscode.ditheringGUI.Main"};
            if (passedArgs != null) {
                PApplet.main(concat(appletArgs, passedArgs));
            } else {
                PApplet.main(appletArgs);
            }
        }
    }
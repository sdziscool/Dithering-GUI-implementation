import java.util.*; 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.io.File; 
import javax.swing.filechooser.FileFilter; 
import javax.swing.filechooser.FileNameExtensionFilter;


PImage src;
PImage res;
int[][] matrix;
float scale;

// Bayer matrix 8x8
int[][] matrix3x3 = {
  {
    1, 8, 4
  }
  , 
  {
    7, 6, 3
  }
  , 
  {
    5, 2, 9
  }
};
int[][] matrix4x4 = {   
  {
    1, 9, 3, 11
  }
  , 
  {
    13, 5, 15, 7
  }
  , 
  {
    4, 12, 2, 10
  }
  , 
  {
    16, 8, 14, 6
  }
};
int[][] matrix2x2 = {
  {
    1, 3
  }
  , 
  {
    4, 2
  }
};
int[][] matrix8x8 = {
  {
    1, 49, 13, 61, 4, 52, 16, 64
  }
  , 
  {
    33, 17, 45, 29, 36, 20, 48, 32
  }
  , 
  {
    9, 57, 5, 53, 12, 60, 8, 56
  }
  , 
  {
    41, 25, 37, 21, 44, 28, 40, 24
  }
  , 
  {
    3, 51, 15, 63, 2, 50, 14, 62
  }
  , 
  {
    35, 19, 47, 31, 34, 18, 46, 30
  }
  , 
  {
    11, 59, 7, 55, 10, 58, 6, 54
  }
  , 
  {
    43, 27, 39, 23, 42, 26, 38, 22
  }
};

int coln;
String impl = "ord"; //ord, atkin, floyd, ran
float mratio = 10 / 18.5f; //okay as is 1.0 / 18.5
float mfactor = 255.0f / 50; //okay as is 255.0 / 5
int coloram = 24; //IMPORTANT Amount of colors you want in your result
int palsw = 1; //paletteswitch, 0 is random, 1 is random+black, 2 is 3bit and anything else is b&w monochrome
int loops = 2; //amount of loops for randomcolor.
String image = "start.jpg"; //path to image
float endscale = 2; //scaling of image before processing, can be done without artefacting when using dithering
int s = 1; // steps, best left at 1
int msize = 8; //2,3,4,8 also defines matrix!, anything other than 2,3,4 and 8 results in msize = 4 
int[] palette; //main palette, could later be implemented to be user defined
int[] altpal; //array containg all random colors
int[] ownpal;
int[] scalepal;
float scalar = 4; //now working correctly, non aliased scaling, 3 results in pics with "square" of 3 pixels wide(aka pixelart in high resolution)
File[] selectedFile;
int imam;
int scaleam = 6;
int hsbSwitch = 0;
//GUI_start

JRadioButton impl1, impl2, impl3, impl4, pal1, pal2, pal3, pal4, pal5, pal6, hc, sc, bc; //DONE
JTextField tmratio, tmfactor, tcoloram, tloops, tscalar, tsteps, tendscale, tmsize, towncol, tscales;
JButton start, filebutton, owncolset;
JCheckBox adv, linestep; //done

JFrame frame;
JPanel panel1, panel2, panel3, panel4, panel5, panel6, panel7, subpanel1, subpanel2, subpanel3;
JLabel loopl, coloraml, scalarl, stepsl, mratl, mfactl, endscalel, linestepl, msizel, owncoll;

JPanel[] c;
JPanel[] d;

//GUI_end
public void setup() {
  scale =  1/scalar;
  //println(scale);
  src = loadImage(image);  
  res = createImage(src.width, src.height, RGB);
  src.resize((int)(scale*src.width), (int)(scale*src.height)); //resize
  surface.setResizable(true);
  surface.setSize(src.width, src.height);
  noLoop();
  surface.setVisible(false);
}


public void setunder() {
  scale =  1/scalar;
  //println(scale);
  src = loadImage(selectedFile[imam].getAbsolutePath());  
  res = createImage(src.width, src.height, RGB);
  src.resize((int)(scale*src.width), (int)(scale*src.height)); //resize
  surface.setResizable(true);
  surface.setSize(src.width, src.height);
}

public void draw() {

  Hardcode beginning = new Hardcode();
  beginning.buildit();
  image(src, 0, 0);
}

public class Hardcode implements ActionListener {
  public Hardcode() {
  }
  public void buildit() {
    start = new JButton("Start");
    impl4 = new JRadioButton("Atkinson");
    impl1 = new JRadioButton("Ordered", true);
    impl2 = new JRadioButton("Random");
    impl3 = new JRadioButton("Floyd Steinberg");

    pal4 = new JRadioButton("Black and White");
    pal1 = new JRadioButton("Random Colors", true);
    pal2 = new JRadioButton("Random Colors + Black");
    pal3 = new JRadioButton("3bit");
    pal5 = new JRadioButton("Own Colors");
    pal6 = new JRadioButton("Scaling of single color");

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
    
    //advanced options
    adv = new JCheckBox("Advanced Options", false);
    adv.addActionListener(this);
    tsteps = new JTextField("1", 3);
    stepsl = new JLabel("steps for the algorithm to take");
    linestep = new JCheckBox("liner");
    linestepl = new JLabel("Create horizontal lines in steps");
    tmratio = new JTextField("0.54054", 6);
    mratl = new JLabel("mratio");
    tmfactor = new JTextField("5.1", 6);
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
    panel1.setLayout( new GridLayout(4, 1));
    panel2.setLayout( new GridLayout(4, 1));
    panel3.setLayout( new GridLayout(5, 2));
    panel4.setLayout( new GridLayout(2, 2));
    panel5.setLayout( new GridLayout(5, 2));
    tcoloram.setSize(panel3.getWidth()/16, 20);
    tloops.setSize(panel3.getWidth()/16, 20);
    ButtonGroup modulo = new ButtonGroup();
    modulo.add(impl1);
    modulo.add(impl2);
    modulo.add(impl3);
    modulo.add(impl4);
    modulo.add(impl4);
    panel1.add(impl1);
    panel1.add(impl2);
    panel1.add(impl3);
    panel1.add(impl4);

    pal1.addActionListener(this);
    pal2.addActionListener(this);
    pal3.addActionListener(this);
    pal4.addActionListener(this);
    pal5.addActionListener(this);
    pal6.addActionListener(this);
    pal1.setActionCommand("Random Colors");
    pal2.setActionCommand("Random Colors + Black");
    pal3.setActionCommand("3bit");
    pal4.setActionCommand("Black and White");
    pal5.setActionCommand("Random Colors");
    pal6.setActionCommand("Own Colors");
    adv.setActionCommand("Advanced Options");
    ButtonGroup modula = new ButtonGroup();
    modula.add(pal1);
    modula.add(pal2);
    modula.add(pal3);
    modula.add(pal4);
    modula.add(pal5);
    modula.add(pal6);
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

    panel1.setBorder(BorderFactory.createLineBorder(Color.black));
    panel2.setBorder(BorderFactory.createLineBorder(Color.black));
    panel3.setBorder(BorderFactory.createLineBorder(Color.black));
    panel7.setBorder(BorderFactory.createLineBorder(Color.black));

    panel5.add(adv);
    panel6.setLayout( new GridLayout(1, 2));
    panel7.setLayout( new GridLayout());
    panel6.add(towncol);
    panel6.add(owncolset);
    frame.setLayout(new GridLayout(5, 2));

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
    frame.setVisible(true);
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

    coloram = Integer.parseInt(towncol.getText());
    ownpal = new int[coloram];
    c = new JPanel[coloram];

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
    //
  }

  public void actionPerformed (ActionEvent e) {

    if (e.getActionCommand().equals("Pick Color(s)")) {
      //JColorChooser picker = new JColorChooser(Color.BLACK);
      Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
      ownpal[coln] = newColor.getRGB();
      coln++;
      c[coln-1] = new JPanel();
      c[coln-1].setBackground(newColor);
      c[coln-1].setOpaque(true);
      panel7.add(c[coln-1]);
      panel7.revalidate();
      panel7.repaint();
    }

    if (e.getActionCommand().equals("set colam")) {
      coloram = Integer.parseInt(towncol.getText());
      ownpal = new int[coloram];
      c = new JPanel[coloram];
      coln = 0;
      panel7.setBackground(Color.BLACK);
      panel7.removeAll();
      panel7.repaint();
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
        scale =  1/scalar;
        imam = selectedFile.length - 1;

        //image embedding
        src = loadImage(selectedFile[imam].getAbsolutePath());
        res = createImage(src.width, src.height, RGB);
        src.resize((int)(scale*src.width), (int)(scale*src.height)); //resize
        surface.setResizable(true);
        surface.setSize(src.width, src.height);
      }
    }


    surface.setResizable(true);
    surface.setSize(src.width, src.height);
    if ((e.getActionCommand().equals("Random Colors") || e.getActionCommand().equals("Random Colors + Black"))) {
      panel6.removeAll();
      panel6.setLayout( new GridLayout(1, 2));
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
      panel6.setLayout( new GridLayout(1, 2));
      panel6.add(towncol);
      panel6.add(owncolset);

      panel3.removeAll();
      panel3.revalidate();
      panel3.repaint();
      panel6.revalidate();
      panel6.repaint();
    } else if (e.getActionCommand().equals("Own Colors")) {
      panel6.removeAll();
      panel6.setLayout( new GridLayout(5, 2));
      JLabel infohsb = new JLabel("Scaling based on brightness, saturation or hue?");
      panel6.add(infohsb);
      panel6.add(bc);
      panel6.add(sc);
      panel6.add(hc);
      panel6.add(tscales);
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
      coloram = Integer.parseInt(tcoloram.getText());
      altpal = new int[coloram];
      s = Integer.parseInt(tsteps.getText());
      mratio = Float.parseFloat(tmratio.getText());
      mfactor = Float.parseFloat(tmfactor.getText());
      scalar = Float.parseFloat(tscalar.getText());
      endscale = Float.parseFloat(tendscale.getText());
      msize = Integer.parseInt(tmsize.getText());
      scaleam = Integer.parseInt(tscales.getText());

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
      } else {
        palsw = 5;
      }
      //GUI
      while (imam >= 0) {
        setunder();

        for (int i = 0; i<loops; i++) {




          // Init canvas
          background(0, 0, 0);

          image(src, 0, 0);

          // Define step

          if (pal1.isSelected() || pal2.isSelected()) {
            collorcollector();
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
            matrix =  matrix3x3;
          } else if (msize == 8) { 
            matrix =  matrix8x8;
          } else { //failsafe
            matrix = matrix4x4;
            msize = 4;
          }



          //println("beep2");
          // Scan image
          for (int x = 0; x < src.width; x+=s) {
            for (int y = 0; y < src.height; y+=s) {

              int oldpixel = src.get(x, y); // Calculate pixel

              int newpixel;

              if (impl1.isSelected()) { //impl.equals("ord") impl.equals("ran")
                int value = color( (oldpixel >> 16 & 0xFF) + (mratio*matrix[x%msize][y%msize] * mfactor), (oldpixel >> 8 & 0xFF) + (mratio*matrix[x%msize][y%msize] * mfactor), (oldpixel & 0xFF) + + (mratio*matrix[x%msize][y%msize] * mfactor) );
                newpixel = findClosestColor(value);      
                src.set(x, y, newpixel);
              } else if (impl2.isSelected()) {
                newpixel = findClosestColor( color ( red(oldpixel) + random(-64, 64), green(oldpixel) + random(-64, 64), blue(oldpixel) + random(-64, 64) ) );
                src.set(x, y, newpixel);
              } else if (impl3.isSelected()) {
                newpixel = findClosestColor(oldpixel);  
                int quant_error = color(red(oldpixel) - red(newpixel), green(oldpixel) - green(newpixel), blue(oldpixel) - blue(newpixel));
                src.set(x, y, newpixel);

                //Floys Steinberg
                int s1 = src.get(x+s, y);
                src.set(x+s, y, color( red(s1) + 7.0f/16 * red(quant_error), green(s1) + 7.0f/16 * green(quant_error), blue(s1) + 7.0f/16 * blue(quant_error) ));
                int s2 = src.get(x-s, y+s);
                src.set(x-s, y+s, color( red(s2) + 3.0f/16 * red(quant_error), green(s2) + 3.0f/16 * green(quant_error), blue(s2) + 3.0f/16 * blue(quant_error) ));
                int s3 = src.get(x, y+s);
                src.set(x, y+s, color( red(s3) + 5.0f/16 * red(quant_error), green(s3) + 5.0f/16 * green(quant_error), blue(s3) + 5.0f/16 * blue(quant_error) ));
                int s4 = src.get(x+s, y+s);
                src.set(x+s, y+s, color( red(s4) + 1.0f/16 * red(quant_error), green(s4) + 1.0f/16 * green(quant_error), blue(s4) + 1.0f/16 * blue(quant_error) ));
              } else { //if (impl.equals("atkin"))

                newpixel = findClosestColor(oldpixel);
                int quant_error = color(red(oldpixel) - red(newpixel), green(oldpixel) - green(newpixel), blue(oldpixel) - blue(newpixel));
                src.set(x, y, newpixel);

                // Atkinson algorithm http://verlagmartinkoch.at/software/dither/index.html
                int s1 = src.get(x+s, y);
                src.set(x+s, y, color( red(s1) + 1.0f/8 * red(quant_error), green(s1) + 1.0f/8 * green(quant_error), blue(s1) + 1.0f/8 * blue(quant_error) ));      
                int s2 = src.get(x-s, y+s);
                src.set(x-s, y+s, color( red(s2) + 1.0f/8 * red(quant_error), green(s2) + 1.0f/8 * green(quant_error), blue(s2) + 1.0f/8 * blue(quant_error) ));      
                int s3 = src.get(x, y+s);
                src.set(x, y+s, color( red(s3) + 1.0f/8 * red(quant_error), green(s3) + 1.0f/8 * green(quant_error), blue(s3) + 1.0f/8 * blue(quant_error) ));      
                int s4 = src.get(x+s, y+s);
                src.set(x+s, y+s, color( red(s4) + 1.0f/8 * red(quant_error), green(s4) + 1.0f/8 * green(quant_error), blue(s4) + 1.0f/8 * blue(quant_error) ));      
                int s5 = src.get(x+2*s, y);
                src.set(x+2*s, y, color( red(s5) + 1.0f/8 * red(quant_error), green(s5) + 1.0f/8 * green(quant_error), blue(s5) + 1.0f/8 * blue(quant_error) ));      
                int s6 = src.get(x, y+2*s);
                src.set(x, y+2*s, color( red(s6) + 1.0f/8 * red(quant_error), green(s6) + 1.0f/8 * green(quant_error), blue(s6) + 1.0f/8 * blue(quant_error) ));
              }

              // Draw
              stroke(newpixel);   
              point(x, y);
              if (linestep.isSelected()) {
                //println("beep");
                line(x, y, x+s, y+s);
              }
            }
          }





          res = qs(scalar, src);
          //println("beep");
          if (!(endscale == 1)) {
            res = qs(endscale, res);
          }

          //println("beep");

          image(res, 0, 0);
          String path = savePath(selectedFile[imam].getAbsolutePath() + "_result\\" + palsw + "_" + hour() + second() + millis()*100 + "_" + i + "result.png");
          //println("beep");
          res.save(path);

          if (!(palsw == 0 || palsw == 1)) {
            break;
          }
          src = loadImage(selectedFile[imam].getAbsolutePath());
          src.resize((int)(scale*src.width), (int)(scale*src.height)); //resize
        }
        imam--;
        //frame.dispose();
        //draw();
      }
    }
  }
}
// Find closest colors in palette
public int findClosestColor(int in) {

  //Palette colors
  int[] pal3bit = { // 3bit color palette
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

  int[] monopal = {
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
  } else {
    palette = scalepal;
  }



  PVector[] vpalette = new PVector[palette.length];  
  PVector vcolor = new PVector( (in >> 16 & 0xFF), (in >> 8 & 0xFF), (in & 0xFF));
  int current = 0;
  float distance = vcolor.dist(new PVector(0, 0, 0));

  for (int i=0; i<palette.length; i++) {
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

public void collorcollector() { //extracts colors from original image at random, only takes new colors

  int i = 0;
  if (palsw == 1) {
    altpal[0] = color(0);
    i++;
  }
  int rx;
  int ry;


  rx = PApplet.parseInt(random(src.width));
  ry = PApplet.parseInt(random(src.height));

  //println("beep3");

  for (int x = rx; x < src.width; x++) {
    for (int y = ry; y < src.height; y++) {
      //i < altpal.length
      if ( !Arrays.asList(altpal).contains(color(src.get(x, y)))) {
        if (i == altpal.length) {
          break;
        }
        //println(x + " " + y);
        altpal[i]=color(src.get(x, y));
        i++;

        x = PApplet.parseInt(random(src.width));
        y = PApplet.parseInt(random(src.height));
      }
    }
  }
  //println("beepend");
}
public void scaleCalculator() {

  scalepal = new int[scaleam];

  colorMode(HSB, 255);

  for (int i = 0; i < scaleam; i++) {
    if (hsbSwitch == 2) {
      scalepal[i] = color(hue(ownpal[0]), saturation(ownpal[0]), 255- 255*i/scaleam ); //brightness(ownpal[ownpal.length])
    } else if (hsbSwitch == 1) {
      scalepal[i] = color(hue(ownpal[0]), 255- 255*i/scaleam, brightness(ownpal[0]));
    } else {
      scalepal[i] = color(255- 255*i/scaleam, saturation(ownpal[0]), brightness(ownpal[0]));
    }
  }

  colorMode(RGB, 255);
}


public PImage qs(float scalor, PImage orig) { //QuanteSize, non smooth image resizeing
  surface.setSize((int)(scalor*(orig.width)), (int)(scalor*(orig.height)));
  PImage result = new PImage((int)(scalor*(orig.width)), (int)(scalor*(orig.height)));
  for (int x = 0; x < orig.width; x++) {
    for (int y = 0; y < orig.height; y++) {
      for (int a = 0; a<scalor; a++) {
        for (int b = 0; b<scalor; b++) {
          //println(x+ " "+ y  +" " + a + " " + b );

          result.set((int)(scalor*x+a), (int)(scalor*y+b), orig.get(x, y));
        }
      }
    }
    //println(x);
  }
  return result;
}
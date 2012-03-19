package Provider.GoogleMapsStatic;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
 
public class ZoomSlider extends JPanel
                        implements 
                                   WindowListener,
                                   ChangeListener {
    //Set up animation parameters.
    static final int FPS_MIN = 1;
    static final int FPS_MAX = 19;
    static final int FPS_INIT = 14;    //initial frames per second
 
    //This label uses ImageIcon to show the doggy pictures.
    JLabel picture;
    public int value = 14;
    JTextField lblZoom = new JTextField("14");
    
    public ZoomSlider() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
 
        //Create the slider.
        JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL,
                                              FPS_MIN, FPS_MAX, FPS_INIT);
         
 
        framesPerSecond.addChangeListener(this);
 
        //Turn on labels at major tick marks.
 
        framesPerSecond.setMajorTickSpacing(19);
        framesPerSecond.setMinorTickSpacing(1);
        framesPerSecond.setMajorTickSpacing(3);
        framesPerSecond.setPaintTicks(true);
        framesPerSecond.setPaintLabels(true);
        /*
        framesPerSecond.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));
        Font font = new Font("Serif", Font.ITALIC, 15);
        framesPerSecond.setFont(font);
 
        
        

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		*/
        add(lblZoom);
        add(framesPerSecond);
        
    }
 
    /** Add a listener for window events. */
    void addWindowListener(Window w) {
        w.addWindowListener(this);
    }
 
    //React to window events.
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
 
    /** Listen to the slider. */
    public void stateChanged(ChangeEvent e) {
    	JSlider source = (JSlider)e.getSource();
    	if (!source.getValueIsAdjusting()) {
    		int value = (int)source.getValue();
    		this.value = value;
    		lblZoom.setText(Integer.toString(value));
    	}
    	
    }
 

    public static void main(String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("SliderDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ZoomSlider animator = new ZoomSlider();
                 
        //Add content to the window.
        frame.add(animator, BorderLayout.CENTER);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

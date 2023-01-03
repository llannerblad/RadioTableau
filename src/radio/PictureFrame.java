package radio;

import javax.swing.*;

public class PictureFrame extends  javax.swing.JFrame {

    private JFrame frame;
    private ImageIcon imageStart;

    private JLabel displayField;

    public PictureFrame(){
        frame = new JFrame("Image Diplay Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            imageStart = new ImageIcon("startbild.png");
            displayField = new JLabel(imageStart);
            frame.add(displayField);
        } catch (Exception e){
            System.out.println("Image could not be found");
        }
        frame.setSize(400,400);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        PictureFrame p = new PictureFrame();
    }
}

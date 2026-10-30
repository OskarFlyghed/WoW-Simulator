import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputExample implements KeyListener {
        private int keyCode = -1;
        private char keyChar = 0;
        private final JTextField inputField;

        public KeyInputExample() {
            JFrame frame = new JFrame("Key Input Example");
            inputField = new JTextField(20);
            inputField.addKeyListener(this);
            frame.add(inputField);

            frame.setSize(300, 100);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }

        public int getKeyCode() {
            return keyCode;
        }

        public char getKeyChar() {
            return keyChar;
        }

        public void clear() {
            keyCode = -1;
            keyChar = 0;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            keyCode = e.getKeyCode();
            keyChar = e.getKeyChar();

            if (keyCode != KeyEvent.VK_ENTER) {
              //  System.out.println("Key pressed: " + keyChar);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}
    }

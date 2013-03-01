
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * User: Maciej Poleski
 * Date: 28.02.13
 * Time: 21:41
 */
public class UserInterface {
    private static SocketService service;
    private JButton sendButton;
    private JButton exitButton;
    private JList<String> usersList;
    private JTextArea chatPane;
    private JTextField newMessageField;
    private JPanel mainPanel;

    public UserInterface() {
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.shutdown();
                System.exit(0);
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        newMessageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    sendMessage();
                    e.consume();
                }
            }
        });
    }

    public static void main(final String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Podaj nick");
            System.exit(1);
        }
        service = new SocketService(args[0]);
        new Thread(service).start();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("UserInterface");
                UserInterface ui = new UserInterface();
                service.setUi(ui);
                frame.setContentPane(ui.mainPanel);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

    }

    private void sendMessage() {
        try {
            service.sendMessage(usersList.getSelectedValue(), prepareMessage(newMessageField.getText()));
            displayMessage(service.getName(), prepareMessage(newMessageField.getText()));
            newMessageField.setText("");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex, "An error occurred", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ignored) {
            // When no destination is provided in view
        }
    }

    private void createUIComponents() {
        usersList = new JList<>(service.getUsersListModel());
    }

    public void displayMessage(String from, String message) {
        chatPane.append(from + ":\n" + message + "\n\n");
    }

    private String prepareMessage(String message) {
        return message.replace('#', ' ').replace('\n', ' ');
    }
}

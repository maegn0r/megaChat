package client.gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class ChatFrame {
    private final JFrame mainFrame;
    private final Supplier<String> listener;

    public ChatFrame(Supplier<String> listener) {
        this.listener = listener;

        mainFrame = new JFrame();

        mainFrame.setTitle("OutstandingChat v. 1.0");
        mainFrame.setBounds(new Rectangle(400, 700));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(createTop(), BorderLayout.CENTER);
        mainFrame.add(createBottom(), BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    private JPanel createTop(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JTextArea chattingArea = new JTextArea();
        chattingArea.setEditable(false);
        jPanel.add(chattingArea, BorderLayout.CENTER);
        return jPanel;
    }

    private JPanel createBottom(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        JTextField inputArea = new JTextField();
        JButton submitButton = new JButton("Submit");
       // submitButton.addActionListener();

        jPanel.add(inputArea, BorderLayout.CENTER);
        jPanel.add(submitButton, BorderLayout.EAST);
        return jPanel;
    }
}

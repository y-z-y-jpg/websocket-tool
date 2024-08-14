package com.example.websocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebSocketSwingGUI {

    private WebSocketClient webSocketClient;
    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton clearButton;
    private JTextField uriField;

    public WebSocketSwingGUI() {
        webSocketClient = new WebSocketClient();

        // 创建并设置 GUI
        frame = new JFrame("WebSocket Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        uriField = new JTextField("wss://echo.websocket.org", 30);
        topPanel.add(new JLabel("WebSocket URI:"));
        topPanel.add(uriField);

        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        clearButton = new JButton("Clear Messages");

        topPanel.add(connectButton);
        topPanel.add(disconnectButton);
        topPanel.add(clearButton);

        controlPanel.add(topPanel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());

        textField = new JTextField();
        sendButton = new JButton("Send");

        messagePanel.add(textField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        controlPanel.add(messagePanel, BorderLayout.SOUTH);

        frame.add(controlPanel, BorderLayout.SOUTH);

        // 事件处理
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        frame.setVisible(true);
    }

    private void connect() {
        String uri = uriField.getText();
        webSocketClient.connect(uri);
        // 可以添加其他连接成功后的操作，比如更新状态
    }

    private void disconnect() {
        // 实际的断开连接操作可以根据 WebSocketClient 的实现来添加
        // 这里暂时假设 WebSocketClient 已经提供了断开连接的功能
        if (webSocketClient != null) {
            webSocketClient.close(); // 假设有 close 方法
        }
    }

    private void sendMessage() {
        String content = textField.getText();
        if (!content.trim().isEmpty()) {
            WebSocketClient.MyMessage message = new WebSocketClient.MyMessage();
            message.type = "text";
            message.content = content;
            webSocketClient.sendMessage(message);
            textArea.append("Sent: " + content + "\n");
            textField.setText(""); // 清空输入框
        }
    }

    public void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append("Received: " + message + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength()); // 自动滚动到最新消息
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WebSocketSwingGUI::new);
    }
}

package coocoogame.com.et_tool;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class FileNameInputDialog extends DialogWrapper {
    private JTextField fileNameTextField;
    // LocationType
    private JRadioButton radioClient;
    private JRadioButton radioServer;
    private JRadioButton radioShare;
    private ButtonGroup locationTypeButtonGroup;
    // CSType
    private JRadioButton radioComponent;
    private JRadioButton radioSystem;
    private JRadioButton radioMessage;
    private ButtonGroup CSTypeButtonGroup;

    public FileNameInputDialog(Project project) {
        super(true); // true 表示对话框是模态的
        setTitle("Enter File Name");
        init(); // 初始化对话框
        setSize(400, 250); // 调整对话框大小以适应新增的单选按钮
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        // 创建中间面板，用户可以在这里输入文件名和选择单选按钮
        JPanel panel = new JPanel(new BorderLayout(10, 10)); // 增加间距

        // 文件名输入部分
        JPanel fileNamePanel = new JPanel(new BorderLayout());
        fileNameTextField = new JTextField();
        fileNamePanel.add(new JLabel("File name:"), BorderLayout.WEST);
        fileNamePanel.add(fileNameTextField, BorderLayout.CENTER);

        // 单选按钮部分
        JPanel selectLocationTypePanel = new JPanel(new FlowLayout());
        radioClient = new JRadioButton("Client");
        radioServer = new JRadioButton("Server");
        radioShare = new JRadioButton("Share");
        locationTypeButtonGroup = new ButtonGroup();
        locationTypeButtonGroup.add(radioClient);
        locationTypeButtonGroup.add(radioServer);
        locationTypeButtonGroup.add(radioShare);

        selectLocationTypePanel.add(radioClient);
        selectLocationTypePanel.add(radioServer);
        selectLocationTypePanel.add(radioShare);

        // 单选按钮部分
        JPanel csTypePanel = new JPanel(new FlowLayout());
        radioComponent = new JRadioButton("Component");
        radioSystem = new JRadioButton("System");
        radioMessage=new JRadioButton("Message");
        CSTypeButtonGroup = new ButtonGroup();
        CSTypeButtonGroup.add(radioComponent);
        CSTypeButtonGroup.add(radioSystem);
        CSTypeButtonGroup.add(radioMessage);

        csTypePanel.add(radioComponent);
        csTypePanel.add(radioSystem);
        csTypePanel.add(radioMessage);
        // 将文件名输入面板和单选按钮面板添加到主面板中
        panel.add(fileNamePanel, BorderLayout.NORTH);
        panel.add(selectLocationTypePanel, BorderLayout.CENTER);
        panel.add(csTypePanel, BorderLayout.SOUTH);

        return panel;
    }

    @Override
    protected void doOKAction() {
        // 当用户点击 OK 时，可以在这里添加验证逻辑
        String fileName = fileNameTextField.getText();
        if (fileName == null || fileName.trim().isEmpty()) {
            // 如果文件名为空，则显示错误消息并取消关闭对话框
            JOptionPane.showMessageDialog(this.getContentPane(), "File name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // 如果文件名有效，则关闭对话框
            super.doOKAction();
        }
    }

    public String getFileName() {
        // 返回用户输入的文件名
        return fileNameTextField.getText();
    }

    public LocationType getSelectedLocationType() {
        // 返回当前选中的单选按钮的文本
        if (radioClient.isSelected()) {
            return LocationType.Client;
        } else if (radioServer.isSelected()) {
            return LocationType.Server;
        } else if (radioShare.isSelected()) {
            return LocationType.Share;
        } else {
            return null; // 如果没有选中任何按钮，则返回null
        }
    }

    public CSType getSelectedCSType() {
        // 返回当前选中的单选按钮的文本
        if (radioComponent.isSelected()) {
            return CSType.Component;
        } else if (radioSystem.isSelected()) {
            return CSType.System;
        } else if (radioMessage.isSelected()) {
            return CSType.MessageHandler;
        } else {
            return null; // 如果没有选中任何按钮，则返回null
        }
    }

}


package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class UIConstant {
	
	public static final Color PRIMARY_COLOR = new Color(139,137,112);
	public static final Color DANGER_COLOR = new Color(198, 0, 0);
	public static final Color WARNING_COLOR = new Color(0x5d69e1);//new Color(0xE96819);
	public static final Color DISABLE_COLOR = new Color(0x888A88);
	public static final Color LINE_COLOR = new Color(0x9C9C9C);
	public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 13);
	public static final Color HEADING_COLOR = new Color(0x0EAF03);
	public static final Color BELOW_COLOR = new Color(0xF5F5F5);
	public static void showInfo(Component component, String info) {
		JOptionPane.showMessageDialog(component, info, "Thông báo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/info.png"));
	}
	public static void showWarning(Component component, String warning) {
		JOptionPane.showMessageDialog(component, warning, "Cảnh báo", JOptionPane.WARNING_MESSAGE, new ImageIcon("img/warning.png"));
	}
	public static void showError(Component component, String error) {
		JOptionPane.showMessageDialog(component, error, "Lỗi", JOptionPane.ERROR_MESSAGE, new ImageIcon("img/error.png"));
	}
}

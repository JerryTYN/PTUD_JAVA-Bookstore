package ui;


import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;


import ui.FlashScreen;

public class ProjectManager {
	public static void main(String[] args) {
		new FlashScreen().setVisible(true);
		FlatArcOrangeIJTheme.setup();
	}
}

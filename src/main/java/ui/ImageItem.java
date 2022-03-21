package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageItem extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Image image;
	
	public ImageItem(byte[] arr) {
		try {
			this.image = new ImageIcon(arr).getImage();
			
			setPreferredSize(new Dimension(200, 200));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public ImageItem(ImageIcon icon) {
		this.image = icon.getImage();
	}
	
	@Override
	public void paint(Graphics g) {
		draw(g);
	}

	private void draw(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getWidth() * 3 / 4, this);
	}

}

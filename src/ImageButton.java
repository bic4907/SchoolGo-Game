import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ImageButton extends JButton  {
	
	// 두 이미지 모두 생성자에서 이미지가 리사이징 됨
	private Image img_normal;
	private Image img_pressed;
	
	private SoundPlayer.SoundList btn_sound;

	public ImageButton() {
		super();
		
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// 마우스 클릭시 이벤트
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// 마우스 클릭시 눌러진 이미지로 변경
				
				((ImageButton) e.getSource()).setIcon(new ImageIcon(img_pressed));
				
				if(btn_sound != null) {
					SoundPlayer.getInstance().playOnce(btn_sound);
				}
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// 마우스 뗄시 기본이미지로 변경
				
				((ImageButton) e.getSource()).setIcon(new ImageIcon(img_normal));
			}			
			
			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}			
		});
		btn_sound = null;
	}
	
	public ImageButton(String path) {
		this();

		try {
			Image img = ImageIO.read(new File(path));
			img_normal = img;
			img = ImageIO.read(new File(path));
			img_pressed = img;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ImageButton(String normal_path, String press_path) {
		try {
			img_normal = ImageIO.read(new File(normal_path));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			img_pressed = ImageIO.read(new File(press_path));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setNormalImage(String s) {
		try {
			img_normal = ImageIO.read(new File(s));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	public void setPressedImage(String s) {
		try {
			img_pressed = ImageIO.read(new File(s));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setClickSound(SoundPlayer.SoundList sound) {
		this.btn_sound = sound;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {

		super.setBounds(x, y, width, height);
		
		try {
			BufferedImage bImg = (BufferedImage)img_normal;
			Image newImage = bImg.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
			img_normal = newImage;
			ImageIcon icon = new ImageIcon(newImage);
			this.setIcon(icon);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			BufferedImage bImg = (BufferedImage)img_pressed;
			Image newImage = bImg.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
			img_pressed = newImage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}

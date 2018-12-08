import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class CollegeSelectDialog extends JPanel {
	
	private GameManager.PersonType type;
	private Map<String, String> imgName;
	
	
	
	public CollegeSelectDialog() {
		
		imgName = new HashMap<String, String>();
		imgName.put("인문대", "res/book.png");
		imgName.put("자연대", "res/nature.png");
		imgName.put("공과대", "res/gear.png");
		imgName.put("예체능대", "res/art.png");
		
		this.setLayout(null);
		this.setPreferredSize(new Dimension(400,100));
		this.setBackground(new Color(0, 0, 0, 150));
		Vector<String> cType = SceneManager.getInstance().getGameManager().CollegeType;
		for(int i = 0; i < cType.size(); i++) {
			JLabel lbl = new JLabel();
			lbl.setText(cType.get(i));
			
			lbl.setHorizontalTextPosition(SwingConstants.CENTER);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			
			lbl.setBackground(Color.RED);
			lbl.setForeground(Color.WHITE);

			lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lbl.setBounds(100 * i, 65, 100, 50);
			
			ImageButton imgBtn = new ImageButton();
			imgBtn.setNormalImage(imgName.get(cType.get(i)));
			imgBtn.setPressedImage(imgName.get(cType.get(i)));
			imgBtn.setBounds(100 * i + 15, 10, 70, 70);
			imgBtn.setName(cType.get(i));
			imgBtn.setClickSound(SoundPlayer.SoundList.ButtonSound01);
			imgBtn.addMouseListener(new MouseListener( ) {

				@Override
				public void mouseClicked(MouseEvent event) {}

				@Override
				public void mouseEntered(MouseEvent e) {
					MainFrame.getInstance().getPanel().repaint();
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					MainFrame.getInstance().getPanel().repaint();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					ImageButton btn = (ImageButton)e.getSource();
					SceneManager.getInstance().getGameManager().scoutPerson(type, btn.getName());
					SceneManager.getInstance().getGameManager().setScouting(false);
					MainFrame.getInstance().getPanel().repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					MainFrame.getInstance().getPanel().repaint();
				}
				
			});


			this.add(lbl);
			this.add(imgBtn);
		}
		
		
		
		
	}


	public void setType(GameManager.PersonType type) {
		this.type = type;
	}
	
	
	
	
	
	
	
	
}

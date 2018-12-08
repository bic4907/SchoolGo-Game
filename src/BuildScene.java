import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BuildScene extends JPanel {

	private ImageIcon Back;

	private JPanel orderPanel;
	private ImageButton btnExit;
	private buildingListener btnL;
	private int changingY;
	private WheelListener wheel;
	//data
	
	public BuildScene() {
		this.setPreferredSize(new Dimension(800,500));
		this.setLayout(null);
		
		GameManager gm = SceneManager.getInstance().getGameManager();
		
		/* 건물 스크롤 */
		orderPanel = new JPanel();
		orderPanel.setBounds(0, 0, 800, 800);
		orderPanel.setLayout(null);
		orderPanel.setOpaque(false);
		changingY = 50;
		wheel = new WheelListener();
		this.addMouseWheelListener(wheel);
		
		/* 건물 스크롤 끝 */
		
		Back = new ImageIcon("res/start/bg.jpg"); //배경 이미지
		btnL = new buildingListener(); //버튼리스너
		
		int idx = 0;
		for(Building b : gm.getBuildingList()) {
			
			boolean is_display = true;
			for(Building bb : gm.getMyBuilding()) {
				if(b.getName() == bb.getName()) {
					is_display = false;
					break;
				}
			}
			if(!is_display) continue;
			//아직 건설하지 않은 건물들을 보여준다
			
			JPanel panel = getBuildingPanel(b);
			
			
			int x, y;
			if(idx % 2 == 0) {
				x = 50; // 왼쪽에 붙을 경우
			} else {
				x = 400; // 오른쪽에 붙일 경우
			}
			
			y = (panel.getPreferredSize().height + 10) * ((int)(idx / 2)) + 50;

			panel.setBounds(x, y, panel.getPreferredSize().width, panel.getPreferredSize().height);

			panel.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent event) {
					JPanel panel = (JPanel) event.getSource();
					
					
					boolean ret = SceneManager.getInstance().getGameManager().buyBuilding(panel.getName());
					if(ret) {
						SceneManager.getInstance().changeState(SceneManager.SceneType.INGAME);
						SceneManager.getInstance().addAlert(new AlertDialog(panel.getName() + "을 지었습니다!"));						
					} else {
						SceneManager.getInstance().addAlert(new AlertDialog("돈이 부족해요!!"));						
					}
					
					SoundPlayer.getInstance().playOnce(SoundPlayer.SoundList.ButtonSound01);
					
				} //mouseClicked()

				@Override
				public void mouseEntered(MouseEvent event) {
					JPanel panel = (JPanel) event.getSource();
					panel.setBackground(new Color(200, 200, 200));
					MainFrame.getInstance().getPanel().repaint();
				} //mouseEntered()

				@Override
				public void mouseExited(MouseEvent event) {
					JPanel panel = (JPanel) event.getSource();
					panel.setBackground(Color.WHITE);
				} //mouseExited
				
				//hovering
				
				@Override
				public void mousePressed(MouseEvent arg0) {}

				@Override
				public void mouseReleased(MouseEvent arg0) {}
			});
			orderPanel.add(panel);
			
			idx++;
		}
		
		btnExit = new ImageButton("res/exit.png");
		btnExit.setBounds(740,10,30,30);
		btnExit.addActionListener(btnL);
		btnExit.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		this.add(btnExit);
		//EXIT 버튼
		
		this.add(orderPanel);
		
		for(HumanWalking hw : SceneManager.getInstance().getGameManager().getMyHuman()) {
			hw.attached = false;
		}
		
		
		
		// BGM 플레이
		SoundPlayer.getInstance().play(SoundPlayer.SoundList.Curning);
	} //BuildScene()
	
	private JPanel getBuildingPanel(Building b) {
		// 건물 하나를 나타내는 패널생성
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(300, 110));
		panel.setBackground(Color.white);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Font customFont, customFont2;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(20f);
			customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(16f);
		} catch (Exception e) {
			e.printStackTrace();
			customFont = customFont2 = new Font("Arial", Font.BOLD, 30);
		}	
		
		JLabel lblIcon, lblName, lblPrice, lblCapacity;
		
		lblIcon = new JLabel();
		lblIcon.setIcon(b.getImage());
		lblIcon.setSize(100, 100);

		Image img;
		try {
			img = ImageIO.read(new File(b.getImage().toString()));
			BufferedImage bImg = (BufferedImage)img;
			Image newImage = bImg.getScaledInstance(lblIcon.getWidth(), lblIcon.getHeight(), Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newImage);
			lblIcon.setIcon(icon);
		
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		lblIcon.setBounds(5, 5, 100, 100);
		panel.add(lblIcon);
		
		lblName = new JLabel(b.getName());
		lblName.setFont(customFont);
		lblName.setBounds(120, 10, 250, 30);
		panel.add(lblName);
		
		lblPrice = new JLabel("가격 : " + String.format("%,d", b.getPrice()));
		lblPrice.setFont(customFont2);
		lblPrice.setBounds(120, 40, 250, 30);
		panel.add(lblPrice);
		
		lblCapacity = new JLabel("수용 능력 : " + Integer.toString(b.getCapacity()));
		lblCapacity.setFont(customFont2);
		lblCapacity.setBounds(120, 70, 250, 30);
		panel.add(lblCapacity);
	
		panel.setName(b.getName());
		
		return panel;
		
	} //getBuildingPanel()
	

	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		page.drawImage(Back.getImage(), 0, 0, null);
	} //paintComponent()
	
	private class buildingListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event) {
			Object obj = event.getSource();
			
			if(obj == btnExit) { 
				SceneManager.getInstance().changeState(SceneManager.SceneType.INGAME);
			}

		
		}//actionPerforemed()
		
	}//buildingListener class
	
	
	private class WheelListener implements MouseWheelListener 
	{

		@Override
		public void mouseWheelMoved(MouseWheelEvent event) {
			// TODO Auto-generated method stub
			
			int n = event.getWheelRotation(); //휠 위 아래 구분
			
			if(n > 0)
			{
				if(changingY > -400) 
				{
					changingY -= 50; //휠을 내리면 빌딩 패널이 나타나는 Y좌표 감소
					orderPanel.setLocation(0, changingY);
				}
			} //mouse wheel down
			else
			{
				if(changingY < 0)
				{
					changingY += 50; //휠을 올리면 빌딩 패널이 나타나는 Y좌표 증가
					orderPanel.setLocation(0, changingY);
				}
			} //mouse wheel up
		} // mouseWheelMoved()
	} //WheelListener class	
} //BuildScene class

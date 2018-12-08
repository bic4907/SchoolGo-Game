import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ResultScene extends JPanel {
	
	private ArrayList<String> results;
	
	private JLabel		lblResultTitle;
	private ImageButton		btnRestart;
	private ImageButton		btnExit;
	private JLabel 		resultImg;
	private Font customFont2;
	private int cntDesc;
	private Image bgImage;
	private AnimeThread aThd;
	
	public ResultScene() {
		this.setLayout(null);
		this.setBackground(Color.WHITE);

		/* 재시작 버튼 */
		btnRestart = new ImageButton();
		btnRestart.setNormalImage("res/restart_btn_normal.png");
		btnRestart.setPressedImage("res/restart_btn_pressed.png");
		btnRestart.setBounds(650, 310, 100, 60);
		btnRestart.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		btnRestart.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if(aThd != null) {
					aThd.stop();
					aThd = null;
				}
				SceneManager.getInstance().resetGameManager();
				SceneManager.getInstance().changeState(SceneManager.SceneType.TUTORIAL);
			}
		});
		this.add(btnRestart);		
		
		/* 종료 버튼 */
		btnExit = new ImageButton();
		btnExit.setNormalImage("res/exit_btn_normal.png");
		btnExit.setPressedImage("res/exit_btn_pressed.png");
		btnExit.setBounds(650, 370, 100, 60);
		btnExit.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(btnExit);
		
		/*
		 *  커스텀 폰트 참고 주소
			https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
		*/
		Font customFont, customFont2;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(30f);
			customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(18f);
			this.customFont2 = customFont2;
		} catch (Exception e) {
			e.printStackTrace();
			customFont = customFont2 = new Font("Arial", Font.BOLD, 30);
		}		

		Result result = SceneManager.getInstance().getGameManager().getResult();
		String title = "";
		String imgPath = "";
		if(result.type == GameManager.ResultType.Perfect) {
			title = "세상에 이렇게 완벽한 학교가 있을수가!!!";
			imgPath = "res/result/havard.jpg";
		} else if(result.type == GameManager.ResultType.OK) {
			title = "그래도 좋은 학교네요!";
			imgPath = "res/result/sejong.jpg";
		} else if(result.type == GameManager.ResultType.Inmun) {
			title = "여기는 인문계만 유명한가요?";
			imgPath = "res/book.png";
		} else if(result.type == GameManager.ResultType.Gonggwa) {
			title = "공과대학교가 잘나가는 대학교";
			imgPath = "res/gear.png";
		} else if(result.type == GameManager.ResultType.Jayon) {
			title = "과학자는 많이 나오겠네요";
			imgPath = "res/nature.png";
		} else if(result.type == GameManager.ResultType.Yeche) {
			title = "예체능이 학생들이 굉장하네요~";
			imgPath = "res/art.png";
		} else if(result.type == GameManager.ResultType.Fail) {
			title = "어떻게 잘하는게 하나도 없죠?";
			imgPath = "res/result/fail.jpg";
		} else if(result.type == GameManager.ResultType.Pasan) {
			title = "파산했습니다. 경찰서갈 준비하세요";
			imgPath = "res/result/police.jpg";
		}
		
		/* 결과 타이틀 */
		lblResultTitle = new JLabel(title);
		lblResultTitle.setFont(customFont);
		lblResultTitle.setBounds(0, -50, 800, 50);
		lblResultTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblResultTitle);
		
		/* 결과 이미지 */
		resultImg = new JLabel();
		resultImg.setBounds(70, 150, 250, 200);
		resultImg.setOpaque(true);
		resultImg.setVisible(false);
		
		try {
			BufferedImage bImg = (BufferedImage)ImageIO.read(new File(imgPath));
			Image newImage = bImg.getScaledInstance(resultImg.getWidth(), resultImg.getHeight(), Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newImage);
			resultImg.setIcon(icon);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		this.add(resultImg);

		results = result.desc;

		SoundPlayer.getInstance().play(SoundPlayer.SoundList.MapleMain);
		
		aThd = new AnimeThread();
		aThd.start();
	}

	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		if(bgImage == null) {
			try {
				bgImage = ImageIO.read(new File("res/start/bg_end.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(bgImage != null) {
			page.drawImage(bgImage, 0, 0, this);
		}
	}	
	
	void addDescToScreen(String s) {
		
		/* 텍스트 슬라이드 */
		JLabel lbl = new JLabel();
		lbl.setFont(customFont2);
		lbl.setBounds(400, 150 + (35 * cntDesc), 300, 35);
		lbl.setText(s);
		this.add(lbl);
		repaint();
		cntDesc++;
	}
	
	private class AnimeThread extends Thread {
	
		@Override
		public void run() {
			
			sleep(2000);
			int init_y = lblResultTitle.getY();

			for(double i = 0; i < Math.PI /
					2; i += 0.01) {
				int y_pos = init_y + (int)(i * 70);

				lblResultTitle.setLocation(lblResultTitle.getX(), y_pos);
				sleep(2);
			}
			sleep(300);
			resultImg.setVisible(true);
			sleep(600);
			for(String s : results) {
				addDescToScreen(s);
				sleep(600);
			}
			
		}
		
		public void sleep(int millis) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		
	}

}

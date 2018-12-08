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
	
	private JLabel		lblResultTitle;		// 엔딩 타입에 따른 타이틀 JLabel
	private ImageButton		btnRestart;	// 게임 재시작 버튼
	private ImageButton		btnExit;	// 게임종료 버튼
	private JLabel 		resultImg;		// 왼쪽에 들어갈 이미지의 JLabel
	private Font customFont2;			// 폰트 저장
	private int cntDesc;				// 단과대학 설명이 몇 번 째로 표시되고 있는지 저장
	private Image bgImage;  			// ResultScene 배경화면(고정)
	private AnimeThread aThd; 			// ResultScene에 들어가는 애니메이션 쓰레드
	
	public ResultScene() {
		this.setLayout(null);
		this.setBackground(Color.WHITE);

		/* 재시작 버튼 */
		btnRestart = new ImageButton();
		btnRestart.setNormalImage("res/restart_btn_normal.png"); 	// 때었을 때 이미지 적용
		btnRestart.setPressedImage("res/restart_btn_pressed.png");	// 눌렀을 때 이미지 적용
		btnRestart.setBounds(650, 310, 100, 60);			// 버튼 리사이징 
		btnRestart.setClickSound(SoundPlayer.SoundList.ButtonSound01); 	// 클릭사운드 적용
		btnRestart.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if(aThd != null) {
					aThd.stop(); // 쓰레드 삭제
					aThd = null;
				}
				SceneManager.getInstance().resetGameManager();					// 설정되었던 GameManager 초기화
				SceneManager.getInstance().changeState(SceneManager.SceneType.TUTORIAL);	// 화면을 전환
			}
		});
		this.add(btnRestart)	
		
		/* 종료 버튼 */
		btnExit = new ImageButton();
		btnExit.setNormalImage("res/exit_btn_normal.png");		// 때었을 때 이미지 적용
		btnExit.setPressedImage("res/exit_btn_pressed.png");		// 눌럿을 때 이미지 적용
		btnExit.setBounds(650, 370, 100, 60);				// 버튼 리사이징
		btnExit.setClickSound(SoundPlayer.SoundList.ButtonSound01);	// 클락사운드 적용
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);					// 프로그램 완전 종료
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

		// GameManager에서 결과를 가져옴
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
		
		// 결과 이미지 리사이징
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

		SoundPlayer.getInstance().play(SoundPlayer.SoundList.MapleMain); // 배경음악 재생
		
		aThd = new AnimeThread();
		aThd.start();
	}

	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		// 배경이미지 한번 불러들여서 Image객체로 저장해놓고 재사용
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
	
	// 스크린에 단과대학별 설정을 붙여주는 method 정의
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
			// 1. 결과 타이틀 위에서 아래로 내려오는 애니메이션
			int init_y = lblResultTitle.getY();

			for(double i = 0; i < Math.PI /
					2; i += 0.01) {
				int y_pos = init_y + (int)(i * 70);

				lblResultTitle.setLocation(lblResultTitle.getX(), y_pos);
				sleep(2);
			}
			sleep(300);
			// 2. 결과 이미지 보여주기
			resultImg.setVisible(true);
			sleep(600);
			for(String s : results) {
				addDescToScreen(s); // 3.... 결과 단과대학별 상태 
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

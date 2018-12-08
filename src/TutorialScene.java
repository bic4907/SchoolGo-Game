import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TutorialScene extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private int pageNum;
	private Vector<Slide> slides;
	
	
	private JLabel		lblSceneTitle;
	private JLabel		lblCurrent;
	private JLabel		lblTotal;
	
	private ImageButton		btnPrevSlide;
	private ImageButton		btnNextSlide;
	private ImageButton		btnSkip;
	private JLabel 		slideImg;
	private JLabel		slideDesc;
	
	private Image bgImage;
	
	private SlideButtonListener slideBtnL;
	
	private SlideThread sThd;
	private boolean flag_anime;
	
	public TutorialScene() {
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		
		/* Init the Slides */
		// Slide 객체를 통해 미리 슬라이드 내용을 초기화 
		this.slides = new Vector<Slide>();
		this.slides.add(new Slide("res/tutorial/t1.jpg", "<html><p>①매달 1일, 15일마다 활성화<br>되는 학생/교수 모집 버튼<br>클릭시 각각 학생, 교수 수가 증가<br>학생 수 증가 시 수입 증가<br>교수 증가시 학업도 증가, 돈 감소<br><br>②각 단과대 별 모집 버튼<br>단과대마다 오르는 능력치가 다름<br></p></html>"));
		this.slides.add(new Slide("res/tutorial/t2.jpg", "<html><p>①현재 게임 현황<br>시간에 따라 이벤트가 발생<br>7월이 되면 게임종료<br>학생 수와 교수 수를 표시<br>현재 갖고 있는 돈과 수입 표시<br><br>②학생마다 랜덤으로 생기는 이벤트<br>책을 클릭하면 능력치가 오름<br>하트 클릭시 학생 행복도 증가</p></html>"));
		this.slides.add(new Slide("res/tutorial/t3.jpg", "<html><p>①건설되어 있는 건물<br>건물에 따라 학생정원, 능력치 영향<br>건물 건설비용, 관리비용 소모<br><br>②건물 건설 버튼<br>클릭시 건물건설 화면으로 이동</p></html>"));
		this.slides.add(new Slide("res/tutorial/t4.jpg", "<html><p>①건물 건설 버튼<br>건물 가격과 늘어나는 학생정원 표시<br>클릭 시 게임에 건물 건설하고 게임화면으로 이동<br><br>②건물 건설 화면 나가기 버튼<br>클릭시 건물 건설 하지 않고 게임화면으로 이동</p></html>"));
		this.slides.add(new Slide("res/tutorial/t5.jpg", "<html><p>①랜덤 이벤트<br>여러가지 이벤트가 있음<br>각 이벤트마다 게임에 영향을 주는게 다름<br><br>②이벤트 선택 버튼<br>플레이어의 선택에 따라 능력치, 게임 결과에 영향을 줌</p></html>"));
		pageNum = 0;
		flag_anime = false;
		
		// 슬라이드 버튼 리스너
		slideBtnL = new SlideButtonListener();				
		
		/* 이전 슬라이드로 버튼 */
		btnPrevSlide = new ImageButton();
		btnPrevSlide.setNormalImage("res/prev_btn_normal.png");
		btnPrevSlide.setPressedImage("res/prev_btn_pressed.png");
		btnPrevSlide.setBounds(50, 370, 100, 60);
		btnPrevSlide.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		btnPrevSlide.addActionListener(slideBtnL);
		this.add(btnPrevSlide);		
		
		/* 다음 슬라이드로 버튼 */
		btnNextSlide = new ImageButton();
		btnNextSlide.setNormalImage("res/next_btn_normal.png");
		btnNextSlide.setPressedImage("res/next_btn_pressed.png");
		btnNextSlide.setBounds(650, 370, 100, 60);
		btnNextSlide.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		btnNextSlide.addActionListener(slideBtnL);
		this.add(btnNextSlide);
		
		/* 튜툐리얼 스킵 버튼 */
		btnSkip = new ImageButton();
		btnSkip.setNormalImage("res/skip_btn_normal.png");
		btnSkip.setPressedImage("res/skip_btn_pressed.png");
		btnSkip.setBounds(650, 20, 100, 60);
		btnSkip.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		btnSkip.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent event) {
				SceneManager.getInstance().changeState(SceneManager.SceneType.INGAME);
			}
		});
		this.add(btnSkip);
		
		
		/* 이미지 슬라이드 */
		slideImg = new JLabel();
		slideImg.setBounds(90, 120, 350, 220);
		slideImg.setOpaque(true);
		slideImg.setForeground(Color.RED);
		slideImg.setBackground(new Color(0, 0, 0, 200));
		this.add(slideImg);
		
		/* 텍스트 슬라이드 */
		slideDesc = new JLabel();
		slideDesc.setFont(new Font("Dotum", Font.BOLD, 15));
		slideDesc.setBounds(480, 140, 250, 160);
		slideDesc.setBackground(Color.GRAY);
		this.add(this.slideDesc);

		/*
		 *  커스텀 폰트 참고 주소
			https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
		*/
		Font customFont, customFont2;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(40f);
			customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(20f);
		} catch (Exception e) {
			e.printStackTrace();
			customFont = customFont2 = new Font("Arial", Font.BOLD, 30);
		}		
		
	

		/* Tutorial 글씨 */
		lblSceneTitle = new JLabel("튜토리얼");
		lblSceneTitle.setFont(customFont);
		lblSceneTitle.setBounds(330, 10, 200, 50);
		this.add(lblSceneTitle);
		
		/* 전체 슬라이드 표시 라벨 */
		lblTotal = new JLabel();
		lblTotal.setFont(customFont2);
		lblTotal.setBounds(400, 380, 200, 50);
		lblTotal.setText("/ " + this.slides.size());
		this.add(lblTotal);
		
		/* 현재 페이지 표시 라벨 */
		lblCurrent = new JLabel();
		lblCurrent.setFont(customFont2);
		lblCurrent.setBounds(380, 380, 200, 50);
		this.add(lblCurrent);
		
		refreshSlide();
		
		SoundPlayer.getInstance().play(SoundPlayer.SoundList.Ellinia);
	}

	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		if(bgImage == null) {
			try {
				bgImage = ImageIO.read(new File("res/tutorial/bg.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(bgImage != null) {
			page.drawImage(bgImage, 0, 0, this);
		}
	}
	
	
	private class Slide {
		private ImageIcon image;
		private String desc;
		
		public Slide(String src, String desc) {
			this.image = new ImageIcon(src);
			this.desc = desc;
		}
		
		public String getImagePath() {
			return this.image.toString();
		}
		
		@SuppressWarnings("unused")
		public ImageIcon getImage() {
			return this.image;
		}
		public String getDesc() {
			return this.desc;
		}
	}
	
	private class SlideButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnPrevSlide && !flag_anime) {
				pageNum--;
				flag_anime = true; // 이전,다음 슬라이드 버튼을 사용 불가능하게 만든다.
				sThd = new SlideThread();
				sThd.start(); // 애니메이션 쓰레드 시작
			} else if(e.getSource() == btnNextSlide && !flag_anime) {
				pageNum++;
				flag_anime = true; // 이전,다음 슬라이드 버튼을 사용 불가능하게 만든다.
				sThd = new SlideThread();
				sThd.start(); // 애니메이션 쓰레드 시작
			}
		} 
	}

	public void refreshSlide() {
		Slide s = this.slides.get(pageNum);

		// 슬라이드 이미지 리사이징
		try {
			BufferedImage bImg = null;
			bImg = ImageIO.read(new File(s.getImagePath()));
			Image img = bImg.getScaledInstance(slideImg.getWidth(), slideImg.getHeight(), Image.SCALE_SMOOTH);
			slideImg.setIcon(new ImageIcon(img));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		slideDesc.setText(s.getDesc());
		lblCurrent.setText(String.valueOf(pageNum + 1)); // 현재 페이지 갱신
		
		btnPrevSlide.setVisible(true);
		btnNextSlide.setVisible(true);
		
		if(pageNum == 0) {
			btnPrevSlide.setVisible(false);
		} else if(pageNum == this.slides.size() - 1) {
			btnNextSlide.setVisible(false);
		}
	}
	
	// 튜툐리얼 애니메이션 쓰레드
	private class SlideThread extends Thread {
		
		@Override
		public void run() {
			
			int init_img = slideImg.getX();
			int init_desc = slideDesc.getX();
			
			boolean is_change = false;
			for(double i = 0; i < Math.PI; i += 0.01) {
				
				// 만약 끝까지 이동된 경우 내용 바꿔치기
				if(!is_change && i > Math.PI / 2) {
					refreshSlide();
					is_change = true;
				}
				int dist = (int)(Math.sin(i) * 500); // iteration 별로 위치 구하기

				slideImg.setLocation(init_img - dist, slideImg.getY());
				slideDesc.setLocation(init_desc + dist, slideDesc.getY());
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// 위치 갱신
			slideImg.setLocation(init_img, slideImg.getY());
			slideDesc.setLocation(init_desc, slideDesc.getY());
			
			flag_anime = false; // 이전,다음 슬라이드 버튼을 다시 사용 가능하게 만든다.
		}
	}
	
}

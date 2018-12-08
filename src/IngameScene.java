import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class IngameScene extends JPanel {
	

	private ImageIcon iconMoney, iconTime, iconStud, iconProf, iconEntr; // 사진
	private ImageButton btnIStud, btnIProf, btnIBuild; // 이미지버튼
	private JPanel infoPanel; // 게임정보 담는 패널
	private JLabel lblTime, lblMoney, lblStud, lblProf, lblEntr, lblSName; // 게임정보
	private CollegeSelectDialog scoutDlg;
	
	
	private ImageIcon backIcon = new ImageIcon("res/IngameScene2.png"); // 배경

	private BtnListener btnL; // 액션리스너
	
	private GameThread gameThd; // 게임용 쓰레드
	private HumanWalkingThread hwThd; // NPC용 쓰레드
	public IngameScene() {
		GameManager manager = SceneManager.getInstance().getGameManager(); // 정보 가져오기
		
		setPreferredSize(new Dimension(800,500)); // 크기
		setLayout(null);

		iconMoney = new ImageIcon("res/lblicon/money.png"); // label에 추가할 돈 아이콘
		iconTime = new ImageIcon("res/lblicon/time.png"); // label에 추가할 시간 아이콘
		iconStud = new ImageIcon("res/lblicon/stud.png"); // label에 추가할 학생 아이콘
		iconProf = new ImageIcon("res/lblicon/prof.png"); // label에 추가할 교수 아이콘
		iconEntr = new ImageIcon("res/lblicon/entr.png"); // 학교 아이콘

		Font customFont2, customFont1; // 폰트생성
		try {
			customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(18f);
			customFont1 = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(14f);
		} catch (Exception e) {
			e.printStackTrace();
			customFont2 = new Font("Arial", Font.BOLD, 30);
			customFont1 = new Font("mFont", Font.BOLD, 12);
		}	// JLabel 폰트
		
		btnL = new BtnListener(); // buttonListener
		
		infoPanel = new JPanel(); // Label담는 Panel 
		infoPanel.setBounds(100,20,430,55);
		infoPanel.setBackground(Color.ORANGE);
		infoPanel.setLayout(null);
		this.add(infoPanel);
	
		
		lblTime = new JLabel(); 
		lblTime.setBounds(10,5,250,20);
		lblTime.setFont(customFont2);
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setText(String.valueOf(manager.getMonth()) 
				+ "/" +
				String.valueOf(manager.getDay()));
		lblTime.setIcon(iconTime);
		infoPanel.add(lblTime);
		//시간정보
		
		lblMoney = new JLabel(""+ manager.getMoney(),iconMoney,SwingConstants.LEFT);
		lblMoney.setBounds(10,30,250,20);
		lblMoney.setFont(customFont2);
		lblMoney.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblMoney);
		// 돈정보
		
		lblStud = new JLabel("",iconStud,SwingConstants.LEFT);
		lblStud.setBounds(220,5,200,20);
		lblStud.setFont(customFont2);
		lblStud.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblStud);
		// 학생 수
		
		lblProf = new JLabel("",iconProf,SwingConstants.LEFT);
		lblProf.setBounds(220,30,200,20);
		lblProf.setFont(customFont2);
		lblProf.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblProf);
		// 교수 수
		
		btnIBuild = new ImageButton();
		btnIBuild.setNormalImage("res/Ingame/BUILDB.png");
		btnIBuild.setPressedImage("res/Ingame/BUILDB2.png");
		btnIBuild.setBounds(650,10,100,100);
		btnIBuild.addActionListener(btnL);
		add(btnIBuild);
		// 건물 건설 버튼
		
		btnIStud = new ImageButton();
		btnIStud.setNormalImage("res/Ingame/STUDB.png");
		btnIStud.setPressedImage("res/Ingame/STUDB2.png");
		btnIStud.setBounds(15, 90, 100, 50);
		btnIStud.addActionListener(btnL);
		add(btnIStud);
		// 학생 모집 버튼
		
		btnIProf = new ImageButton();
		btnIProf.setNormalImage("res/Ingame/PROFB.png");
		btnIProf.setPressedImage("res/Ingame/PROFB2.png");
		btnIProf.setBounds(15, 140, 100, 50);
		btnIProf.addActionListener(btnL);
		add(btnIProf);
		// 교수 모집 버튼


		lblSName = new JLabel();
		lblSName.setOpaque(true);
		lblSName.setBackground(Color.white);
		lblSName.setBorder(BorderFactory.createLineBorder(Color.lightGray, 3));
		lblSName.setHorizontalAlignment(SwingConstants.CENTER);
		lblSName.setFont(customFont1);
		lblSName.setBounds(20,70,82,20);
		add(lblSName); // 학교이름 추가
		
		lblEntr = new JLabel(iconEntr);
		lblEntr.setBounds(10, 0, 100, 100);
		add(lblEntr); // 학교 그림 추가
		
		
		// 만약 학생이나 교수 모집버튼을 누르면 모집창을 띄어줌
		scoutDlg = new CollegeSelectDialog();
		scoutDlg.setBounds(200, 260, 400, 100);
		scoutDlg.setVisible(false);
		this.add(scoutDlg);		
		
		// 쓰레드
		if(gameThd == null) {
			gameThd = new GameThread();
			gameThd.start();
		} 
		if(hwThd == null) {
			hwThd = new HumanWalkingThread();
			hwThd.start();
		}
		SoundPlayer.getInstance().play(SoundPlayer.SoundList.Ludibriam);
		
		render(); // 있는 정보만큼 화면에 추가하기
	}

	public void paintComponent(Graphics page)
	{
		super.paintComponent(page);
		GameManager manager = SceneManager.getInstance().getGameManager();		
		if(manager.getScouting()) {
			scoutDlg.setVisible(true);
		} else {
			scoutDlg.setVisible(false);
		} // 시간마다 모집창 띄우기
			
		
		page.drawImage(backIcon.getImage(),0,0,this);
	} // 배경화면 추가
	
	public void render() {
		GameManager manager = SceneManager.getInstance().getGameManager(); // 정보		
		

		lblSName.setText(SceneManager.getInstance().getUserSchool()+"대학교"); // 입력한 대학교 이름 띄우기
		
		lblTime.setText(String.valueOf(manager.getMonth()) + "월 " + String.valueOf(manager.getDay()) + "일"); // 시간
		
		if((manager.getDay() > 1 && manager.getDay() <= 5) || (manager.getDay() > 15 && manager.getDay() <= 20)) {
			this.btnIStud.setVisible(true);
			this.btnIProf.setVisible(true);
		} else {
			this.btnIStud.setVisible(false);
			this.btnIProf.setVisible(false);
		} // 모집버튼 활성화
		if(manager.getDay() == 1 || manager.getDay() == 15) {
			SceneManager.getInstance().addAlert(new AlertDialog("곧 원서접수가 시작됩니다!"));
		} // 지정 시간마다 dialog 띄우기

		if(manager.getOccur() == false) //한 달에 한번
		{
			if((manager.getDay() >= 5) && (manager.getDay() <= 23)) //5일 ~ 23일 사이에 랜덤
			{	
				if(manager.getDay() == manager.getRandomDay())
				{
					int random = (int)(Math.random()*7); //랜덤 이벤트
					
					for(int i = 0;i<7;i++)
					{
						if(manager.getRandomList()[i] == -1)
						{
							manager.getRandomList()[i] = random; 
							break;
						} //이벤트가 이미 발생했음을 알기 위해 배열에 해당 이벤트를 저장 
						
						else if(manager.getRandomList()[i] == random)
						{
							while(manager.getRandomList()[i] == random)
							{
								random = (int)(Math.random()*7);
							}
						} //이미 발생한 이벤트는 발생하지 않도록함
					}
	
					EventDialog ed = new EventDialog(random);
					manager.setOccur(true);
					SceneManager.getInstance().addEvent(ed);
				}
			} // 랜덤 이벤트 발생
		}		
		
		this.lblMoney.setText(String.format("%,d", manager.getMoney()) + "(" + String.format("%+,d", manager.getLastIncome()) + ")");
		this.lblStud.setText(Integer.toString(manager.getStudentCount())
				+ "/" + Integer.toString(manager.getAllCapacity())
				);
		this.lblProf.setText(Integer.toString(manager.getProfessorCount()));
		// 돈, 학생, 교수 정보 갱신
		
		for(Building b : manager.getMyBuilding()) {
			
			JLabel bIcon = new JLabel();

			bIcon.setBounds(b.getposition().x, b.getposition().y, 100, 100); // 위치에 맞게 
			
			Image img;
			try {
				img = ImageIO.read(new File(b.getImage().toString()));
				BufferedImage bImg = (BufferedImage)img;
				Image newImage = bImg.getScaledInstance(bIcon.getWidth(), bIcon.getHeight(), Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(newImage);
				bIcon.setIcon(icon);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.add(bIcon);
		} // 건물저장해놓은 배열, 건물 그리기
		
		for(HumanWalking data : manager.getMyHuman()) {
			if(data.attached == false) { // 그려지지 않은 사람만 추가로 그리기
				add(data); // NPC
				add(data.getEventPanel()); // NPC 이벤트 패널
				data.attached = true; // 정보 갱신
			}
		} // 사람 그리기
		
		
		if(manager.getScouting()) {
			scoutDlg.setVisible(true);
		} else {
			scoutDlg.setVisible(false);
		}
		// 모집상황에 따라 표시하고 지우기
		repaint();

	}
	
	public CollegeSelectDialog getScoutDlg() {
		return this.scoutDlg;
	}
	
	private class BtnListener implements ActionListener {
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent event)
		{
			GameManager manager = SceneManager.getInstance().getGameManager();
			
			Object obj = event.getSource();
			if(obj == btnIStud)
			{
				manager.setScouting(true);
				scoutDlg.setVisible(true);
				scoutDlg.setType(GameManager.PersonType.Student);				
			} // 학생모집 버튼
			
			else if(obj == btnIProf)
			{
				manager.setScouting(true);
				scoutDlg.setVisible(true);
				scoutDlg.setType(GameManager.PersonType.Professor);
			} // 교수모집 버튼
			
			else if(obj == btnIBuild)	
			{
				if(gameThd != null && gameThd.isAlive()) {
					gameThd.stop();
				}
				gameThd = null;
				
				SceneManager.getInstance().changeState(SceneManager.SceneType.BUILD);
				
			} // 건물건설 버튼
		}
		
	}
	
	
	
	
	
	/* 1초마다 게임시간을 흘려줌 */
	
	private class GameThread extends Thread {
		
		@Override
		public void run() {
			GameManager gm = SceneManager.getInstance().getGameManager();
			while(true) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				gm.nextDay();
				render(); // 정보 갱신
				
				if(gm.getMonth() >= 7 || gm.getMoney() <= -500000) {
					break;
				} // 끝나는 조건
			}
			SceneManager.getInstance().changeState(SceneManager.SceneType.RESULT);
			
		}
	}
	
	private class HumanWalkingThread extends Thread { // NPC용 시간
		
		@Override
		public void run() {
			
			GameManager gm = SceneManager.getInstance().getGameManager();
			while(true) {
				for(HumanWalking hw : gm.getMyHuman()) { // 계속 갱신
					hw.setLocation(hw.x, hw.y);
					hw.repaint();
					hw.rEvent.repaint();
					repaint();
				}
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					break;

				}
			}
		}
	}
	
	
	
	
	
	
	
}

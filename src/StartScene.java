import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;


@SuppressWarnings("serial")
public class StartScene extends JPanel {

	private JPanel loginPanel;
	private ImageIcon BackImage;
	
	private JLabel lblName;
	private JLabel lblSchool;
	private JLabel lblUniversity;
	
	private JTextField inputName; //사용자 이름 입력
	private JTextField inputSchool; //사용자 학교 입력
	private ImageButton btnLogin; //로그인 버튼
	
	@SuppressWarnings("unused")
	private Font font, font2;
	private LoginListener btnL;
	//data
	
	public StartScene() {
		setPreferredSize(new Dimension(800,500));
		setBackground(Color.white);
		setLayout(null);

		SoundPlayer.getInstance().play(SoundPlayer.SoundList.Jjangu); //bgm

		
		try {
			BufferedImage bImg = (BufferedImage)ImageIO.read(new File("res/start/bg_main.jpg"));
			Image newImage = bImg.getScaledInstance(800, 475, Image.SCALE_SMOOTH);
			BackImage = new ImageIcon(newImage);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		btnL = new LoginListener();
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(40f);
			font2 = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/BMHANNAPro.ttf")).deriveFont(20f);
		} catch (Exception e) {
			e.printStackTrace();
			font = font2 = new Font("Arial", Font.BOLD, 30);
		}

		loginPanel = new JPanel();
		loginPanel.setLayout(null);
		loginPanel.setBounds(250, 250, 420, 200);
		loginPanel.setOpaque(false);
		add(loginPanel);
		
		lblName = new JLabel("이름");
		lblName.setFont(font2);
		lblName.setBounds(10, 0, 50, 50);
		loginPanel.add(lblName);
		
		lblSchool = new JLabel("학교");
		lblSchool.setFont(font2);
		lblSchool.setBounds(10, 50, 50, 50);
		loginPanel.add(lblSchool);
		
		inputName = new JTextField();
		inputName.setFont(font2);
		inputName.setBounds(60, 10, 220, 30);
		inputName.addActionListener(btnL);
		loginPanel.add(inputName);
		
		inputSchool = new JTextField();
		inputSchool.setFont(font2);
		inputSchool.setBounds(60, 60, 150, 30);
		inputSchool.addActionListener(btnL);
		loginPanel.add(inputSchool);
		
		lblUniversity = new JLabel("대학교");
		lblUniversity.setFont(font2);
		lblUniversity.setBounds(225, 60, 100, 30);
		loginPanel.add(lblUniversity);
		
		btnLogin = new ImageButton();
		btnLogin.setNormalImage("res/start/login_btn_normal.png");
		btnLogin.setPressedImage("res/start/login_btn_pressed.png");
		btnLogin.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		btnLogin.setBounds(90, 100, 140, 50);
		btnLogin.addActionListener(btnL);
		loginPanel.add(btnLogin);
	}//StartScene()
	
	public void paintComponent(Graphics page)
	{
		super.paintComponent(page);
		page.drawImage(BackImage.getImage(), 0, 0, this);
	} //paintComponent - 배경 이미지 그리기
	
	private class LoginListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			Object obj = event.getSource();
			int maxLimit = 6;
			int minLimit = 1;
			// 1 글자 이상 6글자 이하 제한
			if(obj == btnLogin || obj == inputName || obj == inputSchool)
			{
				if(inputName.getText().length() < minLimit || inputSchool.getText().length() < minLimit)
				{
					SceneManager.getInstance().addAlert(new AlertDialog("아무것도 입력되지 않았습니다"));
				} //글자 수 최소 제한
				else if(inputName.getText().length() > maxLimit )
				{
					SceneManager.getInstance().addAlert(new AlertDialog("이름이 6자를 초과하였습니다."));
				} //글자 수 최대 제한
				else if(inputSchool.getText().length() > 3)
				{
					SceneManager.getInstance().addAlert(new AlertDialog("학교명이 3자를 초과하였습니다."));
				} //학교 글자 수 제한
				else if(!inputName.getText().matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-R]*"))
				{
					SceneManager.getInstance().addAlert(new AlertDialog("특수 문자는 입력할 수 없습니다."));
				} //inputName 에 특수문자 입력 시 예외처리
				
				else if(!inputSchool.getText().matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-R]*"))
				{
					SceneManager.getInstance().addAlert(new AlertDialog("특수 문자는 입력할 수 없습니다."));
				} //inputSchool 에 특수문자 입력 시 예외처리
				else
				{
					SceneManager.getInstance().setUserName(inputName.getText());
					SceneManager.getInstance().setUserSchool(inputSchool.getText());
					
					SceneManager.getInstance().changeState(SceneManager.SceneType.TUTORIAL); //튜토리얼 화면으로 넘어감
					
				} //정상 로그인
			} //로그인 버튼 or enter
		} //actionPerformed()
	} //LoginListener class
} //StartScene class

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
	
	private JTextField inputName; //»ç¿ëÀÚ ÀÌ¸§ ÀÔ·Â
	private JTextField inputSchool; //»ç¿ëÀÚ ÇÐ±³ ÀÔ·Â
	private ImageButton btnLogin; //·Î±×ÀÎ ¹öÆ°
	
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
		
		lblName = new JLabel("ÀÌ¸§");
		lblName.setFont(font2);
		lblName.setBounds(10, 0, 50, 50);
		loginPanel.add(lblName);
		
		lblSchool = new JLabel("ÇÐ±³");
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
		
		lblUniversity = new JLabel("´ëÇÐ±³");
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
	} //paintComponent - ¹è°æ ÀÌ¹ÌÁö ±×¸®±â
	
	private class LoginListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			Object obj = event.getSource();
			int maxLimit = 6;
			int minLimit = 1;
			
			if(obj == btnLogin || obj == inputName || obj == inputSchool)
			{
				if(inputName.getText().length() < minLimit || inputSchool.getText().length() < minLimit)
				{
					SceneManager.getInstance().addAlert(new AlertDialog("¾Æ¹«°Íµµ ÀÔ·ÂµÇÁö ¾Ê¾Ò½À´Ï´Ù"));
				} //±ÛÀÚ ¼ö ÃÖ¼Ò Á¦ÇÑ
				else if(inputName.getText().length() > maxLimit )
				{
					SceneManager.getInstance().addAlert(new AlertDialog("ÀÌ¸§ÀÌ 6ÀÚ¸¦ ÃÊ°úÇÏ¿´½À´Ï´Ù."));
				} //±ÛÀÚ ¼ö ÃÖ´ë Á¦ÇÑ
				else if(inputSchool.getText().length() > 3)
				{
					SceneManager.getInstance().addAlert(new AlertDialog("ÇÐ±³¸íÀÌ 3ÀÚ¸¦ ÃÊ°úÇÏ¿´½À´Ï´Ù."));
				} //ÇÐ±³ ±ÛÀÚ ¼ö Á¦ÇÑ
				else if(!inputName.getText().matches("[0-9|a-z|A-Z|¤¡-¤¾|¤¿-¤Ó|°¡-ÆR]*"))
				{
					SceneManager.getInstance().addAlert(new AlertDialog("Æ¯¼ö ¹®ÀÚ´Â ÀÔ·ÂÇÒ ¼ö ¾ø½À´Ï´Ù."));
				} //inputName ¿¡ Æ¯¼ö¹®ÀÚ ÀÔ·Â ½Ã ¿¹¿ÜÃ³¸®
				
				else if(!inputSchool.getText().matches("[0-9|a-z|A-Z|¤¡-¤¾|¤¿-¤Ó|°¡-ÆR]*"))
				{
					SceneManager.getInstance().addAlert(new AlertDialog("Æ¯¼ö ¹®ÀÚ´Â ÀÔ·ÂÇÒ ¼ö ¾ø½À´Ï´Ù."));
				} //inputSchool ¿¡ Æ¯¼ö¹®ÀÚ ÀÔ·Â ½Ã ¿¹¿ÜÃ³¸®
				else
				{
					SceneManager.getInstance().setUserName(inputName.getText());
					SceneManager.getInstance().setUserSchool(inputSchool.getText());
					
					SceneManager.getInstance().changeState(SceneManager.SceneType.TUTORIAL); //Æ©Åä¸®¾ó È­¸éÀ¸·Î ³Ñ¾î°¨
					
				} //Á¤»ó ·Î±×ÀÎ
			} //·Î±×ÀÎ ¹öÆ° or enter
		} //actionPerformed()
	} //LoginListener class
} //StartScene class

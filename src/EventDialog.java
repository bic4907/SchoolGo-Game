import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class EventDialog extends JPanel {

	private ImageIcon img;
	private String message;
	private String userName;
	private String[] messageList;
	private JLabel lblMessage;
	private JLabel lblName;
	private ImageButton btnYes, btnNo;
	private btnListener btnL;
	private Object obj;
	private int eventKind;
	
	public EventDialog(int random) {
		super();
		
		userName = SceneManager.getInstance().getUserName() + " 총장님! ";
		
		messageList = new String[7];

		messageList[0] = "외부에 행사를 위해 대양홀을 빌려주시겠습니까?"; //yes : 돈 증가, 행복도 감소
		messageList[1] = "주차비를 올리시겠습니까?"; //yes : 돈 증가, 행복도 감소
		messageList[2] = "식중독 피해 보상을 하시겠습니까?"; //yes : 돈 감소, 행복도 증가
		messageList[3] = "교환 학생 프로그램을 실시하시겠습니까?"; //yes : 돈 감소, 능력 증가
		messageList[4] = "해외 학회에 참가하시겠습니까?"; //yes : 돈 감소, 능력 증가
		messageList[5] = "SW중심대학에 선정되시겠습니까?"; //yes : 돈 증가, 능력 증가, 행복도 증가
		messageList[6] = "학교 평가단이 왔습니다. 마중을 나가시겠습니까?"; //yes : 돈 감소, 능력 감소, 행복도 감소
		
		this.eventKind = random;
		
		try {
			Image img = ImageIO.read(new File("res/event/" + random + ".png")); //랜덤으로 이미지 결정
			BufferedImage bImg = (BufferedImage)img;
			Image newImage = bImg.getScaledInstance(400, 200, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newImage);
			this.setImg(icon);
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.setMessage(messageList[random]); //랜덤으로 선택된 이미지에 맞는 메시지 결정

		this.setBackground(new Color(255, 200, 0));
		this.setOpaque(true);
		
		render();
	}

	public void render() {
		this.setPreferredSize(new Dimension(500, 320));
		this.setLayout(null);
		
		btnL = new btnListener();
		
		JLabel lblIcon = new JLabel();
		lblIcon.setIcon(this.img);
		lblIcon.setBounds(50, 0, 500, 200);

		this.add(lblIcon);

		lblMessage = new JLabel();
		lblMessage.setText(this.message);
		lblMessage.setBounds(10, 230, 500, 30);
		lblMessage.setForeground(Color.black);
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblName = new JLabel();
		switch(eventKind) //이벤트 종류에 따라 다르게 설정
		{
		case 0 : lblName.setText(this.userName + "콘서트가 열립니다!");
			break;
		case 1 : lblName.setText(this.userName + "주차 요금이 너무 낮습니다");
			break;
		case 2 : lblName.setText(this.userName + "식중독이 발생했습니다!");
			break;
		case 3 : lblName.setText(this.userName + "해외 학교에서 제안이 왔습니다!");
			break;
		case 4 : lblName.setText(this.userName + "학회에 초대받았습니다!");
			break;
		case 5 : lblName.setText(this.userName + "축하합니다!");
			break;
		case 6 : lblName.setText(this.userName + "큰일났습니다!");
			break;
		default : lblName.setText(this.userName);	
		}
		lblName.setBounds(10, 200, 500, 30);
		lblName.setForeground(Color.black);
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setFont(new Font("Dotum", Font.BOLD, 15));
		this.add(lblName);
		
		btnYes = new ImageButton();
		btnYes.setNormalImage("res/event/yes_btn_normal.png");
		btnYes.setPressedImage("res/event/yes_btn_pressed.png");
		btnYes.setBounds(170, 270, 80, 30);
		btnYes.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		btnYes.addActionListener(btnL);
		this.add(btnYes);
		
		btnNo = new ImageButton();
		btnNo.setNormalImage("res/event/no_btn_normal.png");
		btnNo.setPressedImage("res/event/no_btn_pressed.png");
		btnNo.setBounds(270, 270, 80, 30);
		btnNo.setClickSound(SoundPlayer.SoundList.ButtonSound01);
		btnNo.addActionListener(btnL);
		this.add(btnNo);
		
		if(this.message.length() > 10) {
			lblMessage.setFont(new Font("Dotum", Font.BOLD, 12));
		} else {
			lblMessage.setFont(new Font("Dotum", Font.BOLD, 15));
		}
		
		this.add(lblMessage);
	}
	
	public String getMessage() {
		return message;
	}
	
	public String[] getMessageList() {return messageList;}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setlblMessage(String message)
	{
		lblMessage.setText(message);
	}
	
	public void setMessage(int random)
	{
		int i;
		
		for(i =0;i<7;i++)
		{
			if(random == i)
			{
				break;
			}
		}
		this.message = messageList[i];
	}

	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	public void setImg(String path) {
		try {
			Image img = ImageIO.read(new File(path));
			BufferedImage bImg = (BufferedImage)img;
			Image newImage = bImg.getScaledInstance(400,200, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newImage);
			this.setImg(icon);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object getObj() {return obj;}
	public Object getBtnYes() {return btnYes;}
	public Object getBtnNo() {return btnNo;}
	
	public void SelectedHandling()
	{
		btnYes.doClick(); //선택되지 않을 경우 강제 선택
	} //SelectedHandling()
	
	private class btnListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			
			obj = event.getSource();
			GameManager mgr = SceneManager.getInstance().getGameManager();
			
			if(eventKind == 0)
			{
				if(obj == btnYes)
				{
					mgr.setMoney(mgr.getMoney() + 100000);
					SceneManager.getInstance().addAlert(new AlertDialog("대양홀을 빌려주었습니다"));
				}
				else if(obj == btnNo)
				{
					mgr.applyEffect(new Effect(0, 0, 0, 0, 10, 10, 10, 10, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("대양홀은 평화롭습니다"));
				}
			} //대양홀 이벤트
			else if(eventKind == 1)
			{
				if(obj == btnYes)
				{
					mgr.setMoney(mgr.getMoney() + 100000);
					mgr.applyEffect(new Effect(0, 0, 0, 0, -10, -10, -10, -10, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("주차비가 올랐습니다"));
				}
				else if(obj == btnNo)
				{
					System.out.println("NO!");
					mgr.applyEffect(new Effect(0, 0, 0, 0, 5, 5, 5, 5, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("주차비가 동결되었습니다"));
				}
			} //주차비 이벤트
			else if(eventKind == 2)
			{
				if(obj == btnYes)
				{
					mgr.setMoney(mgr.getMoney() - 50000);
					mgr.applyEffect(new Effect(0, 0, 0, 0, 10, 10, 10, 10, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("식중독 피해 보상을 하였습니다"));
				}
				else if(obj == btnNo)
				{
					mgr.applyEffect(new Effect(0, 0, 0, 0, -10, -10, -10, -10, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("항의 전화가 빗발치고 있습니다"));
				}
			} //식중독 이벤트
			else if(eventKind == 3)
			{
				if(obj == btnYes)
				{
					mgr.setMoney(mgr.getMoney() - 50000);
					mgr.applyEffect(new Effect(10, 10, 10, 10, 0, 0, 0, 0, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("교환 학생이 많아졌습니다"));
				}
				else if(obj == btnNo)
				{
					mgr.applyEffect(new Effect(0, 0, 0, 0, -5, -5, -5, -5, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("100% 한국인 학생입니다"));
				}
			} //교환 학생 이벤트
			else if(eventKind == 4)
			{
				if(obj == btnYes)
				{
					mgr.setMoney(mgr.getMoney() + 50000);
					mgr.applyEffect(new Effect(10, 10, 10, 10, 0, 0, 0, 0, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("해외 학회에 참석하였습니다"));
				}
				else if(obj == btnNo)
				{
					mgr.applyEffect(new Effect(0, 0, 0, 0, -5, -5, -5, -5, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("해회 학회에 불참했습니다"));
				}
			} //해외 학회 이벤트
			else if(eventKind == 5)
			{
				if(obj == btnYes)
				{
					mgr.setMoney(mgr.getMoney() + 100000);
					mgr.applyEffect(new Effect(0, 0, 10, 0, 0, 0, 10, 0, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("SW중심대학에 선정되었습니다"));
				}
				else if(obj == btnNo)
				{
					mgr.applyEffect(new Effect(0, 0, 0, 0, -2, -2, -2, -2, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("SW중심대학은 다음 기회에"));
				}
			} //SW중심대학 이벤트
			else if(eventKind == 6)
			{
				if(obj == btnNo)
				{
					SceneManager.getInstance().addAlert(new AlertDialog("당신은 기회주의자.."));
				}
				else if(obj == btnYes)
				{
					mgr.applyEffect(new Effect(0, 0, 0, 0, -10, -10, -10, -10, "", ""));
					SceneManager.getInstance().addAlert(new AlertDialog("평가단의 표정이 안좋습니다"));
				}
			} //학교 평가단 이벤트
			SceneManager.getInstance().getGameManager().setOccur(false);
		} //actionPerformed()
	} //btnListener class
} //EventDialog class

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")

public class HumanWalking extends JLabel{

	private BufferedImage img; // chipsetimg
	public int x, y, nJudge; // point, 방향전환 포인트
	private int moveStatus; // 이미지 불러오는 기준
	private int count; // 사진변환 시간 측정용
	private int rint; // subMoveStatus
	private int humanStatus, humanTimer; // 사람 event 상태 표시, 꺼지는 타이머
	private ImageIcon lblIcon[][] = new ImageIcon[3][4]; // 임시저장용 배열
	private Thread th;
	

	public JPanel rEvent; // 이벤트 표시용 패널
	private ImageButton btnEvent, btnEvent2; // 책, 하트 이벤트
	
	private HashMap<String, Image> images; // 사진 저장, 호출용
	
	public boolean attached = false; // 이미 생성되어있으면 다시 생성하지 않기위한 flag
	
	public HumanWalking(int nType)
	{
		if (nType>9)
			nType -= 10; // 10이상 수가 들어오면 -10 (중복정보 생성방지) 
		this.count = 0;
		//학교 입구에 사람 추가, 시작좌표
		this.x = 330;
		this.y = 350;
		this.moveStatus = 2;
		this.humanStatus = 0;
		this.humanTimer = 0;
		this.rint = 0;
		
		this.images = new HashMap<String, Image>(); 
		
		rEvent = new JPanel(); // 버튼넣는 패널
		rEvent.setBounds(x,y-12,24,30);
		rEvent.setOpaque(false);
		rEvent.setLayout(null);
		rEvent.setVisible(true);
		
		btnEvent = new ImageButton(); // 랜덤1으로 만들어지는 버튼
		btnEvent.setNormalImage("res/lblicon/closedBook1.png");
		btnEvent.setPressedImage("res/lblicon/openedBook1.png");
		btnEvent.setVisible(false);
		btnEvent.setBounds(0,0,24,30);
		
		
		btnEvent2 = new ImageButton(); // 랜덤2으로 만들어지는 버튼
		btnEvent2.setNormalImage("res/lblicon/heart.png");
		btnEvent2.setPressedImage("res/lblicon/heartClicked.png");
		btnEvent2.setVisible(false);
		btnEvent2.setBounds(0,0,24,30);
		
		btnEvent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				SceneManager.getInstance().getGameManager().applyEffect(new Effect(1, 1, 1, 1, 0, 0, 0, 0, "", ""));
		    	System.out.println("지식증가");
				humanStatus = 0;
		    	humanTimer = 0;
		    	btnEvent.setVisible(false);
			} // 버튼클릭시 버튼 없어지게
		}); // btnEvent에 Listener 달기
		rEvent.add(btnEvent); // 패널에 버튼 추가
		
		btnEvent2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				SceneManager.getInstance().getGameManager().applyEffect(new Effect(0, 0, 0, 0, -1, -1, -1, -1, "", ""));
				System.out.println("행복도증가");
				humanStatus = 0;
		    	humanTimer = 0;
		    	btnEvent2.setVisible(false);
			} // 버튼클릭시 버튼 없어지게
		}); // btnEvent2에 Listener 달기
		rEvent.add(btnEvent2); // 패널에 추가
		
		
		
		try {
			img = ImageIO.read(new File("res/character/chipset00"+nType+".png")); // 자르기전 이미지 불러오기
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // try catch
		for(int i = 0;i<3;i++)
		{
			for(int j = 0;j<4;j++)
			{
				try { // 이미지 12등분 3x4배열에 저장
					if(!images.containsKey("temp"+nType+"_"+i+"_"+j+".png"))
						images.put("temp"+nType+"_"+i+"_"+j+".png", img.getSubimage((i*24), (j*32), 24, 32));
					// hashmap에 저장되어있지 않으면 추가하기
					
					Image tempimg1 = images.get("temp"+nType+"_"+i+"_"+j+".png"); // image 임시 저장
					lblIcon[i][j] = new ImageIcon(tempimg1);  // ImageIcon으로 바꿔서 저장
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // try catch
			} // for
		} // for
		setIcon(lblIcon[0][0]); // 초기설정
		start(); // 시작
	} // HumanWalking()

	public void start()
	{
		th = new MyThread();
		th.start();
	} // start()

	private class MyThread extends Thread
	{
		@Override
		public void run()
		{
		while(true)
		{
			try 
			{
				JudgePoint(); // 좌표이동후
				MoveImage(); // 사진을 바꿀지 판단
				setBounds(x,y,24,32);
				if(humanStatus == 1) // 책생성되어있을때
				{
					humanTimer++;
					if(humanTimer == 200) // 책이 사라지는 시간
					{
						humanStatus = 0;
						humanTimer = 0;
						rEvent.setVisible(false);
						btnEvent.setVisible(false);
					}
				}
				else if(humanStatus == 2) // 하트가 생성되어 있을때
				{
					humanTimer++;
					if(humanTimer == 200) // 하트가 사라지는 시간
					{
						humanStatus = 0;
						humanTimer = 0;
						rEvent.setVisible(false);
						btnEvent2.setVisible(false);
					}
				}
				else if(humanStatus == 0) // 이벤트가 없을때
				{
					if((int)(Math.random()*2000) == 1) // 랜덤으로 책생성
					{
						humanStatus = 1;
						rEvent.setVisible(true);
						btnEvent.setVisible(true);
					}
					if((int)(Math.random()*2000) == 2) // 하트
					{
						humanStatus = 2;
						rEvent.setVisible(true);
						btnEvent2.setVisible(true);
					}
				}
				Thread.sleep(20);
				count++; // 시간
			}catch(Exception e) {}
		} // while
	} // run
	} // MyThread
	
	public void MoveImage()
	{	// count에 따라 사진 바꾸기
			if ( count / 10 % 4 == 0 )
				{ 
					setIcon(lblIcon[0][moveStatus]);
				}
			else  if(count/10%4 == 1)
				{ 
					setIcon(lblIcon[1][moveStatus]);
				}
			else  if(count/10%4 == 2)
				{  
					setIcon(lblIcon[2][moveStatus]);
				}
			else  if(count/10%4 == 3)
				{ 
					setIcon(lblIcon[1][moveStatus]);
				}
	} // MoveImage
	
	public void JudgePoint()
	{ // 길만들기, 갈림길마다 랜덤으로 방향 설정
		if(x == 330&& y == 315)
		{
			nJudge = (int)(Math.random()*3);
			if(nJudge == 0)
				rint = 0;
			else if(nJudge == 1)
				rint = 1;
			else
				rint = 3;
		}
		else if(x == 330 && y == 350)
			rint = 0;
		else if(x == 500 && y == 315)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 0;
			else
				rint = 2;
		}
		else if(x == 500 && y == 285)
		{
			nJudge = (int)(Math.random()*3);
			if(nJudge == 0)
				rint = 0;
			else if(nJudge == 1)
				rint = 1;
			else 
				rint = 3;
		}
		else if(x == 600 && y == 285)
			rint = 2;
		else if(x == 500 && y == 195)
		{
			nJudge = (int)(Math.random()*3);
			if(nJudge == 0)
				rint = 1;
			else if (nJudge == 1)
				rint = 2;
			else 
				rint = 3;
		}
		else if(x == 520 && y == 195)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 0;
			else
				rint = 2;
		}
		else if(x == 520 && y == 135)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 1;
			else rint = 3;
		}
		else if(x == 620 && y == 135)
		{
			rint = 2;
		}
		else if(x == 380 && y == 195)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 0;
			else 
				rint = 3;
		}
		else if(x == 380 && y == 140)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 1;
			else
				rint = 2;
		}
		else if(x == 245 && y == 140)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 1;
			else
				rint = 3;
		}
		else if(x == 245 && y == 170)
		{
			nJudge = (int)(Math.random()*3);
			if(nJudge == 0)
				rint = 0;
			else if(nJudge == 1)
				rint = 1;
			else
				rint = 2;
		}
		else if(x == 150 && y == 170)
			rint = 3;
		else if(x == 245 && y == 265)
		{
			nJudge = (int)(Math.random()*3);
			if(nJudge == 0)
				rint = 0;
			else if(nJudge == 1)
				rint = 2;
			else if(nJudge == 2)
				rint = 3;
		}
		else if(x == 330 && y == 265)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 1;
			else 
				rint = 2;
		}
		else if(x == 210 && y == 265)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 1;
			else
				rint = 3;
		}
		else if(x == 210 && y == 345)
		{
			nJudge = (int)(Math.random()*2);
			if(nJudge == 0)
				rint = 0;
			else
				rint = 2;
		}
		else if(x == 150 && y == 345)
			rint = 3;
		
		
		MacroProcess();
	} // JudgePoint 각 지점마다 랜덤방향설정
	
	public void MacroProcess()
	{
		if(rint == 0) // 위로 
		{
			y-=1;
			moveStatus = 0;
		}
		
		if(rint == 1) // 아래로
		{
			y+=1;
			moveStatus = 2;
		}
			
		if(rint == 2) // 왼쪽으로
		{
			x-=1;
			moveStatus = 3;
		}
			
		if(rint == 3) // 오른쪽으로
		{
			x+=1;
			moveStatus = 1;
		}
		rEvent.setLocation(x,y-12); // 캐릭터 이동한만큼 이벤트패널도 이동
	}
	

	public JPanel getEventPanel()
	{
		return rEvent;
	} // 패널 정보 받기
	
	
	
}

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
	public int x, y, nJudge; // point 
	private int moveStatus;
	private int count;
	private int rint; // subMoveStatus
	private int humanStatus, humanTimer; //
	private ImageIcon lblIcon[][] = new ImageIcon[3][4]; // 임시저장용 배열
	private Thread th;
	

	public JPanel rEvent;
	private ImageButton btnEvent, btnEvent2;
	
	private HashMap<String, Image> images;
	
	public boolean attached = false;
	
	public HumanWalking(int nType)
	{
		if (nType>9)
			nType -= 10; // 10이상 수가 들어오면 -10 (중복정보 생성방지) 
		this.count = 0;
		//학교 입구에 사람 추가
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
		rEvent.add(btnEvent);
		
		btnEvent2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				SceneManager.getInstance().getGameManager().applyEffect(new Effect(0, 0, 0, 0, -1, -1, -1, -1, "", ""));
				System.out.println("행복도증가");
				humanStatus = 0;
		    	humanTimer = 0;
		    	btnEvent2.setVisible(false);
			} // 버튼클릭시 버튼 없어지게
		}); // btnEvent2에 Listener 달기
		rEvent.add(btnEvent2);
		
		
		
		try {
			img = ImageIO.read(new File("res/character/chipset00"+nType+".png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // try catch
		for(int i = 0;i<3;i++)
		{
			for(int j = 0;j<4;j++)
			{
				try { // 이미지 12등분
					if(!images.containsKey("temp"+nType+"_"+i+"_"+j+".png"))
						images.put("temp"+nType+"_"+i+"_"+j+".png", img.getSubimage((i*24), (j*32), 24, 32));
					
					
					
					//ImageIO.write(img.getSubimage((i*24), (j*32), 24, 32), "png", new File("temp"+nType+"_"+i+"_"+j+".png"));
					Image tempimg1 = images.get("temp"+nType+"_"+i+"_"+j+".png");
					lblIcon[i][j] = new ImageIcon(tempimg1); 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // try catch
			} // for
		} // for
		setIcon(lblIcon[0][0]);
		start();
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
				MoveImage();
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
				else if(humanStatus == 2)
				{
					humanTimer++;
					if(humanTimer == 200) // 책이 사라지는 시간
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
	{	
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
	
	/*
	public void Process()
	{
		
		if(count%100 == 0)
		{
			rint = (int)(Math.random()*4);
		}// 갈방향 정하기 (count 100마다)
		
		if(rint == 0)
		{
			y-=1;
			if(y<100)
				rint = 1; // 밖으로 나가면 재설정
			moveStatus = 0;
		}
		
		if(rint == 1)
		{
			y+=1;
			if(y>350)
				rint = 0; // 밖으로 나가면 재설정
			moveStatus = 2;
		}
			
		if(rint == 2)
		{
			x-=1;
			if(x<200)
				rint = 3; // 밖으로 나가면 재설정
			moveStatus = 3;
		}
			
		if(rint == 3)
		{
			x+=1;
			if(x>600)
				rint = 2; // 밖으로 나가면 재설정
			moveStatus = 1;
		}
	} // process
	*/
	
	public void JudgePoint()
	{
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
		
		/*
		if(count%100 == 0)
		{
			rint = (int)(Math.random()*4);
		}// 갈방향 정하기 (count 100마다)
		*/
		
		if(rint == 0)
		{
			y-=1;
		//	if(y<100)
		//		rint = 1; // 밖으로 나가면 재설정
			moveStatus = 0;
		}
		
		if(rint == 1)
		{
			y+=1;
		//	if(y>350)
		//		rint = 0; // 밖으로 나가면 재설정
			moveStatus = 2;
		}
			
		if(rint == 2)
		{
			x-=1;
		//	if(x<200)
		//		rint = 3; // 밖으로 나가면 재설정
			moveStatus = 3;
		}
			
		if(rint == 3)
		{
			x+=1;
		//	if(x>600)
		//		rint = 2; // 밖으로 나가면 재설정
			moveStatus = 1;
		}
		rEvent.setLocation(x,y-12);
		//rEvent2.setLocation(x, y-12);
	}
	

	public JPanel getEventPanel()
	{
		return rEvent;
	} // 패널 정보 받기
	
	
	
}

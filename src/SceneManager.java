import java.awt.Color;
import java.util.ArrayDeque;

public class SceneManager {

	
	/* Singleton 클래스 */
	private static SceneManager instance;
	public static SceneManager getInstance() {
		if(instance == null) {
			instance = new SceneManager();
		}
		return instance;	
	}
	
	// 화면 상태 정의
	public enum SceneType {
		START, TUTORIAL, INGAME, BUILD, RESULT
	}
	
	private SceneType currentState;
	
	// 알림을 저장해놨다가 하나씩 풀어주는 큐와 쓰레드
	private ArrayDeque<AlertDialog> alertQueue;
	private AlertThread alertThd;
	AlertDialog ad;
	
	// 특수 이벤트를 저장해놨다가 하나씩 풀어주는 큐와 쓰레드
	private ArrayDeque<EventDialog> eventQueue;
	private EventThread eventThd;
	private String userName, userSchool;
	
	
	public SceneManager() {
		alertQueue = new ArrayDeque<AlertDialog>();
		eventQueue = new ArrayDeque<EventDialog>();
	}
	
	public void changeState(SceneType type) {
		if(currentState == type) return;
		// 호출된 Scene의 type을 가지고 새로운 객체를 생성하고 붙여줌, 객체삭제는 MainFrame에서...
		if(type == SceneType.START) {
			StartScene scene = new StartScene();
			MainFrame.getInstance().setPanel(scene);
		} else if(type == SceneType.TUTORIAL) {
			TutorialScene scene = new TutorialScene();
			MainFrame.getInstance().setPanel(scene);
		} else if(type == SceneType.INGAME) {
			IngameScene scene = new IngameScene();
			MainFrame.getInstance().setPanel(scene);
		} else if(type == SceneType.BUILD) {
			BuildScene scene = new BuildScene();
			MainFrame.getInstance().setPanel(scene);
		} else if(type == SceneType.RESULT) {
			ResultScene scene = new ResultScene();
			MainFrame.getInstance().setPanel(scene);
		}
		MainFrame.getInstance().getPanel().repaint();

	}

	private GameManager gm;
	
	public GameManager getGameManager() {
		if(gm == null) {
			gm = new GameManager(); // 게임매니저가 있으면 인스턴스 반환, 없으면 생성해서 보내줌
		}
		return gm;
	}
	
	public void resetGameManager() { // 게임매니저 인스턴스 삭제, 다음 getGameManager에서 새로 생성하여 보내줄 것임
		gm = null;
	}
	
	// 알림이 추가된다면 큐에 담아놓음
	public void addAlert(AlertDialog ad) {
		this.alertQueue.add(ad);
		
		// 쓰레드가 돌고있지 않으면 시작, 큐가 비면 그땐 쓰레드를 
		if(this.alertThd == null || (this.alertThd != null && !this.alertThd.isAlive())) {
			this.alertThd = new AlertThread();
			this.alertThd.start();
		}
	}
	
	// 알림이 있다면 하나씩 보여주는 쓰레드
	private class AlertThread extends Thread {
		
		@Override
		public void run() {
			SceneManager sm = SceneManager.getInstance();
			
			while(!sm.alertQueue.isEmpty()) { // 만약 알림큐가 비었을 경우 쓰레드를 종료 시킨다.
				ad = sm.alertQueue.remove(); // 알림을 하나 빼서
				ad.setBounds(240,160, ad.getPreferredSize().width, ad.getPreferredSize().height);
				ad.setBackground(new Color(0, 0, 0, 150));
				ad.setVisible(true); // 보여준다.
				
				MainFrame.getInstance().getPanel().add(ad); // 지금 보여주고 있는 패널 위에 붙이고
				MainFrame.getInstance().getPanel().setComponentZOrder(ad, 0); // 알림창 우선순위 설정
				MainFrame.getInstance().getPanel().repaint(); // 다시 그려준다

				// 0.7초를 기다린 후에
				try {
					sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				MainFrame.getInstance().getPanel().remove(ad); // 삭제하고
				MainFrame.getInstance().getPanel().repaint(); // 다시 그래준다
				
				// 다음 알림을 보여주기 전에 0.1ch eorl
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public void addEvent(EventDialog ev) {
		this.eventQueue.add(ev);
		
		if(this.eventThd == null || (this.eventThd != null && !this.eventThd.isAlive())) {
			this.eventThd = new EventThread();
			this.eventThd.start();
		}
	} //addEvent()
	
	public String getUserName() {return userName;}
	public String getUserSchool() {return userSchool;}
	public void setUserName(String userName) {this.userName = userName;}
	public void setUserSchool(String userSchool) {this.userSchool = userSchool;}	
	
	// 특수 이벤트를 하나씩 보여주는 쓰레드
	private class EventThread extends Thread {
		
		@Override
		public void run() {
			SceneManager sm = SceneManager.getInstance();
			int count = 5; // 5 기다려줄 예정임
			while(!sm.eventQueue.isEmpty()) {
				EventDialog ev = sm.eventQueue.remove(); // 만약 이벤트큐가 없을 경우 쓰레드를 종료시킨다.
				ev.setBounds(150, 100, ev.getPreferredSize().width, ev.getPreferredSize().height);
				ev.setVisible(true);
				
				MainFrame.getInstance().getPanel().add(ev); // 현재 보고 있는 패널에다가 위에 갖다 붙임.
				MainFrame.getInstance().getPanel().setComponentZOrder(ev, 0); // 패널 우선순위 설정
				MainFrame.getInstance().getPanel().repaint(); // 다시 그린다.

				while(count > 0)
				{
					try {
						ev.setlblMessage(ev.getMessage() + " " + count + "초 후 자동 YES 선택");
						count--;
						
						if(ev.getObj() == ev.getBtnYes() || ev.getObj() == ev.getBtnNo())
						{
							count = -1;
						}
						else
						sleep(1000); // 5초를 기다리기 위해 count를 1씩 증가시키면서 5번 기다림
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(count == 0) //아무것도 선택되지 않았다면
				{
					ev.SelectedHandling();
				}
				MainFrame.getInstance().getPanel().remove(ev); // 이벤트가 끝난 경우 패널에서 삭제
				MainFrame.getInstance().getPanel().repaint(); // 다시 그려준다
				// 다음이벤트가 그려지기전까지 0.1초 대기
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} //run()
		
	} //EventThread class	
	
}

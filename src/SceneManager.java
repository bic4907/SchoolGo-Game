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
	
	public enum SceneType {
		START, TUTORIAL, INGAME, BUILD, RESULT
	}
	
	private SceneType currentState;
	
	private ArrayDeque<AlertDialog> alertQueue;
	private AlertThread alertThd;
	AlertDialog ad;
	
	private ArrayDeque<EventDialog> eventQueue;
	private EventThread eventThd;
	private String userName, userSchool;
	
	
	public SceneManager() {
		alertQueue = new ArrayDeque<AlertDialog>();
		eventQueue = new ArrayDeque<EventDialog>();
	}
	
	public void changeState(SceneType type) {
		if(currentState == type) return;

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
			gm = new GameManager();
		}
		return gm;
	}
	
	public void resetGameManager() {
		gm = null;
	}

	public void addAlert(AlertDialog ad) {
		this.alertQueue.add(ad);
		
		if(this.alertThd == null || (this.alertThd != null && !this.alertThd.isAlive())) {
			this.alertThd = new AlertThread();
			this.alertThd.start();
		}
	}
	
	private class AlertThread extends Thread {
		
		@Override
		public void run() {
			SceneManager sm = SceneManager.getInstance();
			
			while(!sm.alertQueue.isEmpty()) {
				ad = sm.alertQueue.remove();
				ad.setBounds(240,160, ad.getPreferredSize().width, ad.getPreferredSize().height);
				ad.setBackground(new Color(0, 0, 0, 150));
				ad.setVisible(true);
				
				MainFrame.getInstance().getPanel().add(ad);
				MainFrame.getInstance().getPanel().setComponentZOrder(ad, 0);
				MainFrame.getInstance().getPanel().repaint();

				
				try {
					sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MainFrame.getInstance().getPanel().remove(ad);
				MainFrame.getInstance().getPanel().repaint();
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
	
	private class EventThread extends Thread {
		
		@Override
		public void run() {
			SceneManager sm = SceneManager.getInstance();
			int count = 5;
			while(!sm.eventQueue.isEmpty()) {
				EventDialog ev = sm.eventQueue.remove();
				ev.setBounds(150, 100, ev.getPreferredSize().width, ev.getPreferredSize().height);
				ev.setVisible(true);
				
				MainFrame.getInstance().getPanel().add(ev);
				MainFrame.getInstance().getPanel().setComponentZOrder(ev, 0);
				MainFrame.getInstance().getPanel().repaint();

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
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(count == 0) //아무것도 선택되지 않았다면
				{
					ev.SelectedHandling();
				}
				MainFrame.getInstance().getPanel().remove(ev);
				MainFrame.getInstance().getPanel().repaint();
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} //run()
		
	} //EventThread class	
	
}

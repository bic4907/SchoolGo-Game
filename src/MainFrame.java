import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame {
	
	/* Singleton 클래스 */
	private static MainFrame instance;
	public static MainFrame getInstance() {
		if(instance == null) {
			instance = new MainFrame();
		}
		return instance;	
	}
	
	private JFrame frame;
	private JPanel currentPanel;
	
	public MainFrame() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setResizable(false);
		frame.setTitle("SchoolGo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setPanel(JPanel panel) {
		// 만약 기존 패널이 붙어있었다면 제거
		if(currentPanel != null) {
			currentPanel.setVisible(false);
			frame.getContentPane().remove(currentPanel);
		}
		// 새로운 패널 추가
		frame.getContentPane().add(panel);

		// 현재 
		currentPanel = panel;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public JPanel getPanel() {
		return this.currentPanel;
	}
	
	
	public static void main(String[] args) {
		SceneManager.getInstance().changeState(SceneManager.SceneType.START);
	}
}

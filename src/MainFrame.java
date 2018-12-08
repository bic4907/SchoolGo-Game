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
		frame.setTitle("학교 고!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setPanel(JPanel panel) {
		if(currentPanel != null) {
			currentPanel.setVisible(false);
			frame.getContentPane().remove(currentPanel);
		}
		frame.getContentPane().add(panel);

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

package fr.xvirus9.xlauncher.launcher;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame {
	
	private static LauncherFrame instance;
	private LauncherPanel launcherPanel;
	
	public LauncherFrame(){
		this.setTitle("X-Launcher");
		this.setSize(1037, 584);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setIconImage(Swinger.getResource("icon.png"));
		this.setContentPane(launcherPanel = new LauncherPanel());
		
		WindowMover mover = new WindowMover(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		Launcher.X_DIR.mkdirs();
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/fr/xvirus9/xlauncher/launcher/ressources/");
		
		instance = new LauncherFrame();
	}
	
	public static LauncherFrame getInstance(){
		return instance;
	}
	
	public LauncherPanel getLauncherPanel(){
		return this.launcherPanel;
	}

}

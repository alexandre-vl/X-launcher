package fr.xvirus9.xlauncher.launcher;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
	
	private Image background = Swinger.getResource("background.png");
	
	private Saver saver = new Saver(new File(Launcher.X_DIR, "launcher.properties"));
	
	private JTextField usernameField = new JTextField(saver.get("username"));
	private JPasswordField passwordField = new JPasswordField(saver.get("password"));
	
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("play1.png"));
	private STexturedButton quitButton = new STexturedButton(Swinger.getResource("quit.png"));
	private STexturedButton hideButton = new STexturedButton(Swinger.getResource("hide.png"));

	private SColoredButton ramButton = new SColoredButton(Swinger.getTransparentWhite(100), Swinger.getTransparentWhite(175));

	public static SColoredBar progressBar = new SColoredBar(Swinger.getTransparentWhite(100), Swinger.getTransparentWhite(175));
	public static JLabel infoLabel = new JLabel("Clique sur Jouer !", SwingConstants.CENTER);

	private RamSelector ramSelector = new RamSelector(new File(Launcher.X_DIR, "ram.txt"));

	public LauncherPanel() {
		this.setLayout(null);
		
		usernameField.setForeground(Color.BLACK);
		usernameField.setFont(usernameField.getFont().deriveFont(20F));
		usernameField.setCaretColor(Color.BLACK);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(658, 274, 258, 50);
		this.add(usernameField);
		
		passwordField.setForeground(Color.BLACK);
		passwordField.setFont(usernameField.getFont());
		passwordField.setCaretColor(Color.BLACK);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setBounds(658, 370, 258, 50);
		this.add(passwordField);
		
		playButton.setBounds(655, 440, 250, 74);
		playButton.addEventListener(this);
		this.add(playButton);
		
		quitButton.setBounds(983, 5, 50, 50);
		quitButton.addEventListener(this);
		this.add(quitButton);
		
		hideButton.setBounds(933, 5, 50, 50);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		progressBar.setBounds(5, 560, 1027, 20);
		this.add(progressBar);
		
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(usernameField.getFont());
		infoLabel.setBounds(5, 530, 1027, 25);
		this.add(infoLabel);

		ramButton.addEventListener(this);
		ramButton.setBounds(882, 5, 50, 50);
		this.add(ramButton);

		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEvent(SwingerEvent e) {
		if(e.getSource() == playButton){
			setFieldsEnabled(false);
			
			if(usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer une adresse email et un mot de passe valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try{
						Launcher.auth(usernameField.getText(), passwordField.getText());
					} catch(AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de se connecter : " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}

					saver.set("username", usernameField.getText());
					saver.set("password", passwordField.getText());
					ramSelector.save();

					try{
						Launcher.updateMinecraftForge();

					} catch (Exception exception) {
						exception.printStackTrace();
					}
					try{
						Launcher.launch();
					} catch (LaunchException launchException) {
						launchException.printStackTrace();
					}

				}
			};
			t.start();
		}else if(e.getSource() == quitButton){
			System.exit(0);
		}else if(e.getSource() == hideButton){
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
		}else if(e.getSource() == ramButton){
			ramSelector.display();
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar() {
		return progressBar;
	}
	
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}

	public RamSelector getRamSelector() {
		return ramSelector;
	}
}

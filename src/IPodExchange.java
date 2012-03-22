import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class IPodExchange extends JFrame
{
	public JLabel lbl_source;
	public JTextField tf_source;
	public JButton btn_search;
	
	public JLabel lbl_progress;
	
	public JLabel lbl_destination;
	public JTextField tf_destination;
	public JButton btn_copy;
	
	public JLabel lbl_progress_2;
	public JLabel lbl_progress_3;
	
	public JButton btn_view_unknown_artist;
	
	public SearchFilesThread search_thread;
	public CopyFilesThread copy_thread;
	
	
	public String str_dest = System.getProperty("user.home") + "\\AG Software\\Music";
	
	public IPodExchange() {
		super("AG iPod Exchange");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 284);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		
		/* this.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				System.out.println(e.getKeyCode());
			}
			public void keyPressed(KeyEvent e) { System.out.println(e.getKeyCode()); }
			public void keyTyped(KeyEvent e) { 
				System.out.println(e.getKeyCode());
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			} }
		}); */
		
		final IPodExchange self = this;
		
		copy_thread = new CopyFilesThread(self);
		
		lbl_source = new JLabel("Enter a directory to copy from:");
		lbl_source.setBounds(10, 5, 200, 20);
		
		tf_source = new JTextField(20);
		tf_source.setText("G:/iPod_Control/Music");
		tf_source.setBounds(10, 30, getWidth() - 26, 30);
		lbl_source.setLabelFor(tf_source);
		
		
		btn_search = new JButton("Start");
		btn_search.setBounds(getWidth() - 117, 65, 100, 30);
		btn_search.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				File f = new File(tf_source.getText());
				search_thread = new SearchFilesThread(self, f);
				search_thread.start();
			}
		});
		
		lbl_progress = new JLabel("");
		lbl_progress.setBounds(10, 65, 200, 30);
		
		lbl_destination = new JLabel("Enter a directory to copy to:");
		lbl_destination.setBounds(10, 95, 200, 20);
		lbl_destination.setVisible(false);
		
		tf_destination = new JTextField(20);
		tf_destination.setText(str_dest);
		tf_destination.setBounds(10, 120, getWidth() - 26, 30);
		tf_destination.setVisible(false);
		lbl_destination.setLabelFor(tf_destination);
		
		btn_copy = new JButton("Copy");
		btn_copy.setBounds(getWidth() - 117, 155, 100, 30);
		btn_copy.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				//System.out.println(search_thread.g_files);
				copy_thread.start();
			}
		});
		btn_copy.setVisible(false);
		
		lbl_progress_2 = new JLabel("");
		lbl_progress_2.setBounds(10, 155, 200, 30);
		
		lbl_progress_3 = new JLabel("");
		lbl_progress_3.setBounds(10, 180, 200, 30);
		
		btn_view_unknown_artist = new JButton("View Unknown Artist Files");
		btn_view_unknown_artist.setBounds(10, 215, getWidth() - 27, 30);
		btn_view_unknown_artist.setVisible(false);
		btn_view_unknown_artist.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				new UnknownArtistFilesFrame(self, self);
			}
		});
		
		add(lbl_source);
		add(tf_source);
		add(btn_search);
		add(lbl_progress);
		add(lbl_destination);
		add(tf_destination);
		add(btn_copy);
		add(lbl_progress_2);
		add(lbl_progress_3);
		add(btn_view_unknown_artist);
		
		setVisible(true);
		
		//System.out.println(str_dest);
	}
	
	
	
	public static void main(String[] args) {
		new IPodExchange();
	}

}

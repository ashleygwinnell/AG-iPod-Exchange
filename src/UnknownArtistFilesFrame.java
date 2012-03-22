import java.awt.Color;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;



public class UnknownArtistFilesFrame extends JDialog {
	
	IPodExchange exchange;
	JList list;
	DefaultListModel model;
	
	Thread refresh;
	
	public UnknownArtistFilesFrame(IPodExchange ex, JFrame parent) {
		super(parent,"Unknown Artist Files");
		exchange = ex;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(450, 250);
		setLocationRelativeTo(parent);
		setResizable(false);
		setLayout(null);
		
		JLabel title = new JLabel("Unknown Artist files are added to \"Unknown Artist\" folder.");
		title.setBounds(10, 5, 400, 25);
		
		
		
		model = new DefaultListModel();
		
		for (int i = 0; i < exchange.copy_thread.unknown_artist_files.size(); i++) {
			File f = exchange.copy_thread.unknown_artist_files.get(i);
			//JLabel lbl = new JLabel();
			//lbl.setText();
			//lbl.setBounds(0, );
			model.addElement(f.getAbsolutePath());
		}
		
		list = new JList(model);
		
		JScrollPane pane = new JScrollPane(list);
		pane.setBounds(10, 30, getWidth() - 29, 175);
		pane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		
		add(title);
		add(pane);
		
		setVisible(true);
		
		refresh = new Thread(new Runnable() { 
			public void run() {
				while(true) {
					for (int i = 0; i < exchange.copy_thread.unknown_artist_files.size(); i++) {
						if (!model.contains(exchange.copy_thread.unknown_artist_files.get(i).getAbsolutePath())) {
							model.addElement(exchange.copy_thread.unknown_artist_files.get(i).getAbsolutePath());
							list.invalidate();
						}
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		refresh.start();
	
		
	}
	
	
	
	
	
	
	
}

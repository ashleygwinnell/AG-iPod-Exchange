import java.io.File;
import java.util.ArrayList;

public class SearchFilesThread extends Thread {
	
	public ArrayList<File> g_files;
	public File current_file;
	public IPodExchange exchange;
	public SearchFilesThread(IPodExchange ex, File start) {
		super();
		current_file = start;
		g_files = new ArrayList<File>();
		exchange = ex;
	}
	
	@Override
	public void run() {
		exchange.tf_source.setEnabled(false);
		exchange.btn_search.setEnabled(false);
		exchange.lbl_progress.setText("Loading...");
		exchange.invalidate();
		
		build(current_file);
		
		//System.out.println(g_files);
		exchange.lbl_destination.setVisible(true);
		exchange.tf_destination.setVisible(true);
		exchange.btn_copy.setVisible(true);
		//exchange.add(new JButton("Complete!"));
		//exchange.invalidate();
	}
	
	public void build(File f) {
		// it must be a directory!
		if (!current_file.exists()) { return; }  
		if (!current_file.isDirectory()) { return;	}
		String path = f.getPath();
		String name = f.getName();
		
		// loop through files and directories!
		String[] files = f.list();
		for (int i = 0; i < files.length; i++) {
			File f2 = new File(path, files[i]);
			if (f2.isFile()) {
				//System.out.println("File: " + f2.getName());
				g_files.add(f2);
				exchange.lbl_progress.setText("Found " + g_files.size() + " Files");
				exchange.invalidate();
			} else if (f2.isDirectory()) {
				File f3 = new File(path, files[i]);
				//System.out.println("Dir: " + f2.getName());
				build(f3);
			}
		}
	}
}

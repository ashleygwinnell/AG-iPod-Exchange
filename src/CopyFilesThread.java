import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.id3.ID3v2_2;


public class CopyFilesThread extends Thread {
	
	public ArrayList<File> unknown_artist_files;
	public IPodExchange exchange;
	public boolean running;
	public CopyFilesThread(IPodExchange ex) {
		super();
		exchange = ex;
		unknown_artist_files = new ArrayList<File>();
		running = false;
	}
	
	public void run() {
		running = true;
		exchange.tf_destination.setEnabled(false);
		exchange.btn_copy.setEnabled(false);
		exchange.lbl_progress_2.setText("Loading...");
		
		exchange.btn_view_unknown_artist.setVisible(true);
		
		copyFiles();
		
		exchange.lbl_progress_2.setText("Copied all " + exchange.search_thread.g_files.size() + " files.");
		exchange.lbl_progress_3.setText("Finished!");
		running = false;
	}
	public String makesafe(String f) {
		return f.replace('?', ' ').replace('<', ' ').replace('>',' ').replace('*', ' ').replace(':', ' ').replace('"', ' ').replace('|', ' ').replace('/', ' ').replace('\\', ' ');
	}
	public String getPath(File f) {
		return f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf('\\'));
	}
	public String getExtension(String name) {
		return name.substring(name.indexOf('.') + 1);
	}
	public void copyFiles() {
		for (int i = 0; i < exchange.search_thread.g_files.size(); i++) {
			File temp = exchange.search_thread.g_files.get(i);
			//System.out.println(temp.getAbsolutePath() + ": " + getExtension(temp.getName()));
			if (!getExtension(temp.getName()).toLowerCase().equals("mp3")) {
				unknown_artist_files.add(temp);
				continue;
			}
			try {
				MP3File f = new MP3File(temp);
				if (f.hasID3v1Tag()) {
					
					ID3v1 tag = f.getID3v1Tag();
					String num = tag.getTrackNumberOnAlbum();
					String album = tag.getAlbum();
					String artist = tag.getArtist();
					String title = tag.getTitle();
					
					exchange.lbl_progress_2.setText("Copying file " + (i+1) + " of " + exchange.search_thread.g_files.size() + ".");
					exchange.lbl_progress_3.setText(artist + " - " + title);
					
					String dest_str = makeFileName(artist, album, num, title);
					dest_str += "." + getExtension(temp.getName());
					
					File dest_f = new File(dest_str);
					File dest_dir = new File(getPath(dest_f));
					dest_dir.mkdirs();
					
					copyFile(temp, dest_f);
					
				} else if (f.hasID3v2Tag()) {
					
					AbstractID3v2 tag = f.getID3v2Tag();
					String num = tag.getTrackNumberOnAlbum();
					String album = tag.getAlbumTitle();
					String artist = tag.getLeadArtist();
					String title = tag.getSongTitle();
					
					exchange.lbl_progress_2.setText("Copying file " + (i+1) + " of " + exchange.search_thread.g_files.size() + ".");
					exchange.lbl_progress_3.setText(artist + " - " + title);
					
					String dest_str = makeFileName(artist, album, num, title);
					dest_str += "." + getExtension(temp.getName());
					
					File dest_f = new File(dest_str);
					File dest_dir = new File(getPath(dest_f));
					dest_dir.mkdirs();
					
					copyFile(temp, dest_f);
					
				}
			} catch (UnsupportedOperationException e) {
				unknown_artist_files.add(temp);
			} catch (StringIndexOutOfBoundsException e) {
				unknown_artist_files.add(temp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TagException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < unknown_artist_files.size(); i++) {
			String dest_str = exchange.tf_destination.getText();
			dest_str += "\\Unknown Artist" + "\\";
			dest_str += i + "." + getExtension(unknown_artist_files.get(i).getName());
			File dest_file = new File(dest_str);
			
			File dest_dir = new File(getPath(dest_file));
			dest_dir.mkdirs();
			
			exchange.lbl_progress_2.setText("Copying unknown artist file " + (i+1) + " of " + unknown_artist_files.size() + ".");
			exchange.lbl_progress_3.setText("Unknown Artist - Unknown Title");
			
			try {
				copyFile(unknown_artist_files.get(i), dest_file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String makeFileName(String artist, String album, String trackno, String title) {
		String dest_str = exchange.tf_destination.getText();
		if (artist.trim().length() != 0) {
			dest_str += "\\" + makesafe(artist) + "\\";
		} else {
			dest_str += "\\Unknown Artist" + "\\";
		}
		
		if (album.trim().length() != 0) {
			dest_str += makesafe(album) + " - ";
		} else {
			dest_str += "Unknown Album - ";
		}
		
		if (trackno.trim().length() != 0) {
			dest_str += trackno + " - ";
		}
		
		if (title.trim().length() != 0) {
			dest_str += makesafe(title);
		} else {
			dest_str += "Unknown Title";
		}
		
		return dest_str;
	}
	
	public void copyFile(File source, File destination) throws IOException {
		if (!destination.exists()) {
			destination.createNewFile();
		} 
		if (destination.exists()) {
			return;
		}
		
		//System.out.println("Copying File: " + temp.getAbsolutePath() + " to " + dest_f.getAbsolutePath());
		
		FileInputStream in = new FileInputStream(source);
	    FileOutputStream out = new FileOutputStream(destination);
	    
	    byte[] buffer = new byte[4096];
	    int bytesRead;

	    while ((bytesRead = in.read(buffer)) != -1) {
	        out.write(buffer, 0, bytesRead); // write
	    }

	    in.close();
	    out.close();
	}
	
}

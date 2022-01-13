package omega.vibencode;
import java.util.LinkedList;

import java.io.File;

import omegaui.dynamic.database.DataBase;
public class QueueManager extends DataBase{
	
	public LinkedList<String> musicFilePaths = new LinkedList<>();
	
	public QueueManager(){
		super(".omega-ide" + File.separator + ".vibencode-queue");
		load();
	}

	public void load(){
		musicFilePaths.clear();
		getEntriesAsString("Music File Locations").forEach(this::add);
	}

	public void add(String path){
		for(String px : musicFilePaths){
			if(px.equals(path))
				return;
		}
		if(!new File(path).exists())
			return;
		musicFilePaths.add(path);
	}

	public int indexOf(String path){
		for(int i = 0; i < musicFilePaths.size(); i++){
			String px = musicFilePaths.get(i);
			if(px.equals(path))
				return i;
		}
		return -1;
	}

	public void remove(String path){
		musicFilePaths.remove(path);
	}

	@Override
	public void save(){
		clear();
		musicFilePaths.forEach(path->addEntry("Music File Locations", path));
		super.save();
	}
}

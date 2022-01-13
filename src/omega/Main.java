package omega;
import omega.vibencode.MusicPlayerPlugin;

import java.io.File;
public class Main {
	public static void main(String[] args) throws Exception{
		IDE.main(args);
		MusicPlayerPlugin p = new MusicPlayerPlugin();
		p.init();
		p.enable();
	}
}

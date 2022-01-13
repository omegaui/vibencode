package omega.vibencode;
import java.io.File;

import omega.io.IconManager;

import java.util.LinkedList;

import omega.plugin.event.PluginReactionEvent;

import omega.Screen;

import omegaui.component.TextComp;

import java.net.URL;

import omega.plugin.Plugin;
import omega.plugin.PluginCategory;

import static omega.io.UIManager.*;
import static omegaui.component.animation.Animations.*;

public class MusicPlayerPlugin implements Plugin{
	
	private MusicPlayerPanel musicPlayerPanel;
	
	private TextComp musicComp;
	
	@Override
	public boolean init() {
		Thread.currentThread().setContextClassLoader(Screen.getPluginManager().pluginLoader.classLoader);
		musicComp = new TextComp(IconProvider.playIcon, 20, 20, back2, back2, TOOLMENU_COLOR1, ()->{
			
			Screen.getScreen().toggleLeftComponent(musicPlayerPanel);
			Screen.getScreen().splitPane.setDividerLocation(300);
			
		});
		musicComp.setArc(2, 2);
		
		putAnimationLayer(musicComp, getImageSizeAnimationLayer(25, 5, true), ACTION_MOUSE_ENTERED);
		
		musicPlayerPanel = new MusicPlayerPanel(musicComp);
		return true;
	}
	
	@Override
	public boolean enable() {
		musicComp.setBounds(0, 100, 30, 25);
		Screen.getScreen().getSideMenu().add(musicComp);
		Screen.getScreen().getSideMenu().repaint();
		return true;
	}
	
	@Override
	public boolean disable() {
		Screen.getScreen().getSideMenu().remove(musicComp);
		Screen.getScreen().getSideMenu().repaint();
		return true;
	}
	
	@Override
	public String getName() {
		return "Vibe n Code";
	}
	@Override
	public String getVersion() {
		return "v2.2";
	}
	@Override
	public String getPluginCategory() {
		return PluginCategory.EDITING;
	}
	@Override
	public String getDescription() {
		return "Vibe n Code!";
	}
	@Override
	public String getAuthor() {
		return "Omega UI";
	}
	@Override
	public String getLicense() {
		return "GNU GPL v3";
	}
	@Override
	public String getSizeInMegaBytes() {
		return "2.2 MB";
	}
	@Override
	public URL getImage() {
		try{
			return getClass().getResource("/fluent-icons/icons8-circled-play-96.png");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean needsRestart() {
		return false;
	}
	
}

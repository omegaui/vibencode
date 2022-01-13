package omega.vibencode;
import javax.swing.ImageIcon;

import javax.imageio.ImageIO;

import java.awt.Image;

import java.awt.image.BufferedImage;
public final class IconProvider {
	public static BufferedImage playIcon = getFluentIcon("circled-play-96.png");
	public static BufferedImage pauseIcon = getFluentIcon("pause-button-96.png");
	public static BufferedImage forwardIcon = getFluentIcon("fast-forward-round-96.png");
	public static BufferedImage rewindIcon = getFluentIcon("rewind-button-round-96.png");
	public static BufferedImage audioWaveIcon = getFluentIcon("audio-wave-96.png");
	public static BufferedImage resetIcon = getFluentIcon("reset-96.png");
	
	public static Image audioWaveGif = getFluentGif("audio-wave.gif");

	public static BufferedImage getFluentIcon(String name){
		try{
			return ImageIO.read(IconProvider.class.getResourceAsStream("/fluent-icons/icons8-" + name));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Image getFluentGif(String name){
		try{
			return new ImageIcon(IconProvider.class.getResourceAsStream("/fluent-gifs/icons8-" + name).readAllBytes()).getImage();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}

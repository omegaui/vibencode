package omega.vibencode;
import java.awt.BorderLayout;

import omega.Screen;

import omega.ui.dialog.FileSelectionDialog;

import com.goxr3plus.streamplayer.enums.Status;

import java.util.Map;
import java.util.LinkedList;

import java.io.File;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerListener;
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent;

import omegaui.component.TextComp;
import omegaui.component.FlexPanel;

import javax.swing.JPanel;

import static omega.io.UIManager.*;
import static omegaui.component.animation.Animations.*;

public class MusicPlayerPanel extends JPanel implements StreamPlayerListener{
	
	private static final String TEXT0 = "Browse and Select a Music";

	private FlexPanel flexPanel;
	private JPanel panel;
	
	private TextComp viewComp;
	
	private TextComp gifComp;
	
	private TextComp titleComp;
	
	private TextComp goLeftComp;
	private TextComp playOrPauseComp;
	private TextComp goRightComp;
	
	private TextComp replayComp;
	
	private TextComp updateQueueComp;
	
	private StreamPlayer player;
	
	private File currentFile;
	
	public QueueManager queueManager;
	public QueueView queuePanel;
	
	private FileSelectionDialog fileSelectionDialog;
	
	private int pointer;
	
	public MusicPlayerPanel(TextComp viewComp){
		super(new BorderLayout());
		this.viewComp = viewComp;
		setBackground(c2);
		init();
	}
	
	public void init(){
		flexPanel = new FlexPanel(null, TOOLMENU_COLOR3_SHADE, null);
		flexPanel.setArc(0, 0);
		add(flexPanel, BorderLayout.CENTER);
		
		panel = new JPanel(null);
		panel.setBackground(c2);
		
		flexPanel.add(panel);
		
		player = new StreamPlayer(){
			@Override
			public void open(File file) throws com.goxr3plus.streamplayer.stream.StreamPlayerException{
				System.out.println("We got : " + file);
				super.open(file);
			}
		};
		player.addStreamPlayerListener(this);
		
		queuePanel = new QueueView(this);
		queueManager = new QueueManager();
		
		panel.add(queuePanel);
		
		fileSelectionDialog = new FileSelectionDialog(Screen.getScreen());
		fileSelectionDialog.setTitle("Select a Music File (only .wav is supported)");
		fileSelectionDialog.setFileExtensions(".wav");
		
		gifComp = new TextComp("", c2, c2, c2, null);
		gifComp.setImage(IconProvider.audioWaveIcon, 48, 48);
		panel.add(gifComp);
		
		titleComp = new TextComp(TEXT0, c2, c2, glow, null);
		titleComp.setFont(PX14);
		titleComp.setArc(0, 0);
		titleComp.setPaintGradientEnabled(true);
		titleComp.setGradientColor(TOOLMENU_GRADIENT);
		titleComp.setClickable(false);
		panel.add(titleComp);
		
		playOrPauseComp = new TextComp(IconProvider.playIcon, 24, 24, TOOLMENU_COLOR3_SHADE, back2, back2, this::togglePlayOrPause);
		playOrPauseComp.setArc(15, 15);
		panel.add(playOrPauseComp);
		
		goLeftComp = new TextComp(IconProvider.rewindIcon, 24, 24, TOOLMENU_COLOR3_SHADE, back2, back2, this::goUpInQueue);
		goLeftComp.setArc(15, 15);
		panel.add(goLeftComp);
		
		goRightComp = new TextComp(IconProvider.forwardIcon, 24, 24, TOOLMENU_COLOR3_SHADE, back2, back2, this::goDownInQueue);
		goRightComp.setArc(15, 15);
		panel.add(goRightComp);
		
		replayComp = new TextComp(IconProvider.resetIcon, 24, 24, TOOLMENU_COLOR3_SHADE, back1, back2, this::replay);
		replayComp.setArc(15, 15);
		panel.add(replayComp);
		
		updateQueueComp = new TextComp("Add To Queue", TOOLMENU_COLOR5_SHADE, back2, TOOLMENU_COLOR5, ()->addMoreToQueue());
		updateQueueComp.setFont(PX14);
		updateQueueComp.setArc(5, 5);
		panel.add(updateQueueComp);
		
		putAnimationLayer(playOrPauseComp, getImageSizeAnimationLayer(20, +5, true), ACTION_MOUSE_ENTERED);
	}
	
	public void replay(){
		if(currentFile == null)
			return;
		
		File x = currentFile;
		reset();
		play(x);
	}
	
	public void goUpInQueue(){
		if(pointer - 1 >= 0)
			pointer--;
		play(new File(queueManager.musicFilePaths.get(pointer)));
	}
	
	public void goDownInQueue(){
		if(pointer + 1 < queueManager.musicFilePaths.size())
			pointer++;
		play(new File(queueManager.musicFilePaths.get(pointer)));
	}
	
	public void updateView(){
		playOrPauseComp.setImage(isPlaying() ? IconProvider.pauseIcon : IconProvider.playIcon);
		if(isPlaying()){
			gifComp.setImage(null);
			gifComp.setGifImage(IconProvider.audioWaveGif);
		}
		else{
			gifComp.setImage(IconProvider.audioWaveIcon);
			gifComp.setGifImage(null);
		}
	}
	
	public void togglePlayOrPause(){
		try{
			
			if(currentFile == null)
				return;
			
			if(player.isPlaying())
				player.pause();
			else
				player.resume();
			
			updateView();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void play(File file){
		try{
			if(currentFile != null && currentFile.getAbsolutePath().equals(file.getAbsolutePath()))
				return;
			
			if(currentFile != null)
				player.stop();
			
			this.currentFile = file;
			
			this.pointer = queueManager.indexOf(file.getAbsolutePath());
			
			titleComp.setText(file.getName());
			
			player.open(file);
			
			player.play();
			
			updateView();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void reset(){
		player.stop();
		currentFile = null;
		titleComp.setText(TEXT0);
		updateView();
	}
	
	public boolean isPlaying(){
		return currentFile != null && player.isPlaying();
	}
	
	public void addMoreToQueue(){
		LinkedList<File> files = fileSelectionDialog.selectFiles();
		if(!files.isEmpty()){
			files.forEach((file)->queueManager.add(file.getAbsolutePath()));
			queuePanel.genList(queueManager.musicFilePaths);
			queueManager.save();
		}
	}
	
	@Override
	public void opened(Object dataSource, Map<String, Object> properties) {
		updateView();
	}
	
	@Override
	public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {
		
	}
	
	@Override
	public void statusUpdated(StreamPlayerEvent streamPlayerEvent) {
		
	}
	
	@Override
	public void layout(){
		flexPanel.setBounds(0, 0, getWidth(), getHeight());
		panel.setBounds(5, 5, flexPanel.getWidth() - 10, flexPanel.getHeight() - 10);
		gifComp.setBounds(getWidth()/2 - 24, 10, 48, 48);
		titleComp.setBounds(0, 70, getWidth(), 30);
		playOrPauseComp.setBounds(getWidth()/2 - 15, 110, 30, 30);
		goLeftComp.setBounds(playOrPauseComp.getX() - 60, 113, 26, 26);
		goRightComp.setBounds(playOrPauseComp.getX() + 30 + 35, 113, 26, 26);
		replayComp.setBounds(getWidth()/2 - 30/2, 150, 30, 30);
		updateQueueComp.setBounds(getWidth()/2 - 150/2, 195, 150, 25);
		queuePanel.setBounds(5, 230, getWidth() - 10, getHeight() - 232);
		super.layout();
	}
	
	@Override
	public void setVisible(boolean value){
		if(value){
			queuePanel.genList(queueManager.musicFilePaths);
		}
		super.setVisible(value);
	}
	
	public omega.vibencode.QueueManager getQueueManager() {
		return queueManager;
	}
	
}

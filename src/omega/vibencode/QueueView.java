package omega.vibencode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import omegaui.component.TextComp;

import java.io.File;

import java.util.LinkedList;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import static omega.io.UIManager.*;
import static omegaui.component.animation.Animations.*;

public class QueueView extends JPanel{

	private MusicPlayerPanel musicPlayerPanel;
	
	private JScrollPane scrollPane;
	private JPanel panel;

	private LinkedList<TextComp> comps = new LinkedList<>();

	private int block = 0;
	
	public QueueView(MusicPlayerPanel musicPlayerPanel){
		super(new BorderLayout());
		this.musicPlayerPanel = musicPlayerPanel;
		setBackground(c2);
		init();
	}

	public void init(){
		scrollPane = new JScrollPane(panel = new JPanel(null));
		scrollPane.setBackground(back2);
		scrollPane.setBorder(null);
		panel.setBackground(c2);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void genList(LinkedList<String> paths){
		block = 0;
		
		comps.forEach(panel::remove);
		comps.clear();
		
		for(String path : paths){
			File file = new File(path);
			TextComp comp = new TextComp(file.getName(), TOOLMENU_COLOR1_SHADE, back2, glow, ()->musicPlayerPanel.play(file));
			comp.setBounds(0, block, 290, 25);
			comp.setFont(PX14);
			comp.setArc(0, 0);
			comp.alignX = 5;
			comp.addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e){
					if(e.getButton() == 3){
						musicPlayerPanel.getQueueManager().remove(path);
						musicPlayerPanel.getQueueManager().save();
						genList(musicPlayerPanel.getQueueManager().musicFilePaths);
						musicPlayerPanel.reset();
					}
				}
			});
			panel.add(comp);
			comps.add(comp);

			block += 25;
		}

		panel.setSize(scrollPane.getWidth() - 5, block);
		panel.setPreferredSize(panel.getSize());

		repaint();
	}

	@Override
	public void layout(){
		panel.setSize(scrollPane.getWidth() - 5, block);
		panel.setPreferredSize(panel.getSize());
		super.layout();
	}
}

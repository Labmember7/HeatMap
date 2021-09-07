import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



class ButtonPanel extends JPanel implements ActionListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		static int imageType=0; 
		DrawingArea drawingArea;
		JLabel labx =new JLabel("X (m)");
		static JTextField x = new JTextField(5);
		JLabel laby =new JLabel("Y (m)");
		static JTextField y = new JTextField(5);
		JLabel labp =new JLabel("Power(db)");
		JTextField p = new JTextField(5);
		JLabel obs = new JLabel("Obstacles");
		AccessPoint ap = null;
		public ButtonPanel(DrawingArea drawingArea)
		{
			
			this.drawingArea = drawingArea;
			
			
		  	add(labx);
			add(x);
		    add(laby);
		    add(y);
		    add(labp);
		    add(p);
			add( createButton("Add Access Point", Color.ORANGE) );
			add( createButton("Add User", Color.YELLOW) );
			add(obs);
			
			Color wood=new Color(137,101,47);
			Color glass=new Color(204,229,255);
			Color brick=new Color(172,64,64);
			Color ceramic=new Color(102,178,255);
			Color concrete=new Color(160,160,160);
			Color metal=new Color(64,64,64);

			add( createButton("Wood(20%)", wood) );
			add( createButton("Glass(30%)",glass ) );
			add( createButton("Brick(50%)", brick) );
			add( createButton("Ceramic(70%)",ceramic ) );
			add( createButton("Concrete(85%)", concrete) );
			add( createButton("Metal(90%)",metal ) );
			add( createButton("Show Data", null) );
			add( createButton("Clear", null) );
		}
		
		private JButton createButton(String text, Color background)
		{
			JButton button = new JButton( text );
			button.setBackground( background );
			button.addActionListener( this );

			return button;
		}

		
		
		public void actionPerformed(ActionEvent e)
		{
			JButton button = (JButton)e.getSource();
		
			if ("Clear".equals(e.getActionCommand()))
			{
				drawingArea.clear();
				imageType=0;
			}
			else if("Add User".equals(e.getActionCommand()))
			{			
				imageType=1;
				 try
				 {
					 int cordx=Integer.parseInt(x.getText());
					 int cordy=Integer.parseInt(y.getText());
					 User u =new User(cordx,cordy);
					 drawingArea.users.add(u);
					 drawingArea.addUser(cordx,cordy);

					 
				 }
				 catch (Exception e1) 
				 {
					 JOptionPane.showMessageDialog(drawingArea,
							    "You need to add the position of the User (X ,Y)",
							    "Warning!",
							    JOptionPane.WARNING_MESSAGE);
				 }
			}

			else if("Add Access Point".equals(e.getActionCommand()))
			{	
				imageType=2;
				 try
				 {
					 int cordx=Integer.parseInt(x.getText());
					 int cordy=Integer.parseInt(y.getText());
					 int puissance=Integer.parseInt(p.getText());
					 double couverture =1.66*puissance +83.33;
					 
					 ap =new AccessPoint(cordx,cordy,(int)(couverture));
					 drawingArea.a.add(ap);
					 drawingArea.a.get(drawingArea.a.size()-1).number=drawingArea.a.size();
					 
					 
					 drawingArea.addDegradedCircle(cordx, cordy,(int)(couverture));
				 
				 }
				 catch (Exception e2) 
				 {
					 JOptionPane.showMessageDialog(drawingArea,
							    "Make sure that the X ,Y and Power are filled correctly",
							    "Warning",
							    JOptionPane.WARNING_MESSAGE);
				}
				 
			}
			else if("Show Data".equals(e.getActionCommand()))
			{
				drawingArea.data=new JTableEx();
				drawingArea.refreshDataTable();
				drawingArea.obsTab=new JTableObstacles();
				drawingArea.refreshObstTable();
				
			}
			
			
			else
			{
				imageType=3;
				drawingArea.setForeground( button.getBackground() );
				
				
				
				
			}
		}
	}
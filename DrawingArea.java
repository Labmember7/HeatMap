import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

class DrawingArea extends JPanel
	{
		/**
		 * 
		 */
		static final long serialVersionUID = 1L;
		Color wood=new Color(137,101,47);
		Color glass=new Color(204,229,255);
		Color brick=new Color(172,64,64);
		Color ceramic=new Color(102,178,255);
		Color concrete=new Color(160,160,160);
		Color metal=new Color(64,64,64);
		
		 final static int AREA_SIZE = 400;
			
			/** A Polygon object which we will re-use for each shadow geometry. */
			protected final static Polygon POLYGON = new Polygon();
			private static int width = 1450, height = 720;
			
			/** Returns the width of the game canvas. */
			public int getWidth() {
				return Math.max(0, width);
			}
			
			/** Returns the height of the game canvas. */
			public int getHeight() {
				return Math.max(0, height);
			}
			
			/** The buffer strategy used for smooth active rendering. */
			protected BufferStrategy strategy;
			
			/** True if the game loop is running. */
			protected boolean running;
			
			/** The current frames per second, used for debugging performance. */
			protected int fps = 60;
			
			/** Timer used for adding entities every N seconds. */
		    float tickTimer;
		 
		 
		 private BufferedImage image = new BufferedImage(AREA_SIZE+1050, AREA_SIZE+400, BufferedImage.TYPE_4BYTE_ABGR);
		 JTableEx data ;
		 JTableObstacles obsTab;
		 ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
		 ArrayList<User> users = new ArrayList<User>();
		 ArrayList<AccessPoint> a = new ArrayList<AccessPoint>();
		 ArrayList<Shape> shapes = new ArrayList<Shape>();
		 Rectangle2D shape;
		 
		public DrawingArea()
		{
			setBackground(Color.WHITE);

			MyMouseListener ml = new MyMouseListener();
			MouseClickedListener mc = new MouseClickedListener();
			addMouseListener(ml);
			addMouseListener(mc);
			addMouseMotionListener(ml);
			

		}

		@Override
		public Dimension getPreferredSize()
		{
			return isPreferredSizeSet() ?
			super.getPreferredSize() : new Dimension(AREA_SIZE, AREA_SIZE);
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			//  Custom code to paint all the shapes from the List

				if(image != null)
				{
					g.drawImage(image, 0, 0, null);
				
				}
			//  Paint the shape as the mouse is being dragged

			if (shape != null)
			{
				Graphics2D g2d = (Graphics2D)g;
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.draw( shape );
				
			}
		}
		
		
		
	/*-----------------------------METHODS FOR ADDING-----------------------*/	
		
		public void addUser(int x1,int y1) throws AWTException
		{
			
			Graphics2D g2d = (Graphics2D)image.getGraphics();

			g2d.setColor( Color.BLACK);
			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
		    g2d.setStroke(dashed);
		    
		   	AccessPoint access = new AccessPoint(0,0,0);
		   	access=bestQuality(users.get(users.size()-1));
		   	g2d.drawString("User "+users.size(), x1, y1);

		     
		    
            // get the pixel color 
		  
		    
		    int clr=  image.getRGB(x1,y1); 
		    int  red   = (clr & 0x00ff0000) >> 16;
		    int  green = (clr & 0x0000ff00) >> 8;
		    int  blue  =  clr & 0x000000ff;
		    System.out.println("Red Color value = "+ red);
		    System.out.println("Green Color value = "+ green);
		    System.out.println("Blue Color value = "+ blue);
           
		   	if(access.rayon>=(int)(distance(x1,y1,access.xc,access.yc)))
		   	{
		   		if ((green==255&&blue==255&&red<200)||(green<255&&blue<255&&red<200))
		   		{
		   			g2d.drawLine(x1,y1,access.xc,access.yc);
		   			users.get(users.size()-1).status="Connected";
		   			users.get(users.size()-1).accessPoint="Access Point "+String.valueOf(access.number);
		   		}
		   	}
		   	repaint();
		}
		
		
		public void addRectangle(Rectangle2D rectangle, Color color)
		{
		//  Draw the Rectangle onto the BufferedImage

					Graphics2D g2d = (Graphics2D)image.getGraphics();
					g2d.setColor( color );
					g2d.draw( rectangle );
					g2d.fill(rectangle);
					g2d.setColor( Color.BLACK );
					g2d.draw( rectangle );
					
					shape =rectangle;
					shapes.add(shape);
					System.out.println(shapes.size());
					
					repaint();
		}

		
		public void addDegradedCircle(int xc,int yc,int r)
		{
		
			System.out.println(r);
			Graphics2D g2d = (Graphics2D)image.getGraphics();
			double f=0;
			int x=0;
			int y=0;
			int G=0;
			try 
			{	
				for (int i=0; i<r*2;i+=1)
				{
					x=(xc-r)+ i;
					
					for (int j=0; j<r*2;j+=1)
					{ 
						
						y=(yc-r)+ j;
						f=attinuationFacteur (xc, yc ,r, x, y);
					
						G=(int) (255.0-f*255.0);
					
						if (f!=0)
						{	
							//G in [54 .. 253]
	
							Color C= new Color(255-G,G,G,175);
							shape = new Rectangle2D.Double(x,y,1, 1);
							g2d.setPaint(C);
							g2d.fill(shape);
							
							repaint();
						}
						
						if(i==r)
						{
							
						}
						}
						
					  }

				renderShadows(g2d, xc, yc,r);
				g2d.drawString("AP "+a.size(), xc, yc);
				
				
			}
		    catch(Exception e1) {System.out.println("erreur addDegradedCircle "+f ); }
		}
		
		double attinuationFacteur (int xc, int yc ,int r, int x, int y)
		{
			//double f =100+ Math.log10(distance(xc,yc,x,y));//((double)r));
			double f = 1.0-distance(xc,yc,x,y)/((double)r);
			if (f<0) return (0);
			return (f);
		}

		double signalQuality (double rayon ,double d)
		{
			double quality =100*((rayon-d)/rayon);
			
			if (quality<0) return (0);
			return (quality);
		}


		
		double distance (int x1,int y1, int x, int y)
		{ return Math.sqrt( (x1-x)*(x1-x)+(y1-y)*(y1-y)); }
		
		
		void drawLine(Point p1,Point p2)
		{
			Graphics2D g2d = (Graphics2D)image.getGraphics();
			g2d.setColor(Color.BLUE);
			g2d.drawLine(p1.x,p1.y, p2.x, p2.y);
			repaint();
			
		}
		
		AccessPoint bestQuality(User us)
		{

			int xu=us.x,yu=us.y;
			double dis=0;
			int i=0;
			AccessPoint a1 =new AccessPoint(0,0,0);
			if(a.size()>0)
			{
				a1=a.get(0);
			
				dis=distance(xu,yu,a1.xc,a1.yc);
				double quality =signalQuality(a1.rayon,dis);
				us.signalQuality=quality;
			
				for (AccessPoint ap : a)
				{		
					if(i>0)
					{
						dis=distance(xu,yu,ap.xc,ap.yc);
						
						if(signalQuality(ap.rayon,dis)>=quality)
						{
							quality =signalQuality(ap.rayon,dis);
							a1=ap;
							us.signalQuality=quality;
							
						}
						
					}
					i++;
				}
			}
			return a1;
		  }
		
		
		public void clear()
		{
			createEmptyImage();
			a.clear();
			users.clear();
			obstacles.clear();
			shapes.clear();
			shape=null;
			repaint();
			System.gc();
		}
		
		
		private void createEmptyImage() 
		{
			image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = (Graphics2D)image.getGraphics();
			g2d.setColor(Color.BLACK);
			g2d.drawString("Everything is Set Up Create Your Own HEATMAP", 40, 15);

		}

		
		void refreshDataTable()
		{
			if (users.size()!=0)
			{
				int i=1;
				
				for (User us : users)
				{
					String user ="User "+String.valueOf(i);
					String status =us.status;
					String accessPoint =us.accessPoint;
					String sigQuality =String.valueOf((int)(us.signalQuality))+"%";
				
					data.tableModel.insertRow(0, new Object[] { user,status ,accessPoint ,sigQuality});
					i++;
				}
			}
		}
		void refreshObstTable()
		{
			if (obstacles.size()!=0)
			{
				int i=1;
				
				for (Obstacle o : obstacles)
				{
					String type =o.type;
					String attenuation=String.valueOf(o.attenuation);
					String num=String.valueOf(i);
				
					obsTab.tableModel.insertRow(0, new Object[] { num,type,attenuation});
					i++;
				}
			}
		}
		
		
		void renderShadows(Graphics2D g,int mouseX,int mouseY,int rayon) 
		{ 
			
			/** The gradient radius for our shadow. */
			 float GRADIENT_SIZE = (float) rayon;
			
			/** The fractions for our shadow gradient, going from 0.0 (black) to 1.0 (transparent). */
		    float[] GRADIENT_FRACTIONS = new float[]{0f, 1f};
			
			/** The colors for our shadow, going from opaque black to transparent black. */
			
		    final  Color[] GRADIENT_COLORS = new Color[] { new Color(0,255,255), new Color(255,255,255,200)};
			
			
			
			//old Paint object for resetting it later
			Paint oldPaint = g.getPaint();
			
			//minimum distance (squared) which will save us some checks
			float minDistSq = GRADIENT_SIZE*GRADIENT_SIZE;
			//amount to extrude our shadow polygon by
			//use a large enough value to ensure that it is way off screen
			 float SHADOW_EXTRUDE =rayon;// (float) (0.1*GRADIENT_SIZE*GRADIENT_SIZE);
			
			//we'll use a radial gradient from the mouse center
			//final Paint GRADIENT_PAINT = new RadialGradientPaint(new Point2D.Float(mouseX, mouseY), GRADIENT_SIZE, GRADIENT_FRACTIONS, GRADIENT_COLORS);
			 Paint GRADIENT_PAINT ;
			final Point2D.Float mouse = new Point2D.Float(mouseX, mouseY);
			//for each entity
			for (int i=0; i<shapes.size(); i++) 
			{
				Shape e = shapes.get(i);
				Obstacle o= obstacles.get(i);
				
				Rectangle2D bounds = e.getBounds2D();
				
				//radius of Entity's bounding circle
				float r = (float)bounds.getWidth()/2f; 
				float r1= (float)bounds.getHeight()/2f;
				
				if(r<r1)
				{
					r=r1;
				}
				//get center of entity			
				int cx = o.center.x;
				int cy = o.center.y;
				System.out.println("real Center obs = "+o.center.x+" , "+o.center.y);
				System.out.println("Center shape = "+cx+" , "+cy);
			

				//get direction from mouse to entity center
				float dx = cx - mouse.x;
				float dy = cy - mouse.y;
				System.out.println("direction = "+dx+" , "+dy);
				
							
				//get euclidean distance from mouse to center
				float distSq = dx * dx + dy * dy; //avoid sqrt for performance
							
				//if the entity is outside of the shadow radius, then ignore
				if (distSq > minDistSq) 
					continue; 
				
				//normalize the direction to a unit vector
				float len = (float)Math.sqrt(distSq);
				float nx = dx;
				float ny = dy;
				if (len != 0) { //avoid division by 0
					nx /= len;
					ny /= len;
				}
				
				//get perpendicular of unit vector
				float px = -ny;
				float py = nx;
				
				SHADOW_EXTRUDE=(float) (rayon-distance(mouseX, mouseY, cx,cy))+30;
				
				
				//our perpendicular points in either direction from radius
				Point2D.Float A = new Point2D.Float(cx - px * r, cy - py * r);
				Point2D.Float B = new Point2D.Float(cx + px * r, cy + py * r);
				
				//project the points by our SHADOW_EXTRUDE amount
				Point2D.Float C = project(mouse, A, SHADOW_EXTRUDE);
				Point2D.Float D = project(mouse, B, SHADOW_EXTRUDE);
				
				//construct a polygon from our points
				POLYGON.reset();
				POLYGON.addPoint((int)A.x, (int)A.y);
				POLYGON.addPoint((int)B.x, (int)B.y);
				POLYGON.addPoint((int)D.x, (int)D.y);
				POLYGON.addPoint((int)C.x, (int)C.y);
				
				//fill the polygon with the gradient paint
				GRADIENT_COLORS[0]=new Color(o.attenuation*2,255,255,150);
				//int attenuation=o.attenuation;
				GRADIENT_PAINT = new RadialGradientPaint(new Point2D.Float(mouseX, mouseY), GRADIENT_SIZE, GRADIENT_FRACTIONS, GRADIENT_COLORS);
				g.setPaint(GRADIENT_PAINT);
				g.fill(POLYGON);
			}
			
			//reset to old Paint object
			g.setPaint(oldPaint);
		}

		/** Projects a point from end along the vector (end - start) by the given scalar amount. */
		private Point2D.Float project(Point2D.Float start, Point2D.Float end, float scalar) 
		{
			float dx = end.x - start.x;
			float dy = end.y - start.y;
			//euclidean length
			float len = (float)Math.sqrt(dx * dx + dy * dy);
			//normalize to unit vector
			if (len != 0) { //avoid division by 0
				dx /= len;
				dy /= len;
			}
			//multiply by scalar amount
			dx *= scalar;
			dy *= scalar;
			return new Point2D.Float(end.x + dx, end.y + dy);
		}
		
		
		//------------------LISTENERS---------------------------//
		class MyMouseListener extends MouseInputAdapter
		{
			 Point startPoint;
			 int width;
			 int height;

			public void mousePressed(MouseEvent e)
			{
				//draw Obstacle
				if(ButtonPanel.imageType==3)
				{
					startPoint = e.getPoint();
					shape = new Rectangle();
					
				}
				
				//draw Access Point
				if(ButtonPanel.imageType==2)
				{
					startPoint = e.getPoint();
					
				}
			}
			public void mouseDragged(MouseEvent e)
			{
				//draw Obstacle
				if(ButtonPanel.imageType==3)
				{
					int x = Math.min(startPoint.x, e.getX());
					int y = Math.min(startPoint.y, e.getY());
					width = Math.abs(startPoint.x - e.getX());
				    height = Math.abs(startPoint.y - e.getY());
				    
					((Rectangle) shape).setBounds(x, y, width, height);
					repaint();
				}	
			}

		
			public void mouseReleased(MouseEvent e)
			{
				Point p2=null,p4 = null;
				
				//draw Obstacle
				if(ButtonPanel.imageType==3)
				{
					if (((Rectangle) shape).width != 0 || ((Rectangle) shape).height != 0)
					{
						addRectangle(((Rectangle) shape), e.getComponent().getForeground());
					try {
							Point pnow=e.getPoint();
							Point center;
							p4=new Point(startPoint.x,pnow.y);
							
							if(pnow.x>startPoint.x)
							{
								p2=new Point((startPoint.x)+width,(startPoint.y));	
								if(pnow.y>startPoint.y)
								{
									center= new Point(startPoint.x+(width/2),pnow.y-(height/2));
								}
								else
								{
									center= new Point(startPoint.x+(width/2),pnow.y+(height/2));
								}
						
							}
							else
							{
								p2=new Point((startPoint.x)-width,(startPoint.y));
								if(pnow.y>startPoint.y)
								{
									center= new Point(startPoint.x-(width/2),pnow.y-(height/2));
								}
								else
								{
									center= new Point(startPoint.x-(width/2),pnow.y+(height/2));
								}
							}

							
							Obstacle ob=new Obstacle(startPoint,p2,pnow,p4,e.getComponent().getForeground());
								ob.center=center;
							if(e.getComponent().getForeground().equals(wood))
							{
								ob.attenuation=20;
								ob.type="Wood";
							}
							else if(e.getComponent().getForeground().equals(glass))
							{
								ob.attenuation=30;
								ob.type="Glass";

							}
							else if(e.getComponent().getForeground().equals(brick))
							{
								ob.attenuation=50;
								ob.type="Brick";

							}
							else if(e.getComponent().getForeground().equals(ceramic))
							{
								ob.attenuation=70;
								ob.type="Ceramic";

							}
							else if(e.getComponent().getForeground().equals(concrete))
							{
								ob.attenuation=85;
								ob.type="Concrete";

							}
							else
							{
								ob.attenuation=90;
								ob.type="Metal";

							}
							
							
							obstacles.add(ob);
							System.out.println("nb obstacle ="+obstacles.size());
							System.out.println("P1 ("+startPoint.x+ " , "+startPoint.y+ ")"+"P2 ("+p2.x+ " , "+p2.y+ ")"+
												"P3 ("+pnow.x+ " , "+pnow.y+ ")"+"P4 ("+p4.x+ " , "+p4.y+ ")"+"Center ("+center.x+ " , "+center.y+ ")");
						
						}catch(Exception s) {System.out.println("erreur");	}
					}
					shape = null;
				}
		    }
	
		}
		
		
		
		
	   class MouseClickedListener implements MouseListener 
	   {
		   
			   
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited (MouseEvent e){}
			public void mouseReleased(MouseEvent e)
			{
				try
				{
					if(e.getButton() == MouseEvent.BUTTON1)
					{
					//Recover (x,y) from the mouse
					int x1= e.getX();
					int y2= e.getY();
					ButtonPanel.x.setText(String.valueOf(x1));
					ButtonPanel.y.setText(String.valueOf(y2));
					}
				}catch(Exception e1){System.out.println("Erreur");}
				
			}
	   }
	   
	   
	   
	   
}
import java.awt.Color;
import java.awt.Point;


class Obstacle
		{
			Point a=new Point();
			Point b=new Point();
			Point c=new Point();
			Point d=new Point();
			Point center=new Point(0,0);
			Color col=new Color(0,0,0);
			int attenuation=0;
			String type;
			public Obstacle(Point p1,Point p2,Point p3,Point p4, Color co)
			{
				a=p1;
				b=p2;
				c=p3;
				d=p4;
				col=co;
				
						
			}


		}

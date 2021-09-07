import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
class Draw 
{
	//THE MAIN METHOD:
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{	
				new Draw();
			}
		});
	}
	
	/*-----------------------------------------*/
	
	
	Draw()
	{
		//

		DrawingArea drawingArea = new DrawingArea();
		ButtonPanel buttonPanel = new ButtonPanel( drawingArea );
		
		
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Draw On Component");
		
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add(drawingArea);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.setSize(1400,800);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
	
		
	}
	
	

	
	

	
}
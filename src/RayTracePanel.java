
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author Stephen Elliott
 * JPanel customized to display and raytrace a BufferedImage.
 */
public class RayTracePanel extends JPanel {

	private static final long serialVersionUID = 1L;
    
	BufferedImage image;
	int width;
	int height;
	int antiAliasing;
    int numThreads;
    
    Scene s;
	
	public RayTracePanel(int w, int h, int aa, int t) {
		this.width = w;
		this.height = h;
		this.antiAliasing = aa;
        this.numThreads = t;
		
		this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		this.image.createGraphics().setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(w, h));
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawRenderedImage(this.image, new AffineTransform());
	}
	
	/**
	 * Saves the rendered image to a File.
	 * @param f - File object to save our rendered image to.
	 * @param type - The output image format.
	 */
	public void saveToFile(File f, String type) {
		try {
			ImageIO.write(image, type, f);
		} catch (IOException e) {
			System.out.println("Saving failed! " + e.getMessage());
		}
	}
	
	/**
	 * Clears the buffered image.
	 */
	public void clear() {
		for (int y = 0; y < this.height; y++)
			for (int x = 0; x < this.width; x++)
				this.image.setRGB(x, y, 0);
		this.repaint();
	}
	
	/**
	 * Ray-traces and displays a new scene.
	 * @param s The scene to display.
	 * @throws InterruptedException 
	 */
	public void changeScene(Scene s) throws InterruptedException {
		Thread[] threads = new Thread[this.numThreads];
        this.s = s;
		this.clear();
        for (int i = 0; i < this.numThreads; i++) {
            threads[i] = new RenderThread(s, this.image, this.antiAliasing, this.numThreads, i, this);
            threads[i].start();
        }
        for (int i = 0; i < this.numThreads; i++)
        	threads[i].join();
	}
    
    public void stopRendering() {
        this.s.stopTrace();
    }
    
    private class RenderThread extends Thread {
        Scene s;
        BufferedImage im;
        int aa;
        int threads;
        int index;
        JComponent c;
        
        RenderThread(Scene s, BufferedImage im, int aa, int t, int i, JComponent c) {
            this.s = s;
            this.im = im;
            this.aa = aa;
            this.threads = t;
            this.index = i;
            this.c = c;
        }
        
        public void run() {
            s.rayTrace(this.im, this.aa, this.index, this.threads, this.c);
        }
    }
	
}

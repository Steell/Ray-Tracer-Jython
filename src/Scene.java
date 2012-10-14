
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * @author Stephen Elliott
 * Class representing a 3D scene capable of raytracing.
 */
public class Scene {

	Camera cam;
	Light[] lights;
	Surface[] shapes;
	boolean diffuse;
	boolean specular;
	boolean shadows;
	int mirrorDepth;
	double ambientIntensity;
    
    private boolean rendering;

	public Scene(Camera cam, Light[] lights, Surface[] shapes, boolean diffuse,
			boolean specular, boolean shadows, int mirrorDepth,
			double ambientIntensity) {
		this.cam = cam;
		this.lights = lights;
		this.shapes = shapes;
		this.diffuse = diffuse;
		this.specular = specular;
		this.shadows = shadows;
		this.mirrorDepth = mirrorDepth;
		this.ambientIntensity = ambientIntensity;
	}
	
	/**
	 * @param d - Array of length 3 representing the RGB color.
	 * @return An integer representation of the RGB color.
	 */
	private static int toRGB(double[] d) {
		return ((int) d[0]) << 16 | ((int) d[1]) << 8 | ((int) d[2]);
	}
    
    public void stopTrace() {
        this.rendering = false;
    }
	
	/**
	 * Paints this scene on the given BufferedImage. If c is not null,
	 * it will be repainted every time a new pixel is drawn.
	 * @param im BufferedImage to draw on. 
     * @param aa Number of samples per pixel for anti-aliasing.
     * @param sub The subdivision of the scene to trace.
     * @param totalSub The total number of subdivisions to trace.
	 * @param c - Component to repaint after a pixel is drawn.
	 */
	public void rayTrace(BufferedImage im, int aa, int sub, int totalSub, JComponent c) {
		this.rendering = true;
        this.rayTraceHelper(im, aa, sub, totalSub, c);
	}
	
	/**
	 * Performs the actual ray-tracing.
	 */
	private void rayTraceHelper(BufferedImage im, double aa, int sub, int totalSub, JComponent c) {
		int w = im.getWidth();
		int h = im.getHeight();
		double z = -Math.sqrt(3) / 2;
		double r = -(h * 1.0) / w;
		double aa2 = aa * aa;
        boolean randomAA = aa >= 4;
        
        //Multiplie division for mutli-threading.
        int minY, maxY, minX, maxX;
        if (totalSub > 1) {        	
            double interval = w * h / totalSub;
            double start = interval * sub;
            double end = start + interval;
            minY = (int)start / w;
            maxY = (int)end / w;
            minX = (int)start % h;
            maxX = (int)end % h;
        }
        else {
            minY = 0;
            maxY = h;
            minX = 0;
            maxX = 0;
        }
        
        int x = minX;
		for (int y = minY; y <= maxY; y++) {
            while (x < w) {
                if (!rendering || (y == maxY && x == maxX)) return;
				
                //Anti-aliasing implementation
                double[] color = {0, 0, 0};
                for (double ay = 0; ay < aa; ay++) {
                    for (double ax = 0; ax < aa; ax++) {
                        //Trace from this coordinate.
                        double[] d = this.tracePoint(
                                        x + (ax + (randomAA ? Math.random() : 0)) / aa,
                                        y + (ay + (randomAA ? Math.random() : 0)) / aa,
                                        w, h,
                                        z, r);
                        color[0] += d[0];
                        color[1] += d[1];
                        color[2] += d[2];
                    }
                }
                //Average samples.
                color[0] /= aa2;
                color[1] /= aa2;
                color[2] /= aa2;
                
                im.setRGB(x, y, toRGB(color));
                if (c != null)
                    c.repaint();         
                
                x++;
			}
            x = 0;
		}
	}
	
	/**
	 * Returns the color result for the given pixel (x, y) by tracing the ray
	 * emitted from that coordinate.
	 */
	private double[] tracePoint(double x, double y, int w, int h, double z, double r) {
		Ray p = new Ray(
				this.cam.pos,
				new Vector3D(
						(x / w) - .5,
						r * ((y / h) - .5),
						z
				).transform(this.cam.basis).normalize()			
		);
		double[] result = this.traceRay(p, 0, this.mirrorDepth).mul(255).toArray();
		//Clamp results to be no greater than 255.
		return new double[] {
			Math.min(result[0], 255),
			Math.min(result[1], 255),
			Math.min(result[2], 255)
		};
	}

	/**
	 * Traces the given Ray throughout the Scene, returning an accumulater
	 * color value.
	 */
	private Vector3D traceRay(Ray ray, double t0, int depth) {
		//Nearest-hit
		double tmin = Double.POSITIVE_INFINITY;
		Surface cShape = null;
        HitRecord hrmin = null;
        
        HitRecord hr;
        double t;
		for (Surface shape : this.shapes) {
            hr = new HitRecord();
			t = shape.rayIntersect(ray, t0, Double.POSITIVE_INFINITY, hr);
			if (t < tmin) {
				tmin = t;
				cShape = shape;
                hrmin = hr;
			}
		}
        
		if (cShape != null) {
			//Lighting
			Vector3D intersection = ray.getPoint(tmin);
			Vector3D surfaceNormal = cShape.getNormal(intersection, hrmin);
			Vector3D color = new Vector3D(cShape.am).mul(this.ambientIntensity);
			for (Light light : this.lights) {
				color = color.add(
						light.resultColor(
								ray, intersection, surfaceNormal, cShape, this.shapes,
								this.diffuse, this.specular, this.shadows
						)
				);
			}
			//Mirror-Reflection
			if (depth == 0)	return color;
			else
				return color.add(
					new Vector3D(cShape.sm)
						.mul(this.traceRay(
								new Ray(intersection, ray.dir.sub(surfaceNormal.mul(2 * ray.dir.dot(surfaceNormal))).normalize()),
								.001,
								depth - 1
							))
					);
		}
		else return new Vector3D(0, 0, 0);
	}
	
}

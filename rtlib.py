'''
Created on Apr 5, 2010
@author: Stephen Elliott
'''
import math
import Camera, DirectionalLight, Plane, PointLight, Scene, Sphere, Vector3D, RayTracePanel, Triangle
from array import array
from javax.swing import JFrame, JMenuBar, JMenu, JMenuItem, JFileChooser, UIManager, ImageIcon, KeyStroke, JScrollPane, SwingUtilities, JApplet
from javax.swing.filechooser import FileNameExtensionFilter
from java.io import File
from java.awt.event import KeyEvent, ActionEvent
from java.lang import Thread

class Vertex(object):
    '''Vertex object used to store vertices as they're read in from
    the scene file.'''
    def __init__(self, x, y, z, dx, dy, dz):
        self.pos = Vector3D(x, y, z)
        self.dir = Vector3D(dx, dy, dz)

class Painter(Thread):
    def __init__(self, canvas, scene, *comps):
        self.canvas = canvas
        self.scene = scene
        self.comps = comps
    
    def run(self):
        for c in self.comps: c.setEnabled(not c.isEnabled())
        self.canvas.changeScene(self.scene)
        for c in self.comps: c.setEnabled(not c.isEnabled())
		
class RTApplet(JApplet):
    def init():
        canvas = RayTracePanel(w, h, aa, threads)
        self.add(canvas)
        
        #Save FileChooser
        fcS = JFileChooser()
        fcS.addChoosableFileFilter(FileNameExtensionFilter('Windows Bitmap (*.bmp)', ['bmp']))
        fcS.addChoosableFileFilter(FileNameExtensionFilter('JPEG / JFIF (*.jpg)', ['jpg']))
        fcS.addChoosableFileFilter(FileNameExtensionFilter('Portable Network Graphics (*.png)', ['png']))
        def saveFile(event):
            '''Performed when the save button is pressed'''
            result = fcS.showSaveDialog(frame)
            if result == JFileChooser.APPROVE_OPTION:
                file = fcS.getSelectedFile()
                fname = file.getPath()
                ext = fcS.getFileFilter().getExtensions()[0]
                if not fname.endswith('.' + ext):
                    file = File(fname + '.' + ext)
                canvas.saveToFile(file, ext)
        
        #Open FileChooser
        fcO = JFileChooser()
        fcO.addChoosableFileFilter(FileNameExtensionFilter('RayTrace Scene File (*.rts)', ['rts']))
        def openFile(event):
            '''Performed when the open button is pressed'''
            result = fcO.showOpenDialog(frame)
            if result == JFileChooser.APPROVE_OPTION:
                fname = fcO.getSelectedFile().getPath()
                if fname.endswith('.rts'):
                    f = open(fname, 'rb')
                    newScene = SceneFactory().buildScene(f)
                    f.close()
                    Painter(canvas, newScene, openButton, saveButton, stopButton).start()
            
        def stop(event):
            '''Peformed when the stop button is pressed'''
            canvas.stopRendering()
            
        #Setup Menu
        menuBar = JMenuBar()
        menu = JMenu("File")
        menuBar.add(menu)
        openButton = JMenuItem("Open...", actionPerformed=openFile)
        openButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK))
        menu.add(openButton)
        saveButton = JMenuItem("Save as...", actionPerformed=saveFile)
        saveButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK))
        menu.add(saveButton)
        menu.addSeparator()
        stopButton = JMenuItem('Stop Render', actionPerformed=stop)
        stopButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        stopButton.setEnabled(False)
        menu.add(stopButton)
        menu.addSeparator()
        closeButton = JMenuItem('Close', actionPerformed=exit)
        closeButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK))
        menu.add(closeButton)
        self.setJMenuBar(menuBar)

def run(scene, w=512, h=512, aa=1, threads=1):
        '''Create GUI and perform ray-tracing.'''
        #Make Swing not look like garbage (so much)
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        #Setup frame
        frame = JFrame(
            "RayTracer",
            defaultCloseOperation=JFrame.EXIT_ON_CLOSE,
            size=(w, h)
        )
        frame.setIconImage(ImageIcon('resources/icon.png').getImage())
        canvas = RayTracePanel(w, h, aa, threads)
        frame.getContentPane().add(JScrollPane(canvas))
        
        #Save FileChooser
        fcS = JFileChooser()
        fcS.addChoosableFileFilter(FileNameExtensionFilter('Windows Bitmap (*.bmp)', ['bmp']))
        fcS.addChoosableFileFilter(FileNameExtensionFilter('JPEG / JFIF (*.jpg)', ['jpg']))
        fcS.addChoosableFileFilter(FileNameExtensionFilter('Portable Network Graphics (*.png)', ['png']))
        def saveFile(event):
            '''Performed when the save button is pressed'''
            result = fcS.showSaveDialog(frame)
            if result == JFileChooser.APPROVE_OPTION:
                file = fcS.getSelectedFile()
                fname = file.getPath()
                ext = fcS.getFileFilter().getExtensions()[0]
                if not fname.endswith('.' + ext):
                    file = File(fname + '.' + ext)
                canvas.saveToFile(file, ext)
        
        #Open FileChooser
        fcO = JFileChooser()
        fcO.addChoosableFileFilter(FileNameExtensionFilter('RayTrace Scene File (*.rts)', ['rts']))
        def openFile(event):
            '''Performed when the open button is pressed'''
            result = fcO.showOpenDialog(frame)
            if result == JFileChooser.APPROVE_OPTION:
                fname = fcO.getSelectedFile().getPath()
                if fname.endswith('.rts'):
                    f = open(fname, 'rb')
                    newScene = SceneFactory().buildScene(f)
                    f.close()
                    Painter(canvas, newScene, openButton, saveButton, stopButton).start()
                    
        def exit(event):
            '''Performed when the exit button is pressed'''
            import sys
            sys.exit(0)
            
        def stop(event):
            '''Peformed when the stop button is pressed'''
            canvas.stopRendering()
            
        
        #Setup Menu
        menuBar = JMenuBar()
        menu = JMenu("File")
        menuBar.add(menu)
        openButton = JMenuItem("Open...", actionPerformed=openFile)
        openButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK))
        menu.add(openButton)
        saveButton = JMenuItem("Save as...", actionPerformed=saveFile)
        saveButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK))
        menu.add(saveButton)
        menu.addSeparator()
        stopButton = JMenuItem('Stop Render', actionPerformed=stop)
        stopButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        stopButton.setEnabled(False)
        menu.add(stopButton)
        menu.addSeparator()
        closeButton = JMenuItem('Close', actionPerformed=exit)
        closeButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK))
        menu.add(closeButton)
        frame.setJMenuBar(menuBar)
        
        #Finish initializing GUI
        self.pack()
        #frame.setLocationRelativeTo(None)
        #frame.setVisible(True)
        
        #Perform ray-tracing
        #if scene is not None:
        #    Thread(Painter(canvas, scene, openButton, saveButton, stopButton)).start()
    
class SceneFactory(object):
    '''
        State machine which builds a new instance of a ray-traceable Scene object.
        The only public method is SceneFactory.buildScene(File), which is used
        to generate a scene from a file object.
    '''
    def __init__(self):
        self.vertices = []
        self.curAmbiMat = listToArray([.2, .2, .2])
        self.curDiffMat = listToArray([1.0, 1.0, 1.0])
        self.curSpecMat = listToArray([1.0, 1.0, 1.0, 64])
        self.shapes = []
        self.settings = {
            'd': True,
            's': True,
            'a': True,
            'm': 1,
            'i': 1.0
        }
        self.lights = []
        self.camera = (self.makeCamera, {'v': Vertex(0, 0, 0, 0, 0, -1)})
    
    def parseVertex(self, *args): self.vertices.append(Vertex(*[float(a) for a in args]))
    
    def parseAmbiMat(self, *args): self.curAmbiMat = listToArray([float(a) for a in args])
    def parseDiffMat(self, *args): self.curDiffMat = listToArray([float(a) for a in args])
    def parseSpecMat(self, *args): self.curSpecMat = listToArray([float(a) for a in args])
    
    def parseTriangle(self, i, j, k): self.shapes.append((self.makeTriangle, [int(i), int(j), int(k), self.curAmbiMat, self.curDiffMat, self.curSpecMat]))
    def makeTriangle(self, i, j, k, a, d, s):
        vi = self.vertices[i]
        vj = self.vertices[j]
        vk = self.vertices[k]
        return Triangle(vi.pos, vj.pos, vk.pos, vi.dir.normalize(), vj.dir.normalize(), vk.dir.normalize(), a, d, s)
        
    def parseSphere(self, i): self.shapes.append((self.makeSphere, [int(i), self.curAmbiMat, self.curDiffMat, self.curSpecMat]))
    def makeSphere(self, i, a, d, s):
        v = self.vertices[i]
        return Sphere(v.pos, v.dir.magnitude(), a, d, s)
    
    def parsePlane(self, i): self.shapes.append((self.makePlane, [int(i), self.curAmbiMat, self.curDiffMat, self.curSpecMat]))
    def makePlane(self, i, a, d, s):
        v = self.vertices[i]
        return Plane(v.pos, v.dir, a, d, s)
    
    def parseSettings(self, d, s, a, m, i):
        self.settings['d'] = d == 'd'
        self.settings['s'] = s == 's'
        self.settings['a'] = a == 'a'
        self.settings['m'] = int(m)
        self.settings['i'] = float(i)
    
    def parsePointLight(self, i, I): self.lights.append((self.makePointLight, [int(i), float(I)]))
    def makePointLight(self, i, I): return PointLight(self.vertices[i].pos, I)
    
    def parseDirecLight(self, i, I): self.lights.append((self.makeDirecLight, [int(i), float(I)]))
    def makeDirecLight(self, i, I): return DirectionalLight(self.vertices[i].dir, I)
        
    def parseCamera(self, i): self.camera = self.makeCamera, {'i': int(i)}
    def makeCamera(self, i=0, v=None):
        if v is not None:
            return Camera(v.pos, v.dir)
        else:
            i = self.vertices[i]
            return Camera(i.pos, i.dir)
            
    def buildScene(self, file):
        '''Makes a new scene from the given file.'''
        switch = {
            'vv': self.parseVertex,
            'am': self.parseAmbiMat,
            'dm': self.parseDiffMat,
            'sm': self.parseSpecMat,
            'ts': self.parseTriangle,
            'ss': self.parseSphere,
            'ps': self.parsePlane,
            'se': self.parseSettings,
            'pl': self.parsePointLight,
            'dl': self.parseDirecLight,
            'cc': self.parseCamera
        }
        
        def f(x):
            input = x.split()
            if input and not input[0].startswith('#'):
                try:
                    switch[input[0][:2]](*input[1:])
                except KeyError:
                    print 'No build-case for "' + input[0] + '"'
        map(f, file)
        
        def g(x): return x[0](*x[1])
        scene = Scene(
            self.camera[0](**self.camera[1]),
            map(g, self.lights),
            map(g, self.shapes),
            self.settings['d'],
            self.settings['s'],
            self.settings['a'],
            self.settings['m'],
            self.settings['i'],
        ) 
        return scene

def listToArray(list):
    '''Converts a Python list to a Java array'''
    #array = Array.newInstance(DOUBLE, [len(list)])
    #for i in xrange(len(list)):
    #    array[i] = list[i]
    return array('d', list)
    
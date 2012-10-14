Stephen Elliott
CS4300 - Computer Graphics
HW Assignment 6 - Ray Tracer
*****************************

This program is NOT confirmed to run on the CCIS Linux machines in the WVH lab.
I spoke with CCIS Systems and they are in the process of having Jython installed on the lab machines, but I haven't heard from them since earlier this week. I have included instructions on how to aquire Jython for running this assignment. I have spoken with Professor Vona about this issue and he has Jython installed on his computer.

-----------------------------

This assignment needs Jython installed on your system in order to run. Jython is available for free at http://www.jython.org/ for any system running Java. You can also install Jython on Linux by running "apt-get install jython" from a terminal.

To run this assignment, on any machine with Jython installed, run the following from the command line:

    jython /path/to/assignment/raytracer.py [flags] < input
    
    Flags:
        -n : Run without any input from standard input.
        -a <n> : Anti-aliasing amount where n >= 1. Defaults to 1.
        -t <n> : Number of threads to render with where n >= 1. Defaults to 1.
        -d <x> <y> : Dimensions of the render. Defaults to 512 x 512.
        
Features implemented:
    -Spheres
    -Point Lights
    -Directional Lights
    -Ambient Lighting
    -Diffuse Shading
    -Specular Shading
    -Planes
    -Shadows
    -Triangles (Extra)
    -Mirror Reflection (Extra)
    -Anti-aliasing (Extra)
    -Threaded Rendering (Extra)
    -Save as image file (Extra)

Bugs:
    None that I know of.
    
Compiling (if you wish):
    The entire source is located in the src/ directory. To compile it, you will need a JDK installed on your system. There are two .py (Python) scripts which are not compiled and can be run as-is.
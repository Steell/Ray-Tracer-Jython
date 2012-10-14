from sys import stdin, argv
from raytracing.raytracing import SceneFactory, run

w = 512
h = 512
aa = 1
threads = 1
f = stdin

x = 1
while x < len(argv):
    if argv[x].startswith('-n'):
        f = []
    if argv[x] in ['-d', '-nd']:
        if x+2 > len(argv):
            raise ValueError('-d flag requires two arguments: length and width')
        else:
            w = int(argv[x+1])
            h = int(argv[x+2])
            x += 2
    elif argv[x] in ['-a', '-na']:
        if x+1 > len(argv):
            raise ValueError('-a flag requires one argument: anti-aliasing amount')
        else:
            aa = int(argv[x+1])
            x += 1
    elif argv[x] in ['-t', '-nt']:
        if x+1 > len(argv):
            raise ValueError('-t flag requires one argument: number of threads')
        else:
            threads = int(argv[x+1])
            x += 1
    x += 1

#Build our scene from standard input
scene = SceneFactory().buildScene(f) if f else None
run(scene, w, h, aa, threads)
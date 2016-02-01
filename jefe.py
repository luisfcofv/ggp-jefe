import sys, getopt
from subprocess import call


def jefe(argv):
    port = 9147

    try:
      opts, args = getopt.getopt(argv,"p:",["port="])
    except getopt.GetoptError:
      print 'usage: jefe.py -p <port>'
      sys.exit(2)

    for opt, arg in opts:
      if opt == '-p':
         port = arg

    call(["./gradlew", "playerRunner", "-Pport=%s" %port, "-Pgamer=JefeGamer"])

if __name__ == "__main__":
    jefe(sys.argv[1:])

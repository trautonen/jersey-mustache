#!/usr/bin/env python
import os
import subprocess
import sys

options = sys.argv[1::]
goals = ["clean"]

pullrequest = os.environ["TRAVIS_PULL_REQUEST"]
version = subprocess.check_output("mvn help:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)'", shell = True).strip()

if pullrequest == "false" and version.endswith("-SNAPSHOT"):
  options.append("-Pprepare-deploy")
  goals.append("deploy")
else:
  goals.append("verify")

command = "mvn " + " ".join(options + goals)

sys.exit(subprocess.call(command, shell = True))

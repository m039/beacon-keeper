#!/bin/sh
#--------

mvn package -pl beacon-keeper-app -am android:deploy android:run

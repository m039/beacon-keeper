#!/bin/sh
#--------

mvn package -pl ibeacon-keeper-app -am android:deploy android:run

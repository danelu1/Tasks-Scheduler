#!/bin/bash

correct=0

echo "VMCHECKER_TRACE_CLEANUP"
date

cd ../src
rm -rf *.class

cp ../checker/skeleton/*.java .
cp -r ../checker/in .

# se compileaza tema
javac *.java &> build.txt

if [ ! -f Main.class ]
then
	echo "E: Nu s-a putut compila tema"
	cat build.txt
	echo "Total: 0/120"
	rm -rf build.txt
	exit
fi

rm -rf build.txt

java Main

rm -rf *.class
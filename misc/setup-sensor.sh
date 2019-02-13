#!/bin/bash
# Copyright 2019 Aleksander Gajewski <adiog@brainfuck.pl>
#   created:  Sun 10 Feb 2019 08:16:04 AM CET
#   modified: Wed 13 Feb 2019 10:00:50 AM CET

cd $(dirname $0)

echo "Please setup Android HotSpot on 2.4GHz band and connect the device via USB."
echo "Enter the network SSID [and press ENTER]:"
read SSID
echo "Enter the network password [and press ENTER]:"
read PASS
echo "Detecting the device IP..."
HOST=`sudo adb shell ifconfig | grep -A 1 wlan0 | tail -n 1 | awk '{print $2}' | cut -d: -f2`

[[ ! -e embed-esp-sensor ]] && git clone --recursive https://github.com/adiog/embed-esp-sensor

ESPINO=embed-esp-sensor/esp-project/esp-project.ino

echo "Setting up sensor ($SSID/$PASS with $HOST)... [press ENTER]"
read

sed -e "s#ssid = \"\(.*\)\"#ssid = \"$SSID\"#" -i ${ESPINO}
sed -e "s#password = \"\(.*\)\"#password = \"$PASS\"#" -i ${ESPINO}
sed -e "s#host\[16\] = \"\(.*\)\"#host[16] = \"$HOST\"#" -i ${ESPINO}

echo "Flashing the firmware... [press ENTER]"
read
(cd embed-esp-sensor/ && sudo ./build.sh)


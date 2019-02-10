// This file is a part of Humanoid project.
// Copyright (C) 2019 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import pl.brainfuck.humanoid.HumanContext;
import pl.brainfuck.humanoid.canvas.model.Segment;
import pl.brainfuck.humanoid.sensor.SensorData;

public class UdpServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[36];
    private HumanContext humanContext;
    private ClientMatcher clientMatcher = new ClientMatcher();

    public UdpServer(HumanContext humanContext) throws SocketException {
        socket = new DatagramSocket(4445);
        this.humanContext = humanContext;
    }

    private DatagramPacket getPacket() {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, port);

        return packet;
    }

    public void run() {
        running = true;

        SensorData sensorData = new SensorData();

        while (running) {
            DatagramPacket packet = getPacket();
            sensorData.parse(packet.getData());
            Segment segment = clientMatcher.match(packet.getAddress());
            humanContext.update(segment, sensorData);
        }

        socket.close();
    }
}
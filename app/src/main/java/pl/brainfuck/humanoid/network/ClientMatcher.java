// This file is a part of Humanoid project.
// Copyright (C) 2019 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import pl.brainfuck.humanoid.canvas.model.Segment;


class ClientMatcher {
    private Map<String, Segment> matching = new HashMap<String, Segment>();
    private Queue<Segment> parts = new LinkedList<Segment>();

    ClientMatcher() {
        parts.add(Segment.UpperLegRight);
        parts.add(Segment.LowerLegRight);
        parts.add(Segment.UpperLegLeft);
        parts.add(Segment.LowerLegLeft);
    }

    Segment match(InetAddress address) {
        String addressString = address.getHostAddress();
        Segment segment = matching.get(addressString);
        if (segment == null){
            segment = parts.poll();
            if (segment == null){
                segment = Segment.Head;
            }
            matching.put(addressString, segment);
            return segment;
        } else {
            return segment;
        }

    }

}

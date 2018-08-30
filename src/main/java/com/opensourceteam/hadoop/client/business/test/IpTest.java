package com.opensourceteam.hadoop.client.business.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpTest {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println(inetAddress.getHostName());
        System.out.println(inetAddress.getHostAddress());
        System.out.println(inetAddress.getCanonicalHostName());
    }
}

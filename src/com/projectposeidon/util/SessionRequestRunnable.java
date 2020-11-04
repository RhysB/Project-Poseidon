package com.projectposeidon.util;

public interface SessionRequestRunnable
{
    public void callback(int responseCode, String username, String uuid, String ip);
}

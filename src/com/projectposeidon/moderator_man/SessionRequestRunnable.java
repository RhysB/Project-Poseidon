package com.projectposeidon.moderator_man;

public interface SessionRequestRunnable
{
    public void callback(int responseCode, String username, String uuid, String ip);
}

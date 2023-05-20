package com.legacyminecraft.poseidon.util;

public class HTTPResponse
{
    private String response;
    private int responseCode;
    
    public HTTPResponse(String response, int responseCode)
    {
        this.response = response;
        this.responseCode = responseCode;
    }
    
    public String getResponse()
    {
        return response;
    }
    
    public int getResponseCode()
    {
        return responseCode;
    }
}

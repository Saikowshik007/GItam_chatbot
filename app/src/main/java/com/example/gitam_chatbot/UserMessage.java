package com.example.gitam_chatbot;

public class UserMessage{
    private String sent;
    private String receive;
    private boolean flag=false;
    public UserMessage(String sent,boolean c){ this.sent = sent;this.flag=true; }

    public UserMessage(boolean d, String receive){ this.receive=receive;}
    public String getSentMessage(){return sent;}
    public String getReceiveMessage(){return receive;}
    public String getSender(){if(flag){return "user"; } else {return "bot";}}
}

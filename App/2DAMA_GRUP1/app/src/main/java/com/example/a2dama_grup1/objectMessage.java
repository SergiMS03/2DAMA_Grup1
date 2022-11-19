package com.example.a2dama_grup1;

public class objectMessage {
    int id_missatge;
    int id_chat;
    int id_emisor;
    String missatge;

    public objectMessage(int id_missatge, int id_chat, int id_emisor, String missatge) {
        this.id_missatge = id_missatge;
        this.id_chat = id_chat;
        this.id_emisor = id_emisor;
        this.missatge = missatge;
    }
}

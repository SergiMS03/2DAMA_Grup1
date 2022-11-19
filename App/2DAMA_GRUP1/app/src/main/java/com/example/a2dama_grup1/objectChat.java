package com.example.a2dama_grup1;

import android.graphics.Bitmap;

public class objectChat {

    private int id_chat;
    private int id_venedor;
    private int id_comprador;
    private int id_producte;
    private String nom_producte;
    private String path_img;
    private Bitmap img;

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getNom_producte() {
        return nom_producte;
    }

    public void setNom_producte(String nom_producte) {
        this.nom_producte = nom_producte;
    }

    public String getPath_img() {
        return path_img;
    }

    public void setPath_img(String path_img) {
        this.path_img = path_img;
    }

    public int getId_chat() {
        return id_chat;
    }

    public void setId_chat(int id_chat) {
        this.id_chat = id_chat;
    }

    public int getId_venedor() {
        return id_venedor;
    }

    public void setId_venedor(int id_venedor) {
        this.id_venedor = id_venedor;
    }

    public int getId_comprador() {
        return id_comprador;
    }

    public void setId_comprador(int id_comprador) {
        this.id_comprador = id_comprador;
    }

    public int getId_producte() {
        return id_producte;
    }

    public void setId_producte(int id_producte) {
        this.id_producte = id_producte;
    }

    public objectChat(int id_chat, int id_venedor, int id_comprador, int id_producte, String nom_producte, String path_img) {
        this.id_chat = id_chat;
        this.id_venedor = id_venedor;
        this.id_comprador = id_comprador;
        this.id_producte = id_producte;
        this.nom_producte = nom_producte;
        this.path_img = path_img;
    }
}

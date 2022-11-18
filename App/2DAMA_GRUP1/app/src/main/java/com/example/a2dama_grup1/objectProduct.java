package com.example.a2dama_grup1;

import android.graphics.Bitmap;

public class objectProduct {

    int id_producte;
    String nom_producte;
    float preu;
    int stock;
    String descripcio;
    String pathImg;
    Bitmap img;
    int id_vendedor;


    public objectProduct(int id_producte, String nom_producte, float preu, int stock, String descripcio, String pathImage, Bitmap img, int id_vendedor){
        this.id_producte = id_producte;
        this.nom_producte = nom_producte;
        this.preu = preu;
        this.stock = stock;
        this.descripcio = descripcio;
        this.pathImg = pathImage;
        this.img = img;
        this.id_vendedor = id_vendedor;
    }

    public objectProduct(int id_producte, String nom_producte, float preu, int stock, String descripcio, String pathImage, int id_vendedor){
        this.id_producte = id_producte;
        this.nom_producte = nom_producte;
        this.preu = preu;
        this.stock = stock;
        this.descripcio = descripcio;
        this.pathImg = pathImage;
        this.img = null;
        this.id_vendedor = id_vendedor;
    }

    public objectProduct(){
    }

    public String priceToString(){
        String price = preu + "€";
        return price;
    }

    public String stockToString(){
        String stock = Integer.toString(this.stock);
        return stock;
    }

    public String idVenedorToString(){
        String id_venedor = Integer.toString(this.id_vendedor);
        return id_venedor;
    }

    public int getId_producte() {
        return id_producte;
    }

    public void setId_producte(int id_producte) {
        this.id_producte = id_producte;
    }

    public String getNom_producte() {
        return nom_producte;
    }

    public void setNom_producte(String nom_producte) {
        this.nom_producte = nom_producte;
    }

    public float getPreu() {
        return preu;
    }

    public void setPreu(float preu) {
        this.preu = preu;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

}

package com.example.a2dama_grup1;

import java.io.Serializable;

public class objectUser implements Serializable {

    int id_usuari;
    String nom;
    String cognoms;
    String email;
    String rol;
    String descripcio;
    String tel;

    public objectUser(int id_usuari, String nom, String cognoms, String email, String rol, String descripcio, String tel){
        this.id_usuari = id_usuari;
        this.nom = nom;
        this.cognoms = cognoms;
        this.email = email;
        this.rol = rol;
        this.descripcio = descripcio;
        this.tel = tel;
    }




}

package com.ob.xspeedit.model;

import com.ob.xspeedit.util.Constantes;

public class Carton {

    public Carton(){
        this.placeRestante = Constantes.MAX_CAPACITE;
        this.remplissage = "";
    }

    public Carton(Article article){
        this.placeRestante = Constantes.MAX_CAPACITE - article.poids;
        this.remplissage=""+article.poids;
    }

    public int placeRestante;
    public String remplissage;

}

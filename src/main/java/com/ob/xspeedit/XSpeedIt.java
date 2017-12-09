package com.ob.xspeedit;


import com.ob.xspeedit.model.Article;
import com.ob.xspeedit.model.Carton;
import com.ob.xspeedit.util.Constantes;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Classe qui répond à un problème de bin packing 1d
 * L'algorithme brute force censé donner le meilleur résultat car il teste toutes les combinaisons possibles
 * a été écarté car beaucoup trop lent, voire infini selon l'input et les capacités de la machine
 */
public class XSpeedIt {

    /**
     * Algo Next Fit
     * Parcours les articles de gauche à droite
     * Ouvre une nouvelle caisse sil'article courant
     * ne peut pas rentrer dans la caisse courante
     * @param articles
     * @return
     */
    public List<Carton> nextFit(final List<Article> articles){
        ArrayList<Carton> result = new ArrayList<>();

        Carton carton=new Carton();
        for(Article article:articles){
            if (carton.placeRestante<article.poids){
                result.add(carton);
                carton = new Carton();
                carton.placeRestante = Constantes.MAX_CAPACITE - article.poids;
            }
            else {
                carton.placeRestante -= article.poids;
            }
            carton.remplissage+=""+article.poids;
        }
        result.add(carton);

        return result;
    }

    /**
     * Algo First Fit
     * Ouvre un carton
     * Parcours les articles de gauche à droite
     * en remplissant le carton s'il y a encore de la place pour l'article courant
     * Ouvre un nouveau carton
     * Et on recommence le parcours des articles qui restent
     * On itère jusqu'à ce que tous les articles soient dans des cartons
     * @param articles2
     * @return
     */
    public List<Carton> firstFit(final List<Article> articles2){
        ArrayList<Carton> result = new ArrayList<>();

        //on crée une copie des objets car on va modifier des attributs (isPacked)
        ArrayList<Article> articles = new ArrayList<>();
        for(Article article2:articles2){
            articles.add(new Article(article2.poids));
        }

        int nbIsPacked = 0;
        Carton carton=new Carton();
        while (nbIsPacked < articles.size()) {
            for (Article article : articles) {
                if (!article.isPacked) {
                    if (carton.placeRestante - article.poids >= 0) {
                        carton.placeRestante -= article.poids;
                        carton.remplissage += ""+article.poids;
                        article.isPacked = true;
                        nbIsPacked++;
                    }
                }
            }
            result.add(carton);
            carton = new Carton();
        }

        return result;
    }

    /**
     * Algo First Fit Decreasing
     * First Fit avec liste préalablement classée en ordre décroissant
     * @param articles
     * @return
     */
    public List<Carton> firstFitDecreasing(List<Article> articles){

        //classement par ordre décroissant de poids
        Collections.sort(articles,new Comparator<Article>() {
            @Override
            public int compare(Article a, Article b) {
                return Integer.valueOf(b.poids).compareTo(Integer.valueOf(a.poids));
            }
        });

       return this.firstFit(articles);
    }

    /**
     * Algo Best Fit
     * On mets tous les articles dans un carton
     * Puis on parcours la liste des articles de gauche à droite
     * Pour chaque article, on le retire de son carton et on le met dans le carton que cet article remplit le mieux
     * A la fin du parcours, on élimine les cartons vides
     * @param articles
     * @return
     */
    public List<Carton> bestFit(List<Article> articles){

        ArrayList<Carton> result = new ArrayList<>();

        //chaque article dans un carton
        ArrayList<Carton> cartons = new ArrayList<Carton>();
        for(Article article:articles){
            cartons.add(new Carton(article));
        }

        int index=0;
        for(Article article:articles){
            //on enlève l'article courant de son carton actuel
            cartons.get(index).placeRestante+=article.poids;
            cartons.get(index).remplissage=cartons.get(index).remplissage.replaceFirst(""+article.poids,"");

            int maxPoidsCarton = 0;
            int indexCartonMax = 0;
            int indexParcoursCarton=0;
            for(Carton cartonCourant:cartons){
                if (article.poids<=cartonCourant.placeRestante
                        && Constantes.MAX_CAPACITE -cartonCourant.placeRestante+article.poids>=maxPoidsCarton){
                    maxPoidsCarton = Constantes.MAX_CAPACITE -cartonCourant.placeRestante+article.poids;
                    indexCartonMax = indexParcoursCarton;
                    //si un carton est rempli, on passe à l'article suivant
                    if (maxPoidsCarton == Constantes.MAX_CAPACITE) break;
                }
                indexParcoursCarton++;
            }
            //on affecte l'article au meilleur carton
            cartons.get(indexCartonMax).placeRestante-=article.poids;
            cartons.get(indexCartonMax).remplissage+=""+article.poids;

            index++;
        }

        int nbCartons = 0;
        for(Carton carton:cartons){
            //on élimine les cartons vides
            if (carton.placeRestante<Constantes.MAX_CAPACITE){
                result.add(carton);
            }
        }

        return result;
    }

    /**
     * Transforme l'input qui est sous forme d'une String
     * en une liste d'articles
     * @param items
     * @return
     */
    public List<Article> transformerStringEnListeArticles(String items){
        ArrayList<Article> chars = new ArrayList<Article>();
        for (char c : items.toCharArray()) {
            chars.add(new Article(Integer.parseInt(String.valueOf(c))));
        }
        return chars;
    }

    /**
     * Renvoie une liste de cartons sous forme d'une String affichable
     * @param cartons
     * @return
     */
    public String afficherResultat(List<Carton> cartons){
        StringBuilder resultat = new StringBuilder("");
        if (cartons == null || cartons.size()==0){
            resultat.append("0 carton");
        }
        else{
            for(Carton carton:cartons){
                resultat.append(carton.remplissage).append(Constantes.SEPARATEUR);
            }
            //on enlève le dernier "/"
            resultat.deleteCharAt(resultat.length()-1)
                    .append(" => ").append(cartons.size()).append(" cartons");
        }
        return resultat.toString();
    }

    public static void main(String[] args) {
        String input = "";
        String pattern = "^[1-9]+$";
        if (args.length == 1) {
            input = args[0];
            if(!Pattern.matches(pattern, input)){
                System.out.println("Format invalide");
                System.out.println("Veuillez saisir une suite de chiffres compris entre 1 et 9  (ex : 1495873)");
                System.exit(1);
            }
        }
        else{
            Scanner reader = new Scanner(System.in);  // Reading from System.in
            System.out.println("Veuillez saisir une suite de chiffres compris entre 1 et 9  (ex : 1495873) : ");
            while (reader.hasNext()){
                input = reader.next();
                if(Pattern.matches(pattern, input)){
                    break;
                }
                System.out.println("Format invalide");
                System.out.println("Veuillez saisir une suite de chiffres compris entre 1 et 9  (ex : 1495873) : ");
            }
            reader.close();
        }

        XSpeedIt xs = new XSpeedIt();

        System.out.println("Rappel input : "+input);

        List<Article> articlesInput=xs.transformerStringEnListeArticles(input);

        System.out.println("**************Robot actuel**************");
        System.out.println("algo NextFit : "+xs.afficherResultat(xs.nextFit(articlesInput)));
        System.out.println("**************Robot optimisé**************");
        System.out.println("algo FirstFit : "+xs.afficherResultat(xs.firstFit(articlesInput)));
        System.out.println("algo FirstFitDecreasing : "+xs.afficherResultat(xs.firstFitDecreasing(articlesInput)));
        System.out.println("algo BestFit : "+xs.afficherResultat(xs.bestFit(articlesInput)));
    }

}

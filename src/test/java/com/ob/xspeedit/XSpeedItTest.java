package com.ob.xspeedit;

import com.ob.xspeedit.model.Article;
import com.ob.xspeedit.model.Carton;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class XSpeedItTest {

    private XSpeedIt xs;

    @Before
    public void instancie_xspeedit(){
       xs = new XSpeedIt();
    }

    @Test
    public final void test_transfo_string_longueur_2_en_liste(){
        List<Article> articles=xs.transformerStringEnListeArticles("12");
        assertEquals(articles.size(),2);
    }

    @Test
    public final void test_affichage_liste_cartons(){
        ArrayList<Carton> cartons = new ArrayList<>();
        cartons.add(new Carton());
        assertTrue(xs.afficherResultat(cartons).contains("carton"));
    }

    @Test
    public final void test_algo_next_fit_with_163841689525773(){
        List<Article> articlesInput=xs.transformerStringEnListeArticles("163841689525773");
        assertEquals(xs.nextFit(articlesInput).size(),10);
    }

    @Test
    public final void test_algo_first_fit_with_163841689525773(){
        List<Article> articlesInput=xs.transformerStringEnListeArticles("163841689525773");
        assertEquals(xs.firstFit(articlesInput).size(),8);
    }

    @Test
    public final void test_algo_first_fit_decreasing_with_163841689525773(){
        List<Article> articlesInput=xs.transformerStringEnListeArticles("163841689525773");
        assertEquals(xs.firstFitDecreasing(articlesInput).size(),8);
    }

    @Test
    public final void test_algo_best_fit_with_163841689525773(){
        List<Article> articlesInput=xs.transformerStringEnListeArticles("163841689525773");
        assertEquals(xs.bestFit(articlesInput).size(),8);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Grafo;

/**
 *
 * @author ThiagoH
 */
public class Persistencia {
    
    //String caminho = "";
    
    public static boolean salvaGrafo(Grafo grafo,String caminho)
    {
        //limpaArquivo(caminho);
        Gson gson = new GsonBuilder().create();
        try 
        {
            Writer writer = new FileWriter(caminho);
            gson.toJson(grafo,writer);
            writer.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static Grafo getGrafo(String caminho)
    {
        Gson gson = new Gson();
        Grafo g = null;
        try 
        {
            g = gson.fromJson(new FileReader(caminho),Grafo.class);
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        return g;
        
    }
    
    public static void limpaArquivo(String caminho)
    {
        PrintWriter writer;
        try 
        {
            writer = new PrintWriter(caminho);
            writer.print("");
            writer.close();
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

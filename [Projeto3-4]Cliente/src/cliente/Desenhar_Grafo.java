package cliente;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import shared.Aresta;
import shared.Grafo;
import shared.Vertice;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ThiagoH
 */
public class Desenhar_Grafo extends JFrame{
    
    //Grafo grafo;
    List<Aresta> arestas;
    List<Vertice> vertices;
    public Desenhar_Grafo(List<Aresta> arestas,List<Vertice> vertices){
        super("Grafo");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(1366,768);
        this.setVisible(true);
        this.arestas = arestas;
        this.vertices = vertices;


    }
    public void paint(Graphics g){
        
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, this.getSize().width, this.getSize().height);
       
        desenhar_vertices(g2d);

    }

   
    public void desenhar_vertices(Graphics2D g){
        
        if(vertices.size() <= 0) return;
        g.setFont(new Font("TimesRoman", Font.BOLD, 20));
        
        int i = 0,j,aux;
        Aresta aresta;
        //Gerando as coordenadas aleatorias.
        int coordenadasx[] = new int[vertices.size()];
        int coordenadasy[] = new int[vertices.size()];
  
        Random gerador = new Random();
  
        while(i < vertices.size()){
            aux = 0;
            coordenadasx[i] = 50 + gerador.nextInt(980);
            coordenadasy[i] = 50 + gerador.nextInt(650);
            
            for(j=0 ; j<i ; j++){
                if((coordenadasx[i] > coordenadasx[j]-50 && coordenadasx[i] < coordenadasx[j] + 50) && (coordenadasy[i] > coordenadasy[j] - 50 && coordenadasy[i] < coordenadasy[j] + 50)){
                    aux = 1;
                }
            }
            
            if(aux == 0){
                i++;
            }
        }
        
        if(arestas.size() > 0)
        {
            for(Aresta a : arestas)
            {
                g.setPaint(Color.BLACK);//desenhando as linhas
                //pega coordenadas v1
                int indexOrigem = getIdByVertice(a.getVertice1());
                //pega coordenadas v2
                int indexDestino = getIdByVertice(a.getVertice2());
           
                g.draw(new Line2D.Double(coordenadasx[indexOrigem]+25,coordenadasy[indexOrigem]+25,coordenadasx[indexDestino]+25,coordenadasy[indexDestino]+25));
                
                g.setPaint(Color.BLUE); //Desenhando os pesos e nome
                g.drawString("Nota:"+a.getPeso(), ((coordenadasx[indexOrigem] + coordenadasx[indexDestino]) / 2) + 25, ((coordenadasy[indexOrigem] + coordenadasy[indexDestino]) / 2)+25);
                //g.drawPolygon(new int[] {10, 20, 30}, new int[] {100, 20, 100}, 3);
            }
        }
        
        g.setPaint(Color.BLACK);//desenhando os vertices
        for(i=0 ; i<vertices.size() ; i++) //Desenhando os vertices
        {
            g.setPaint(geraCor(vertices.get(i).cor));
            g.fill(new Ellipse2D.Double(coordenadasx[i],coordenadasy[i],50,50)); //desenhando os circulos
            
            g.setPaint(Color.WHITE);
            g.drawString(""+vertices.get(i).getId(), coordenadasx[i] + 20, coordenadasy[i] + 30); //desenhando o nro dos vertices
            g.setPaint(Color.BLACK);
            g.drawString(""+vertices.get(i).getDesc(), coordenadasx[i] + 50, coordenadasy[i] + 30); //desenhando o nome dos vertices
        }
    }
    public Color geraCor(int cor)
    {
        cor = cor % 13;
        switch(cor)
        {
            case 1:
                return Color.BLACK;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.CYAN;
            case 4:
                return Color.DARK_GRAY;
            case 5:
                return Color.GRAY;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.LIGHT_GRAY;
            case 8:
                return Color.MAGENTA;
            case 9:
                return Color.ORANGE;
            case 10:
                return Color.PINK;
            case 11:
                return Color.RED;
            case 12:
                return Color.WHITE;
            case 13:
                return Color.YELLOW;
        }
        return Color.BLACK;
    }
    
    public int getIdByVertice(int id)
  {
      int i = 0;
      for(Vertice v : vertices)
      {
          if(id == v.id) return i;
          i++;
      }
      return 0;
  }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.util.List;
import shared.*;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class ClienteSD {
    private GrafoDB.Client client;
    private TTransport transport;
    
    public ClienteSD()
    {
        try 
        {
            
      
            transport = new TSocket("localhost", 9090);
            transport.open();
     
            TProtocol protocol = new  TBinaryProtocol(transport);
            client = new GrafoDB.Client(protocol);
            //GrafoDB.Client client2 = new GrafoDB.Client(protocol);

            //perform(client);
            //perform(client2);

            
        } 
        catch (TException x) 
        {
            x.printStackTrace();
        }
    }
    
    public GrafoDB.Client getClient()
    {
        return this.client;
    }
    public void fechaConexao()
    {
        try
        {
            transport.close();
        }
        catch(Exception x) 
        {
            x.printStackTrace();
        }
    }
    /*public static void main(String [] args) {
      
    }
  
    
    private static void perform(GrafoDB.Client client) throws TException
    {
        boolean retornoPing = client.ping();
        System.out.println("ping() " + retornoPing);
        
        Vertice v = new Vertice();
        v.setDesc("Vertice1");
        v.setCor(1);
        v.setId(1);
        v.setPeso(2.0);
        
        Vertice v2 = new Vertice();
        v2.setDesc("Vertice2");
        v2.setCor(1);
        v2.setId(2);
        v2.setPeso(2.0);
        
        Vertice v3 = new Vertice();
        v3.setDesc("Vertice3");
        v3.setCor(1);
        v3.setId(3);
        v3.setPeso(2.0);
        
        /*Vertice v4 = new Vertice();
        v4.setDesc("Vertice4");
        v4.setCor(1);
        v4.setId(4);
        v4.setPeso(2.0);
        
        Aresta a = new Aresta();
        a.setDesc("Aresta1");
        a.setDirecionado(false);
        a.setVertice1(1);
        a.setVertice2(2);
        a.setPeso(1.0);
        
        Aresta a2 = new Aresta();
        a2.setDesc("Aresta2");
        a2.setDirecionado(false);
        a2.setVertice1(2);
        a2.setVertice2(3);
        a2.setPeso(2.0);
        
        Aresta a3 = new Aresta();
        a2.setDesc("Aresta3");
        a2.setDirecionado(false);
        a2.setVertice1(1);
        a2.setVertice2(3);
        a2.setPeso(2.0);
        
        if(client.insereVertice(v))
        {
            System.out.println("INSERIU VERTICE");
        }
        if(client.insereVertice(v2))
        {
            System.out.println("INSERIU VERTICE");
        }
        
        try
        {
        if(client.insereAresta(a))
        {
            System.out.println("INSERIU ARESTA");
        }
        client.insereVertice(v3);
        client.insereAresta(a2);
                client.insereAresta(a3);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        client.getMenorCaminho(v, v3);
        Grafo g = client.getGrafo();
        g.print();
    } */
}

package servidorsd;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import java.util.HashMap;
import servidorsd.DBHandler;
import servidorsd.ServerUI;
import shared.*;


public class ServidorSD {

    public static DBHandler handler;

    public static GrafoDB.Processor processor;
    
    private String caminho = "";
    private int porta;
    
    public ServidorSD(String caminho,int porta)
    {
        this.caminho = caminho;
        this.porta = porta;

    }
    public void setCaminho(String caminho)
    {
        this.caminho = caminho;
    }
    
    public void setPorta(int porta)
    {
        this.porta = porta;
    }
    /*public static void main(String args[]) {
        try 
        {
            handler = new DBHandler();
            processor = new GrafoDB.Processor(handler);

            Runnable simple = new Runnable() {
                public void run() {
                simple(processor);
            }};      

            new Thread(simple).start();
            //new Thread(secure).start();
            
            /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerUI(super).setVisible(true);
            }
        });
        } 
        catch (Exception x) 
        {
            x.printStackTrace();
        }
        
        
        
        Vertice v = new Vertice();
        v.setDesc("Vertice1");
        v.setCor(1);
        v.setId(1);
        v.setPeso(2.0);
        
        handler.insereVertice(v);
        
        Vertice v2 = new Vertice();
        v2.setDesc("Vertice2");
        v2.setCor(1);
        v2.setId(2);
        v2.setPeso(2.0);
        handler.insereVertice(v2);
        
        Vertice v3 = new Vertice();
        v3.setDesc("Vertice3");
        v3.setCor(1);
        v3.setId(3);
        v3.setPeso(2.0);
        handler.insereVertice(v3);
        
        Vertice v4 = new Vertice();
        v4.setDesc("Vertice4");
        v4.setCor(1);
        v4.setId(4);
        v4.setPeso(2.0);
        handler.insereVertice(v4);
        
        Aresta a = new Aresta();
        a.setDesc("Aresta1");
        a.setDirecionado(false);
        a.setVertice1(1);
        a.setVertice2(2);
        a.setPeso(1.0);
        handler.insereAresta(a);
        
        Aresta a2 = new Aresta();
        a2.setDesc("Aresta2");
        a2.setDirecionado(false);
        a2.setVertice1(2);
        a2.setVertice2(3);
        a2.setPeso(5.0);
        handler.insereAresta(a2);
        
        Aresta a3 = new Aresta();
        a3.setDesc("Aresta3");
        a3.setDirecionado(false);
        a3.setVertice1(3);
        a3.setVertice2(4);
        a3.setPeso(1.0);
        handler.insereAresta(a3);
        
        Aresta a4 = new Aresta();
        a4.setDesc("Aresta4");
        a4.setDirecionado(false);
        a4.setVertice1(2);
        a4.setVertice2(4);
        a4.setPeso(1.0);
        handler.insereAresta(a4);
        
        //handler.getMenorCaminho(v, v3);
        
    }*/

    public void IniciaServer()
    {
        try 
        {
            if(porta == 9090)
            {
               handler = new DBHandler(caminho,0); //server id
            }
            else if(porta == 9091)
            {
               handler = new DBHandler(caminho,1); //server id
            }
            else
            {
               handler = new DBHandler(caminho,2); //server id
            }
            
            processor = new GrafoDB.Processor(handler);

            Runnable simple = new Runnable() {
                public void run() {
                simple(processor);
            }};      

            new Thread(simple).start();
            //new Thread(secure).start();
            
            /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerUI(super).setVisible(true);
            }
        });*/
        } 
        catch (Exception x) 
        {
            x.printStackTrace();
        }
    }
    public void simple(GrafoDB.Processor processor) {
        
        try 
        {
            TServerTransport serverTransport = new TServerSocket(porta);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("[SERVER] Starting server...");
            server.serve();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
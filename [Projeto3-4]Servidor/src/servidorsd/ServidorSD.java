
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
    private int idServidor;
    
    public ServidorSD()
    {
    }
    
    public ServidorSD(String caminho,int porta)
    {
        this.caminho = caminho;
        this.porta = porta;

    }
    public void setCaminho(String caminho)
    {
        this.caminho = caminho;
    }
    
    public void setId(int idServidor)
    {
        this.idServidor = idServidor;
    }
    
    public void setPorta(int porta)
    {
        this.porta = porta;
    }

    public void IniciaServer()
    {
        try 
        {
            handler = new DBHandler(caminho,idServidor); //server id

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

            System.out.println("[SERVER] Starting server... |"+ idServidor+ ":" +porta );
            server.serve();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsd;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import shared.Aresta;
import shared.GrafoDB;
import shared.Vertice;

/**
 *
 * @author ThiagoH
 */
public class ClienteAux {
    
    private static GrafoDB.Client client;
    private static TTransport transport;
    
    public static boolean iniciaConexao(String URL)
    {
        try 
        {
            String[] serverURL = URL.split(":");
            transport = new TSocket(serverURL[0], Integer.parseInt(serverURL[1]));
            transport.open();
     
            TProtocol protocol = new  TBinaryProtocol(transport);
            client = new GrafoDB.Client(protocol); 
            return true;
        } 
        catch (TException x) 
        {
            x.printStackTrace();
            return false;
        }
    }
    
    public static void fechaConexao()
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
    
    public static boolean insereVertice(String URL,Vertice vert)
    {
        if(!iniciaConexao(URL)) return false;
        
        try
        {
            return client.insereVertice(vert);

        } catch (TException ex)
        {
           return false;
        }
        finally
        {
            fechaConexao();
        } 
    }
    
    public static boolean insereAresta(String URL,Aresta a)
    {
        if(!iniciaConexao(URL)) return false;
        
        try
        {
           return client.insereAresta(a);

        } catch (TException ex)
        {
           return false;
        }
        finally
        {
            fechaConexao();
        }
    }

    
    public static Vertice getVertice(String URL,int id)
    {
        if(!iniciaConexao(URL)) return null;
        
        try
        {
            return client.getVertice(id);

        } catch (TException ex)
        {
           return null;
        }
        finally
        {
            fechaConexao();
        } 
   }
   
    public static boolean removeVertice(String URL,Vertice v)
    {
        if(!iniciaConexao(URL)) return false;
        
        try
        {
            return client.removeVertice(v);

        } catch (TException ex)
        {
           return false;
        }
        finally
        {
            fechaConexao();
        } 
   }
   
    public static boolean removeArestaFromVertice(String URL, int id)
    {
        if(!iniciaConexao(URL)) return false;
        
        try
        {
            return client.removeArestaFromVertice(id);

        } catch (TException ex)
        {
           return false;
        }
        finally
        {
            fechaConexao();
        } 
    }
   
    public static boolean removeAresta(String URL,Aresta a)
    {
        if(!iniciaConexao(URL)) return false;
        
        try
        {
            return client.removeAresta(a);

        } catch (TException ex)
        {
           return false;
        }
        finally
        {
            fechaConexao();
        } 
    }
   
    public static boolean editaAresta(String URL,Aresta a)
    {
        if(!iniciaConexao(URL)) return false;
        
        try
        {
            return client.editaAresta(a);

        } catch (TException ex)
        {
           return false;
        }
        finally
        {
            fechaConexao();
        } 
    }
   
    public static boolean editaVertice(String URL,Vertice vert)
    {
        if(!iniciaConexao(URL)) return false;
        
        try
        {
            return client.editaVertice(vert);

        } catch (TException ex)
        {
           return false;
        }
        finally
        {
            fechaConexao();
        } 
    }
   
    public static List<Aresta> getArestas(String URL)
    {
        if(!iniciaConexao(URL)) return null;
        
        try
        {
            return client.getArestas(null);

        } catch (TException ex)
        {
           return null;
        }
        finally
        {
            fechaConexao();
        } 
    }
   
    public static List<Vertice> getVertices(String URL)
    {
        if(!iniciaConexao(URL)) return null;
        
        try
        {
            return client.getVertices(null);

        } catch (TException ex)
        {
           return null;
        }
        finally
        {
            fechaConexao();
        } 
    }
}

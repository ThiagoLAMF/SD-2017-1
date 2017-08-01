/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsd;

import io.atomix.catalyst.transport.Address;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.thrift.TException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.*;

public class DBHandler implements GrafoDB.Iface {

    private final int N_SERVERS = 3;
    private final String[][] serverURLs = {{"localhost:9000"},{"localhost:9001"},{"localhost:9002"}};
    /*private final String[][] serverURLs = {{"localhost:9000","localhost:9001","localhost:9002"},
                                            {"localhost:9001","localhost:9011","localhost:9012"},
                                            {"localhost:9002","localhost:9021","localhost:9022"},};*/
    
    private final Address[][] raftServerList = {{new Address("localhost",8000),new Address("localhost",8001),new Address("localhost",8002)},
                                                {new Address("localhost",8010),new Address("localhost",8011),new Address("localhost",8012)},
                                                {new Address("localhost",8020),new Address("localhost",8021),new Address("localhost",8022)}};
    
    private final int serverID; //ID do server corrente
    
    private Grafo grafo = new Grafo();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * ReadWriteLock -> ReadLocks podem ser acessadas em paralelo desde que 
     * uma WriteLock não está ativa.
     */
    private String caminho = "";
    

    public DBHandler(String caminho,int serverID) {
        //log = new HashMap<Integer, SharedStruct>();
        this.caminho = caminho;
        /*Grafo g = Persistencia.getGrafo(caminho);
        if(g == null || (g.getArestasSize() == 0 && g.getVerticesSize() == 0))
        {
            grafo = new Grafo();
        }
        else
        {
            grafo = g;
        }*/
        grafo = new Grafo();
        this.serverID = serverID;
        System.out.println("ID:"+serverID);
    }

    /**
     * Verifica o server em que o vértice será inserido.
     * @param nomeVertice
     * @return 
     */
    private int getServer(String nomeVertice)
    {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(nomeVertice.getBytes());
            int server = ByteBuffer.wrap(digest).getInt();
            if(server<0) server = server * -1;
            return server % N_SERVERS;
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            return -1;
        }
    }
    
    public boolean ping() {
        System.out.println("[DB] ping()");
        return true;
    }
    
    @Override
    public Vertice getVertice(int id){
        try
        {
            lock.readLock().lock();
            for(Vertice v : grafo.getVertices())
            {
                if(v.getId() == id)
                {
                    return v;
                }
            }
            return null;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    @Override
    public Aresta getAresta(int id1, int id2) {
        try
        {
            lock.readLock().lock();
            for(Aresta a : grafo.getArestas())
            {
                if(a.getVertice1() == id1 && a.getVertice2() == id2)
                {
                    return a;
                }
            }
            return null;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean insereVertice(Vertice vert){
        System.out.println("[DB] insereVertice()");
        
        int server = getServer(vert.getId() + "");
        
        System.out.println("[DB-"+serverID+"]Vertice irá ser inserido em:" + server);
        
        if(server != serverID) // é preciso inserir o vertice em outro servidor
        {
            return ClienteAux.insereVertice(serverURLs[server], vert);
        }
        
        // Vértice será inserido neste servidor
        try
        {
            lock.writeLock().lock();
            //Verifica se o vertice está no grafo
            if(grafo.getVerticesSize() > 0)
            {
                for(Vertice v : grafo.getVertices())
                {
                    if(v.getDesc().equals(vert.getDesc())) 
                    {
                        return false;
                    }
                    if(v.getId() == vert.getId()) 
                    {
                        return false;
                    }
                }
            }
            grafo.addToVertices(vert);
            //Persistencia.limpaArquivo(caminho);
            //Persistencia.salvaGrafo(grafo, caminho);
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public boolean insereAresta(Aresta arest) {
        System.out.println("[DB] insereAresta()");
        
        
        //Verificar a qual servidor pertence o primeiro vertice
        int server = getServer(arest.getVertice1() + "");
        
        if(server != serverID) // é preciso inserir a aresta em outro servidor
        {
            return ClienteAux.insereAresta(serverURLs[server], arest);
        }
        
        //Aresta será inserida no servidor corrente:
        //Verifica se os vértices estão no servidor:
        boolean flagVertice1 = false;
        boolean flagVertice2 = false;
        
        server = getServer(arest.getVertice2() + "");
        if(server != serverID) //Vertice 2 está em um servidor diferente
        {
            if(ClienteAux.getVertice(serverURLs[server], arest.getVertice2()) != null) 
                flagVertice2 = true;
        }
        else //Vertice 2 está no servidor corrente
        {
            if(this.getVertice(arest.getVertice2()) != null)
                flagVertice2 = true;
        }
         
        //Verificar se vertice 1 existe
        if(this.getVertice(arest.getVertice1()) != null)
            flagVertice1 = true;
        
        if(!flagVertice1 || !flagVertice2) return false;
            
        try
        {
            lock.writeLock().lock();

            grafo.addToArestas(arest);
            //Persistencia.limpaArquivo(caminho);
            //Persistencia.salvaGrafo(grafo, caminho);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeArestaFromVertice(int id){
        System.out.println("[DB] removeArestaFromVertice("+id+")");
         
        try
        {
            lock.writeLock().lock();
            List<Aresta> paraRemover = new ArrayList<>();
            for(Aresta a : grafo.getArestas() )
            {
                if(id == a.getVertice1() || id == a.getVertice2() )
                {
                    paraRemover.add(a);
                }
            }
            grafo.getArestas().removeAll(paraRemover);
 
            //Persistencia.limpaArquivo(caminho);
            //Persistencia.salvaGrafo(grafo, caminho);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean removeVertice(Vertice vert){
        System.out.println("[DB] removeVertice("+vert.getId()+")");
        
        int server = getServer(vert.getId() + "");
        
        if(server != serverID) // é preciso remover o vertice em outro servidor
        {
            return ClienteAux.removeVertice(serverURLs[server], vert);
        }
        
        //É preciso remover arestas de todos os servidores:
        /*for(String[] URLs : serverURLs)
        {
            if(URLs.compareTo(serverURLs[serverID]) != 0)
                    ClienteAux.removeArestaFromVertice(URLs, vert.getId());
        }*/
        for(int i =0;i<N_SERVERS;i++)
        {
            if(i != serverID)
            {
                ClienteAux.removeArestaFromVertice(serverURLs[i], vert.getId());
            }
        }
        
        try
        {
            lock.writeLock().lock();
            if(grafo.getArestasSize()> 0)
            {
                List<Aresta> paraRemover = new ArrayList<>();
                for(Aresta a : grafo.getArestas() )
                {
                    if(vert.getId() == a.getVertice1() || vert.getId() == a.getVertice2() )
                    {
                        paraRemover.add(a);
                    }
                }
                grafo.getArestas().removeAll(paraRemover);
            }
            if(grafo.getVerticesSize() > 0)
            {
                boolean retorno = grafo.getVertices().remove(vert);
                //Persistencia.limpaArquivo(caminho);
                //Persistencia.salvaGrafo(grafo, caminho);
                return retorno;
            }
        }
        catch(Exception e)
        {
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
        
        return false;
    }

    @Override
    public boolean removeAresta(Aresta arest){
        System.out.println("[DB] removeAresta()");
        
        int server = getServer(arest.getVertice1() + "");
        
        if(server != serverID) //Aresta esta em outro server
        {
            return ClienteAux.removeAresta(serverURLs[server], arest);
        }
        
        try
        {
            lock.writeLock().lock();
            if(grafo.getArestasSize() > 0)
            {
                grafo.getArestas().remove(arest);
                //Persistencia.limpaArquivo(caminho);
                //Persistencia.salvaGrafo(grafo, caminho);
                
            }
            else return false;
            
        }
        catch(Exception e)
        {
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
        
        return true;
    }

    @Override
    public boolean editaAresta(Aresta arest) {
        
        int server = getServer(arest.getVertice1() + "");
        
        if(server != serverID) //Aresta esta em outro server
        {
            return ClienteAux.editaAresta(serverURLs[server], arest);
        }
        
        try
        {
            lock.writeLock().lock();
            if(grafo.getArestas().size() > 0)
            {
                for(Aresta a:grafo.getArestas())
                {
                    if(arest.getVertice1() == a.getVertice1() && arest.getVertice2() == arest.getVertice2())
                    {
                        a.setDesc(arest.getDesc());
                        a.setPeso(arest.getPeso());
                        a.setDirecionado(arest.isDirecionado());
                    }
                }
                //Persistencia.limpaArquivo(caminho);
                //Persistencia.salvaGrafo(grafo, caminho);
            }
        }
        catch(Exception e)
        {
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public boolean editaVertice(Vertice vert) {
        
        int server = getServer(vert.getId() + "");
        
        if(server != serverID) //Aresta esta em outro server
        {
            return ClienteAux.editaVertice(serverURLs[server], vert);
        }
        
        try
        {
            lock.writeLock().lock();
            if(grafo.getVertices().size() > 0)
            {
                for(Vertice v:grafo.getVertices())
                {
                    if(v.getId() == vert.getId())
                    {
                        v.setPeso(vert.getPeso());
                        v.setCor(vert.getCor());
                    }
                }
            }
            //Persistencia.limpaArquivo(caminho);
            //Persistencia.salvaGrafo(grafo, caminho);
        }
        catch(Exception e)
        {
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public Grafo getGrafo(){
        Grafo g = null;
        try
        {
            lock.readLock().lock();
            g = grafo;
        }
        finally
        {
            lock.readLock().unlock();
        }
        return g;
    }

    @Override
    public List<Aresta> getArestas(boolean recursive) {
        System.out.println("[DB] getArestas()");
        List<Aresta> listaArestas = new ArrayList<Aresta>();
        
        //É preciso pegar as arestas dos outros servidores
        if(recursive)
        {
            /*for(String URLs : serverURLs)
            {
                if(URLs.compareTo(serverURLs[serverID]) != 0)
                    listaArestas.addAll(ClienteAux.getArestas(URLs));
            }*/
            for(int i =0;i<N_SERVERS;i++)
            {
                if(i != serverID)
                {
                    listaArestas.addAll(ClienteAux.getArestas(serverURLs[i]));
                }
            }
        }
        try
        {
            lock.readLock().lock();
            if(grafo.getArestasSize()> 0) listaArestas.addAll(grafo.getArestas());
        }
        finally
        {
            lock.readLock().unlock();
        }
        
        return listaArestas;
    }

    @Override
    public List<Vertice> getVertices(boolean recursive){
        System.out.println("[DB] getVertices()");
        List<Vertice> vertices = new ArrayList<Vertice>();
        
        //É preciso pegar as arestas dos outros servidores
        if(recursive)
        {
            /*for(String URLs : serverURLs)
            {
                if(URLs.compareTo(serverURLs[serverID]) != 0)
                    vertices.addAll(ClienteAux.getVertices(URLs));
            }*/
            for(int i =0;i<N_SERVERS;i++)
            {
                if(i != serverID)
                {
                    vertices.addAll(ClienteAux.getVertices(serverURLs[i]));
                }
            }
        }
        try
        {
            lock.readLock().lock();
            if(grafo.getVerticesSize() > 0)vertices.addAll(grafo.getVertices());
        }
        finally
        {
            lock.readLock().unlock();
        }
        return vertices;
    }

    @Override
    public List<Vertice> getVizinhos(Vertice vert){
        System.out.println("[DB] getVizinhos()");
        
        List<Vertice> verticesVizinhos = new ArrayList<>();
        
        List<Aresta> arestas = this.getArestas(true);
        List<Vertice> vertices = this.getVertices(true);
        
        if (vertices.size() > 0)
        {
            for (Aresta a : arestas)
            {
                if ((a.getVertice1() == vert.getId() && vert.getId() != a.getVertice2()))
                {
                    //verticesVizinhos.add(grafo.getVerticeById(a.getVertice2()));
                    verticesVizinhos.add(vertices.stream().filter(f -> f.getId() == a.getVertice2()).findFirst().get());
                }
                if ((a.getVertice2() == vert.getId() && vert.getId() != a.getVertice1()))
                {
                    verticesVizinhos.add(vertices.stream().filter(f -> f.getId() == a.getVertice1()).findFirst().get());
                }
            }
        }
        return verticesVizinhos;
    }
    @Override
    public boolean resetaGrafo() throws TException {
        try
        {
            lock.writeLock().lock();
            grafo.clear();
            grafo = new Grafo();
            //Persistencia.limpaArquivo(caminho);
        }
        catch(Exception e)
        {
            return false;
        }
        finally
        {
            lock.writeLock().unlock();
        }
        return true;
    }
    /**
     * Menor caminho implementado com Dijkstra
     * @param v1
     * @param v2
     * @return 
     */
    @Override
    public List<Vertice> getMenorCaminho(Vertice v1, Vertice v2) {
        List<Aresta> arestas = this.getArestas(true);
        List<Vertice> vertices = this.getVertices(true);
        try 
        {
            lock.readLock().lock();
            //visitados
            List<Vertice> visitados = new ArrayList<>();
            //não visitados
            List<Vertice> naovisitados = new ArrayList<>();
            //tabela
            List<TabelaDijkstra> caminhos = new ArrayList<>();

            boolean flagV1 = false;
            boolean flagV2 = false;
            //Inicia tabela e não visitados/Verifica se v1 e v2 estão no grafo
            for (Vertice v : vertices) 
            {
                naovisitados.add(v);
                TabelaDijkstra novo = new TabelaDijkstra();
                novo.vertice = v;
                if (v.getId() == v1.getId()) 
                {
                    novo.dist_min = 0.0;
                    flagV1 = true;
                } 
                else 
                {
                    novo.dist_min = Double.POSITIVE_INFINITY;
                }
                novo.verticeAnterior = null;
                caminhos.add(novo);
                if (v.getId() == v2.getId()) 
                {
                    flagV2 = true;
                }
            }
            if (!flagV1 || !flagV2) 
            {
                return null;
            }

            Vertice atual = v1;

            while (naovisitados.size() > 0) 
            {
                //System.out.println("Visitando: " + atual.getId());
                //pega os vizinhos
                List<Vertice> vizinhos = getVizinhos(atual);

                //Pega somente vizinhos não visitados
                if (vizinhos != null && vizinhos.size() > 0) 
                {
                    List<Vertice> paraRemover = new ArrayList<>();
                    for (Vertice v : vizinhos) 
                    {
                        if (visitados.contains(v)) 
                        {
                            paraRemover.add(v);
                        }
                    }
                    vizinhos.removeAll(paraRemover);
                }

                //atualiza distancia dos vizinhos
                double distanciaAtual = getDistancia(caminhos, atual.getId());
                System.out.println("Vizinhos size: " + vizinhos.size());
                for (Vertice v : vizinhos) 
                {
                    Aresta arestaAtual = getArestaFrom(atual, v, true,arestas);
                    double distanciaAteVizinho = distanciaAtual + arestaAtual.peso;
                    TabelaDijkstra viz = getAt(caminhos, v.getId());
                    
                    if (distanciaAteVizinho < viz.dist_min) 
                    {
                        viz.dist_min = distanciaAteVizinho;
                        viz.verticeAnterior = atual;
                    }
                }
                //remove o vertice da lista de nao visitados e adiciona na lista de visitados
                naovisitados.remove(atual);
                visitados.add(atual);
                Collections.sort(caminhos, (a, b) -> a.compareTo(b)); //ordena tabela 

                //Pega o vertice com menor distancia para ser o atual:
                //Como a tabela de caminhos está ordenada, o primeiro vertice não visitado na tabela será escolhido
                for (TabelaDijkstra c : caminhos) 
                {
                    if (!visitados.contains(c.vertice))
                    {
                        atual = c.vertice;
                        break;
                    }
                }
            }
            /*System.out.println("-----TABELAFINAL-----");
            for (TabelaDijkstra t : caminhos) {
                System.out.println("Entrada tabela Dijkstra:");
                System.out.println("Vertice: " + t.vertice.getId());
                System.out.println("Distancia: " + t.dist_min);
                if (t.verticeAnterior != null) {
                    System.out.println("Source: " + t.verticeAnterior.getId());
                } else {
                    System.out.println("Source: null");
                }
            }
            System.out.println("-----FIM-----");*/

            List<Integer> caminhoFinal = new ArrayList<>();
            List<Vertice> caminhoRetorno = new ArrayList<>();

            int anterior = getAnterior(caminhos, v2.getId());
            Vertice verticeAnterior = getVerticeAnterior(caminhos,v2);
            
            caminhoFinal.add(v2.getId());
            caminhoRetorno.add(v2);
            while (anterior != v1.getId()) 
            {
                caminhoFinal.add(anterior);
                anterior = getAnterior(caminhos, anterior);
                caminhoRetorno.add(verticeAnterior);
                verticeAnterior = getVerticeAnterior(caminhos,verticeAnterior);
            }
            caminhoFinal.add(v1.getId());
            caminhoRetorno.add(v1);
            Collections.reverse(caminhoRetorno);
            Collections.reverse(caminhoFinal);
            return caminhoRetorno;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    private Aresta getArestaFrom(Vertice v1, Vertice v2,boolean bidirecional,List<Aresta> arestas)
    {
        if(!bidirecional)return arestas.stream().filter(f -> f.getVertice1() == v1.getId() && f.getVertice2() == v2.getId()).findFirst().get();
        else return arestas.stream().filter(f -> 
                (f.getVertice1() == v1.getId() && f.getVertice2() == v2.getId()) ||
                (f.getVertice1() == v2.getId() && f.getVertice2() == v1.getId())        ).findFirst().get();
    }
    private TabelaDijkstra getAt(List<TabelaDijkstra> caminhos,int vId)
    {
        return caminhos.stream().filter(f -> f.vertice.getId() == vId).findFirst().get();
    }
    private double getDistancia(List<TabelaDijkstra> caminhos,int vId)
    {
        return caminhos.stream().filter(f -> f.vertice.getId() == vId).findFirst().get().dist_min;
    }
    private int getAnterior(List<TabelaDijkstra>caminhos,int vId)
    {
        return caminhos.stream().filter(f -> f.vertice.getId() == vId).findFirst().get().verticeAnterior.getId();
    }
    private Vertice getVerticeAnterior(List<TabelaDijkstra>caminhos,Vertice vId)
    {
        return caminhos.stream().filter(f -> f.vertice.getId() == vId.getId()).findFirst().get().verticeAnterior;
    }
    
    private class TabelaDijkstra implements Comparable<TabelaDijkstra>
    {
        Vertice vertice;
        double dist_min;
        Vertice verticeAnterior;

        @Override
        public int compareTo(TabelaDijkstra t) {
            if(this.dist_min < t.dist_min) return -1;
            else return 1;
        }
    }
}


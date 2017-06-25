/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsd;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.thrift.TException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import shared.*;

public class DBHandler implements GrafoDB.Iface {

    private Grafo grafo = new Grafo();
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * ReadWriteLock -> ReadLocks podem ser acessadas em paralelo desde que 
     * uma WriteLock não está ativa.
     */
    private String caminho = "";
    

    public DBHandler(String caminho) {
        //log = new HashMap<Integer, SharedStruct>();
        this.caminho = caminho;
        Grafo g = Persistencia.getGrafo(caminho);
        if(g == null || (g.getArestasSize() == 0 && g.getVerticesSize() == 0))
        {
            grafo = new Grafo();
        }
        else
        {
            grafo = g;
        }
    }

    public boolean ping() {
        System.out.println("[DB] ping()");
        return true;
    }

    @Override
    public boolean insereVertice(Vertice vert){
        System.out.println("[DB] insereVertice()");
        
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
            Persistencia.limpaArquivo(caminho);
            Persistencia.salvaGrafo(grafo, caminho);
            
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
        
        boolean flagVertice1 = false;
        boolean flagVertice2 = false;
        try
        {
            lock.writeLock().lock();
            if(grafo.getVerticesSize() > 0)
            {
                for(Vertice v : grafo.getVertices()) //Verifica se ambos vertices estão no grafo
                {
                    if(v.getId() == arest.getVertice1()) flagVertice1 = true;
                    if(v.getId() == arest.getVertice2()) flagVertice2 = true;
            
                    if(flagVertice1 && flagVertice2) break;
                }
            }
            if(flagVertice1 && flagVertice2)
            {
                grafo.addToArestas(arest);
                Persistencia.limpaArquivo(caminho);
                Persistencia.salvaGrafo(grafo, caminho);
            }
            else
            {
                return false;
            }
            
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
    public boolean removeVertice(Vertice vert){
        System.out.println("[DB] removeVertice("+vert.getId()+")");
        
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
                Persistencia.limpaArquivo(caminho);
                Persistencia.salvaGrafo(grafo, caminho);
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
        
        try
        {
            lock.writeLock().lock();
            if(grafo.getArestasSize() > 0)
            {
                grafo.getArestas().remove(arest);
                Persistencia.limpaArquivo(caminho);
                Persistencia.salvaGrafo(grafo, caminho);
                
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
                Persistencia.limpaArquivo(caminho);
                Persistencia.salvaGrafo(grafo, caminho);
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
            Persistencia.limpaArquivo(caminho);
            Persistencia.salvaGrafo(grafo, caminho);
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
    public List<Aresta> getArestas(Vertice vert) {
        System.out.println("[DB] getArestas()");
        List<Aresta> listaArestas = null;
        
        try
        {
            lock.readLock().lock();
            if(grafo.getArestasSize()> 0) listaArestas = grafo.getArestas();
        }
        finally
        {
            lock.readLock().unlock();
        }
        
        return listaArestas;
    }

    @Override
    public List<Vertice> getVertices(Aresta arest){
        System.out.println("[DB] getVertices()");
        List<Vertice> vertices = null;
        
        try
        {
            lock.readLock().lock();
            if(grafo.getVerticesSize() > 0)vertices = grafo.getVertices();
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
        
        List<Vertice> vertices = new ArrayList<>();
        
        try
        {
            lock.readLock().lock();
            if(grafo.getVerticesSize() > 0)
            {
                for(Aresta a : grafo.getArestas())
                {
                    if( (a.getVertice1() == vert.getId() && vert.getId() != a.getVertice2())) 
                    {
                        vertices.add(grafo.getVerticeById(a.getVertice2()));
                    }
                    if((a.getVertice2() == vert.getId() && vert.getId() != a.getVertice1()))
                    {
                        vertices.add(grafo.getVerticeById(a.getVertice1()));
                    }
                }
            }
        }
        finally
        {
            lock.readLock().unlock();
        }
        return vertices;
    }
    @Override
    public boolean resetaGrafo() throws TException {
        try
        {
            lock.writeLock().lock();
            grafo.clear();
            grafo = new Grafo();
            Persistencia.limpaArquivo(caminho);
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
    public List<Integer> getMenorCaminho(Vertice v1, Vertice v2) {
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
            for (Vertice v : grafo.getVertices()) 
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
                //System.out.println("Vizinhos size: " + vizinhos.size());
                for (Vertice v : vizinhos) 
                {
                    Aresta arestaAtual = getArestaFrom(atual, v, true);
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

            int anterior = getAnterior(caminhos, v2.getId());
            caminhoFinal.add(v2.getId());
            while (anterior != v1.getId()) 
            {
                caminhoFinal.add(anterior);
                anterior = getAnterior(caminhos, anterior);
            }
            caminhoFinal.add(v1.getId());
            Collections.reverse(caminhoFinal);
            return caminhoFinal;
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
    
    private Aresta getArestaFrom(Vertice v1, Vertice v2,boolean bidirecional)
    {
        if(!bidirecional)return grafo.getArestas().stream().filter(f -> f.getVertice1() == v1.getId() && f.getVertice2() == v2.getId()).findFirst().get();
        else return grafo.getArestas().stream().filter(f -> 
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


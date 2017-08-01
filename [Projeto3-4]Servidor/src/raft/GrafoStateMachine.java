/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raft;

import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import shared.Aresta;
import shared.Grafo;
import shared.Vertice;

/**
 *
 * @author ThiagoH
 */
public class GrafoStateMachine extends StateMachine {
    private Map<Object, Object> map = new HashMap<>();
    
    public Object put(Commit<PutCommand> commit) {
        try {
            map.put(commit.operation().key(), commit.operation().value());
            commit.session().publish("add");
        } finally {
        commit.close();
        }
        return null;
    }

    public Object get(Commit<GetQuery> commit) {
        try {
            return map.get(commit.operation().key());
        } finally {
        commit.close();
        }
    }
    
    
    /*public java.util.List<Vertice> vertices = new ArrayList<>(); 
    public java.util.List<Aresta> arestas = new ArrayList<>(); 
    
    public Object put(Commit<PutCommand> commit) {
        try 
        {
            if(commit.operation().value().getClass() == Vertice.class)
            {
                Vertice add = (Vertice) commit.operation().value();
                for(Vertice v : vertices) //verifica vertice repetido
                {
                    if(v.id == add.id) return null;
                }
                
                vertices.add(add);
            }
            else if(commit.operation().value().getClass() == Aresta.class)
            {
                Aresta add = (Aresta) commit.operation().value();
                
                arestas.add(add);
            }
        } finally {
        commit.close();
        }
        return null;
    }
    
    public Object get(Commit<GetQuery> commit) {
        try {
            Grafo g = new Grafo();
            g.setArestas(arestas);
            g.setVertices(vertices);
            return g;
        } finally {
        commit.close();
        }
  }*/

  
}

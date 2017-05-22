/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * This Thrift file can be included by other Thrift files that want to share
 * these definitions.
 */


namespace java shared

struct Vertice {
  1: i32 id
  2: i32 cor
  3: string desc
  4: double peso
}

struct Aresta {
  1: i32 vertice1
  2: i32 vertice2
  3: double peso
  4: bool direcionado
  5: string desc
}

struct Grafo{
	1: list<Vertice> vertices,
	2: list<Aresta> arestas
}


service GrafoDB {

  bool ping(),
  bool insereVertice(1:Vertice vert),
  bool insereAresta(1:Aresta arest),
  bool removeVertice(1:Vertice vert),
  bool removeAresta(1:Aresta arest),
  bool editaAresta(1:Aresta arest),
  bool editaVertice(1:Vertice vert),
  Grafo getGrafo(),
  list<Aresta> getArestas(1:Vertice vert), 
  list<Vertice> getVertices(1:Aresta arest), 
  list<Vertice> getVizinhos(1:Vertice vert),
  list<i32> getMenorCaminho(1:Vertice v1, 2:Vertice v2),
  bool resetaGrafo()
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raft;


import io.atomix.copycat.server.CopycatServer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import shared.Vertice;

/**
 *
 * @author ThiagoH
 */
public class RaftServer {
      
   /**
    * Inicia o primeiro servidor e inicia o bootstrap.
    */
    public static CopycatServer criaServer(Collection<Address> cluster,String enderecoServidor,int porta)
    {
       
        Address address = new Address(enderecoServidor,porta);// endereço do servidor copycat
        CopycatServer Server = CopycatServer.builder(address)
                            .withStateMachine(GrafoStateMachine::new)
                            .withTransport(NettyTransport.builder()
                            .withThreads(4)
                            .build())
                            .withStorage(Storage.builder()
                                .withDirectory(new File("logs" +porta))//Cada servidor terá um diretório
                                .withStorageLevel(StorageLevel.DISK)
                                .build())
                            .build();

        CompletableFuture<CopycatServer> future = Server.bootstrap();
        future.join();
        return Server;
   }
   
   /**
    * Inicia servidores onde o bootstrap já está criado
    */
   public static CopycatServer adicionaServer(Collection<Address> cluster,String enderecoServidor,int porta)
   {
        Address address = new Address(enderecoServidor,porta);// endereço do servidor copycat
        CopycatServer server = CopycatServer.builder(address)
                            .withStateMachine(GrafoStateMachine::new)
                            .withTransport(NettyTransport.builder()
                            .withThreads(4)
                            .build())
                            .withStorage(Storage.builder()
                                .withDirectory(new File("logs" +porta))
                                .withStorageLevel(StorageLevel.DISK)
                                .build())
                            .build();

        server.join(cluster).join();
        return server;
   }
   
   public static CopycatClient iniciaCliente(Collection<Address> cluster)
   {
       CopycatClient client = CopycatClient.builder(cluster)
        .withTransport(NettyTransport.builder()
        .withThreads(2)
            .build())
        .build();
       
       CompletableFuture<CopycatClient> future = client.connect(cluster);
       future.join();
       
       return client;
   }
   /*public static void salvaLista(Collection<Address> cluster,String key,List<?> lista)
   {
       
       
       CompletableFuture<CopycatClient> future = client.connect(cluster);
       future.join();
        
       CompletableFuture<Object> future1 = client.submit(new PutCommand(key, lista));
       future1.join();
   }
   
   public static List<?> getLista(Collection<Address> cluster,String key)
   {
       CopycatClient client = CopycatClient.builder(cluster)
        .withTransport(NettyTransport.builder()
        .withThreads(2)
            .build())
        .build();
       
       CompletableFuture<CopycatClient> future = client.connect(cluster);
       future.join();
       
       return (List<Object>) client.submit(new GetQuery(key)).join();

   }*/
}

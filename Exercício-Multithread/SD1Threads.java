/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd1.threads;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author ThiagoH
 */
public class SD1Threads {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException 
    {
        
        String mascara = "abcdefghijklmnopqrstuvwxz";
        int n = mascara.length();
        Random rand = new Random();
        String msg = "";
        
        for(int i = 0;i< 80;i++)
        {
            msg += mascara.charAt(rand.nextInt(n)); //Gera mensagem aleatoria
        }

        ExecutorService application = Executors.newFixedThreadPool(30);
        SyncBuffer sharedLocation = new SyncBuffer(msg); //Classe compartilhada por todas as threads
       
        try 
        {
            for(int i = 0;i<30;i++)
                application.execute(new thread(sharedLocation));  //Inicia todas as threads
        } 
        catch (Exception exception) 
        {
            System.out.println("" + exception);
        }
        
        application.shutdown(); //Termina programa
    }
    
}

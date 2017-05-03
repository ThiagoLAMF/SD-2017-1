/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd1.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ThiagoH
 */
public class thread extends Thread{
    
    private SyncBuffer buff;
    protected static int id = 0;
   
    
    public thread(SyncBuffer msg)
    {
        this.buff = msg;
    }
    
    @Override
    public void run()
    {
        Thread.currentThread().setName("Thread" + id++);
        while(buff.mudaString())
        {
            System.out.println(Thread.currentThread().getName() + " processou a mensagem!");        
        }  
    }
}

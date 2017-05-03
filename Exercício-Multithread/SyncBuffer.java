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
public class SyncBuffer {
    
    Lock acessLock = new ReentrantLock();
    private Condition controle = acessLock.newCondition();
   
    private String msg;
    
    public SyncBuffer(String msg)
    {
        this.msg = msg;
    }
    
    public boolean mudaString()
    {
        boolean flagMudou = false;
        acessLock.lock();
        try 
        {    
            String novaMensagem = "";
            for(Character ch : msg.toCharArray())
            {
                if(!flagMudou && ch >= 'a' && ch <= 'z')
                {
                    novaMensagem += Character.toUpperCase(ch); //Muda mensagem
                    flagMudou = true;
                }
                else
                {
                    novaMensagem += ch;
                }
            }
            msg = novaMensagem;
            System.out.println("[MSG] " + msg);
            
            //Thread dorme por 1 segundo
            Thread.currentThread().sleep(1000);
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(SyncBuffer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {
            controle.signalAll(); //Thread repassa a mensagem.
            acessLock.unlock();
        }
       
        return flagMudou;
    }
    
}

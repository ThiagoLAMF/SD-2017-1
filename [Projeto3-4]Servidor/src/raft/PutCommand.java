/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raft;
import io.atomix.copycat.Command;
/**
 *
 * @author ThiagoH
 */
public class PutCommand implements Command<Object> {
  private final Object key;
  private final Object value;

  public PutCommand(Object key,Object value) {
    this.key = key;
    this.value = value;
  }

  public Object key() {
    return key;
  }

  public Object value() {
    return value;
  }
}

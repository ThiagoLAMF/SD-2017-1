/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raft;
import io.atomix.copycat.Query;
/**
 *
 * @author ThiagoH
 */
public class GetQuery implements Query<Object> {
  private final Object key;

  public GetQuery(Object key) {
    this.key = key;
  }

  public Object key() {
    return key;
  }
}
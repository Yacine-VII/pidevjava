/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;

/**
 *
 * @author wiemhjiri
 */
public interface IService<T> {
    void insert(T t);
    void update(T t);
    void delete(int id);
    T readById(int id);
    ArrayList<T>readAll();
    
}

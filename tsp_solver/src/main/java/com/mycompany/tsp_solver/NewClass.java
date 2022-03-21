/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import java.awt.EventQueue;

/**
 *
 * @author mic45
 */
public class NewClass {
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                new NewJFrame();
            }
        });
    }
}

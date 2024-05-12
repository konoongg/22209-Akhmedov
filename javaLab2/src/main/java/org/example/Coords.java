package org.example;

public class Coords {
    private double x;
    private double y;
    public Coords(double x, double y ){
        this.x = x;
        this.y = y;
    }
     public void ChangeX(double x){
        this.x = x;
     }

     public void ChangeY(double y){
        this.y = y;
     }

     public double X(){
        return x;
     }

     public double Y(){
        return y;
     }
}

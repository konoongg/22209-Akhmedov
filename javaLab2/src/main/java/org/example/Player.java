package org.example;

public class Player {
    private int hp;
    private int money;

    public Player(int startHp, int startMoney){
        hp = startHp;
        money = startMoney;
    }

    public void Restart(int startHp, int startMoney){
        hp = startHp;
        money = startMoney;
    }

    public void ChangeMoney(int profit){
        money += profit;
    }

    public void ChangeHp(int damage){
        hp -= damage;
    }

    public boolean IsAlive(){
        return hp > 0;
    }

    public int GetHp(){
        return hp;
    }

    public int GetMoney(){
        return money;
    }
}

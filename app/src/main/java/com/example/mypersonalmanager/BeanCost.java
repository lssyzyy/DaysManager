package com.example.mypersonalmanager;

public class BeanCost {
    private String costTitle;
    private String costDate;
    private String costMoney;

    public String toString(){
        return "BeanCost{" +
                "costTitle='"+costTitle+'\''+
                ", costDate='" + costDate + '\'' +
                ", costMoney='" + costMoney + '\'' +
                '}';
    }
    public BeanCost(){};
    public BeanCost(String costTitle,String costDate,String costMoney){
        this.costTitle=costTitle;
        this.costDate=costDate;
        this.costMoney=costMoney;
    }
    public String getCostTitle() {
        return costTitle;
    }

    public void setCostTitle(String costTitle) {
        this.costTitle = costTitle;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public String getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney;
    }
}

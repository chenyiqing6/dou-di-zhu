package Poker01;


//牌的信息类

import sun.util.resources.cldr.sr.CalendarData_sr_Latn_RS;

import java.util.ArrayList;
import java.util.List;

public class CardProduct {
    public final List<Card> list;   //牌
    public final int type;   //牌型
    public final Card maxCard;   //某种意义上最大的牌，但被带的牌不会算上
    public final List<CardGroup> group;   //存放牌型及其张数的集合

    //构造函数
    public CardProduct(List<Card> list, int type, Card maxCard, List<CardGroup> group) {
        this.list = list;
        this.type = type;
        this.maxCard = maxCard;
        this.group = group;
    }

    //正在出牌的玩家调用这个方法，传进去的CardProduct cardproduct表示其上家的牌
    public boolean isGreaterThan(CardProduct cardProduct) {
        //表示如果“我”出了牌且没有出错，而且上家没出牌
        if (type != Number.TYPE_ERROR && type != Number.TYPE_NULL && cardProduct.type == Number.TYPE_NULL) {
            return true;//那么“我”的牌就比上家的大
        } else if (type == Number.TYPE_BOOM) {//该玩家出的是炸子
            if (cardProduct.type == Number.TYPE_BOOM) {
                //如果我的第一张牌就是王，并且上家的第一张牌也是王
                if (list.get(0).isJoker() && cardProduct.list.get(0).isJoker()) {
                    //我的牌和上家的牌长度相等
                    if (list.size() == cardProduct.list.size()) {
                        int bigJokerSum1 = 0, bigJokerSum2 = 0;
                        //用增强for对我的card遍历
                        for (Card card : list) {
                            //如果有大王，。。1++
                            if (card.id == Number.ID_JOKER_2) bigJokerSum1++;
                        }
                        //用增强for对上家的card遍历
                        for (Card card : cardProduct.list) {
                            //如果有大王， 。。2++
                            if (card.id == Number.ID_JOKER_2) bigJokerSum2++;
                        }
                        return bigJokerSum1 > bigJokerSum2;
                    } else return list.size() > cardProduct.list.size();
                } else if (list.get(0).isJoker() && !cardProduct.list.get(0).isJoker()) {
                    //王炸 炸 炸弹
                    return list.size() * 2 >= cardProduct.list.size();
                } else if (!list.get(0).isJoker() && cardProduct.list.get(0).isJoker()) {
                    //炸弹 炸 王炸
                    return list.size() > cardProduct.list.size() * 2;
                } else if (!list.get(0).isJoker() && !cardProduct.list.get(0).isJoker()) {
                    //炸弹 炸 炸弹
                    if (list.size() > cardProduct.list.size()) return true;
                    else if (list.size() == cardProduct.list.size()) {
                        return list.get(0).num > cardProduct.list.get(0).num;
                    } else return false;
                }
            } else return true;
        } else if (type == cardProduct.type && list.size() == cardProduct.list.size()) {
            if (type == Number.TYPE_1 && maxCard.num == Number.NUM_JOKER && cardProduct.maxCard.num == Number.NUM_JOKER) {
                //由于大小王的num都是相同的 所以比较大小王的话要对比id
                return maxCard.id > cardProduct.maxCard.id;
            } else {
                return maxCard.num > cardProduct.maxCard.num;
            }
        }
        return false;
    }

    private String typeToString(){
        switch(type){
            case Number.TYPE_ERROR:
                return "错误";
            case Number.TYPE_NULL:
                return "无";
            case Number.TYPE_1:
                return "单牌";
            case Number.TYPE_22:
                return "对子";
            case Number.TYPE_333:
                return "三个";
            case Number.TYPE_BOOM:
                return "炸弹";
            case Number.TYPE_3331:
                return "三带一";
            case Number.TYPE_33322:
                return "三带一对";
            case Number.TYPE_444412:
                return "四带二";
            case Number.TYPE_44441122:
                return "四带两对";
            case Number.TYPE_34567:
                return "顺子";
            case Number.TYPE_334455:
                return "连对";
            case Number.TYPE_333444:
                return "飞机";
            case Number.TYPE_33344412:
                return "飞机带单牌";
            case Number.TYPE_3334441122:
                return "飞机带对子";
        }
        return "未知";
    }

    @Override
    public String toString() {
        return "CardProduct{" +
                "list=" + list +
                ", type=" + typeToString()+
                ", maxCard=" + maxCard +
                ", len="+list.size()+
                ", group=" + group +
                '}';
    }

    public static CardProduct createNullType(){
        return new CardProduct(new ArrayList<>(),Number.TYPE_NULL,null,null);
    }
}

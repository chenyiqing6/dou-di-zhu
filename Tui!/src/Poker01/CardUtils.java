package Poker01;

//工具类

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.util.*;
import java.util.stream.Collectors;

public class CardUtils {

    //将玩家输入的字符串储存到一个全为Card类型的List集合中并返回该集合
    public static List<Card> CreateCards(String cards) {
        List<Card> list = new ArrayList<>();
        for (int i = 0; i < cards.length(); i++) {
            String s = cards.substring(i, i + 1);  //截取字符串的某一部分
            if (!" ".equals(s)) list.add(new Card(s));
        }
        return list;
    }

    //统计List中每张牌出现的次数，并用每种牌及其出现次数创建CardGroup类，再将这些类存放到一个List集合中并返回
    public static List<CardGroup> creategroup(List<Card> list) {
        List<CardGroup> groups = new ArrayList<>(); //该集合中存放的都是CardGroup类型的元素，每一个CardGroup又都代表的一种牌及其种类
        LinkedList<Card> temp = new LinkedList<>(list);   //将List转变成LinkedList
        Map<Integer, Integer> map = new HashMap<>();  //键是Num，值是sum
        while (temp.size() > 0) {
            final Card card = temp.removeLast();
            final Integer sum = map.get(card.num);
            if (sum == null)
                map.put(card.num, 1);
            else
                map.put(card.num, sum.intValue() + 1);//输出int数据
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {  //entrySet实现了Set接口，里面存放的是键值对。一个K对应一个V
            groups.add(new CardGroup(entry.getKey(), entry.getValue()));  //int num, int sum
        }
        return groups;
    }

    //删除大小王，返回新的List<CardGroup>
    public static List<CardGroup> removeJokers(List<CardGroup> list) {
        return list.stream().filter(cardGroup -> cardGroup.num != Number.NUM_JOKER).collect(Collectors.toList());
    }

    //ID当索引（带花色），并返回该牌在集合中的索引
    public static int indexOfById(List<Card> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) return i;
        }
        return -1;
    }

    //num当索引（牌的数字），找到了就返回该牌在集合中的索引
    public static int indexOfByNum(List<Card> list, int num) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).num == num) return i;
        }
        return -1;
    }

    //获取List中第一张为num的牌，找到就返回该Card类
    public static Card getCardByNum(List<Card> list, int num) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).num == num)
                return list.get(i);
        }
        return null;
    }

    //从List头部开始搜索，返回len张为num的牌（List排好序了），返回的是一个List<Card>集合
    public static List<Card> getCardByNum(List<Card> list, int num, int len) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).num == num)
                return list.subList(i, i + len);   //取集合中i到i+len-1的元素
        }
        return new ArrayList<>();
    }

    //获取List中最后一张为num的牌,找到就返回该Card类
    public static Card getLastCardByNum(List<Card> list, int num) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).num == num)
                return list.get(i);
        }
        return null;
    }

    //从List尾部开始搜索，返回len张为num的牌（list是排好序的），返回的是一个集合
    public static List<Card> getLastCardByNum(List<Card> list, int num, int len) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).num == num)
                return list.subList(i - len + 1, i + 1);//取得下标为i-len+1到i的元素
        }
        return new ArrayList<>();
    }


    //根据索引返回一个新的List
    public static List<Card> getCardsByIndexes(List<Card> list, List<Integer> indexes) {
        List<Card> newList = new ArrayList<>(indexes.size());
        for (int i = 0; i < indexes.size(); i++) {
            newList.add(list.get(indexes.get(i)));
        }
        return newList;
    }

    //把牌号为num的牌全部移动到前面
    public static void moveToHead(List<Card> list, int num, int len) {
        ArrayList<Card> temp = new ArrayList<>();
        int i = 0;
        while (i < list.size() && temp.size() < len) {
            if (list.get(i).num == num) temp.add(list.remove(i));
            else i++;
        }
        list.addAll(0, temp);
    }

    public static List<Card> removeByIndex(List<Card> list, List<Integer> indexes) {
        List<Card> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!indexes.contains(i)) newList.add(list.get(i));
        }
        return newList;
    }

    //把牌号为num的牌全部移动到后面
    public static void moveToEnd(List<Card> list, int num, int len) {
        ArrayList<Card> temp = new ArrayList<>();
        int i = 0;
        while (i < list.size() && temp.size() < len) {
            if (list.get(i).num == num) temp.add(list.remove(i));
            else i++;
        }
        list.addAll(temp);
    }

}

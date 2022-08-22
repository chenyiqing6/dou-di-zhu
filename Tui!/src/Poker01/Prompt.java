package Poker01;

//根据上家的牌型来在自己的牌中找到所有可以出的牌

import java.util.*;
import java.util.stream.Collectors;

public class Prompt {

    private final List<Card> list;  //从小到大，存放的是我的牌
    private final List<CardGroup> groups;   //按num从小到大（牌型及其数量） (3 44 5 666 8 999 J K A)，存放的是我的牌
    private final CardProduct last;   //牌的信息类，存放的是上家出的牌
    //判断完该出的牌型后将自己所有可以出的牌的组合都放入该集合中
    public final List<List<Card>> prompt;    //如果是find1，可能是  7 8 A  ；如果是find3331，prompt里可能是 9996   JJJ3 等这样可以出的牌

    public Prompt(List<Card> list, CardProduct last) {
        // for(int i = 0;i<1000000000;i++);
        this.list = new ArrayList<>(list);
        this.groups = CardUtils.creategroup(list);
        //从小到大排序
        this.list.sort((o1, o2) -> o1.id - o2.id);
        this.groups.sort(Comparator.comparingInt(o -> o.num));

        this.last = last;
        prompt = new ArrayList<>();

        if (last.type == Number.TYPE_ERROR) return;
        else if (last.type == Number.TYPE_NULL) prompt.addAll(findNull());
        else if (last.type == Number.TYPE_1) prompt.addAll(find1(last.maxCard.num, last.maxCard.id));
        else if (last.type == Number.TYPE_22) prompt.addAll(find22(last.maxCard.num));
        else if (last.type == Number.TYPE_333) prompt.addAll(find333(last.maxCard.num));
        else if (last.type == Number.TYPE_3331) prompt.addAll(find3331(last.maxCard.num));
        else if (last.type == Number.TYPE_33322) prompt.addAll(find33322(last.maxCard.num));
        else if (last.type == Number.TYPE_444412) prompt.addAll(find444412(last.maxCard.num));
        else if (last.type == Number.TYPE_44441122) prompt.addAll(find44441122(last.maxCard.num));
        else if (last.type == Number.TYPE_34567) prompt.addAll(find34567(last.maxCard.num, last.list.size()));
        else if (last.type == Number.TYPE_334455) prompt.addAll(find334455(last.maxCard.num, last.list.size()));
        else if (last.type == Number.TYPE_333444) prompt.addAll(find333444(last.maxCard.num, last.list.size()));
        else if (last.type == Number.TYPE_33344412)
            prompt.addAll(find33344412(last.maxCard.num, last.list.size()));
        else if (last.type == Number.TYPE_3334441122)
            prompt.addAll(find3334441122(last.maxCard.num, last.list.size()));

        prompt.addAll(findBoom(last));
    }

    public List<List<Card>> findNull() {
        //判断能不能一次把所有牌打完 如果能就直接打完
        //判断是否能一次将手中的牌打完，如果能就直接打完
        //CardType(list).products,builder方法接收的是一个能出的牌型
        //其实如果手中的牌是一种正确的牌型的话，list中一共也只有一个元素，get(0)就是获取它
        //如果不能一次出完那它的type就是-1
        if (CardType.builder(list).list.get(0).type != Number.TYPE_ERROR) {
            List<List<Card>> find = new ArrayList<>();
            find.add(list);
            return find;
        }

        //查找飞机带对子
        if (list.size() > 10) {
            final int length = list.size() - list.size() % 5;
            for (int i = length; i >= 10; i -= 5) {
                List<List<Card>> find = find3334441122(null, i);
                if (find.size() > 0) return find;
            }
        }
        //查找飞机带单排
        if (list.size() > 8) {
            final int length = list.size() - list.size() % 4;
            for (int i = length; i >= 8; i -= 4) {
                List<List<Card>> find = find33344412(null, i);
                if (find.size() > 0) return find;
            }
        }
        //查找飞机
        if (list.size() > 6) {
            for (int i = list.size() - list.size() % 3; i >= 6; i -= 3) {
                List<List<Card>> find = find333444(null, i);
                if (find.size() > 0) return find;
            }
            //查找连对
            for (int i = list.size() - list.size() % 2; i >= 6; i -= 2) {
                List<List<Card>> find = find334455(null, i);
                if (find.size() > 0) return find;
            }
        }
        if (list.size() > 5) {
            //查找顺子
            for (int i = list.size() - 1; i >= 5; i -= 1) {
                List<List<Card>> find = find34567(null, i);
                if (find.size() > 0) return find;
            }
            //查找三带一对
            List<List<Card>> find = find33322(null);
            if (find.size() > 0) return find;
        }
        if (list.size() > 4) {
            //查找三带一
            List<List<Card>> find = find3331(null);
            if (find.size() > 0) return find;
        }
        //最后手段
        List<List<Card>> find = new ArrayList<>();
        List<CardGroup> temp = new ArrayList<>();
        List<CardGroup> temp1 = new ArrayList<>();
        for (CardGroup group : groups) {
            if (group.sum < 4 || (group.num == Number.NUM_JOKER && group.sum < 2)) {
                temp.add(group);
            } else {
                temp1.add(group);
            }
        }
        temp.addAll(temp1);
        CardGroup group = temp.get(0);
        find.add(CardUtils.getCardByNum(list, group.num, group.sum));
        return find;
    }

    public List<List<Card>> find1(int maxCardNum, int maxCardId) {
        List<List<Card>> find = new ArrayList<>();   //find是存放一堆牌集合的集合
        List<CardGroup> groups = new ArrayList<>();
                //增强for遍历this.groups集合，获取其中的每一个CardGroup（牌型及其数量）
        for (CardGroup cardGroup : this.groups) {
            //如果这堆牌中有比“最大的牌”还大的牌（4>3这种），就把这种牌（有可能是单牌对子等）加到新创建的groups中
            if (cardGroup.num > maxCardNum) groups.add(cardGroup);
        }
        groups.sort((o1, o2) -> {
            //order by sum desc,num desc
            final int o = o1.sum - o2.sum;
            if (o != 0) return o;
            return o1.num - o2.num;
        });
//		groups.sort(Comparator.comparingInt((CardGroup o) -> o.sum).thenComparingInt(o -> o.num));
        for (CardGroup group : groups) {
            find.add(CardUtils.getCardByNum(list, group.num, 1));
        }
        if (maxCardId < Number.ID_JOKER_2) {
            for (Card card : list) {
                if (card.id == Number.ID_JOKER_2) {
                    List<Card> cards = new ArrayList<>();
                    cards.add(card);
                    find.add(cards);
                    break;
                }
            }
        }
        return find;
    }

    public List<List<Card>> find22(int maxCardNum) {
        List<List<Card>> find = new ArrayList<>();
        List<CardGroup> groups = new ArrayList<>();
        for (CardGroup cardGroup : this.groups) {
            if (cardGroup.num > maxCardNum && cardGroup.sum >= 2 && cardGroup.sum < 4 && cardGroup.num != Number.NUM_JOKER)
                groups.add(cardGroup);
        }
        groups.sort((o1, o2) -> {
            //order by sum desc,num desc
            final int o = o1.sum - o2.sum;
            if (o != 0) return o;
            return o1.num - o2.num;
        });
        for (CardGroup group : groups) {
            find.add(CardUtils.getCardByNum(list, group.num, 2));
        }
        return find;
    }

    public List<List<Card>> find333(int maxCardNum) {
        List<List<Card>> find = new ArrayList<>();
        List<CardGroup> groups = new ArrayList<>();
        for (CardGroup cardGroup : this.groups) {
            if (cardGroup.num > maxCardNum && cardGroup.sum == 3 && cardGroup.num != Number.NUM_JOKER)
                groups.add(cardGroup);
        }
        groups.sort((o1, o2) -> {
            //order by sum desc,num desc
            final int o = o1.sum - o2.sum;
            if (o != 0) return o;
            return o1.num - o2.num;
        });
        for (CardGroup group : groups) {
            find.add(CardUtils.getCardByNum(list, group.num, 3));
        }
        return find;
    }

    public List<List<Card>> findBoom(CardProduct last) {
        List<List<Card>> find = new ArrayList<>();
        List<CardGroup> booms = new ArrayList<>();
        for (CardGroup cardGroup : groups) {
            if (cardGroup.num == Number.NUM_JOKER && cardGroup.sum >= 2 || cardGroup.sum >= 4) {
                booms.add(cardGroup);
            }
        }
        booms.sort((o1, o2) -> {
            final int o;
            if (o1.num == Number.NUM_JOKER) {
                o = o1.sum * 2 - o2.sum;
            } else if (o2.num == Number.NUM_JOKER) {
                o = o1.sum - o2.sum * 2;
            } else {
                o = o1.sum - o2.sum;
            }
            if (o != 0)
                return o;
            return o1.num - o2.num;
        });
        for (CardGroup cardGroup : booms) {
            find.add(CardUtils.getCardByNum(list, cardGroup.num, cardGroup.sum));
        }
        // 拆
        List<List<Card>> split = new ArrayList<>();
        for (List<Card> boom : find) {
            if (boom.size() > 4 && boom.get(0).num != Number.NUM_JOKER) {
                for (int i = 0; i < boom.size() - 4; i++) {
                    // 拆普通炸
                    List<Card> cards = new ArrayList<>(boom);
                    cards.remove(0);
                    for (int j = 0; j < i; j++)
                        cards.remove(cards.size() - 1);
                    split.add(cards);
                }
            } else if (boom.size() > 2 && boom.get(0).num == Number.NUM_JOKER) {
                // 拆王炸
                List<Card> joker1List = new ArrayList<>();
                List<Card> joker2List = new ArrayList<>();
                for (Card card : boom) {
                    if (card.id == Number.ID_JOKER_1)
                        joker1List.add(card);
                    else
                        joker2List.add(card);
                }
                for (int i = 2, len = boom.size(); i < len; i++) {
                    for (int j = 0; j <= i; j++) {// j=大王数量
                        if (i - j <= joker1List.size() && j <= joker2List.size()) {
                            List<Card> cards = new ArrayList<>();
                            for (int k = 0; k < i - j; k++)
                                cards.add(joker1List.get(k));
                            for (int k = 0; k < j; k++)
                                cards.add(joker2List.get(k));
                            split.add(cards);
                        }
                    }
                }
            }
        }
        find.addAll(split);
        // 只留下打得过的炸
        if (last != null && last.type == Number.TYPE_BOOM) {
            int i = 0;
            while (i < find.size()) {
                if (CardType.builder(find.get(i)).isGreaterThan(last) < 0) {
                    find.remove(i);
                } else {
                    i++;
                }
            }
        }
        return find;
    }

    public List<List<Card>> find3331(Integer maxCardNum) {
        List<List<Card>> find = new ArrayList<>();
        for (CardGroup group : CardUtils.removeJokers(groups)) {
            if ((maxCardNum == null || group.num > maxCardNum) && group.sum == 3) {
                List<Card> cards = new ArrayList<>(CardUtils.getCardByNum(list, group.num, 3));
                List<Card> fill = getMinSingleCard(cards, true, 1);
                if (fill.size() == 1) {
                    cards.addAll(fill);
                    find.add(cards);
                }
            }
        }
        return find;
    }

    public List<List<Card>> find33322(Integer maxCardNum) {
        List<List<Card>> find = new ArrayList<>();
        for (CardGroup group : CardUtils.removeJokers(groups)) {
            if ((maxCardNum == null || group.num > maxCardNum) && group.sum == 3) {
                List<Card> cards = new ArrayList<>(CardUtils.getCardByNum(list, group.num, 3));
                List<Card> fill = getMinDoubleCards(cards, true, 1);
                if (fill.size() == 2) {
                    cards.addAll(fill);
                    find.add(cards);
                }
            }
        }
        return find;
    }

    public List<List<Card>> find444412(int maxCardNum) {
        List<List<Card>> find = new ArrayList<>();
        for (CardGroup group : CardUtils.removeJokers(groups)) {
            if (group.num > maxCardNum && group.sum == 4) {
                List<Card> cards = new ArrayList<>(CardUtils.getCardByNum(list, group.num, 4));
                List<Card> fill = getMinSingleCard(cards, true, 2);
                if (fill.size() == 2) {
                    cards.addAll(fill);
                    find.add(cards);
                }
            }
        }
        return find;
    }

    public List<List<Card>> find44441122(int maxCardNum) {
        List<List<Card>> find = new ArrayList<>();
        for (CardGroup group : CardUtils.removeJokers(groups)) {
            if (group.num > maxCardNum && group.sum == 4) {
                List<Card> cards = new ArrayList<>(CardUtils.getCardByNum(list, group.num, 4));
                List<Card> fill = getMinDoubleCards(cards, true, 2);
                if (fill.size() == 4) {
                    cards.addAll(fill);
                    find.add(cards);
                }
            }
        }
        return find;
    }

    public List<List<Card>> find34567(Integer maxCardNum, int len) {
        List<List<Card>> find = new ArrayList<>();    //很多组可以出的顺子（45678，56789，10JQKA）
        List<List<CardGroup>> continuous = new ArrayList<>();   //（45666788，5666788999，10JJQKKKA）差不多这种意思吧
        int start = 0;
        int end = start + 1;    //找牌的始末位置
        //进入循环条件：起始位置加上所需顺子的长度<=我自己牌种类的个数（比如顺子长度是5，而我手中只有四种牌，这种情况没有合适的牌出了）
        while (start + len <= groups.size()) {
            //判断条件1：选中的牌要是连续的 && 最大的那一张比2小 && 最后的那一张牌不能超出我手中牌的最大种类数
            if (groups.get(end).num - groups.get(start).num == end - start && groups.get(end).num < Number.NUM_2
                    && end < groups.size()) {
                //判断条件2：选中的牌正好是所需的顺子个数，要到最后才执行这个
                if (end + 1 - start == len) {
                    //判断条件3：上家没出牌 || 选出的顺子的最后那张牌大于上家顺子最大的那张牌
                    if (maxCardNum == null || groups.get(end).num > maxCardNum) {
                        List<CardGroup> tempGroups = new ArrayList<>();
                        //循环将找到的一组牌型加进tempGroups
                        for (int i = start; i <= end; i++) {
                            tempGroups.add(groups.get(i));
                            // 也可能是3 444 5 66 777这种，因为是把每种牌型里所有的牌都加进去了
                        }
                        continuous.add(tempGroups);//tempGroups加完一组List<CardGroup>（3 444 5 66 777）后将其加入continuous
                    }
                    start++;
                    end = start + 1;
                } else {
                    end++;  //若不满足判断条件2时执行的，如果张数还不够，就把结束点向后移动一位，继续判断后面一个牌型的能不能满足条件
                }
            } else {//若不满足判断条件1时执行
                start++;
                end = start + 1;//例如：如果有不连续的牌，就把起始点和结束点都向后移动一位
            }
        }
        //对continuous循环
        for (List<CardGroup> cardGroups : continuous) {
            List<Card> temp = new ArrayList<>();  //用temp存放一组顺子（678910），这时是每种牌型只有一张牌了
            //再对continuous种的每一个元素（444 5 66 777 88这种，存放的有可能有对子等）进行循环
            for (CardGroup cardGroup : cardGroups) {
                temp.add(CardUtils.getCardByNum(list, cardGroup.num));  //将每一种牌型中的一张牌加入temp
            }
            //这样将每一次循环中找出的temp顺子加进去，find中就是很多组可以打出的顺子啦
            //出牌的时候只需要get(0)获取其中最小的元素就可以了（符合条件的最小顺子）
            find.add(temp);
        }
        return find;
    }

    public List<List<Card>> find334455(Integer maxCardNum, int length) {
        List<List<Card>> find = new ArrayList<>();
        List<List<CardGroup>> continuous = new ArrayList<>();
        List<CardGroup> ge2AndLt4 = this.groups.stream().filter(cardGroup -> cardGroup.sum >= 2 && cardGroup.sum < 4).sorted(Comparator.comparingInt(o -> o.num)).collect(Collectors.toList());

        int len = length / 2;// len为消除重复num后的长度
        int start = 0;
        int end = start + 1;
        while (start + len <= ge2AndLt4.size()) {
            if (ge2AndLt4.get(end).num - ge2AndLt4.get(start).num == end - start && ge2AndLt4.get(end).num < Number.NUM_2
                    && end < ge2AndLt4.size()) {
                if (end + 1 - start == len) {
                    if (maxCardNum == null || ge2AndLt4.get(end).num > maxCardNum) {
                        List<CardGroup> tempGroups = new ArrayList<>();
                        for (int i = start; i <= end; i++) {
                            tempGroups.add(ge2AndLt4.get(i));
                        }
                        continuous.add(tempGroups);
                    }
                    start++;
                    end = start + 1;
                } else {
                    end++;
                }
            } else {
                start++;
                end = start + 1;
            }
        }
        for (List<CardGroup> cardGroups : continuous) {
            List<Card> temp = new ArrayList<>();
            for (CardGroup cardGroup : cardGroups) {
                temp.addAll(CardUtils.getCardByNum(list, cardGroup.num, 2));
            }
            find.add(temp);
        }
        return find;
    }

    public List<List<Card>> find333444(Integer maxCardNum, int length) {
        List<List<Card>> find = new ArrayList<>();
        List<List<CardGroup>> continuous = new ArrayList<>();
        List<CardGroup> eq3 = groups.stream().filter(cardGroup -> cardGroup.sum == 3).sorted(Comparator.comparingInt(o -> o.num)).collect(Collectors.toList());

        int len = length / 3;// len为消除重复num后的长度
        int start = 0;
        int end = start + 1;
        while (start + len <= eq3.size()) {
            if (eq3.get(end).num - eq3.get(start).num == end - start && eq3.get(end).num < Number.NUM_2
                    && end < eq3.size()) {
                if (end + 1 - start == len) {
                    if (maxCardNum == null || eq3.get(end).num > maxCardNum) {
                        List<CardGroup> tempGroups = new ArrayList<>();
                        for (int i = start; i <= end; i++) {
                            tempGroups.add(eq3.get(i));
                        }
                        continuous.add(tempGroups);
                    }
                    start++;
                    end = start + 1;
                } else {
                    end++;
                }
            } else {
                start++;
                end = start + 1;
            }
        }
        for (List<CardGroup> cardGroups : continuous) {
            List<Card> temp = new ArrayList<>();
            for (CardGroup cardGroup : cardGroups) {
                temp.addAll(CardUtils.getCardByNum(list, cardGroup.num, 3));
            }
            find.add(temp);
        }
        return find;
    }

    public List<List<Card>> find33344412(Integer maxCardNum, int length) {
        List<List<Card>> find = new ArrayList<>();
        List<List<CardGroup>> continuous = new ArrayList<>();
        List<CardGroup> eq3 = groups.stream().filter(cardGroup -> cardGroup.sum == 3).sorted(Comparator.comparingInt(o -> o.num)).collect(Collectors.toList());

        int len = length / 4;// len为三顺长度 如333444555的话len就等于3
        int start = 0;
        int end = start + 1;
        while (start + len <= eq3.size()) {
            if (eq3.get(end).num - eq3.get(start).num == end - start && eq3.get(end).num < Number.NUM_2 && end < eq3.size()) {
                if (end + 1 - start == len) {
                    if (maxCardNum == null || eq3.get(end).num > maxCardNum) {
                        List<CardGroup> tempGroups = new ArrayList<>();
                        for (int i = start; i <= end; i++) {
                            tempGroups.add(eq3.get(i));
                        }
                        continuous.add(tempGroups);
                    }
                    start++;
                    end = start + 1;
                } else {
                    end++;
                }
            } else {
                start++;
                end = start + 1;
            }
        }
        for (List<CardGroup> cardGroups : continuous) {
            List<Card> temp = new ArrayList<>();
            for (CardGroup cardGroup : cardGroups) {
                temp.addAll(CardUtils.getCardByNum(list, cardGroup.num, 3));
            }
            List<Card> fill = getMinSingleCard(temp, true, len);
            if (fill.size() == len) {
                temp.addAll(fill);
                find.add(temp);
            }
        }
        return find;
    }

    public List<List<Card>> find3334441122(Integer maxCardNum, int length) {
        List<List<Card>> find = new ArrayList<>();
        List<List<CardGroup>> continuous = new ArrayList<>();
        List<CardGroup> eq3 = groups.stream().filter(cardGroup -> cardGroup.sum == 3).sorted(Comparator.comparingInt(o -> o.num)).collect(Collectors.toList());

        int len = length / 5;// len为三顺长度 如333444555的话len就等于3
        int start = 0;
        int end = start + 1;
        while (start + len <= eq3.size()) {
            //System.out.println("eq3.size()="+eq3.size());
            //System.out.println("len="+len);
           // System.out.println("end="+end);
            if (eq3.get(end).num - eq3.get(start).num == end - start && eq3.get(end).num < Number.NUM_2 && end < eq3.size()) {
                if (end + 1 - start == len) {
                    if (maxCardNum == null || eq3.get(end).num > maxCardNum) {
                        List<CardGroup> tempGroups = new ArrayList<>();
                        for (int i = start; i <= end; i++) {
                            tempGroups.add(eq3.get(i));
                        }
                        continuous.add(tempGroups);
                    }
                    start++;
                    end = start + 1;
                } else {
                    end++;
                }
            } else {
                start++;
                end = start + 1;
            }
        }
        for (List<CardGroup> cardGroups : continuous) {
            List<Card> temp = new ArrayList<>();
            for (CardGroup cardGroup : cardGroups) {
                temp.addAll(CardUtils.getCardByNum(list, cardGroup.num, 3));
            }
            List<Card> fill = getMinDoubleCards(temp, true, len);
            if (fill.size() == len * 2) {
                temp.addAll(fill);
                find.add(temp);
            }
        }
       // System.out.println(find);
        return find;
    }

    /**
     * 获取最小单牌
     *
     * @param notIn
     * @param removeAll 为false时只删除相同的牌 为true时删除num相等的牌
     * @param len       获取的单排数量 最多返回len张牌 也可能一张都不返回
     * @return
     */

    public List<Card> getMinSingleCard(List<Card> notIn, boolean removeAll, int len) {
        List<Card> result = new ArrayList<>();
        List<Card> temp;
        if (removeAll) {
            temp = list.stream().filter(card -> notIn.stream().noneMatch(card1 -> card.num == card1.num)).collect(Collectors.toList());
        } else {
            temp = new ArrayList<>(list);
            temp.removeAll(notIn);
        }
        List<CardGroup> groups = CardUtils.creategroup(temp);
        groups.sort(Comparator.comparingInt((CardGroup o) -> o.sum).thenComparingInt(o -> o.num));// order by sum,num
        int index = 0;
        while (result.size() < len && index < groups.size()) {
            CardGroup cardGroup = groups.get(index);
            if (cardGroup.sum > 0) {
                cardGroup.sum--;
                Card card = CardUtils.getCardByNum(list, cardGroup.num);
                result.add(card);
            } else
                index++;
        }
        return result;
    }

    /**
     * 获取最小对子(不包括大小王)
     *
     * @param notIn
     * @param removeAll 为false时只删除相同的牌 为true时删除num相等的牌
     * @param len       获取的对子数量 最多返回len*2张牌 也可能一张都不返回
     * @return
     */
    public List<Card> getMinDoubleCards(List<Card> notIn, boolean removeAll, int len) {
        List<Card> result = new ArrayList<>();
        List<Card> temp;
        //获取对子不能包含对王
        if (removeAll) {
            temp = list.stream().filter(card -> card.num != Number.NUM_JOKER && notIn.stream().noneMatch(card1 -> card.num == card1.num)).collect(Collectors.toList());
        } else {
            temp = list.stream().filter(card -> card.num != Number.NUM_JOKER && notIn.stream().noneMatch(card1 -> card == card1)).collect(Collectors.toList());
        }
        List<CardGroup> groups = CardUtils.creategroup(temp);
        groups.sort(Comparator.comparingInt((CardGroup o) -> o.sum).thenComparingInt(o -> o.num));// order by sum,num
        if (groups.size() > 0) {
            int index = 0;
            while (result.size() < len * 2 && index < groups.size()) {
                CardGroup cardGroup = groups.get(index);
                if (cardGroup.sum >= 2) {
                    cardGroup.sum -= 2;
                    List<Card> cards = CardUtils.getCardByNum(list, cardGroup.num, 2);
                    result.addAll(cards);
                } else
                    index++;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "当前扑克牌:" + list + " 上家出牌:" + last.list + " 提示(" + prompt.size() + "):" + prompt;
    }

}

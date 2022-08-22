package Poker01;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Play {

    private static List<Card> player;
    private static List<Card> left;
    private static List<Card> right;
    private static CardProduct last = CardProduct.createNullType();


    public static void licensing(int sum, boolean contains345) {

        player = new ArrayList<>();
        left = new ArrayList<>();
        right = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();//牌堆
        for (int i = 0; i < sum; i++) {
            for (int j = contains345 ? Number.ID_3_1 : Number.ID_6_1; j < 54; j++) {
                list.add(j);
            }
        }
        //发牌
        while (list.size() > 0) {
            player.add(new Card(list.remove((int) (Math.random() * list.size()))));
            left.add(new Card(list.remove((int) (Math.random() * list.size()))));
            right.add(new Card(list.remove((int) (Math.random() * list.size()))));
        }
        player.sort((o1, o2) -> o2.id - o1.id);
        left.sort((o1, o2) -> o2.id - o1.id);
        right.sort((o1, o2) -> o2.id - o1.id);
    }


    //我出牌的时候用来判断我输入的牌是否都在player（我现有的牌之中），若都在就返回它们在list中的索引
    public static List<Integer> searchIndexByNum(List<Card> list, List<Card> card) {
        if (card.size() > list.size()) return null;
        //在list中搜索card
        int[] cardNum = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cardNum[i] = list.get(i).num;
        }
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < card.size(); i++) {
            //如果牌比2大
            if (card.get(i).num > Number.NUM_2) {  //分两种情况干嘛
                //得到这张牌的id（花色）
                final int id = card.get(i).id;  //后面都没用
                //再在list中找这张牌
                for (int j = 0; j < list.size(); j++) {
                    //如果找到了且indexes中没有它，就把它加入indexes
                    if (card.get(i).id == list.get(j).id && !indexes.contains(j)) {
                        indexes.add(j);
                        break;   //使每张牌都只找到一张
                    }
                }
            } else {  //如果牌比2小
                for (int j = 0; j < cardNum.length; j++) {  //cardNum.length是list的长度
                    if (card.get(i).num == cardNum[j] && !indexes.contains(j)) {
                        indexes.add(j);
                        break;
                    }
                }
            }
        }
        if (indexes.size() != card.size()) return null;
        return indexes;
    }

    public static void main(String[] args) {
        int sum = 1;//多少副牌
        boolean contains345 = true;//是否包含345
        licensing(sum, contains345);
        System.out.println("您的扑克牌:" + player);
        System.out.println("上家扑克牌:" + left);
        System.out.println("下家扑克牌:" + right);
        boolean playing = true;
        boolean toLeft = false;//出牌
        boolean toMe = false;//Math.random() < 0.5;//我出
        boolean toRight = false;
        boolean l = true;//主动出牌
        boolean m = false;
        boolean r = false;
        //boolean isContinue = false;
        boolean isMe = false;
        Scanner scanner = new Scanner(System.in);
        while (playing) {

//            //其作用就是让玩家在已经进入过toMe的情况下再重新进入一次toMe
//            if (isMe) {//输入?(提示),或输入(查看），或出牌不符合规则--》还该自己出牌
//                isMe = false;
//                toLeft = false;
//                toMe = true;
//                toRight = false;
//                l
//
//                = false;
//                m = false;
//                r = false;
//
//            }

            if (l) {
                System.out.println("==================================上家主动出牌==================================");
                //System.out.println("上家当前扑克牌:" + left);
                //System.out.println("上家当前扑克牌张数:" + left.size());
                //主动出牌的玩家的上家都是空的
                last = CardProduct.createNullType();
                Prompt prompt = new Prompt(left, last);

                //如果上家有可以出的牌
                if (prompt.prompt.size() > 0) {
                    //将其中最小的一组牌牌分牌型整理好
                    CardProducts cardProducts = CardType.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);//打得过则返回CardProduct在list里的索引 否则返回-1
                    if (index >= 0) {
                        last = cardProducts.list.get(index);  //将left出的这组牌作为上家的牌（对于下一个出牌的我来说left就是上家）
                        left.removeAll(last.list);//将出完的牌移除
                        System.out.println("上家主动出牌:" + last.list);
                        //如果left牌出完了
                        if (left.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("上家赢了!");
                            System.out.println("你输啦!");
                            break;
                        }
                        toLeft = false;
                        toMe = true;//轮到我来接牌

                    }
                }
            }

            if (toLeft) {
                System.out.println("=============上家==============");
                //System.out.println("上家当前扑克牌:" + left);
                //System.out.println("上家当前扑克牌张数:" + left.size());

                Prompt prompt = new Prompt(left, last);

                if (prompt.prompt.size() > 0) {
                    CardProducts cardProducts = CardType.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);
                    if (index >= 0) {
                        last = cardProducts.list.get(index);
                        left.removeAll(last.list);
                        System.out.println("上家出牌:" + last.list);
                        if (left.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("上家赢了!");
                            System.out.println("你输啦!");
                            break;
                        }
                        m = false;
                        r = false;
                        l = true;

                        toLeft = false;
                        toMe = true;
                    }

                } else {
                    System.out.println("上家不要");

                    toLeft = false;
                    toMe = true;
                }

            }

            while (m) {
                System.out.println("================================玩家主动出牌======================================");

                //主动出牌的玩家的上家都是空的
                last = CardProduct.createNullType();

                System.out.println("上一次出牌:" + last);
                System.out.println("上家当前扑克牌张数:" + left.size());
                System.out.println("下家当前扑克牌张数:" + right.size());
                System.out.println("您当前的扑克牌:" + player);
                System.out.println("请您出牌：(输入?提示 )");
                String line = scanner.nextLine();
                if (line == null || line.length() == 0) {
                    //isMe = true;
                    continue;
                } else if ("?".equals(line)) {
                    System.out.println("提示:" + new Prompt(player, last).prompt);
                    //isMe = true;
                    continue;
                }
                List<Integer> indexes = searchIndexByNum(player, CardUtils.CreateCards(line));
                if (indexes == null) {
                    System.out.println("您输入的扑克牌无效请重新输入");
                    //isMe = true;
                    continue;
                }
                CardProducts cardProducts = CardType.builder(CardUtils.getCardsByIndexes(player, indexes));
                int index = cardProducts.isGreaterThan(last);
                //if (index >= 0) {
                CardProduct newCardProduct = cardProducts.list.get(index);
                last = newCardProduct;
                player.removeAll(last.list);
                System.out.println("出牌成功:" + last);
                if (player.size() == 0) {
                    System.out.println("=======================================================================");
                    System.out.println("你赢啦!");
                    isMe = true;
                    break;
                }
                toMe = false;
                toRight = !toRight;
                break;
//                 else{
//                    System.out.println("不符合游戏规则:" + cardProducts);
//                    isMe = true;
//                    continue;
//                }
            }

            if (isMe) {
                break;
            }

            while (toMe) {
                System.out.println("=============玩家=================");

                System.out.println("上一次出牌:" + last);
                System.out.println("上家当前扑克牌张数:" + left.size());
                System.out.println("下家当前扑克牌张数:" + right.size());
                System.out.println("您当前的扑克牌:" + player);
                //System.out.println("请您出牌：(输入.不出 输入?提示 输入,偷看电脑的牌)");
                System.out.println("请您出牌：(输入.不出 输入?提示 )");
                String line = scanner.nextLine();
                //如果没输入东西就按了Enter键，系统会重新回到toMe
                if (line == null || line.length() == 0) {
                    //isMe = true;
                    continue;
                } else if (".".equals(line)) {
                    toMe = false;
                    toRight = true;  //如果我不出牌，就轮到下家出牌
                    break;
                } else if ("?".equals(line)) {
                    System.out.println("提示:" + new Prompt(player, last).prompt);
                    //isMe = true; //重启toMe
                    continue;
                }
//                else if (",".equals(line)) {
//                    System.out.println("上家当前扑克牌:" + left);
//                    System.out.println("下家当前扑克牌:" + right);
//                    isMe = true;  //偷看完电脑的牌之后再来一次
//                    continue;
//            }
                //一切正常，我开始出牌
                else {
                    List<Integer> indexes = searchIndexByNum(player, CardUtils.CreateCards(line));
                    if (indexes == null) {
                        System.out.println("您输入的扑克牌无效请重新输入");
                        continue;
                    }
                    CardProducts cardProducts = CardType.builder(CardUtils.getCardsByIndexes(player, indexes));
                    int index = cardProducts.isGreaterThan(last);
                    if (index >= 0) {
                        CardProduct newCardProduct = cardProducts.list.get(index);
                        last = newCardProduct;
                        player.removeAll(last.list);
                        System.out.println("出牌成功:" + last);
                        if (player.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("你赢啦!");
                            isMe = true;
                            break;
                        }
                        l = false;
                        r = false;
                        m = true;

                        toMe = false;
                        toRight = true;
                    } else {
                        System.out.println("不符合游戏规则:" + cardProducts);
                        //isMe = true;
                        continue;
                    }
                }
            }

            if (isMe) {
                break;
            }

            if (r) {
                System.out.println("================================下家主动出牌=======================================");
                last = CardProduct.createNullType();//主动出牌的玩家的上家都是空的

                Prompt prompt = new Prompt(right, last);

                if (prompt.prompt.size() > 0) {
                    CardProducts cardProducts = CardType.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);
                    if (index >= 0) {
                        last = cardProducts.list.get(index);
                        right.removeAll(last.list);
                        System.out.println("下家主动出牌:" + last.list);
                        if (right.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("下家赢了!");
                            System.out.println("你输啦!");
                            break;
                        }
                        toRight = false;
                        toLeft = true;
                    }

                }
            }

            if (toRight) {
                System.out.println("=============下家=================");
                Prompt prompt = new Prompt(right, last);

                if (prompt.prompt.size() > 0) {
                    CardProducts cardProducts = CardType.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);
                    if (index >= 0) {
                        last = cardProducts.list.get(index);
                        right.removeAll(last.list);
                        System.out.println("下家出牌:" + last.list);
                        if (right.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("下家赢了!");
                            System.out.println("你输啦!");
                            break;
                        }
                        l = false;
                        m = false;
                        r = true;

                        toRight = false;
                        toLeft = true;

                    }

                } else {
                    System.out.println("下家不要");

                    toRight = false;
                    toLeft = true;
                }


            }

        }
    }

}



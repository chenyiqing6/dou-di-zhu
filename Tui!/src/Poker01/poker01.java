package Poker01;

import java.util.*;

public class poker01 {

//    public static void main(String[] args) {
//        //创建HashMap，键是编号，值是牌
//        HashMap<Integer, String> hm = new HashMap<Integer, String>();
//
//        //创建ArrayList，存储编号
//        ArrayList<Integer> array = new ArrayList<Integer>();
//
//        //创建花色数组和点数数组
//        String[] colors = {"♦", "♣", "♥", "♠"};
//        String[] numbers = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};
//
//        //从0开始往HashMap里存储编号，并存储对应的牌，同时往ArrayList里面存储编号
//        int a = 0;
//        for (String number : numbers) {
//            for (String color : colors) {
//                hm.put(a, color + number);
//                array.add(a);
//                a++;
//            }
//        }
//        hm.put(a, "小王");
//        array.add(a);
//        a++;
//        hm.put(a, "大王");
//        array.add(a);
//
//        //洗牌（洗的是编号），用Collections的shuffle（）方法实现
//        Collections.shuffle(array);
//
//        //发牌（发的也是编号，为了保证编号是排序的，创建TreeSet集合接受）
//        TreeSet<Integer> myselfSet = new TreeSet<Integer>();
//        TreeSet<Integer> computerFirstSet = new TreeSet<Integer>();
//        TreeSet<Integer> computerSecondSet = new TreeSet<Integer>();
//        TreeSet<Integer> dpSet = new TreeSet<Integer>();
//
//        for (int i = 0; i < array.size(); i++) {
//            int x = array.get(i);
//            if (i >= array.size() - 3) {
//                dpSet.add(x);
//            } else if (i % 3 == 0) {
//                myselfSet.add(x);
//            } else if (i % 3 == 1) {
//                computerFirstSet.add(x);
//            } else if (i % 3 == 2) {
//                computerSecondSet.add(x);
//            }
//        }
//
//        //调用看牌方法
//        lookPoker("您", myselfSet, hm);
////        lookPoker("电脑玩家1",computerFirstSet,hm);
////        lookPoker("电脑玩家2",computerSecondSet,hm);
////        lookPoker("底牌",dpSet,hm);
//
///*        //系统选定第一轮叫牌玩家
//        Random r = new Random();
//        int x = r.nextInt(3) + 1;
//        int b = 1;
//        HashMap<Integer, String> hss = new HashMap<Integer, String>(); //键是编号（1，2，3），值是三位玩家
//        String[] jiaopaiwanjia = {"您", "电脑玩家1", "电脑玩家2"};
//        for (String jpwj : jiaopaiwanjia) {
//            hss.put(b, jpwj);
//            b++;
//        }
//        System.out.println("首轮叫牌的玩家是： " + hss.get(x));
//
//        //叫牌环节
//        if (x == 1) {
//            //玩家叫牌
//            System.out.println("您可以选择“1分”，“2分”，“3分”或者“不叫”");
//            System.out.println("请输入您的选择: ");
//            Scanner sca = new Scanner(System.in);
//            String JDZ = sca.next();      //玩家输入叫牌分值或不叫
//            if(JDZ.equals("1分") || JDZ.equals("2分") || JDZ.equals("3分") || JDZ.equals("不叫")){
//                ShowJP(hss.get(x),JDZ);
//            }else{
//                System.out.println("输入错误！请再次输入！");
//                Scanner s = new Scanner(System.in);
//                String JD = s.next();
//                ShowJP(hss.get(x),JD);
//            }
//            if(JDZ.equals("3分")  || JDZ.equals("3分")) {
//                System.out.println("恭喜您抢到地主！");
//            }
//
//        } else if(x == 2  || x==3){  //电脑叫牌
//            HashMap<Integer, String> jiaop = new HashMap<Integer, String>();
//            jiaop.put(1, "1分");
//            jiaop.put(2, "2分");
//            jiaop.put(3, "3分");
//            jiaop.put(4, "不叫");
//            Random ra = new Random();
//            int y = ra.nextInt(4) + 1;
//            ShowJP(hss.get(x),jiaop.get(x));
//        }
//
//        //抢地主
//
//
//
//
//
//*/
//        //系统确定地主
//        Random r = new Random();
//        int x = r.nextInt(3) + 1;
//        int b = 1;
//        HashMap<Integer, String> hss = new HashMap<Integer, String>(); //键是编号（1，2，3），值是三位玩家
//        String[] jiaopaiwanjia = {"您", "电脑玩家1", "电脑玩家2"};
//        for (String jpwj : jiaopaiwanjia) {
//            hss.put(b, jpwj);
//            b++;
//        }
//        System.out.println("地主是： " + hss.get(x));
//
//        //抢地主结束，亮出底牌
//        lookPoker("底牌",dpSet,hm);
//
//        //地主加底牌
//        if(x == 1){
//            myselfSet.addAll(dpSet);
//        }
//        else if(x == 2){
//            computerFirstSet.addAll(dpSet);
//            }
//        else if(x ==3){
//            computerSecondSet.addAll(dpSet);
//        }
//
//        //看牌
//        lookPoker("您", myselfSet, hm);
//
//
//        //开始打牌啦！！
//        //boolean playing = true;
//        boolean playing = false;
//        while(playing){
//            showComputerCard();     //每轮游戏开始时显示电脑玩家剩余牌数
//            lookPoker("您",myselfSet,hm);    //显示玩家牌
//            System.out.println("------------------------------------------------------------------------");
//
//
//        }
//    }
//
//    //定义方法看牌（遍历TreeSet集合，获取编号，到HashMap集合找对应的牌）
//    public static void lookPoker(String name, TreeSet<Integer> ts, HashMap<Integer, String> hm) {
//        System.out.println(name + "的牌是： ");
//        for (Integer key : ts) {
//            String poker = hm.get(key);   //遍历编号，到Map集合中根据编号找到牌
//            System.out.print(poker + " ");
//        }
//        System.out.println();
//    }
//
//    //定义方法显示叫牌情况
//    public static void ShowJP(String s, String i) {
//        System.out.println(s + "的叫牌选择是：" + i);
//    }
//
//    //定义方法显示电脑剩余牌数
//    public static void showComputerCard(){
//        System.out.println("电脑玩家1剩余的牌有"+ "张");
//        System.out.println("电脑玩家2剩余的牌有"+ "张");
//    }
}

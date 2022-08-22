package Poker01;

//定义一个牌类
//拥有数据成员整形的牌型

public class Card {
    public final int id;//牌的花色
    public final int num;//序号

    public Card(int id) {
        this.id = id;
        num = id / 4;
    }

    public Card(String s) {
        if ("C".equals(s)) {
            id = Number.ID_JOKER_2;
            num = id / 4;
            return;
        }
        switch (s) {
            case "3":
                num = Number.NUM_3;
                break;
            case "4":
                num = Number.NUM_4;
                break;
            case "5":
                num = Number.NUM_5;
                break;
            case "6":
                num = Number.NUM_6;
                break;
            case "7":
                num = Number.NUM_7;
                break;
            case "8":
                num = Number.NUM_8;
                break;
            case "9":
                num = Number.NUM_9;
                break;
            case "0":
                num = Number.NUM_10;
                break;
            case "J":
            case "j":
                num = Number.NUM_J;
                break;
            case "Q":
            case "q":
                num = Number.NUM_Q;
                break;
            case "K":
            case "k":              //字母大小写都可
                num = Number.NUM_K;
                break;
            case "1":
            case "A":
            case "a":
                num = Number.NUM_A;
                break;
            case "2":
                num = Number.NUM_2;
                break;
            case "c":
                num = Number.NUM_JOKER;
                break;
            default:
                throw new RuntimeException();
        }
        id = num * 4;
    }

    public boolean is2() {
        return num == Number.NUM_2;
    }

    public boolean isJoker() {
        return num == Number.NUM_JOKER;
    }    //判断是不是王

    @Override
    public String toString() {
        switch (num) {
            case Number.NUM_3:
                return "3";
            case Number.NUM_4:
                return "4";
            case Number.NUM_5:
                return "5";
            case Number.NUM_6:
                return "6";
            case Number.NUM_7:
                return "7";
            case Number.NUM_8:
                return "8";
            case Number.NUM_9:
                return "9";
            case Number.NUM_10:
                return "10";
            case Number.NUM_J:
                return "J";
            case Number.NUM_Q:
                return "Q";
            case Number.NUM_K:
                return "K";
            case Number.NUM_A:
                return "A";
            case Number.NUM_2:
                return "2";
            case Number.NUM_JOKER:
                if (id == Number.ID_JOKER_1) return "小王";
                return "大王";
        }
        return null;
    }
}
/*
public static final int NUM_3 = 0;
	public static final int NUM_4 = 1;
	public static final int NUM_5 = 2;
	public static final int NUM_6 = 3;
	public static final int NUM_7 = 4;
	public static final int NUM_8 = 5;
	public static final int NUM_9 = 6;
	public static final int NUM_10 = 7;
	public static final int NUM_J = 8;
	public static final int NUM_Q = 9;
	public static final int NUM_K = 10;
	public static final int NUM_A = 11;
	public static final int NUM_2 = 12;
	public static final int NUM_JOKER = 13;

	public static final int ID_3_1 = 0;
	public static final int ID_3_2 = 1;
	public static final int ID_3_3 = 2;
	public static final int ID_3_4 = 3;
	public static final int ID_4_1 = 4;
	public static final int ID_4_2 = 5;
	public static final int ID_4_3 = 6;
	public static final int ID_4_4 = 7;
	public static final int ID_5_1 = 8;
	public static final int ID_5_2 = 9;
	public static final int ID_5_3 = 10;
	public static final int ID_5_4 = 11;
	public static final int ID_6_1 = 12;
	public static final int ID_6_2 = 13;
	public static final int ID_6_3 = 14;
	public static final int ID_6_4 = 15;
	public static final int ID_7_1 = 16;
	public static final int ID_7_2 = 17;
	public static final int ID_7_3 = 18;
	public static final int ID_7_4 = 19;
	public static final int ID_8_1 = 20;
	public static final int ID_8_2 = 21;
	public static final int ID_8_3 = 22;
	public static final int ID_8_4 = 23;
	public static final int ID_9_1 = 24;
	public static final int ID_9_2 = 25;
	public static final int ID_9_3 = 26;
	public static final int ID_9_4 = 27;
	public static final int ID_10_1 = 28;
	public static final int ID_10_2 = 29;
	public static final int ID_10_3 = 30;
	public static final int ID_10_4 = 31;
	public static final int ID_J_1 = 32;
	public static final int ID_J_2 = 33;
	public static final int ID_J_3 = 34;
	public static final int ID_J_4 = 35;
	public static final int ID_Q_1 = 36;
	public static final int ID_Q_2 = 37;
	public static final int ID_Q_3 = 38;
	public static final int ID_Q_4 = 39;
	public static final int ID_K_1 = 40;
	public static final int ID_K_2 = 41;
	public static final int ID_K_3 = 42;
	public static final int ID_K_4 = 43;
	public static final int ID_A_1 = 44;
	public static final int ID_A_2 = 45;
	public static final int ID_A_3 = 46;
	public static final int ID_A_4 = 47;
	public static final int ID_2_1 = 48;
	public static final int ID_2_2 = 49;
	public static final int ID_2_3 = 50;
	public static final int ID_2_4 = 51;
	public static final int ID_JOKER_1 = 52;
	public static final int ID_JOKER_2 = 53;

	public static final int TYPE_ERROR = -1;
	public static final int TYPE_NULL = 0;
	public static final int TYPE_1 = 1;
	public static final int TYPE_22 = 2;
	public static final int TYPE_333 = 3;
	public static final int TYPE_BOOM = 4;
	public static final int TYPE_3331 = 5;
	public static final int TYPE_33322 = 6;
	public static final int TYPE_444412 = 7;
	public static final int TYPE_44441122 = 8;
	public static final int TYPE_34567 = 9;
	public static final int TYPE_334455 = 10;
	public static final int TYPE_333444 = 11;
	public static final int TYPE_33344412 = 12;
	public static final int TYPE_3334441122 = 13;
 */

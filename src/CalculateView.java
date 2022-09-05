import javax.swing.*;
import java.awt.*;

public class CalculateView extends JFrame {
    JTextArea inputExpression;
    InputListener listener = new InputListener();
    JButton plus, dash, multiple, division;     //加号、减号、乘号、除号
    JButton abs;                                //绝对值
    JButton getInt;                             //取整
    JButton sin, cos, tan;                      //三角函数
    JButton asin, acos, atan;                   //反三角函数
    JButton reciprocal;                         //倒数
    JButton squareRoot;                         //平方根
    JButton square, cube, power;                //平方，立方，幂
    JButton log, ln;                            //对数
    JButton pi;         //π
    JButton dot;        //小数点
    JButton DEL;        //退格
    JButton AC;         //清空
    JButton equal;      //等于
    JButton EXP;        //e
    JButton leftBracket;    //左括号
    JButton rightBracket;   //右括号
    JButton factorial;      //阶乘
    JButton mod;            //求余
    JButton percent;        //百分比
    JButton[] num = new JButton[10];    //数字0-9
    //考虑到从按钮输入变量(26个字母)会增多上百行的代码，而这个功能不是必要的，因此这里不提供从按钮输入变量的功能，仅通过键盘输入变量
    CalculateView() {
        init();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    void init() {
        Panel p1 = new Panel();     //p1用于放置文本框
        Panel p2 = new Panel();     //p2使用网格布局存放按钮
        Font font = new Font("Consolas", Font.PLAIN, 18);
        inputExpression = new JTextArea(3, 58); //设置文本框显示3行，因为表达式占一行结果占一行，滚动条占一行
        inputExpression.setFont(font);
        p1.add(inputExpression);
        p1.add(new JScrollPane(inputExpression));   //增加滚动条
        inputExpression.getCaret().setVisible(true);    //显示光标
        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.CENTER);
        p2.setLayout(new GridLayout(5, 8, 2, 2));
        plus = new JButton("+");
        dash = new JButton("-");
        multiple = new JButton("*");
        division = new JButton("/");
        abs = new JButton("|x|");
        getInt = new JButton("[x]");
        sin = new JButton("sin");
        cos = new JButton("cos");
        tan = new JButton("tan");
        asin = new JButton("arcsin");
        acos = new JButton("arccos");
        atan = new JButton("arctan");
        reciprocal = new JButton("1/x");
        squareRoot = new JButton("√▔");
        square = new JButton("^2");
        cube = new JButton("^3");
        power = new JButton("^");
        log = new JButton("log");
        ln = new JButton("ln");
        pi = new JButton("π");
        dot = new JButton(".");
        DEL = new JButton("DEL");
        AC = new JButton("AC");
        equal = new JButton("=");
        EXP = new JButton("E");
        leftBracket = new JButton("(");
        rightBracket = new JButton(")");
        factorial = new JButton("x!");
        mod = new JButton("mod");
        percent = new JButton("%");
        num[0] = new JButton("0");
        num[1] = new JButton("1");
        num[2] = new JButton("2");
        num[3] = new JButton("3");
        num[4] = new JButton("4");
        num[5] = new JButton("5");
        num[6] = new JButton("6");
        num[7] = new JButton("7");
        num[8] = new JButton("8");
        num[9] = new JButton("9");
        listener.setView(this);
        p2.add(leftBracket);
        leftBracket.addActionListener(listener);
        p2.add(rightBracket);
        rightBracket.addActionListener(listener);
        p2.add(reciprocal);
        reciprocal.addActionListener(listener);
        p2.add(abs);
        abs.addActionListener(listener);
        p2.add(AC);
        AC.addActionListener(listener);
        p2.add(division);
        division.addActionListener(listener);
        p2.add(multiple);
        multiple.addActionListener(listener);
        p2.add(DEL);
        DEL.addActionListener(listener);
        p2.add(square);
        square.addActionListener(listener);
        p2.add(cube);
        cube.addActionListener(listener);
        p2.add(power);
        power.addActionListener(listener);
        p2.add(getInt);
        getInt.addActionListener(listener);
        p2.add(num[7]);
        num[7].addActionListener(listener);
        p2.add(num[8]);
        num[8].addActionListener(listener);
        p2.add(num[9]);
        num[9].addActionListener(listener);
        p2.add(dash);
        dash.addActionListener(listener);
        p2.add(sin);
        sin.addActionListener(listener);
        p2.add(cos);
        cos.addActionListener(listener);
        p2.add(tan);
        tan.addActionListener(listener);
        p2.add(factorial);
        factorial.addActionListener(listener);
        p2.add(num[4]);
        num[4].addActionListener(listener);
        p2.add(num[5]);
        num[5].addActionListener(listener);
        p2.add(num[6]);
        num[6].addActionListener(listener);
        p2.add(plus);
        plus.addActionListener(listener);
        p2.add(asin);
        asin.addActionListener(listener);
        p2.add(acos);
        acos.addActionListener(listener);
        p2.add(atan);
        atan.addActionListener(listener);
        p2.add(mod);
        mod.addActionListener(listener);
        p2.add(num[1]);
        num[1].addActionListener(listener);
        p2.add(num[2]);
        num[2].addActionListener(listener);
        p2.add(num[3]);
        num[3].addActionListener(listener);
        p2.add(squareRoot);
        squareRoot.addActionListener(listener);
        p2.add(log);
        log.addActionListener(listener);
        p2.add(ln);
        ln.addActionListener(listener);
        p2.add(EXP);
        EXP.addActionListener(listener);
        p2.add(pi);
        pi.addActionListener(listener);
        p2.add(percent);
        percent.addActionListener(listener);
        p2.add(num[0]);
        num[0].addActionListener(listener);
        p2.add(dot);
        dot.addActionListener(listener);
        p2.add(equal);
        equal.addActionListener(listener);
    }
}